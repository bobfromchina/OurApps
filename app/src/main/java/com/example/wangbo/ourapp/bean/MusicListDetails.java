package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangbo on 2018/8/7.
 */

public class MusicListDetails implements Parcelable{

    private String name;

    private String id;

    private List<String> alia;

    private String publishTime;

    List<Al> al;
//
    List<Ar> ar;

    public MusicListDetails() {
    }

    public MusicListDetails(String name, String id, List<String> alia, String publishTime, List<Al> al, List<Ar> ar) {
        this.name = name;
        this.id = id;
        this.alia = alia;
        this.publishTime = publishTime;
        this.al = al;
        this.ar = ar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAlia() {
        return alia;
    }

    public void setAlia(List<String> alia) {
        this.alia = alia;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<Al> getAl() {
        return al;
    }

    public void setAl(List<Al> al) {
        this.al = al;
    }

    public List<Ar> getAr() {
        return ar;
    }

    public void setAr(List<Ar> ar) {
        this.ar = ar;
    }


    @Override
    public String toString() {
        return "MusicListDetails{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", alia=" + alia +
                ", publishTime='" + publishTime + '\'' +
                ", al=" + al +
                ", ar=" + ar +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeStringList(this.alia);
        dest.writeString(this.publishTime);
        dest.writeList(this.al);
        dest.writeList(this.ar);
    }

    protected MusicListDetails(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.alia = in.createStringArrayList();
        this.publishTime = in.readString();
        this.al = new ArrayList<Al>();
        in.readList(this.al, Al.class.getClassLoader());
        this.ar = new ArrayList<Ar>();
        in.readList(this.ar, Ar.class.getClassLoader());
    }

    public static final Creator<MusicListDetails> CREATOR = new Creator<MusicListDetails>() {
        @Override
        public MusicListDetails createFromParcel(Parcel source) {
            return new MusicListDetails(source);
        }

        @Override
        public MusicListDetails[] newArray(int size) {
            return new MusicListDetails[size];
        }
    };
}
