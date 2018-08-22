package com.jackmar.jframelibray.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.adapter.ImgBrowserPagerAdapter;


/**
 * 图片描述对象
 * Created by lovely3x on 15-12-8.
 */
public class Img implements Parcelable {

    /**
     * 图片的类型
     * 支持的图片类型有 FILE_URI,HTTP(S)_URL,DRAWABLE_RES,ASSETS，BITMAP(需要使用bitmap这个字段，不能用这个字段)
     */
    private int imgType;

    /**
     * 图片对象
     */
    private String img;

    /***
     * 需要加载的图片对象
     * 在指定图片类型为 bitmap后就需要用这个字段来指定
     */
    private Bitmap imgBitmap;


    /**
     * 加载失败显示的图片资源
     */
    private int failureImgRes = R.drawable.icon_loading_failure;
    /**
     * 加载中显示的资源
     */
    private int loadingImgRes = R.drawable.icon_loading;

    /**
     * 加载图片失败显示的图片对象
     */
    private Bitmap failureImgBitmap;
    /**
     * 加载中显示的图片
     */
    private Bitmap loadingImgBitmap;

    /**
     * 对图片的描述
     */
    private String desc;

    /**
     * 图片的名字
     */
    private String name;

    public Img() {
    }

    public Img(Bitmap imgBitmap) {
        this.imgType = ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP;
        this.imgBitmap = imgBitmap;
    }

    public Img(int imgType, String img) {
        this.imgType = imgType;
        this.img = img;
    }


    public Img(int imgType, String img, Bitmap imgBitmap, int failureImgRes, int loadingImgRes, Bitmap failureImgBitmap, Bitmap loadingImgBitmap) {
        this.imgType = imgType;
        this.img = img;
        this.imgBitmap = imgBitmap;
        this.failureImgRes = failureImgRes;
        this.loadingImgRes = loadingImgRes;
        this.failureImgBitmap = failureImgBitmap;
        this.loadingImgBitmap = loadingImgBitmap;
    }

    public Img(int imgType, String img, Bitmap imgBitmap, int failureImgRes, int loadingImgRes, Bitmap failureImgBitmap, Bitmap loadingImgBitmap, String desc, String name) {
        this.imgType = imgType;
        this.img = img;
        this.imgBitmap = imgBitmap;
        this.failureImgRes = failureImgRes;
        this.loadingImgRes = loadingImgRes;
        this.failureImgBitmap = failureImgBitmap;
        this.loadingImgBitmap = loadingImgBitmap;
        this.desc = desc;
        this.name = name;
    }

    public Img(int imgType, String img, Bitmap imgBitmap, int failureImgRes, int loadingImgRes, Bitmap failureImgBitmap, Bitmap loadingImgBitmap, String desc) {
        this.imgType = imgType;
        this.img = img;
        this.imgBitmap = imgBitmap;
        this.failureImgRes = failureImgRes;
        this.loadingImgRes = loadingImgRes;
        this.failureImgBitmap = failureImgBitmap;
        this.loadingImgBitmap = loadingImgBitmap;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public int getFailureImgRes() {
        return failureImgRes;
    }

    public void setFailureImgRes(int failureImgRes) {
        this.failureImgRes = failureImgRes;
    }

    public int getLoadingImgRes() {
        return loadingImgRes;
    }

    public void setLoadingImgRes(int loadingImgRes) {
        this.loadingImgRes = loadingImgRes;
    }

    public Bitmap getFailureImgBitmap() {
        return failureImgBitmap;
    }

    public void setFailureImgBitmap(Bitmap failureImgBitmap) {
        this.failureImgBitmap = failureImgBitmap;
    }

    public Bitmap getLoadingImgBitmap() {
        return loadingImgBitmap;
    }

    public void setLoadingImgBitmap(Bitmap loadingImgBitmap) {
        this.loadingImgBitmap = loadingImgBitmap;
    }

    public static Creator<Img> getCREATOR() {
        return CREATOR;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Img img1 = (Img) o;

        if (imgType != img1.imgType) return false;
        if (failureImgRes != img1.failureImgRes) return false;
        if (loadingImgRes != img1.loadingImgRes) return false;
        if (img != null ? !img.equals(img1.img) : img1.img != null) return false;
        if (imgBitmap != null ? !imgBitmap.equals(img1.imgBitmap) : img1.imgBitmap != null)
            return false;
        if (failureImgBitmap != null ? !failureImgBitmap.equals(img1.failureImgBitmap) : img1.failureImgBitmap != null)
            return false;
        if (loadingImgBitmap != null ? !loadingImgBitmap.equals(img1.loadingImgBitmap) : img1.loadingImgBitmap != null)
            return false;
        return !(desc != null ? !desc.equals(img1.desc) : img1.desc != null);

    }

    @Override
    public int hashCode() {
        int result = imgType;
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (imgBitmap != null ? imgBitmap.hashCode() : 0);
        result = 31 * result + failureImgRes;
        result = 31 * result + loadingImgRes;
        result = 31 * result + (failureImgBitmap != null ? failureImgBitmap.hashCode() : 0);
        result = 31 * result + (loadingImgBitmap != null ? loadingImgBitmap.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Img{" +
                "imgType=" + imgType +
                ", img='" + img + '\'' +
                ", imgBitmap=" + imgBitmap +
                ", failureImgRes=" + failureImgRes +
                ", loadingImgRes=" + loadingImgRes +
                ", failureImgBitmap=" + failureImgBitmap +
                ", loadingImgBitmap=" + loadingImgBitmap +
                ", desc='" + desc + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imgType);
        dest.writeString(this.img);
        dest.writeParcelable(this.imgBitmap, 0);
        dest.writeInt(this.failureImgRes);
        dest.writeInt(this.loadingImgRes);
        dest.writeParcelable(this.failureImgBitmap, 0);
        dest.writeParcelable(this.loadingImgBitmap, 0);
        dest.writeString(this.desc);
        dest.writeString(this.name);
    }

    protected Img(Parcel in) {
        this.imgType = in.readInt();
        this.img = in.readString();
        this.imgBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.failureImgRes = in.readInt();
        this.loadingImgRes = in.readInt();
        this.failureImgBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.loadingImgBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.desc = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Img> CREATOR = new Creator<Img>() {
        public Img createFromParcel(Parcel source) {
            return new Img(source);
        }

        public Img[] newArray(int size) {
            return new Img[size];
        }
    };
}
