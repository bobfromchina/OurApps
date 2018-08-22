package com.lovely3x.common.image.picker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ImageBucket implements Parcelable, Comparable<ImageBucket> {
    public static final Creator<ImageBucket> CREATOR = new Creator<ImageBucket>() {
        public ImageBucket createFromParcel(Parcel source) {
            return new ImageBucket(source);
        }

        public ImageBucket[] newArray(int size) {
            return new ImageBucket[size];
        }
    };
    public int count;
    public String bucketName;
    public ArrayList<ImageItem> bucketList;

    public ImageBucket() {
    }

    protected ImageBucket(Parcel in) {
        this.count = in.readInt();
        this.bucketName = in.readString();
        this.bucketList = in.createTypedArrayList(ImageItem.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeString(this.bucketName);
        dest.writeTypedList(bucketList);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public List<ImageItem> getBucketList() {
        return bucketList;
    }

    public void setBucketList(ArrayList<ImageItem> bucketList) {
        this.bucketList = bucketList;
    }

    @Override
    public int compareTo(@NonNull ImageBucket another) {
        long anotherDate = another.bucketList.get(0).date;
        long currentDate = bucketList.get(0).date;
        if (anotherDate > currentDate) {
            return 1;
        } else if (anotherDate < currentDate) {
            return -1;
        }
        return 0;
    }
}
