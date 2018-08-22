package com.example.wangbo.ourapp.bean;

import java.util.List;

/**
 * Created by JackMar on 2018/4/9.
 */

public class UserEntity {

    private UserBean user;

    List<CommentMusicListBean> music;

    List<NewsBean> news;

    List<CommentSoongsBean> commentSong;

    List<YunBanner> banner;

    private String tokenId;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<CommentMusicListBean> getMusic() {
        return music;
    }

    public void setMusic(List<CommentMusicListBean> music) {
        this.music = music;
    }

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public List<CommentSoongsBean> getCommentSong() {
        return commentSong;
    }

    public void setCommentSong(List<CommentSoongsBean> commentSong) {
        this.commentSong = commentSong;
    }

    public List<YunBanner> getBanner() {
        return banner;
    }

    public void setBanner(List<YunBanner> banner) {
        this.banner = banner;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public static class UserBean {

        private String tokenId;
        private int id;
        private String nikename;
        private String headimage;
        private String name;
        private String idcode;
        private String bank;
        private String openaccount;
        private String openbank;
        private int isrealname;
        private int isbandbank;
        private int count;
        private String isShow;
        private int isbuy;

        public String getTokenId() {
            return tokenId;
        }

        public void setTokenId(String tokenId) {
            this.tokenId = tokenId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getHeadimage() {
            return headimage;
        }

        public void setHeadimage(String headimage) {
            this.headimage = headimage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdcode() {
            return idcode;
        }

        public void setIdcode(String idcode) {
            this.idcode = idcode;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getOpenaccount() {
            return openaccount;
        }

        public void setOpenaccount(String openaccount) {
            this.openaccount = openaccount;
        }

        public String getOpenbank() {
            return openbank;
        }

        public void setOpenbank(String openbank) {
            this.openbank = openbank;
        }

        public int getIsrealname() {
            return isrealname;
        }

        public void setIsrealname(int isrealname) {
            this.isrealname = isrealname;
        }

        public int getIsbandbank() {
            return isbandbank;
        }

        public void setIsbandbank(int isbandbank) {
            this.isbandbank = isbandbank;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getIsShow() {
            return isShow;
        }

        public void setIsShow(String isShow) {
            this.isShow = isShow;
        }

        public int getIsbuy() {
            return isbuy;
        }

        public void setIsbuy(int isbuy) {
            this.isbuy = isbuy;
        }
    }
}
