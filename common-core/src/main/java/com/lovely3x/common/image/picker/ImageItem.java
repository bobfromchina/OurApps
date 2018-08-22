package com.lovely3x.common.image.picker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class ImageItem implements Parcelable, Comparable<ImageItem> {
    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
    public long date;
    private String imageId;
    private String thumbnailPath;
    private String imagePath;

    public ImageItem() {
    }

    protected ImageItem(Parcel in) {
        this.imageId = in.readString();
        this.thumbnailPath = in.readString();
        this.imagePath = in.readString();
        this.date = in.readLong();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageId);
        dest.writeString(this.thumbnailPath);
        dest.writeString(this.imagePath);
        dest.writeLong(this.date);
    }

    @Override
    public int compareTo(@NonNull ImageItem another) {
        if (date > another.date) {
            return -1;
        } else if (date < another.date) {
            return 1;
        }
        return 0;
    }
}
