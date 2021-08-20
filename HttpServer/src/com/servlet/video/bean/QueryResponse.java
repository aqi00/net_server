package com.servlet.video.bean;

import java.util.List;

public class QueryResponse {
    private String code="0";
    private String desc;
    private List<VideoInfo> videoList;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setVideoList(List<VideoInfo> videoList) {
        this.videoList = videoList;
    }

    public List<VideoInfo> getVideoList() {
        return this.videoList;
    }
}
