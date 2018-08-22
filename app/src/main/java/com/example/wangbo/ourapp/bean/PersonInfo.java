package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by handsome-Bob on 2017/9/5.
 *
 *  个人资料
 */

public class PersonInfo implements Parcelable{

    private String username;//姓名
    private String userPhone;//手机
    private String newNum;
    private String noteNum;
    private String sex;//性别
    private String birthday;
    private String age;
    private String headImg;

    public PersonInfo() {

    }

    public PersonInfo(String username, String userPhone, String newNum, String noteNum, String sex, String birthday, String age, String headImg) {
        this.username = username;
        this.userPhone = userPhone;
        this.newNum = newNum;
        this.noteNum = noteNum;
        this.sex = sex;
        this.birthday = birthday;
        this.age = age;
        this.headImg = headImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getNewNum() {
        return newNum;
    }

    public void setNewNum(String newNum) {
        this.newNum = newNum;
    }

    public String getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(String noteNum) {
        this.noteNum = noteNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "username='" + username + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", newNum='" + newNum + '\'' +
                ", noteNum='" + noteNum + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", age='" + age + '\'' +
                ", headImg='" + headImg + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.userPhone);
        dest.writeString(this.newNum);
        dest.writeString(this.noteNum);
        dest.writeString(this.sex);
        dest.writeString(this.birthday);
        dest.writeString(this.age);
        dest.writeString(this.headImg);
    }

    protected PersonInfo(Parcel in) {
        this.username = in.readString();
        this.userPhone = in.readString();
        this.newNum = in.readString();
        this.noteNum = in.readString();
        this.sex = in.readString();
        this.birthday = in.readString();
        this.age = in.readString();
        this.headImg = in.readString();
    }

    public static final Creator<PersonInfo> CREATOR = new Creator<PersonInfo>() {
        @Override
        public PersonInfo createFromParcel(Parcel source) {
            return new PersonInfo(source);
        }

        @Override
        public PersonInfo[] newArray(int size) {
            return new PersonInfo[size];
        }
    };
}
