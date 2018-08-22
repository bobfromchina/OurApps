package com.example.wangbo.ourapp.bean;

import java.util.List;

public class MusicListBean {

    private String coverImgUrl;

    private String name;

    private String id;

    private int playCount;

    private List<String> tags;

    private String commentThreadId;

    private int commentCount;

    private int subscribedCount; // 收藏

    private int shareCount; //分享

    private String nickname;

    private String avatarUrl;

    private List<MusicListDetails> tracks;

    public MusicListBean() {

    }

    public MusicListBean(String coverImgUrl, String name, String id, int playCount, List<String> tags, String commentThreadId, int commentCount, int subscribedCount, int shareCount, String nickname, String avatarUrl, List<MusicListDetails> tracks) {
        this.coverImgUrl = coverImgUrl;
        this.name = name;
        this.id = id;
        this.playCount = playCount;
        this.tags = tags;
        this.commentThreadId = commentThreadId;
        this.commentCount = commentCount;
        this.subscribedCount = subscribedCount;
        this.shareCount = shareCount;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.tracks = tracks;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
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

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCommentThreadId() {
        return commentThreadId;
    }

    public void setCommentThreadId(String commentThreadId) {
        this.commentThreadId = commentThreadId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getSubscribedCount() {
        return subscribedCount;
    }

    public void setSubscribedCount(int subscribedCount) {
        this.subscribedCount = subscribedCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<MusicListDetails> getTracks() {
        return tracks;
    }

    public void setTracks(List<MusicListDetails> tracks) {
        this.tracks = tracks;
    }
}
