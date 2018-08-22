package com.lovely3x.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.lovely3x.jsonparser.annotations.JSON;

/**
 * 城市选择中的城市对象
 *
 * @author lovely3x
 * @version 1.0
 * @time 2015-4-28 上午10:59:55
 */
public class City implements Cloneable, IArea, Parcelable {

    private int id;

    private int parentId;

    @JSON("name")
    private String name;

    @JSON("cid")
    private int code;

    public City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(int parentId, String name, int code) {
        this.parentId = parentId;
        this.name = name;
        this.code = code;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + parentId;
        result = prime * result + code;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getParentCode() {
        return parentId;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        City other = (City) obj;
        if (parentId != other.parentId)
            return false;
        if (code != other.code)
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "City [id=" + id + ", cityId=" + parentId + ", name=" + name + ", code=" + code + "]";
    }

    @Override
    public City clone() {
        try {
            return (City) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.parentId);
        dest.writeString(this.name);
        dest.writeInt(this.code);
    }

    protected City(Parcel in) {
        this.id = in.readInt();
        this.parentId = in.readInt();
        this.name = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
