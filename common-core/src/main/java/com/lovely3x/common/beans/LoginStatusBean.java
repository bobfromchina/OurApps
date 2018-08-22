package com.lovely3x.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbo on 2018/3/8.
 */

public class LoginStatusBean implements Parcelable{

    private int code;
    private String desc;

    public LoginStatusBean() {
    }

    public LoginStatusBean(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "LoginStatusBean{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.desc);
    }

    protected LoginStatusBean(Parcel in) {
        this.code = in.readInt();
        this.desc = in.readString();
    }

    public static final Creator<LoginStatusBean> CREATOR = new Creator<LoginStatusBean>() {
        @Override
        public LoginStatusBean createFromParcel(Parcel source) {
            return new LoginStatusBean(source);
        }

        @Override
        public LoginStatusBean[] newArray(int size) {
            return new LoginStatusBean[size];
        }
    };
}
