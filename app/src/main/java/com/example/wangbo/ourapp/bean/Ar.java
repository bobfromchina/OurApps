package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangbo on 2018/8/7.
 */

public class Ar implements Parcelable{

    private String id;

        private String name;

        public Ar() {
        }

        public Ar(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Ar{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    protected Ar(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Ar> CREATOR = new Creator<Ar>() {
        @Override
        public Ar createFromParcel(Parcel source) {
            return new Ar(source);
        }

        @Override
        public Ar[] newArray(int size) {
            return new Ar[size];
        }
    };
}
