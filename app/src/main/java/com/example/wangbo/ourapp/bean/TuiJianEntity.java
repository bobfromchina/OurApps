package com.example.wangbo.ourapp.bean;

import java.util.List;

/**
 * Created by wangbo on 2018/7/19.
 */

public class TuiJianEntity {

    private List<RecommendGoodsBean> list;

    public TuiJianEntity(List<RecommendGoodsBean> list) {
        this.list = list;
    }

    public List<RecommendGoodsBean> getList() {
        return list;
    }

    public void setList(List<RecommendGoodsBean> list) {
        this.list = list;
    }

    public class RecommendGoodsBean {

        private String goodsId;
        private String goodsName;
        private String goodsImg;
        private String marketPrice;
        private String shopPrice;
        private String goodsTips;
        private int isVirtual;


        private String image;
        private String goodsMoney;


        public RecommendGoodsBean() {

        }

        public RecommendGoodsBean(String goodsId, String goodsName, String goodsImg, String marketPrice, String shopPrice, String goodsTips, int isVirtual, String image, String goodsMoney) {
            this.goodsId = goodsId;
            this.goodsName = goodsName;
            this.goodsImg = goodsImg;
            this.marketPrice = marketPrice;
            this.shopPrice = shopPrice;
            this.goodsTips = goodsTips;
            this.isVirtual = isVirtual;
            this.image = image;
            this.goodsMoney = goodsMoney;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getShopPrice() {
            return shopPrice;
        }

        public void setShopPrice(String shopPrice) {
            this.shopPrice = shopPrice;
        }

        public String getGoodsTips() {
            return goodsTips;
        }

        public void setGoodsTips(String goodsTips) {
            this.goodsTips = goodsTips;
        }

        public int getIsVirtual() {
            return isVirtual;
        }

        public void setIsVirtual(int isVirtual) {
            this.isVirtual = isVirtual;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getGoodsMoney() {
            return goodsMoney;
        }

        public void setGoodsMoney(String goodsMoney) {
            this.goodsMoney = goodsMoney;
        }

        @Override
        public String toString() {
            return "RecommendGoodsBean{" +
                    "goodsId='" + goodsId + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsImg='" + goodsImg + '\'' +
                    ", marketPrice='" + marketPrice + '\'' +
                    ", shopPrice='" + shopPrice + '\'' +
                    ", goodsTips='" + goodsTips + '\'' +
                    ", isVirtual=" + isVirtual +
                    ", image='" + image + '\'' +
                    ", goodsMoney='" + goodsMoney + '\'' +
                    '}';
        }
    }


}
