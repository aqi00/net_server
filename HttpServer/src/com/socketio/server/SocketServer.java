package com.socketio.server;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class SocketServer {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        // 如果调用了setHostname方法，就只能通过主机名访问，不能通过IP访问
        //config.setHostname("localhost");
        config.setPort(9010); // 设置监听端口
        final SocketIOServer server = new SocketIOServer(config);
        // 添加连接连通的监听事件
        server.addConnectListener(client -> {
            System.out.println(client.getSessionId().toString()+"已连接");
        });
        // 添加连接断开的监听事件
        server.addDisconnectListener(client -> {
            System.out.println(client.getSessionId().toString()+"已断开");
        });
        // 添加文本发送的事件监听器
        server.addEventListener("send_text", String.class, (client, message, ackSender) -> {
            System.out.println(client.getSessionId().toString()+"发送文本消息："+message);
            client.sendEvent("receive_text", "不开不开我不开，妈妈没回来谁来也不开。");
        });
        // 添加图像发送的事件监听器
        server.addEventListener("send_image", JSONObject.class, (client, json, ackSender) -> {
            String desc = String.format("%s，序号为%d", json.getString("name"), json.getIntValue("seq"));
            System.out.println(client.getSessionId().toString()+"发送图片消息："+desc);
            client.sendEvent("receive_image", json);
        });

        server.start(); // 启动Socket服务
    }

}
