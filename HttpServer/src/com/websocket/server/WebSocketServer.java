package com.websocket.server;

import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/testWebSocket")
public class WebSocketServer {
    // 存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    private Session mSession; // 当前的连接会话

    // 连接成功后调用
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket连接成功");
        this.mSession = session;
        webSocketSet.add(this);
    }

    // 连接关闭后调用
    @OnClose
    public void onClose() {
        System.out.println("WebSocket连接关闭");
        webSocketSet.remove(this);
    }

    // 连接异常时调用
    @OnError
    public void onError(Throwable error) {
        System.out.println("WebSocket连接异常");
        error.printStackTrace();
    }

    // 收到客户端消息时调用
    @OnMessage
    public void onMessage(String msg) throws Exception {
        System.out.println("接收到客户端消息：" + msg);
        for(WebSocketServer item : webSocketSet){
            item.mSession.getBasicRemote().sendText("我听到消息啦“"+msg+"”");
        }
    }
}
