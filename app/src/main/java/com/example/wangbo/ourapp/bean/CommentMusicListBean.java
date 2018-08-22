package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbo on 2018/7/31.
 */

public class CommentMusicListBean implements Parcelable {

    private String id;
    private int type;
    private String name;
    private String copywriter;
    private String picUrl;
    private boolean canDislike;
    private double playCount;
    private int trackCount;
    private boolean highQuality;
    private String alg;


    public CommentMusicListBean() {
    }

    public CommentMusicListBean(String id, int type, String name, String copywriter, String picUrl, boolean canDislike, double playCount, int trackCount, boolean highQuality, String alg) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.copywriter = copywriter;
        this.picUrl = picUrl;
        this.canDislike = canDislike;
        this.playCount = playCount;
        this.trackCount = trackCount;
        this.highQuality = highQuality;
        this.alg = alg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCopywriter() {
        return copywriter;
    }

    public void setCopywriter(String copywriter) {
        this.copywriter = copywriter;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isCanDislike() {
        return canDislike;
    }

    public void setCanDislike(boolean canDislike) {
        this.canDislike = canDislike;
    }

    public double getPlayCount() {
        return playCount;
    }

    public void setPlayCount(double playCount) {
        this.playCount = playCount;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public boolean isHighQuality() {
        return highQuality;
    }

    public void setHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    @Override
    public String toString() {
        return "CommentMusicListBean{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", copywriter='" + copywriter + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", canDislike=" + canDislike +
                ", playCount=" + playCount +
                ", trackCount=" + trackCount +
                ", highQuality=" + highQuality +
                ", alg='" + alg + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.copywriter);
        dest.writeString(this.picUrl);
        dest.writeByte(this.canDislike ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.playCount);
        dest.writeInt(this.trackCount);
        dest.writeByte(this.highQuality ? (byte) 1 : (byte) 0);
        dest.writeString(this.alg);
    }

    protected CommentMusicListBean(Parcel in) {
        this.id = in.readString();
        this.type = in.readInt();
        this.name = in.readString();
        this.copywriter = in.readString();
        this.picUrl = in.readString();
        this.canDislike = in.readByte() != 0;
        this.playCount = in.readDouble();
        this.trackCount = in.readInt();
        this.highQuality = in.readByte() != 0;
        this.alg = in.readString();
    }

    public static final Creator<CommentMusicListBean> CREATOR = new Creator<CommentMusicListBean>() {
        @Override
        public CommentMusicListBean createFromParcel(Parcel source) {
            return new CommentMusicListBean(source);
        }

        @Override
        public CommentMusicListBean[] newArray(int size) {
            return new CommentMusicListBean[size];
        }
    };
}
