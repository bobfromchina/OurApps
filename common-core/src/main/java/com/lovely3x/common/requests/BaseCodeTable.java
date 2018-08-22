package com.lovely3x.common.requests;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.lovely3x.common.utils.Response;

/**
 * 码表
 * Created by lovely3x on 15-11-16.
 */
public abstract class BaseCodeTable {

    private static BaseCodeTable instance;

    /**
     * 获取码表的实例状态
     *
     * @return 码表实例
     */
    public static BaseCodeTable getInstance() {
        initCheck();
        return instance;
    }

    /**
     * 初始化检查
     */
    private static void initCheck() {
        if (instance == null) throw new IllegalStateException("请还未初始化，请先初始化#init");
    }

    @MainThread
    public static void init(@NonNull BaseCodeTable codeTable) {
        instance = codeTable;
    }


    /**
     * 获取无网络连接错误码
     *
     * @return 无网络连接错误码
     */
    public abstract int getNoNetWorkErrorCode();

    /**
     * 获取未知错误描述码
     *
     * @return 未知错误描述码
     */
    public abstract int getUnknownErrorCode();

    /**
     * 获取服务端响应码错误码
     *
     * @param responseCode 服务端返回的非 200 的 Http 响应码
     * @return 本地对应的服务端响应错误码, 所有的都返回一样的, 也可以根据不同的http码返回不同的
     */
    public abstract int getResponseErrorCode(int responseCode);


    /**
     * 获取对应码的描述
     *
     * @param code 需要获取描述的返回码
     * @return 描述
     */
    public abstract
    @NonNull
    String getCodeDescription(int code);

    /**
     * 获取代码描述
     *
     * @param response 相应对象
     * @return 描述
     */
    public abstract String getCodeDescription(Response response);


    /**
     * 获取解析错误码
     *
     * @param e 异常对象
     * @return 解析错误码
     */
    public abstract int getParseExceptionErrorCode(Exception e);

    /**
     * 获取网络异常代码
     *
     * @return 获取网络异常代码
     */
    public abstract int getNetWorkExceptionCode();


    /**
     * 获取请求成功的返回码
     *
     * @return 请求成功的code
     */
    public abstract int getSuccessfulCode();

    /**
     * 获取激光推送标识异常码
     */
    public abstract int getJPushTagExceptionCode();

    /**
     * 获取清除用户数据失败的代码
     *
     * @return 清除数据失败的代码
     */
    public abstract int getClearUserDataFailureCode();


    /**
     * 获取尚未登陆的code值
     *
     * @return 尚未登陆的code值
     */
    public abstract int getNotLoginCode();

    /**
     * 获取空数据code值
     *
     * @return 空数据code值
     */
    public abstract int getEmptyDataCode();


    /**
     * 获取用户取消操作的错误码
     *
     * @return 用户取消操作
     */
    public abstract int getCanceledExceptionCode();

    /**
     * 获取没有绑定手机的错误码
     * @return
     */
    public abstract int getNotBindPhoneCode();
}
