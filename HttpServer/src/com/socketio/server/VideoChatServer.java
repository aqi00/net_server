package com.socketio.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.socketio.bean.ContactInfo;
import com.socketio.bean.JoinInfo;
import com.socketio.bean.MessageInfo;
import com.socketio.bean.RoomInfo;
import com.socketio.bean.RoomSet;

public class VideoChatServer {

    // 客户端映射
    private static Map<String, SocketIOClient> clientMap = new HashMap<>();
    // 人员名字映射
    private static Map<String, String> nameMap = new HashMap<>();
    // 房间名称与房间观众映射
    private static Map<String, RoomInfo> roomMap = new HashMap<>();
    
    public static void main(String[] args) {
        Configuration config = new Configuration();
        // 如果调用了setHostname方法，就只能通过主机名访问，不能通过IP访问
        //config.setHostname("localhost");
        config.setPort(9012); // 设置监听端口
        final SocketIOServer server = new SocketIOServer(config);
        // 添加连接连通的监听事件
        server.addConnectListener(client -> {
            System.out.println(client.getSessionId().toString()+"已连接");
            clientMap.put(client.getSessionId().toString(), client);
        });
        // 添加连接断开的监听事件
        server.addDisconnectListener(client -> {
            System.out.println(client.getSessionId().toString()+"已断开");
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                if (client.getSessionId().toString().equals(item.getKey())) {
                    clientMap.remove(item.getKey());
                    break;
                }
            }
            nameMap.remove(client.getSessionId().toString());
        });

        // 添加用户上线的事件监听器
        server.addEventListener("self_online", String.class, (client, name, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"已上线："+name);
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                if (!client.getSessionId().toString().equals(item.getKey())) {
                    item.getValue().sendEvent("friend_online", name);
                    client.sendEvent("friend_online", nameMap.get(item.getKey()));
                }
            }
            nameMap.put(client.getSessionId().toString(), name);
        });

        // 添加用户下线的事件监听器
        server.addEventListener("self_offline", String.class, (client, name, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"已下线："+name);
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                if (!client.getSessionId().toString().equals(item.getKey())) {
                    item.getValue().sendEvent("friend_offline", name);
                }
            }
            nameMap.remove(client.getSessionId().toString());
        });

        // 添加ICE候选的事件监听器
        server.addEventListener("IceInfo", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"ICE候选："+json.toString());
            String destId = json.getString("destination");
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                if (destId.equals(item.getValue())) {
                    clientMap.get(item.getKey()).sendEvent("IceInfo", json);
                    break;
                }
            }
        });

        // 添加SDP媒体的事件监听器
        server.addEventListener("SdpInfo", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"SDP媒体："+json.toString());
            String destId = json.getString("destination");
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                if (destId.equals(item.getValue())) {
                    clientMap.get(item.getKey()).sendEvent("SdpInfo", json);
                    break;
                }
            }
        });

        //待接收的：offer_converse提出通话、self_dial_in我方接通、self_hang_up我方挂断
        //待发送的：friend_converse通话请求、other_dial_in对方接通、other_hang_up对方挂断
        // 添加通话请求的事件监听器
        server.addEventListener("offer_converse", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"提出通话："+json.toString());
            ContactInfo contact = (ContactInfo) JSONObject.toJavaObject(json, ContactInfo.class);
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                if (contact.getTo().equals(item.getValue())) {
                    clientMap.get(item.getKey()).sendEvent("friend_converse", contact.getFrom());
                    break;
                }
            }
        });

        // 添加通话拨入的事件监听器
        server.addEventListener("self_dial_in", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"接受通话："+json.toString());
            ContactInfo contact = (ContactInfo) JSONObject.toJavaObject(json, ContactInfo.class);
            nameMap.put(client.getSessionId().toString(), contact.getFrom());
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                if (contact.getTo().equals(item.getValue())) {
                    clientMap.get(item.getKey()).sendEvent("other_dial_in", contact.getFrom());
                    break;
                }
            }
        });

        // 添加通话挂断的事件监听器
        server.addEventListener("self_hang_up", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"挂断通话："+json.toString());
            ContactInfo contact = (ContactInfo) JSONObject.toJavaObject(json, ContactInfo.class);
            for (Map.Entry<String, String> item : nameMap.entrySet()) {
                if (contact.getTo().equals(item.getValue())) {
                    clientMap.get(item.getKey()).sendEvent("other_hang_up", contact.getFrom());
                    break;
                }
            }
        });

        // 添加房间列表获取的事件监听器
        server.addEventListener("get_room_list", String.class, (client, userName, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"获取房间列表："+userName);
            List<RoomInfo> roomList = new ArrayList<RoomInfo>();
            roomList.addAll(roomMap.values());
            RoomSet roomSet = new RoomSet(roomList);
            client.sendEvent("return_room_list", roomSet);
        });

        // 添加房间创建的事件监听器
        server.addEventListener("open_room", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"创建房间："+json.toString());
            RoomInfo room = (RoomInfo) JSONObject.toJavaObject(json, RoomInfo.class);
            roomMap.put(room.getRoom_name(), room);
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                item.getValue().sendEvent("room_have_opened", room);
            }
        });

        // 添加房间关闭的事件监听器
        server.addEventListener("close_room", String.class, (client, roomName, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"关闭房间："+roomName);
            for (Map.Entry<String, SocketIOClient> item : clientMap.entrySet()) {
                item.getValue().sendEvent("room_have_closed", roomName);
            }
            roomMap.remove(roomName);
        });

        // 添加用户加入房间的事件监听器
        server.addEventListener("join_room", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"已进入房间："+json.toString());
            JoinInfo info = (JoinInfo) JSONObject.toJavaObject(json, JoinInfo.class);
            nameMap.put(client.getSessionId().toString(), info.getUser_name());
            if (!roomMap.containsKey(info.getGroup_name())) {
                System.out.println("roomMap.put "+info.getGroup_name());
                roomMap.put(info.getGroup_name(), new RoomInfo(info.getUser_name(), info.getGroup_name(), new HashMap<String, String>()));
            }
            for (Map.Entry<String, RoomInfo> room : roomMap.entrySet()) {
                if (info.getGroup_name().equals(room.getKey())) {
                    room.getValue().getMember_map().put(client.getSessionId().toString(), info.getUser_name());
                    for (Map.Entry<String, String> user : room.getValue().getMember_map().entrySet()) {
                        clientMap.get(user.getKey()).sendEvent("person_in_room", info.getUser_name());
                        System.out.println("notify person_in_room "+user.getKey()+" "+info.getUser_name());
                    }
                    System.out.println("person_count="+room.getValue().getMember_map().size());
                    client.sendEvent("person_count", room.getValue().getMember_map().size());
                }
            }
        });

        // 添加用户退出房间的事件监听器
        server.addEventListener("leave_room", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"已退出房间："+json.toString());
            JoinInfo info = (JoinInfo) JSONObject.toJavaObject(json, JoinInfo.class);
            for (Map.Entry<String, RoomInfo> room : roomMap.entrySet()) {
                if (info.getGroup_name().equals(room.getKey())) {
                    room.getValue().getMember_map().remove(client.getSessionId().toString());
                    for (Map.Entry<String, String> user : room.getValue().getMember_map().entrySet()) {
                        clientMap.get(user.getKey()).sendEvent("person_out_room", info.getUser_name());
                    }
                }
            }
        });

        // 添加发送房间消息的事件监听器
        server.addEventListener("send_room_message", JSONObject.class, (client, json, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"发送消息："+json.toString());
            MessageInfo message = (MessageInfo) JSONObject.toJavaObject(json, MessageInfo.class);
            for (Map.Entry<String, RoomInfo> room : roomMap.entrySet()) {
                if (message.getTo().equals(room.getKey())) {
                    for (Map.Entry<String, String> user : room.getValue().getMember_map().entrySet()) {
                        if (!user.getValue().equals(message.getFrom())) {
                            clientMap.get(user.getKey()).sendEvent("receive_room_message", message);
                        }
                    }
                    break;
                }
            }
        });

        server.start(); // 启动Socket服务
    }

}
