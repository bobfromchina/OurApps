package com.example.wangbo.ourapp.bean;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbo on 2018/8/7.
 */

public class Al implements Parcelable{

    private String id;

    private String name;

    private String picUrl;

    public Al() {
    }

    public Al(String id, String name, String picUrl) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return "Al{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.picUrl);
    }

    protected Al(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.picUrl = in.readString();
    }

    public static final Creator<Al> CREATOR = new Creator<Al>() {
        @Override
        public Al createFromParcel(Parcel source) {
            return new Al(source);
        }

        @Override
        public Al[] newArray(int size) {
            return new Al[size];
        }
    };
}
