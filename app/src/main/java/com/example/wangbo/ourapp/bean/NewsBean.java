package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by handsome-Bob on 2017/10/27.
 * <p>
 * 关于资讯的Bean
 */
public class NewsBean implements Parcelable {

    private List<String> images;

    private String author;

    private String title;

    private int type;

    private String id;

    private String createTime;

    private String linkUrl;

    private String tag;

    private int followStatus;

    private String picInfo;

    public NewsBean() {
    }

    public NewsBean(List<String> images, String author, String title, int type, String id, String createTime, String linkUrl, String tag, int followStatus, String picInfo) {
        this.images = images;
        this.author = author;
        this.title = title;
        this.type = type;
        this.id = id;
        this.createTime = createTime;
        this.linkUrl = linkUrl;
        this.tag = tag;
        this.followStatus = followStatus;
        this.picInfo = picInfo;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public String getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(String picInfo) {
        this.picInfo = picInfo;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "images=" + images +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", tag='" + tag + '\'' +
                ", followStatus=" + followStatus +
                ", picInfo='" + picInfo + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.images);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeString(this.id);
        dest.writeString(this.createTime);
        dest.writeString(this.linkUrl);
        dest.writeString(this.tag);
        dest.writeInt(this.followStatus);
        dest.writeString(this.picInfo);
    }

    protected NewsBean(Parcel in) {
        this.images = in.createStringArrayList();
        this.author = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.id = in.readString();
        this.createTime = in.readString();
        this.linkUrl = in.readString();
        this.tag = in.readString();
        this.followStatus = in.readInt();
        this.picInfo = in.readString();
    }

    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel source) {
            return new NewsBean(source);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };
}
