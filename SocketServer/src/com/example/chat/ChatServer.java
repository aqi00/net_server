package com.example.chat;

import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {
	private static final int SOCKET_PORT = 52000;
	public static ArrayList<SocketBean> mSocketList = new ArrayList<SocketBean>();

	private void initServer() {
		System.out.println("Socket聊天服务已启动");
		try {
			// 创建一个ServerSocket，用于监听客户端Socket的连接请求
			ServerSocket server = new ServerSocket(SOCKET_PORT);
			while (true) {
				// 每当接收到客户端的Socket请求，服务器端也相应的创建一个Socket
				SocketBean socket = new SocketBean(DateUtil.getTimeId(), server.accept());
				mSocketList.add(socket);
				System.out.println("连接了一个客户端");
				// 每连接一个客户端，启动一个ServerThread线程为该客户端服务
				new Thread(new ServerThread(socket)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.initServer();
	}
}
