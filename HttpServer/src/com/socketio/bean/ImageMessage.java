package com.socketio.bean;

public class ImageMessage {
    private String from;
    private String to;
    private ImagePart part;

    public ImageMessage() {
    }
    
    public ImageMessage(String from, String to, ImagePart part) {
        this.from = from;
        this.to = to;
        this.part = part;
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

    public void setPart(ImagePart part) {
    	this.part = part;
    }
    
    public ImagePart getPart() {
    	return this.part;
    }
    
}
