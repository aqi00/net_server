package com.servlet.audio.bean;

public class AudioInfo {
    private String artist;
    private String title;
    private String desc;
    private String cover;
    private String audio;

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getArtist() {
        return this.artist;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getCover() {
        return this.cover;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }
    public String getAudio() {
        return this.audio;
    }
}
