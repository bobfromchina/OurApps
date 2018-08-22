package com.example.wangbo.ourapp.bean;

/**
 * Created by wangbo on 2018/8/2.
 */

public class OneHeaderBean {

    public int id;

    public String name;

    public int tag;


    public OneHeaderBean() {
    }

    public OneHeaderBean(int id, String name, int tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "OneHeaderBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tag=" + tag +
                '}';
    }
}
