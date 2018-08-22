package com.example.wangbo.ourapp.bean;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wangbo on 2018/8/15.
 * <p>
 * 推荐的歌单
 */
public class CommentSoongsBean  implements Parcelable{

    private String albumName;
    private String albumCompany;
    private String artId;
    private String artName;
    private String albumSubType;
    private String musicId;
    private String albumPic;
    private String musicName;
    private List<String> alias;

    public CommentSoongsBean() {
    }

    public CommentSoongsBean(String albumName, String albumCompany, String artId, String artName, String albumSubType, String musicId, String albumPic, String musicName, List<String> alias) {
        this.albumName = albumName;
        this.albumCompany = albumCompany;
        this.artId = artId;
        this.artName = artName;
        this.albumSubType = albumSubType;
        this.musicId = musicId;
        this.albumPic = albumPic;
        this.musicName = musicName;
        this.alias = alias;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCompany() {
        return albumCompany;
    }

    public void setAlbumCompany(String albumCompany) {
        this.albumCompany = albumCompany;
    }

    public String getArtId() {
        return artId;
    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getAlbumSubType() {
        return albumSubType;
    }

    public void setAlbumSubType(String albumSubType) {
        this.albumSubType = albumSubType;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "CommentSoongsBean{" +
                "albumName='" + albumName + '\'' +
                ", albumCompany='" + albumCompany + '\'' +
                ", artId='" + artId + '\'' +
                ", artName='" + artName + '\'' +
                ", albumSubType='" + albumSubType + '\'' +
                ", musicId='" + musicId + '\'' +
                ", albumPic='" + albumPic + '\'' +
                ", musicName='" + musicName + '\'' +
                ", alias=" + alias +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumName);
        dest.writeString(this.albumCompany);
        dest.writeString(this.artId);
        dest.writeString(this.artName);
        dest.writeString(this.albumSubType);
        dest.writeString(this.musicId);
        dest.writeString(this.albumPic);
        dest.writeString(this.musicName);
        dest.writeStringList(this.alias);
    }

    protected CommentSoongsBean(Parcel in) {
        this.albumName = in.readString();
        this.albumCompany = in.readString();
        this.artId = in.readString();
        this.artName = in.readString();
        this.albumSubType = in.readString();
        this.musicId = in.readString();
        this.albumPic = in.readString();
        this.musicName = in.readString();
        this.alias = in.createStringArrayList();
    }

    public static final Creator<CommentSoongsBean> CREATOR = new Creator<CommentSoongsBean>() {
        @Override
        public CommentSoongsBean createFromParcel(Parcel source) {
            return new CommentSoongsBean(source);
        }

        @Override
        public CommentSoongsBean[] newArray(int size) {
            return new CommentSoongsBean[size];
        }
    };
}
