package com.lovely3x.common.requests;

import android.support.annotation.NonNull;

import com.lovely3x.common.CommonApplication;

/**
 * 基础请求器
 * Created by lovely3x on 15-11-16.
 */
public abstract class CommonRequests {
    protected final CommonApplication mContext;
    protected final BaseCodeTable mBaseCodeTable;
    protected final BaseURLConst mURLConst;
    protected java.lang.String TAG = null;
    protected boolean mDebug = Config.DEBUG;

    public CommonRequests(@NonNull CommonApplication context, @NonNull BaseCodeTable table, @NonNull BaseURLConst urlConst) {
        TAG = getClass().getSimpleName();
        this.mContext = context;
        this.mBaseCodeTable = table;
        this.mURLConst = urlConst;
    }

    /**
     * 是否是处于debug模式
     *
     * @return true 或 false
     */
    public boolean isDebugMode() {
        return mDebug;
    }

    /**
     * 设置debug模式
     *
     * @param debugMode 是否开启debug模式
     */
    public void setDebugMode(boolean debugMode) {
        mDebug = debugMode;
    }



}
