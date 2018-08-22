package com.lovely3x.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.lovely3x.jsonparser.annotations.JSON;

/**
 * 地区
 * Created by lovely3x on 15-12-10.
 */
public class District implements IArea, Parcelable {

    private int parentCode;
    @JSON("district_id")
    private int code;
    @JSON("disname")
    private String name;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getParentCode() {
        return parentCode;
    }

    public District() {
    }

    public District(int parentCode, int code, String name) {
        this.parentCode = parentCode;
        this.code = code;
        this.name = name;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "District{" +
                "parentCode=" + parentCode +
                ", code=" + code +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.parentCode);
        dest.writeInt(this.code);
        dest.writeString(this.name);
    }

    protected District(Parcel in) {
        this.parentCode = in.readInt();
        this.code = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<District> CREATOR = new Parcelable.Creator<District>() {
        public District createFromParcel(Parcel source) {
            return new District(source);
        }

        public District[] newArray(int size) {
            return new District[size];
        }
    };
}
