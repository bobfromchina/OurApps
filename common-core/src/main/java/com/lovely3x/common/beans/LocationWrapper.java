package com.lovely3x.common.beans;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.Table;

/**
 * Created by lovely3x on 15-9-10.
 * 位置包装类
 */
public class LocationWrapper implements Cloneable, Parcelable {

    /**
     * 纬度
     */
    private double latitude;
    /**
     * 纬度
     */
    private double longitude;
    /**
     * 全限定名
     */
    private String qualifiedName;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区(县)
     */
    private String district;

    /**
     * 坐标类型
     */
    private CoorType coorType;

    /**
     * 点生成的时间戳
     */
    private long time;

    /**
     * 点的高度
     */
    private double alt;
    /**
     * 精确度
     */
    private float accuracy;

    private String provider;

    private Location originalLocation;

    public LocationWrapper() {
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "LocationWrapper{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                '}';
    }

    @Override
    public LocationWrapper clone() {
        try {
            return (LocationWrapper) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CoorType getCoorType() {
        return coorType;
    }

    public void setCoorType(CoorType coorType) {
        this.coorType = coorType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(Location originalLocation) {
        this.originalLocation = originalLocation;
    }


    /**
     * 经纬度类型
     */
    public enum CoorType {
        /**
         * 火星坐标系
         */
        GCJ_02("gcj02"),
        /**
         * GPS坐标系
         */
        WGS_84("wgs84"),
        /**
         * 百度09坐标系
         */
        BD_09("bd_09");

        CoorType(String coorType) {
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.qualifiedName);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeInt(this.coorType == null ? -1 : this.coorType.ordinal());
        dest.writeLong(this.time);
        dest.writeDouble(this.alt);
        dest.writeFloat(this.accuracy);
        dest.writeString(this.provider);
        dest.writeParcelable(this.originalLocation, 0);
    }

    protected LocationWrapper(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.qualifiedName = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        int tmpCoorType = in.readInt();
        this.coorType = tmpCoorType == -1 ? null : CoorType.values()[tmpCoorType];
        this.time = in.readLong();
        this.alt = in.readDouble();
        this.accuracy = in.readFloat();
        this.provider = in.readString();
        this.originalLocation = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<LocationWrapper> CREATOR = new Creator<LocationWrapper>() {
        public LocationWrapper createFromParcel(Parcel source) {
            return new LocationWrapper(source);
        }

        public LocationWrapper[] newArray(int size) {
            return new LocationWrapper[size];
        }
    };
}
