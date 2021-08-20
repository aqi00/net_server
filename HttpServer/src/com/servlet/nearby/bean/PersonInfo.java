package com.servlet.nearby.bean;

public class PersonInfo {
    private String name;
    private int sex;
    private String face;
    private String phone;
    private String love;
    private String address;
    private String info;
    private double latitude;
    private double longitude;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public int getSex() {
        return this.sex;
    }
    public void setFace(String face) {
        this.face = face;
    }
    public String getFace() {
        return this.face;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setLove(String love) {
        this.love = love;
    }
    public String getLove() {
        return this.love;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return this.address;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return this.info;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLongitude() {
        return this.longitude;
    }
}
