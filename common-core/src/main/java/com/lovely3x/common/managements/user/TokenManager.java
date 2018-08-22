package com.lovely3x.common.managements.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lovely3x on 15-7-22.
 * 认证令牌
 */
public class TokenManager {

    /**
     * tokenManager 的 文件名
     */
    private static final String TOKEN_FILE_NAME = "tfn";
    /**
     * token的值
     */
    private static final String TOKEN_VALUE_KEY = "token_value";
    /**
     * token的保存时间
     */
    private static final String TOKEN_FIRST_TIME_KEY = "token_first_time";
    /**
     * token的更新时间
     */
    private static final String TOKEN_LAST_TIME_KEY = "token_last_time";
    /**
     * 最大的toke操作时间间隙
     * <p/>
     * 默认保存72个小时的毫秒值,这个值服务器应该是和服务器端协商的
     */
    public static long MAX_TIME_INTERVAL = 1000L * 60 * 60 * 24 * 30;
    /**
     * token值
     */
    private static TokenManager tokenManager;

    private Context mContext;
    /**
     * token的保存时间
     */
    private long tokenFirstTime = -1;
    /**
     * token最后更新的
     */
    private long tokenLastTime = -1;
    /**
     * tokenManager 的值
     */
    private String tokeValue;


    private TokenManager() {

    }

    /**
     * 获取Token实例
     *
     * @return 获取Token实例
     */
    public static TokenManager getInstance() {
        synchronized (TokenManager.class) {
            if (tokenManager == null) {
                synchronized (TokenManager.class) {
                    tokenManager = new TokenManager();
                }
            }
        }
        return tokenManager;
    }

    /**
     * @param context 上下文对象建议使用applicationContext
     */
    public void init(Context context) {
        if (mContext == null) {
            this.mContext = context.getApplicationContext();
            invalidateMemoryData();
        }
    }

    /**
     * 获取token的有效时间
     *
     * @return token的保存时间
     */
    public long getTokenValidTime() {
        return MAX_TIME_INTERVAL;
    }

    /**
     * 设置token的有效时间
     *
     * @param tokenValidTime token的有效时间 毫秒
     */
    public void setTokenValidTime(long tokenValidTime) {
        if (tokenValidTime > 0) {
            MAX_TIME_INTERVAL = tokenValidTime;
        }
    }

    /**
     * 获取token的保存时间
     *
     * @return token的保存时间
     */
    public long getTokenFirstTime() {
        return tokenFirstTime;
    }

    /**
     * 获取token的最后更新时间
     *
     * @return token的最后更新时间
     */
    public long getTokenLastTime() {
        return tokenLastTime;
    }

    /**
     * 获取token的值
     *
     * @return 获取token的值
     */
    public synchronized String getTokeValue() {
        final long interval = System.currentTimeMillis() - tokenLastTime;
        if (interval > MAX_TIME_INTERVAL) {//如果已经过期,清除掉记录
            clearTokenRecord();
        }
        return tokeValue;
    }

    /**
     * 更新token的最后更新时间戳
     */
    public synchronized void updateTokeLastTime() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        final long time = System.currentTimeMillis();
        tokenLastTime = time;
        editor.putLong(TOKEN_LAST_TIME_KEY, time);
        editor.apply();
    }

    /**
     * token的值设置 会自动设置firstTime 和 Last Time
     *
     * @param tokenValue 需要保存的token 值
     */
    public synchronized void setTokenValue(String tokenValue) {
        if (tokenValue == null) {
            throw new RuntimeException("Error:tokenValue is null. if  you want to clear the tokenValue,please call clear method.");
        }
        final long time = System.currentTimeMillis();
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(TOKEN_FIRST_TIME_KEY, time);
        editor.putLong(TOKEN_LAST_TIME_KEY, time);
        editor.putString(TOKEN_VALUE_KEY, tokenValue);
        editor.commit();
        this.tokenLastTime = time;
        this.tokenFirstTime = time;
        this.tokeValue = tokenValue;
    }


    /**
     * 清除token的保存记录
     */
    public boolean clearTokenRecord() {
        tokeValue = null;
        tokenFirstTime = -1;
        tokenLastTime = -1;
        return getSharedPreferences().edit().clear().commit();
    }


    /**
     * 获取偏好设置文件
     *
     * @return 偏好设置文件
     */
    private SharedPreferences getSharedPreferences() {
        if (mContext == null) throw new RuntimeException("please init on call before.");
        return mContext.getSharedPreferences(TOKEN_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 从文件中读取数据出来,刷新内存中的数据
     */
    public void invalidateMemoryData() {
        SharedPreferences sp = getSharedPreferences();
        tokenLastTime = sp.getLong(TOKEN_LAST_TIME_KEY, -1);
        tokenFirstTime = sp.getLong(TOKEN_FIRST_TIME_KEY, -1);
        tokeValue = sp.getString(TOKEN_VALUE_KEY, null);
    }

    @Override
    public String toString() {
        return "TokenManager{" +
                "tokenFirstTime=" + tokenFirstTime +
                ", tokenLastTime=" + tokenLastTime +
                ", tokeValue='" + tokeValue + '\'' +
                '}';
    }
}
