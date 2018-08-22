package com.lovely3x.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.lovely3x.jsonparser.annotations.JSON;

/**
 * 省份
 * Created by lovely3x on 15-11-25.
 */
public class Province implements IArea, Parcelable {

    @JSON("proname")
    private String name;

    @JSON("prosort")
    private int code;

    public Province(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public Province() {
    }

    public String getName() {
        return name;
    }

    @Override
    public int getParentCode() {
        return -1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.code);
    }

    protected Province(Parcel in) {
        this.name = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        public Province createFromParcel(Parcel source) {
            return new Province(source);
        }

        public Province[] newArray(int size) {
            return new Province[size];
        }
    };
}
