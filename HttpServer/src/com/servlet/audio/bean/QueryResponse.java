package com.servlet.audio.bean;

import java.util.List;

public class QueryResponse {
    private String code="0";
    private String desc;
    private List<AudioInfo> audioList;

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

    public void setAudioList(List<AudioInfo> audioList) {
        this.audioList = audioList;
    }

    public List<AudioInfo> getAudioList() {
        return this.audioList;
    }
}
