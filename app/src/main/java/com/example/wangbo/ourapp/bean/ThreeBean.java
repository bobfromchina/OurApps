package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbo on 2018/7/30.
 */

public class ThreeBean implements Parcelable{

    private String id;

    private String content;

    private String createTime;

    private String lastTime;

    private int status;

    private String descss;

    private int modifyCount;

    private int nowStatus;

    public ThreeBean() {
    }

    public ThreeBean(String id, String content, String createTime, String lastTime, int status, String descss, int modifyCount, int nowStatus) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.lastTime = lastTime;
        this.status = status;
        this.descss = descss;
        this.modifyCount = modifyCount;
        this.nowStatus = nowStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescss() {
        return descss;
    }

    public void setDescss(String descss) {
        this.descss = descss;
    }

    public int getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(int modifyCount) {
        this.modifyCount = modifyCount;
    }

    public int getNowStatus() {
        return nowStatus;
    }

    public void setNowStatus(int nowStatus) {
        this.nowStatus = nowStatus;
    }

    @Override
    public String toString() {
        return "ThreeBean{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastTime='" + lastTime + '\'' +
                ", status=" + status +
                ", descss='" + descss + '\'' +
                ", modifyCount=" + modifyCount +
                ", nowStatus=" + nowStatus +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.createTime);
        dest.writeString(this.lastTime);
        dest.writeInt(this.status);
        dest.writeString(this.descss);
        dest.writeInt(this.modifyCount);
        dest.writeInt(this.nowStatus);
    }

    protected ThreeBean(Parcel in) {
        this.id = in.readString();
        this.content = in.readString();
        this.createTime = in.readString();
        this.lastTime = in.readString();
        this.status = in.readInt();
        this.descss = in.readString();
        this.modifyCount = in.readInt();
        this.nowStatus = in.readInt();
    }

    public static final Creator<ThreeBean> CREATOR = new Creator<ThreeBean>() {
        @Override
        public ThreeBean createFromParcel(Parcel source) {
            return new ThreeBean(source);
        }

        @Override
        public ThreeBean[] newArray(int size) {
            return new ThreeBean[size];
        }
    };
}
