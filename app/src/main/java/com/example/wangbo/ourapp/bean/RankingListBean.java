package com.example.wangbo.ourapp.bean;

/**
 * Created by wangbo on 2018/8/15.
 */

public class RankingListBean {

    public int id;

    public int img;

    public String name;

    public String upTime;

    public RankingListBean() {
    }

    public RankingListBean(int id, int img, String name, String upTime) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.upTime = upTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }
}
