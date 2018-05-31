package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ServerThread implements Runnable {
	private SocketBean mSocket = null;
	private BufferedReader mReader = null;

	public ServerThread(SocketBean mSocket) throws IOException {
		this.mSocket = mSocket;
		mReader = new BufferedReader(new InputStreamReader(mSocket.socket.getInputStream()));
	}

	@Override
	public void run() {
		try {
			String content = null;
			// 循环不断地从Socket中读取客户端发送过来的数据
			while ((content = mReader.readLine()) != null) {
				System.out.println("content="+content);
				int pos = content.indexOf("|");
				// 包头格式为：动作名称|设备编号|昵称|时间|对方设备编号
				String head = content.substring(0, pos);
				String body = content.substring(pos+1);
				String[] splitArray = head.split(",");
				String action = splitArray[0];
				System.out.println("action="+action);
				if (action.equals("LOGIN")) {
					login(splitArray[1], splitArray[2], splitArray[3]);
				} else if (action.equals("LOGOUT")) {
					logout(splitArray[1]);
					break;
				} else if (action.equals("SENDMSG")) {
					sendmsg("RECVMSG", splitArray[2], splitArray[4], splitArray[1], body);
				} else if (action.equals("GETLIST")) {
					getlist(splitArray[1]);
				} else if (action.equals("SENDPHOTO")) {
					sendmsg("RECVPHOTO", splitArray[2], splitArray[4], splitArray[1], body);
				} else if (action.equals("SENDSOUND")) {
					sendmsg("RECVSOUND", splitArray[2], splitArray[4], splitArray[1], body);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void login(String deviceId, String nickName, String loginTime) throws IOException {
		for (int i=0; i<ChatServer.mSocketList.size(); i++) {
			SocketBean item = ChatServer.mSocketList.get(i);
			if (item.id.equals(mSocket.id)) {
				item.deviceId = deviceId;
				item.nickName = nickName;
				item.loginTime = loginTime;
				ChatServer.mSocketList.set(i, item);
				break;
			}
		}
	}
	
	private String getFriend() {
		String friends = "GETLIST,";
		for (SocketBean item : ChatServer.mSocketList) {
			if (item.deviceId!=null && item.deviceId.length()>0) {
				String friend = String.format("|%s,%s,%s", item.deviceId, item.nickName, item.loginTime);
				friends += friend;
			}
		}
		return friends;
	}
	
	private void getlist(String deviceId) throws IOException {
		for (int i=0; i<ChatServer.mSocketList.size(); i++) {
			SocketBean item = ChatServer.mSocketList.get(i);
			if (item.id.equals(mSocket.id) && item.deviceId.equals(deviceId)) {
				PrintStream ps = new PrintStream(item.socket.getOutputStream());
				ps.println(getFriend());
				break;
			}
		}
	}
	
	private void logout(String deviceId) throws IOException {
		for (int i=0; i<ChatServer.mSocketList.size(); i++) {
			SocketBean item = ChatServer.mSocketList.get(i);
			if (item.id.equals(mSocket.id) && item.deviceId.equals(deviceId)) {
				PrintStream ps = new PrintStream(item.socket.getOutputStream());
				ps.println("LOGOUT,|");
				item.socket.close();
				ChatServer.mSocketList.remove(i);
				break;
			}
		}
	}

	private void sendmsg(String respAction, String otherName, String otherId, String selfId, String message) throws IOException {
		for (int i=0; i<ChatServer.mSocketList.size(); i++) {
			SocketBean item = ChatServer.mSocketList.get(i);
			if (item.deviceId.equals(otherId)) {
				String content = String.format("%s,%s,%s,%s|%s", 
						respAction, selfId, otherName, DateUtil.getNowTime(), message);
				System.out.println("resp="+content);
				PrintStream ps = new PrintStream(item.socket.getOutputStream());
				ps.println(content);
				break;
			}
		}
	}

}