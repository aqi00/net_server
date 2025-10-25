package com.socketio.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.socketio.bean.ImageMessage;
import com.socketio.bean.JoinInfo;
import com.socketio.bean.MessageInfo;

// 仿微信聊天服务端（鸿蒙专用）
public class WeLinkServer {

    // 客户端映射
    private static Map<String, SocketIOClient> clientMap = new HashMap<>();
    // 人员名字映射
    private static Map<String, String> nameMap = new HashMap<>();
    // 群名称与群成员映射
    private static Map<String, Map<String, String>> groupMap = new HashMap<>();

    public static void main(String[] args) {
        Configuration config = new Configuration();
        // 如果调用了setHostname方法，就只能通过主机名访问，不能通过IP访问
        //config.setHostname("localhost");
        config.setPort(9011); // 设置监听端口
        final SocketIOServer server = new SocketIOServer(config);
        // 添加连接连通的监听事件
        server.addConnectListener(client -> {
            String sessionId = client.getSessionId().toString();
            System.out.println("getRemoteAddress "+client.getRemoteAddress().toString());
            System.out.println(sessionId+"已连接");
            clientMap.put(sessionId, client);
        });
        // 添加连接断开的监听事件
        server.addDisconnectListener(client -> {
            String sessionId = client.getSessionId().toString();
            System.out.println(sessionId+"已断开:"+nameMap.get(sessionId).toString());
            for (Map.Entry<String, Map<String, String>> group : groupMap.entrySet()) {
                group.getValue().remove(sessionId);
                for (Map.Entry<String, String> user : group.getValue().entrySet()) {
                    clientMap.get(user.getKey()).sendEvent("person_out_group", nameMap.get(sessionId).toString());
                    System.out.println(user.getKey()+" person_out_group："+nameMap.get(sessionId).toString());
                }
            }
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                if (!(sessionId.equals(item.getKey()))) {
                    item.getValue().sendEvent("friend_offline", nameMap.get(sessionId).toString());
                }
            }
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                if (sessionId.equals(item.getKey())) {
                    clientMap.remove(item.getKey());
                    break;
                }
            }
            nameMap.remove(sessionId);
        });
        // 添加我已上线的监听事件
        server.addEventListener("self_online", String.class, (client, name, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            System.out.println(sessionId+"已上线："+name);
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                item.getValue().sendEvent("friend_online", name);
                Thread.sleep(100);
                if (nameMap.size()>0 && nameMap.containsKey(item.getKey())) {
                    client.sendEvent("friend_online", nameMap.get(item.getKey()));
                    Thread.sleep(100);
                }
            }
            nameMap.put(sessionId, name);
            System.out.println("self_online nameMap.size()："+nameMap.size());
        });
        // 添加我已下线的监听事件
        server.addEventListener("self_offline", String.class, (client, name, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            System.out.println(sessionId+"已下线："+name);
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                if (!(sessionId.equals(item.getKey()))) {
                    item.getValue().sendEvent("friend_offline", name);
                }
            }
            nameMap.remove(sessionId);
        });
        // 添加文本发送的事件监听器
        server.addEventListener("send_friend_message", String.class, (client, content, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            //System.out.println(sessionId+"发送消息："+content);
            System.out.println(nameMap.get(sessionId).toString()+" 发送消息："+content);
            JSONObject json = JSONObject.parseObject(content);
            System.out.println("send_friend_message nameMap.size()："+nameMap.size());
            MessageInfo message = JSONObject.toJavaObject(json, MessageInfo.class);
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                System.out.println("message.getTo()="+message.getTo()+", item.getValue()="+item.getValue());
                if (message.getTo().equals(item.getValue())) {
                    String resp = JSONObject.toJSONString(message);
                    //System.out.println(sessionId+"接收消息："+resp);
                    String other_name = nameMap.get(item.getKey()).toString();
                    System.out.println(other_name+" 接收消息："+resp);
                    clientMap.get(item.getKey()).sendEvent("receive_friend_message", resp);
                    break;
                }
            }
        });
        // 添加图像发送的事件监听器
        server.addEventListener("send_friend_image", String.class, (client, content, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            JSONObject json = JSONObject.parseObject(content);
            ImageMessage message = JSONObject.toJavaObject(json, ImageMessage.class);
            //System.out.println(sessionId+"发送图片："+content);
            System.out.println(nameMap.get(sessionId).toString()+" 发送图片："+message.getPart().getSeq());
            System.out.println("getFrom="+message.getFrom()+",getTo="+message.getTo()+",getName="+message.getPart().getName());
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                if (message.getTo().equals(item.getValue())) {
                    //System.out.println(item.getKey()+"receive_friend_image");
                    String other_name = nameMap.get(item.getKey()).toString();
                    System.out.println(other_name+" 接收图片");
                    String resp = JSONObject.toJSONString(message);
                    clientMap.get(item.getKey()).sendEvent("receive_friend_image", resp);
                    break;
                }
            }
        });
        // 添加入群的事件监听器
        server.addEventListener("join_group", String.class, (client, content, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            //System.out.println(sessionId+"已入群："+content);
            System.out.println(nameMap.get(sessionId).toString()+" 已入群："+content);
            JSONObject json = JSONObject.parseObject(content);
            JoinInfo info = JSONObject.toJavaObject(json, JoinInfo.class);
            if (!groupMap.containsKey(info.getGroup_name())) {
                System.out.println("groupMap.put "+info.getGroup_name());
                groupMap.put(info.getGroup_name(), new HashMap<String, String>());
            }
            for (Map.Entry<String, Map<String, String>> group : groupMap.entrySet()) {
                System.out.println("群名称为"+group.getKey());
                if (info.getGroup_name().equals(group.getKey())) {
                    group.getValue().put(sessionId, info.getUser_name());
                    System.out.println("person_count="+group.getValue().size());
                    client.sendEvent("person_count", group.getValue().size()+"");
                    Thread.sleep(100);
                    for (Map.Entry<String, String> user : group.getValue().entrySet()) {
                        //System.out.println("群成员为"+user.getKey());
                        //System.out.println(user.getKey()+" person_in_group："+info.getUser_name());
                        String other_name = nameMap.get(user.getKey()).toString();
                        System.out.println(other_name+" person_in_group："+info.getUser_name());
                        clientMap.get(user.getKey()).sendEvent("person_in_group", info.getUser_name());
                    }
                }
            }
        });
        // 添加退群的事件监听器
        server.addEventListener("leave_group", String.class, (client, content, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            //System.out.println(sessionId+"已退群："+content);
            System.out.println(nameMap.get(sessionId).toString()+" 已退群："+content);
            JSONObject json = JSONObject.parseObject(content);
            JoinInfo info = JSONObject.toJavaObject(json, JoinInfo.class);
            for (Map.Entry<String, Map<String, String>> group : groupMap.entrySet()) {
                if (info.getGroup_name().equals(group.getKey())) {
                    group.getValue().remove(sessionId);
                    for (Map.Entry<String, String> user : group.getValue().entrySet()) {
                        clientMap.get(user.getKey()).sendEvent("person_out_group", info.getUser_name());
                        //System.out.println(user.getKey()+" person_out_group："+info.getUser_name());
                        String other_name = nameMap.get(user.getKey()).toString();
                        System.out.println(other_name+" person_out_group："+info.getUser_name());
                    }
                }
            }
        });
        // 添加群消息发送的事件监听器
        server.addEventListener("send_group_message", String.class, (client, content, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            //System.out.println(sessionId+"发送群消息："+content);
            System.out.println(nameMap.get(sessionId).toString()+" 发送群消息："+content);
            JSONObject json = JSONObject.parseObject(content);
            MessageInfo message = JSONObject.toJavaObject(json, MessageInfo.class);
            for (Map.Entry<String, Map<String, String>> group : groupMap.entrySet()) {
                if (message.getTo().equals(group.getKey())) {
                    for (Map.Entry<String, String> user : group.getValue().entrySet()) {
                        if (!(user.getValue().equals(message.getFrom()))) {
                            String resp = JSONObject.toJSONString(message);
                            clientMap.get(user.getKey()).sendEvent("receive_group_message", resp);
                            //System.out.println("receive_group_message 接收方为"+user.getKey());
                            String other_name = nameMap.get(user.getKey()).toString();
                            System.out.println(other_name+" 接收群消息："+content);
                        }
                    }
                    break;
                }
            }
        });
        // 添加群图片发送的事件监听器
        server.addEventListener("send_group_image", String.class, (client, content, ackSender) -> {
            String sessionId = client.getSessionId().toString();
            JSONObject json = JSONObject.parseObject(content);
            ImageMessage message = JSONObject.toJavaObject(json, ImageMessage.class);
            //System.out.println(sessionId+"发送群图片："+content);
            System.out.println(nameMap.get(sessionId).toString()+" 发送群图片："+message.getPart().getSeq());
            for (Map.Entry<String, Map<String, String>> group : groupMap.entrySet()) {
                if (message.getTo().equals(group.getKey())) {
                    for (Map.Entry<String, String> user : group.getValue().entrySet()) {
                        if (!(user.getValue().equals(message.getFrom()))) {
                            String resp = JSONObject.toJSONString(message);
                            clientMap.get(user.getKey()).sendEvent("receive_group_image", resp);
                            //System.out.println("receive_group_image 接收方为"+user.getKey());
                            String other_name = nameMap.get(user.getKey()).toString();
                            System.out.println(other_name+" 接收群图片");
                        }
                    }
                    break;
                }
            }
        });

        server.start(); // 启动Socket服务
        System.out.println("已启动WeLinkServer");
    }

}
