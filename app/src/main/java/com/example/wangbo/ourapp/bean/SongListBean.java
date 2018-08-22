package com.example.wangbo.ourapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wangbo on 2018/8/15.
 */

public class SongListBean implements Parcelable{

    private String name;

    private String id;

    private String coverImgUrl;

    private List<String> tags;

    private Creators creator;

    private int playCount;

    public SongListBean() {
    }

    public SongListBean(String name, String id, String coverImgUrl, List<String> tags, Creators creator, int playCount) {
        this.name = name;
        this.id = id;
        this.coverImgUrl = coverImgUrl;
        this.tags = tags;
        this.creator = creator;
        this.playCount = playCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Creators getCreator() {
        return creator;
    }

    public void setCreator(Creators creator) {
        this.creator = creator;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    @Override
    public String toString() {
        return "SongListBean{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", tags=" + tags +
                ", creator=" + creator +
                ", playCount=" + playCount +
                '}';
    }



    //    "name": "分手不必勉强",
//            "id": 2357882874,
//            "trackNumberUpdateTime": 1533955945453,
//            "status": 0,
//            "userId": 1550202783,
//            "createTime": 1533955944673,
//            "updateTime": 1533955945453,
//            "subscribedCount": 0,
//            "trackCount": 79,
//            "cloudTrackCount": 0,
//            "coverImgUrl": "http://p1.music.126.net/PV630HGTPO4N49CLPTILLA==/109951163213836694.jpg",
//            "coverImgId": 109951163213836694,
//            "description": null,
//            "tags": [],
//            "playCount": 13,
//            "trackUpdateTime": 1533955945512,
//            "specialType": 0,
//            "totalDuration": 0,
//            "creator": {
//        "defaultAvatar": false,
//                "province": 450000,
//                "authStatus": 0,
//                "followed": false,
//                "avatarUrl": "http://p1.music.126.net/0rZRXk5nB0EfJ0j2943Ybg==/109951163452839748.jpg",
//                "accountStatus": 0,
//                "gender": 2,
//                "city": 450100,
//                "birthday": -2209017600000,
//                "userId": 1550202783,
//                "userType": 0,
//                "nickname": "1阿平哥",
//                "signature": "",
//                "description": "",
//                "detailDescription": "",
//                "avatarImgId": 109951163452839748,
//                "backgroundImgId": 109951162868128395,
//                "backgroundUrl": "http://p1.music.126.net/2zSNIqTcpHL2jIvU6hG0EA==/109951162868128395.jpg",
//                "authority": 0,
//                "mutual": false,
//                "expertTags": null,
//                "experts": null,
//                "djStatus": 0,
//                "vipType": 0,
//                "remarkName": null,
//                "avatarImgIdStr": "109951163452839748",
//                "backgroundImgIdStr": "109951162868128395",
//                "avatarImgId_str": "109951163452839748"
//    },
//            "tracks": null,
//            "subscribers": [],
//            "subscribed": false,
//            "commentThreadId": "A_PL_0_2357882874",
//            "newImported": false,
//            "adType": 0,
//            "highQuality": false,
//            "privacy": 0,
//            "ordered": false,
//            "anonimous": false,
//            "shareCount": 0,
//            "coverImgId_str": "109951163213836694",
//            "commentCount": 0


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.coverImgUrl);
        dest.writeStringList(this.tags);
        dest.writeParcelable(this.creator, flags);
        dest.writeInt(this.playCount);
    }

    protected SongListBean(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.coverImgUrl = in.readString();
        this.tags = in.createStringArrayList();
        this.creator = in.readParcelable(Creators.class.getClassLoader());
        this.playCount = in.readInt();
    }

    public static final Creator<SongListBean> CREATOR = new Creator<SongListBean>() {
        @Override
        public SongListBean createFromParcel(Parcel source) {
            return new SongListBean(source);
        }

        @Override
        public SongListBean[] newArray(int size) {
            return new SongListBean[size];
        }
    };
}
