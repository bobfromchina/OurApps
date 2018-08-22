package com.lovely3x.common.activities.emptytip;

import android.view.View;

/**
 * Created by lovely3x on 15-8-16.
 * 空内容提示activity
 * <p/>
 * 当前没有内容时提示
 * 或者正在加载提示
 * 加载完成取消提示
 * 推荐子类实现这个类的状态变化视图切换方法
 */
public interface IEmptyContentTip {

    /**
     * 当前的状态是 当前没有可以显示的内容
     */
    int EMPTY_CONTENT = 1;
    /**
     * 当前的状态是 当前正在加载内容
     */
    int LOADING_CONTENT = 2;
    /**
     * 当前的状态是 加载成功
     */
    int LOADING_SUCCESSFUL = 3;

    /**
     * 当前的状态是 加载失败
     */
    int LOADING_FAILURE = 4;

    /**
     * 当没有内容可供显示时调用这个方法
     */
    void handleEmptyContent();

    /**
     * 当没有内容可供显示时调用这个方法
     */
    void handleEmptyContent(View.OnClickListener listener);

    /**
     * 当正在加载数据时调用这个方法
     */
    void handleLoadingContent();

    /**
     * 当数据加载完成后调用这个方法
     */
    void handleLoadSuccessful();

    /**
     * 切换到失败
     *
     * @param errorMsg 失败提示的信息
     */
    void handleLoadFailure(String errorMsg);

    /**
     * 切换到失败状态
     *
     * @param errorMsg      失败的错误信息
     * @param retryListener 重试监听器
     */
    void handleLoadFailure(String errorMsg, View.OnClickListener retryListener);

    /**
     * 当加载失败后调用这个方法
     *
     * @param errorCode 失败的错误代码
     *                  用户可以根据这个错误代码来进行不同的失败提示
     */
    void handleLoadFailure(int errorCode);

    /**
     * 当加载失败后调用这个方法
     *
     * @param errorCode     失败的错误代码
     *                      用户可以根据这个错误代码来进行不同的失败提示
     * @param retryListener 点击监听器
     */
    void handleLoadFailure(int errorCode, View.OnClickListener retryListener);

}
