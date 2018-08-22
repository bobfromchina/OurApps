package com.example.wangbo.ourapp.base;

import java.util.List;

/**
 * Created by wangbo on 2018/7/18.
 * <p>
 * banner的数据
 */
public class BannerBean {

    public List<BannerList> bannerList;

    public List<BannerList> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerList> bannerList) {
        this.bannerList = bannerList;
    }

    public class BannerList {

        private String bannerId;//bannerId

        private String targetUrl;//H5链接地址

        private String goodsId;//商品Id

        private String topicId;//专题ID

        private String bannerImgUrl;//bannerImg

        private String bannerType;//类型 1资讯 2专题

        private String targetName;

        public BannerList() {
        }

        public BannerList(String bannerId, String targetUrl, String goodsId, String topicId, String bannerImgUrl, String bannerType, String targetName) {
            this.bannerId = bannerId;
            this.targetUrl = targetUrl;
            this.goodsId = goodsId;
            this.topicId = topicId;
            this.bannerImgUrl = bannerImgUrl;
            this.bannerType = bannerType;
            this.targetName = targetName;
        }

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getTargetUrl() {
            return targetUrl;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getBannerImgUrl() {
            return bannerImgUrl;
        }

        public void setBannerImgUrl(String bannerImgUrl) {
            this.bannerImgUrl = bannerImgUrl;
        }

        public String getBannerType() {
            return bannerType;
        }

        public void setBannerType(String bannerType) {
            this.bannerType = bannerType;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        @Override
        public String toString() {
            return "BannerBean{" +
                    "bannerId='" + bannerId + '\'' +
                    ", targetUrl='" + targetUrl + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", topicId='" + topicId + '\'' +
                    ", bannerImgUrl='" + bannerImgUrl + '\'' +
                    ", bannerType='" + bannerType + '\'' +
                    ", targetName='" + targetName + '\'' +
                    '}';
        }

    }


}
