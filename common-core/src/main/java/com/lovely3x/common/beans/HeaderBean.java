package com.lovely3x.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by handsome-Bob on 2017/5/29.
 */

public class HeaderBean implements Parcelable{
    private String savePath;//":"upload\/users\/2017-05\/","
    private String name;//":"592b9f02e3694.png","
    private String thumb;//":"592b9f02e3694.png"
    private String domain;


    public HeaderBean() {
    }

    public HeaderBean(String savePath, String name, String thumb, String domain) {
        this.savePath = savePath;
        this.name = name;
        this.thumb = thumb;
        this.domain = domain;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "HeaderBean{" +
                "savePath='" + savePath + '\'' +
                ", name='" + name + '\'' +
                ", thumb='" + thumb + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.savePath);
        dest.writeString(this.name);
        dest.writeString(this.thumb);
        dest.writeString(this.domain);
    }

    protected HeaderBean(Parcel in) {
        this.savePath = in.readString();
        this.name = in.readString();
        this.thumb = in.readString();
        this.domain = in.readString();
    }

    public static final Creator<HeaderBean> CREATOR = new Creator<HeaderBean>() {
        @Override
        public HeaderBean createFromParcel(Parcel source) {
            return new HeaderBean(source);
        }

        @Override
        public HeaderBean[] newArray(int size) {
            return new HeaderBean[size];
        }
    };
}
