package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbo on 2018/8/15.
 */

public class Creators implements Parcelable{

    private String avatarUrl;

    private String nickname;

    private String backgroundUrl;

    public Creators() {
    }

    public Creators(String avatarUrl, String nickname, String backgroundUrl) {
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.backgroundUrl = backgroundUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avatarUrl);
        dest.writeString(this.nickname);
        dest.writeString(this.backgroundUrl);
    }

    protected Creators(Parcel in) {
        this.avatarUrl = in.readString();
        this.nickname = in.readString();
        this.backgroundUrl = in.readString();
    }

    public static final Creator<Creators> CREATOR = new Creator<Creators>() {
        @Override
        public Creators createFromParcel(Parcel source) {
            return new Creators(source);
        }

        @Override
        public Creators[] newArray(int size) {
            return new Creators[size];
        }
    };
}
