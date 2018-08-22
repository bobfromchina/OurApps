package com.example.wangbo.ourapp.http;

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
import com.jackmar.jframelibray.http.base.BaseResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by JackMar on 2017/2/23.
 * <p>
 * 接口地址
 */
public interface ApiNet {

    /**
     * 获取首页banner
     */
    @POST("/test/app/index/banners.html?")
    @FormUrlEncoded
    Observable<BaseResult<BannerBean>> getBanner(@Field("invitationCode") String invitationCode);

    /**
     * 获取首页的列表
     */
    @POST("/app/controller/homePageList")
    @FormUrlEncoded
    Observable<BaseResult<List<NewsBean>>> getHomeList(@Field("page") int page, @Field("current") int pages);

    /**
     * 我关注的新闻
     */
    @POST("/app/controller/commentNewsList")
    @FormUrlEncoded
    Observable<BaseResult<List<NewsBean>>> mineNewList(@Field("page") int page, @Field("token") String token);

    /**
     * 获取验证码
     */
    @POST("/app/controller/getLoginCode")
    @FormUrlEncoded
    Observable<BaseResult<VerifyBean>> getLoginCode(@Field("phone") String phones, @Field("type") int type);

    /**
     * 登录
     */
    @POST("/app/controller/login")
    @FormUrlEncoded
    Observable<BaseResult<UserEntity>> login(@Field("loginKey") String key);

    /**
     * 获取首页详情
     */
    @POST("/app/controller/homeDetails")
    @FormUrlEncoded
    Observable<BaseResult<HomeDetailsBean>> getHomeListDetails(@Field("id") String id);

    /**
     * 获取用户信息
     */
    @POST("/app/controller/userInfo")
    @FormUrlEncoded
    Observable<BaseResult<PersonInfo>> getUserInfo(@Field("token") String token);

    /**
     * 修改用户信息
     */
    @POST("/app/controller/modifyUserInfo")
    @FormUrlEncoded
    Observable<BaseResult<String>> modifyUserInfo(@Field("name") String name, @Field("sex") String sex, @Field("token") String token);

    /**
     * 获取体检列表
     */
    @POST("/app/controller/tiJianList")
    @FormUrlEncoded
    Observable<BaseResult<List<HotMealsBean>>> getTiJianList(@Field("zoneId") String zoneId);

    /**
     * 获取天气预报
     */
    @POST("/app/controller/getWeather")
    @FormUrlEncoded
    Observable<BaseResult<List<WeatherBean>>> getWeather(@Field("city") String city);

    /**
     * 获取记事本列表
     */
    @POST("/app/controller/getNoteBookList")
    @FormUrlEncoded
    Observable<BaseResult<List<ThreeBean>>> getNotBookList(@Field("page") int page, @Field("token") String token);

    /**
     * 获取记事本列表
     */
    @POST("/app/controller/oldNoteList")
    @FormUrlEncoded
    Observable<BaseResult<List<ThreeBean>>> getOldNoteBook(@Field("page") int page, @Field("token") String token);

    /**
     * 获取记事本列表
     */
    @POST("/app/controller/addNoteBook")
    @FormUrlEncoded
    Observable<BaseResult<String>> addNoteBook(@Field("title") String titles, @Field("content") String contents, @Field("id") String id, @Field("token") String token, @Field("type") int type);

    /**
     * 删除记事本的数据
     */
    @POST("/app/controller/deleteNoteBook")
    @FormUrlEncoded
    Observable<BaseResult<String>> deleteNotebook(@Field("id") String id, @Field("token") String userId);

    /**
     * 推荐歌曲
     */
    @POST("/app/controller/getCommentMusicList")
    @FormUrlEncoded
    Observable<BaseResult<List<CommentMusicListBean>>> getCommentList(@Field("token") String userId);

    /**
     * 推荐云banner
     */
    @POST("/app/controller/getYunBanner")
    Observable<BaseResult<List<YunBanner>>> yunBanner();

    /**
     * 天气详情
     */
    @POST("/app/controller/getWeatherDetails")
    @FormUrlEncoded
    Observable<BaseResult<WeatherDetails>> getWeatherDetails(@Field("city") String city);

    /**
     * 验证记事本密码
     */
    @POST("/app/controller/verifyNotePwd")
    @FormUrlEncoded
    Observable<BaseResult<String>> verifyPwd(@Field("pwd") CharSequence text, @Field("token") String userId);

    /**
     * 用户注册
     */
    @POST("/app/controller/register")
    @FormUrlEncoded
    Observable<BaseResult<String>> register(@Field("userPhone") String mobile, @Field("code") String code, @Field("pwd") String pwd, @Field("messageId") String messageId);

    /**
     * 用户忘记密码
     */
    @POST("/app/controller/forgotLoginPwd")
    @FormUrlEncoded
    Observable<BaseResult<String>> forgotPwd(@Field("userPhone") String mobile, @Field("code") String code, @Field("pwd") String pwd, @Field("messageId") String messageId);

    /**
     * 修改密码
     */
    @POST("/app/controller/modifyPwd")
    @FormUrlEncoded
    Observable<BaseResult<String>> modifyPwd(@Field("oldPwd") String oldPwd, @Field("newPwd") String newPwd, @Field("type") int type, @Field("token") String token);

    /**
     * 新闻关注
     */
    @POST("/app/controller/commentNews")
    @FormUrlEncoded
    Observable<BaseResult<NewsBean>> commentNews(@Field("link") String linkUrl, @Field("title") String title, @Field("source") String author, @Field("tag") String tag,
                                                 @Field("picInfo") String pic, @Field("token") String userId);

    /**
     * 新闻取关
     */
    @POST("/app/controller/cancelCommentNews")
    @FormUrlEncoded
    Observable<BaseResult<NewsBean>> cancelCommentNews(@Field("id") String id, @Field("token") String userId);

    /**
     * 意见反馈
     */
    @POST("/app/controller/feedBook")
    @FormUrlEncoded
    Observable<BaseResult<String>> feedBook(@Field("feedContext") String back_book, @Field("phone") String back_phone);

    /**
     * 推荐歌单的详情
     */
    @POST("/app/controller/playListDetails")
    @FormUrlEncoded
    Observable<BaseResult<MusicListBean>> getPlayListDetails(@Field("id") String id);

    /**
     * 推荐歌曲链接
     */
    @POST("/app/controller/songUrlDetails")
    @FormUrlEncoded
    Observable<BaseResult<String>> getSongUrlDetails(@Field("id") String id);

    /**
     * 推荐歌曲链接
     */
    @POST("/app/controller/getEveryDayCommentMusicList")
    @FormUrlEncoded
    Observable<BaseResult<List<CommentSoongsBean>>> getCommentSongs(@Field("id") String s);

    /**
     * 获取歌单列表
     */
    @POST("/app/controller/songOrderList")
    @FormUrlEncoded
    Observable<BaseResult<List<SongListBean>>> songlist(@Field("id") String i);

    /**
     * 获取电台主播 今日优选
     */
    @POST("/app/controller/djRecommend")
    @FormUrlEncoded
    Observable<BaseResult<List<DjRecommend>>> getDjCommend(@Field("id") String s);
}
