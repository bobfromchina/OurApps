package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by handsome-Bob on 2017/9/5.
 * <p>
 * 验证码Bean
 */
public class VerifyBean implements Parcelable{
    private String result;//": "success",
    private String messageId;//": "E6E2585407954C97-1-15E50CE352A-2000001AA"
    private String code;

    public VerifyBean() {
    }

    public VerifyBean(String result, String messageId, String code) {
        this.result = result;
        this.messageId = messageId;
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "VerifyBean{" +
                "result='" + result + '\'' +
                ", messageId='" + messageId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.result);
        dest.writeString(this.messageId);
        dest.writeString(this.code);
    }

    protected VerifyBean(Parcel in) {
        this.result = in.readString();
        this.messageId = in.readString();
        this.code = in.readString();
    }

    public static final Creator<VerifyBean> CREATOR = new Creator<VerifyBean>() {
        @Override
        public VerifyBean createFromParcel(Parcel source) {
            return new VerifyBean(source);
        }

        @Override
        public VerifyBean[] newArray(int size) {
            return new VerifyBean[size];
        }
    };
}
