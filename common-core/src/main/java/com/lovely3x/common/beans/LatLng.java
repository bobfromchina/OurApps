package com.lovely3x.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 描述经纬度实体
 *
 * @author lovely3x
 * @version 1.0
 * @time 2015-4-29 下午2:56:08
 */
public class LatLng implements Serializable, Parcelable {

    private static final long serialVersionUID = 1L;
    /**
     * 经纬度
     */
    public double lat;
    /**
     * 经纬度
     */
    public double lng;
    /**
     * 可描述的位置
     */
    public String location;

    public LatLng() {

    }


    public LatLng(double lat, double lng, String location) {
        this.lat = lat;
        this.lng = lng;
        this.location = location;
    }

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "LatLng [lat=" + lat + ", lng=" + lng + ", location=" + location + "]";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.location);
    }

    protected LatLng(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.location = in.readString();
    }

    public static final Parcelable.Creator<LatLng> CREATOR = new Parcelable.Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel source) {
            return new LatLng(source);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };
}
