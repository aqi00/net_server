package com.socketio.bean;

public class ContactInfo {
    public String from; // 联系来源
    public String to; // 联系目标

    public ContactInfo(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public void setFrom(String from) {
    	this.from = from;
    }
    
    public String getFrom() {
    	return this.from;
    }

    public void setTo(String to) {
    	this.to = to;
    }
    
    public String getTo() {
    	return this.to;
    }

}
