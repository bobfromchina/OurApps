package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class YunBanner implements Parcelable{

    private String picUrl;

    private String url;

    private String targetId;

    private String backgroundUrl;

    private int targetType;

    private String monitorType;

    private String monitorImpress;

    private String monitorClick;

    public YunBanner() {
    }

    public YunBanner(String picUrl, String url, String targetId, String backgroundUrl, int targetType, String monitorType, String monitorImpress, String monitorClick) {
        this.picUrl = picUrl;
        this.url = url;
        this.targetId = targetId;
        this.backgroundUrl = backgroundUrl;
        this.targetType = targetType;
        this.monitorType = monitorType;
        this.monitorImpress = monitorImpress;
        this.monitorClick = monitorClick;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }

    public String getMonitorImpress() {
        return monitorImpress;
    }

    public void setMonitorImpress(String monitorImpress) {
        this.monitorImpress = monitorImpress;
    }

    public String getMonitorClick() {
        return monitorClick;
    }

    public void setMonitorClick(String monitorClick) {
        this.monitorClick = monitorClick;
    }

    @Override
    public String toString() {
        return "YunBanner{" +
                "picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                ", targetId='" + targetId + '\'' +
                ", backgroundUrl='" + backgroundUrl + '\'' +
                ", targetType=" + targetType +
                ", monitorType='" + monitorType + '\'' +
                ", monitorImpress='" + monitorImpress + '\'' +
                ", monitorClick='" + monitorClick + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picUrl);
        dest.writeString(this.url);
        dest.writeString(this.targetId);
        dest.writeString(this.backgroundUrl);
        dest.writeInt(this.targetType);
        dest.writeString(this.monitorType);
        dest.writeString(this.monitorImpress);
        dest.writeString(this.monitorClick);
    }

    protected YunBanner(Parcel in) {
        this.picUrl = in.readString();
        this.url = in.readString();
        this.targetId = in.readString();
        this.backgroundUrl = in.readString();
        this.targetType = in.readInt();
        this.monitorType = in.readString();
        this.monitorImpress = in.readString();
        this.monitorClick = in.readString();
    }

    public static final Creator<YunBanner> CREATOR = new Creator<YunBanner>() {
        @Override
        public YunBanner createFromParcel(Parcel source) {
            return new YunBanner(source);
        }

        @Override
        public YunBanner[] newArray(int size) {
            return new YunBanner[size];
        }
    };
}
