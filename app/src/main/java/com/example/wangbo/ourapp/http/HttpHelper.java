package com.example.wangbo.ourapp.http;


import android.content.Context;

import com.example.wangbo.ourapp.base.BannerBean;
import com.example.wangbo.ourapp.bean.CommentMusicListBean;
import com.example.wangbo.ourapp.bean.CommentSoongsBean;
import com.example.wangbo.ourapp.bean.DjRecommend;
import com.example.wangbo.ourapp.bean.MusicListBean;
import com.example.wangbo.ourapp.bean.NewsBean;
import com.example.wangbo.ourapp.bean.HomeDetailsBean;
import com.example.wangbo.ourapp.bean.HotMealsBean;
import com.example.wangbo.ourapp.bean.PersonInfo;
import com.example.wangbo.ourapp.bean.SongListBean;
import com.example.wangbo.ourapp.bean.ThreeBean;
import com.example.wangbo.ourapp.bean.UserEntity;
import com.example.wangbo.ourapp.bean.VerifyBean;
import com.example.wangbo.ourapp.bean.WeatherBean;
import com.example.wangbo.ourapp.bean.WeatherDetails;
import com.example.wangbo.ourapp.bean.YunBanner;
import com.jackmar.jframelibray.http.NetWorkCenter;
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber;
import com.jackmar.jframelibray.http.util.HttpResultFunc;
import com.jackmar.jframelibray.http.util.RXJavaUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import rx.Observable;

/**
 * 接口控制
 */
public class HttpHelper {
    //接口类
    private ApiNet mApiA, mApiB;

    //私有的构造方法
    private HttpHelper(Context context) {
        //手动创建一个OkHttpClient并设置超时时间
        mApiA = NetWorkCenter.getApi(ApiNet.class, context, null);
        mApiB = NetWorkCenter.getApiB(ApiNet.class, context, null);
    }

    //获取单例
    public static HttpHelper getInstance(Context context) {
        return new HttpHelper(context);
    }

    //获取首页banner
    public void homeBanner(String invitationCode, ProgressSubscriber<BannerBean> subscriber) {
        Observable<BannerBean> observable = mApiB.getBanner(invitationCode).map(new HttpResultFunc<BannerBean>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void homeList(int page,int currentIndex, ProgressSubscriber<List<NewsBean>> subscriber) {
        Observable<List<NewsBean>> observable = mApiA.getHomeList(page,currentIndex).map(new HttpResultFunc<List<NewsBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void mineNewList(int page, String token,ProgressSubscriber<List<NewsBean>> subscriber) {
        Observable<List<NewsBean>> observable = mApiA.mineNewList(page,token).map(new HttpResultFunc<List<NewsBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getLoginCode(String phones, int type, ProgressSubscriber<VerifyBean> subscriber) {
        Observable<VerifyBean> observable = mApiA.getLoginCode(phones, type).map(new HttpResultFunc<VerifyBean>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void login(String key, ProgressSubscriber<UserEntity> subscriber) {
        Observable<UserEntity> observable = mApiA.login(key).map(new HttpResultFunc<UserEntity>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getHomeDetails(String id, ProgressSubscriber<HomeDetailsBean> subscriber) {
        Observable<HomeDetailsBean> observable = mApiA.getHomeListDetails(id).map(new HttpResultFunc<HomeDetailsBean>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getUserInfo(String token, ProgressSubscriber<PersonInfo> subscriber) {
        Observable<PersonInfo> observable = mApiA.getUserInfo(token).map(new HttpResultFunc<PersonInfo>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void modifyUserInfo(String name,  String sex, String token, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.modifyUserInfo(name, sex, token).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getTiJianList(String zoneId, ProgressSubscriber<List<HotMealsBean>> subscriber) {
        Observable<List<HotMealsBean>> observable = mApiA.getTiJianList(zoneId).map(new HttpResultFunc<List<HotMealsBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getWeather(String city, ProgressSubscriber<List<WeatherBean>> subscriber) {
        Observable<List<WeatherBean>> observable = mApiA.getWeather(city).map(new HttpResultFunc<List<WeatherBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getNoteBook(int page, String token, ProgressSubscriber<List<ThreeBean>> subscriber) {
        Observable<List<ThreeBean>> observable = mApiA.getNotBookList(page, token).map(new HttpResultFunc<List<ThreeBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getOldNoteBook(int page, String token, ProgressSubscriber<List<ThreeBean>> subscriber) {
        Observable<List<ThreeBean>> observable = mApiA.getOldNoteBook(page, token).map(new HttpResultFunc<List<ThreeBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void addNoteBook(String titles, String contents, String id, String token, int type, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.addNoteBook(titles, contents, id, token, type).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void deleteNotebook(String id, String userId, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.deleteNotebook(id, userId).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getCommentList(String userId, ProgressSubscriber<List<CommentMusicListBean>> subscriber) {
        Observable<List<CommentMusicListBean>> observable = mApiA.getCommentList(userId).map(new HttpResultFunc<List<CommentMusicListBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void yunBanner(ProgressSubscriber<List<YunBanner>> subscriber) {
        Observable<List<YunBanner>> observable = mApiA.yunBanner().map(new HttpResultFunc<List<YunBanner>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getWeatherDetails(String city, ProgressSubscriber<WeatherDetails> subscriber) {
        Observable<WeatherDetails> observable = mApiA.getWeatherDetails(city).map(new HttpResultFunc<WeatherDetails>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void validePwd(CharSequence text, String userId, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.verifyPwd(text, userId).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void register(String mobile, String code, String pwd, String messageId, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.register(mobile, code, pwd, messageId).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void forgotPwd(String mobile, String code, String pwd, String messageId, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.forgotPwd(mobile, code, pwd, messageId).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void modifyPwd(String oldPwd, String newPwd, int type, String token, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.modifyPwd(oldPwd, newPwd, type, token).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void commentnews(String linkUrl, String title, String author, String tag, String pic, String userId, ProgressSubscriber<NewsBean> subscriber) {
        Observable<NewsBean> observable = mApiA.commentNews(linkUrl, title, author, tag,pic,userId).map(new HttpResultFunc<NewsBean>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void cancelCommentNews(String id, String userId, ProgressSubscriber<NewsBean> subscriber) {
        Observable<NewsBean> observable = mApiA.cancelCommentNews(id, userId).map(new HttpResultFunc<NewsBean>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void feedBook(String back_book, String back_phone, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.feedBook(back_book, back_phone).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getPlayListDetails(String id, ProgressSubscriber<MusicListBean> subscriber) {
        Observable<MusicListBean> observable = mApiA.getPlayListDetails(id).map(new HttpResultFunc<MusicListBean>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getSongUrlDetails(String id, ProgressSubscriber<String> subscriber) {
        Observable<String> observable = mApiA.getSongUrlDetails(id).map(new HttpResultFunc<String>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getCommentSongs(String s, ProgressSubscriber<List<CommentSoongsBean>> subscriber) {
        Observable<List<CommentSoongsBean>> observable = mApiA.getCommentSongs(s).map(new HttpResultFunc<List<CommentSoongsBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void songlist(String i, ProgressSubscriber<List<SongListBean>> subscriber) {
        Observable<List<SongListBean>> observable = mApiA.songlist(i).map(new HttpResultFunc<List<SongListBean>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }

    public void getDjCommend(String s, ProgressSubscriber<List<DjRecommend>> subscriber) {
        Observable<List<DjRecommend>> observable = mApiA.getDjCommend(s).map(new HttpResultFunc<List<DjRecommend>>());
        RXJavaUtil.toSubscribe(observable, subscriber);
    }
}
