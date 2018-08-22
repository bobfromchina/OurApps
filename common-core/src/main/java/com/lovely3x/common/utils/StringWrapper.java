package com.lovely3x.common.utils;

import android.os.Parcel;

/**
 * Created by lovely3x on 17/3/8.
 */

public class StringWrapper implements Displayable {
    private String data;

    public StringWrapper() {
    }

    public StringWrapper(String data) {
        this.data = data;
    }

    @Override
    public String display() {
        return data;
    }

    @Override
    public String getUniqueID() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
    }

    protected StringWrapper(Parcel in) {
        this.data = in.readString();
    }

    public static final Creator<StringWrapper> CREATOR = new Creator<StringWrapper>() {
        @Override
        public StringWrapper createFromParcel(Parcel source) {
            return new StringWrapper(source);
        }

        @Override
        public StringWrapper[] newArray(int size) {
            return new StringWrapper[size];
        }
    };

    @Override
    public String toString() {
        return data;
    }
}
