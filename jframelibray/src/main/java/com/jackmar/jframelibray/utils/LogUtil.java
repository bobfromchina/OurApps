package com.jackmar.jframelibray.utils;

import android.util.Log;


/**
 * 日志打印工具
 * Created by JackMar on 2016/12/7.
 */
public class LogUtil {
    private static final String TAG = "jackmar--->";
    // 是否显示Log消息
    private static boolean isShow = true;

    public static boolean isDebug() {
        return isShow;
    }

    public static void setDebug(boolean show) {
        isShow = show;
    }

    public static void i(String tag, String msg) {
        if (isShow)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isShow)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isShow)
            Log.e(tag, msg);
    }

    public static void all(String msg) {
        if (isShow)
            Log.e("all", msg);
    }

    public static void i(String msg) {
        if (isShow)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (isShow)
            Log.w(TAG, msg);
    }

    public static void e(String msg) {
        if (isShow)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isShow)
            Log.v(TAG, msg);
    }

    public static void d(String msg) {
        if (isShow)
            Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (isShow)
            Log.d(tag, msg);
    }

    public static void w(String tag, String msg, Exception e) {
        if (isShow)
            Log.w(tag, msg, e);
    }

    public static void v(String tag, String msg) {
        if (isShow)
            Log.v(tag, msg);
    }
}
