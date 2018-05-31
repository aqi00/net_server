package com.example.chat;

import java.net.Socket;

public class SocketBean {
	public String id;
	public Socket socket;
	public String deviceId;
	public String nickName;
	public String loginTime;
	
	public SocketBean(String id, Socket socket) {
		this.id = id;
		this.socket = socket;
		this.deviceId = "";
		this.nickName = "";
		this.loginTime = "";
	}

}
