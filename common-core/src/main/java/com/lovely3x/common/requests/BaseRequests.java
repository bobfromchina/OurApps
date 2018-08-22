package com.lovely3x.common.requests;

import android.support.annotation.NonNull;

import com.lovely3x.common.CommonApplication;

/**
 * 基础请求器
 * Created by lovely3x on 15-11-16.
 */
public abstract class BaseRequests {
    protected final CommonApplication mContext;
    protected final BaseCodeTable mBaseCodeTable;
    protected final BaseURLConst mURLConst;
    protected String TAG = "Requests";
    protected boolean mDebug = Config.DEBUG;


    public BaseRequests(@NonNull CommonApplication context, @NonNull BaseCodeTable table, @NonNull BaseURLConst urlConst) {
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

    /**
     * 验证码类型
     */
    public enum VerifyCodeType {

        /**
         * 用于注册的验证码
         */
        register(0),

        /**
         * 忘记密码中的获取验证码
         */
        forgotPassword(1),
        /**
         * 忘记支付密码
         */
        forgotpaymentpassword(2),

        /**
         * 更换绑定手机原手机号
         */
        modifyBindPhoneoldphonenumber(3),
        /**
         * 更换手机绑定新手机号
         */
        modifyBindPhonenewphonenumber(4),
        /**
         * 三方绑定手机
         */
        threebindphonenumber(5),

        /**
         * 修改登录密码
         */
        modifyPassword(7);

        private final int mValue;

        VerifyCodeType(int value) {
            this.mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

}
