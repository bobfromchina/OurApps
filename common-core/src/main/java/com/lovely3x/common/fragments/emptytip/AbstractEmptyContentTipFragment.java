package com.lovely3x.common.fragments.emptytip;

import android.view.View;

import com.lovely3x.common.activities.emptytip.IEmptyContentTip;
import com.lovely3x.common.fragments.BaseCommonFragment;

/**
 * 默认的空内容提示界面
 * Created by lovely3x on 16-1-21.
 */
public abstract class AbstractEmptyContentTipFragment extends BaseCommonFragment implements IEmptyContentTip {

    /**
     * 子类通过这个方法来通知当前的状态发生变化
     * 注意,这个方法不能通知 加载失败,加载失败请使用 带errorCode的方法
     *
     * @param status 当前的状态
     */
    public void onContentStatusChanged(int status) {
        switch (status) {
            case EMPTY_CONTENT:
                handleEmptyContent();
                break;
            case LOADING_CONTENT:
                handleLoadingContent();
                break;
            case LOADING_SUCCESSFUL:
                handleLoadSuccessful();
                break;
        }
    }

    /**
     * 子类通过这个方法来通知当前的状态发生变化
     *
     * @param status    当前的状态
     * @param errorCode 错误码
     */
    public void onContentStatusChanged(int status, int errorCode) {
        switch (status) {
            case EMPTY_CONTENT:
                handleEmptyContent();
                break;
            case LOADING_CONTENT:
                handleLoadingContent();
                break;
            case LOADING_SUCCESSFUL:
                handleLoadSuccessful();
                break;
            case LOADING_FAILURE:
                handleLoadFailure(errorCode);
                break;
        }
    }


    /**
     * 子类通过这个方法来通知当前的状态发生变化
     *
     * @param status   当前的状态
     * @param errorMsg 错误信息
     */
    public void onContentStatusChanged(int status, String errorMsg) {
        switch (status) {
            case EMPTY_CONTENT:
                handleEmptyContent();
                break;
            case LOADING_CONTENT:
                handleLoadingContent();
                break;
            case LOADING_SUCCESSFUL:
                handleLoadSuccessful();
                break;
            case LOADING_FAILURE:
                handleLoadFailure(errorMsg);
                break;
        }
    }


    /**
     * 子类通过这个方法来通知当前的状态发生变化
     *
     * @param status        当前的状态
     * @param errorMsg      错误信息
     * @param retryListener 点击重试监听器
     */
    public void onContentStatusChanged(int status, String errorMsg, View.OnClickListener retryListener) {
        switch (status) {
            case EMPTY_CONTENT:
                handleEmptyContent();
                break;
            case LOADING_CONTENT:
                handleLoadingContent();
                break;
            case LOADING_SUCCESSFUL:
                handleLoadSuccessful();
                break;
            case LOADING_FAILURE:
                handleLoadFailure(errorMsg, retryListener);
                break;
        }
    }


    /**
     * 子类通过这个方法来通知当前的状态发生变化
     *
     * @param status        当前的状态
     * @param errorCode     错误代码
     * @param retryListener 点击重试监听器
     */
    public void onContentStatusChanged(int status, int errorCode, View.OnClickListener retryListener) {
        switch (status) {
            case EMPTY_CONTENT:
                handleEmptyContent();
                break;
            case LOADING_CONTENT:
                handleLoadingContent();
                break;
            case LOADING_SUCCESSFUL:
                handleLoadSuccessful();
                break;
            case LOADING_FAILURE:
                handleLoadFailure(errorCode, retryListener);
                break;
        }
    }
}
