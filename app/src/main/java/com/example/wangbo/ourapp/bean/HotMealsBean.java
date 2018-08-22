package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wangbo on 2018/7/4.
 * <p>
 * 套餐的Bean
 */
public class HotMealsBean implements Parcelable {


    //    "id": "integer,套餐id",
//            "mealName": "string,套餐名",
//            "mealOriginalPrice": "decimal,套餐原价",
//            "mealDiscountDrice": "decimal,套餐折后价",
//            "mealDiscount": "double,套餐折扣",
//            "mealLabel": "string,套餐标签",
//            "organizName": "string,体检机构",
//            "organizDetailAddress": "string,机构地址"

    private String id;//套餐id
    private String mealName;//套餐名
    private String mealOriginalPrice;//套餐原价
    private String mealDiscountPrice;//套餐折后价
    private String mealDiscount;//套餐折扣
    private List<String> mealLabel;//套餐标签
    private String organizName;//体检机构
    private String organizDetailAddress;//机构地址

    public HotMealsBean() {
    }

    public HotMealsBean(String id, String mealName, String mealOriginalPrice, String mealDiscountPrice, String mealDiscount, List<String> mealLabel, String organizName, String organizDetailAddress) {
        this.id = id;
        this.mealName = mealName;
        this.mealOriginalPrice = mealOriginalPrice;
        this.mealDiscountPrice = mealDiscountPrice;
        this.mealDiscount = mealDiscount;
        this.mealLabel = mealLabel;
        this.organizName = organizName;
        this.organizDetailAddress = organizDetailAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealOriginalPrice() {
        return mealOriginalPrice;
    }

    public void setMealOriginalPrice(String mealOriginalPrice) {
        this.mealOriginalPrice = mealOriginalPrice;
    }

    public String getMealDiscountPrice() {
        return mealDiscountPrice;
    }

    public void setMealDiscountPrice(String mealDiscountPrice) {
        this.mealDiscountPrice = mealDiscountPrice;
    }

    public String getMealDiscount() {
        return mealDiscount;
    }

    public void setMealDiscount(String mealDiscount) {
        this.mealDiscount = mealDiscount;
    }

    public List<String> getMealLabel() {
        return mealLabel;
    }

    public void setMealLabel(List<String> mealLabel) {
        this.mealLabel = mealLabel;
    }

    public String getOrganizName() {
        return organizName;
    }

    public void setOrganizName(String organizName) {
        this.organizName = organizName;
    }

    public String getOrganizDetailAddress() {
        return organizDetailAddress;
    }

    public void setOrganizDetailAddress(String organizDetailAddress) {
        this.organizDetailAddress = organizDetailAddress;
    }

    @Override
    public String toString() {
        return "HotMealsBean{" +
                "id='" + id + '\'' +
                ", mealName='" + mealName + '\'' +
                ", mealOriginalPrice='" + mealOriginalPrice + '\'' +
                ", mealDiscountPrice='" + mealDiscountPrice + '\'' +
                ", mealDiscount='" + mealDiscount + '\'' +
                ", mealLabel=" + mealLabel +
                ", organizName='" + organizName + '\'' +
                ", organizDetailAddress='" + organizDetailAddress + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mealName);
        dest.writeString(this.mealOriginalPrice);
        dest.writeString(this.mealDiscountPrice);
        dest.writeString(this.mealDiscount);
        dest.writeStringList(this.mealLabel);
        dest.writeString(this.organizName);
        dest.writeString(this.organizDetailAddress);
    }

    protected HotMealsBean(Parcel in) {
        this.id = in.readString();
        this.mealName = in.readString();
        this.mealOriginalPrice = in.readString();
        this.mealDiscountPrice = in.readString();
        this.mealDiscount = in.readString();
        this.mealLabel = in.createStringArrayList();
        this.organizName = in.readString();
        this.organizDetailAddress = in.readString();
    }

    public static final Creator<HotMealsBean> CREATOR = new Creator<HotMealsBean>() {
        @Override
        public HotMealsBean createFromParcel(Parcel source) {
            return new HotMealsBean(source);
        }

        @Override
        public HotMealsBean[] newArray(int size) {
            return new HotMealsBean[size];
        }
    };
}
