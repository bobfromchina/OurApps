package com.lovely3x.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接工具
 * 主要就是判断当前的网络是否打开
 * 及其是什么类型的网络
 *
 * @author lovely3x
 * @version 1.0
 * @time 2015-4-15 下午4:13:48
 */
public class NetUtils {

    private static Context context;

    /**
     * 建议在程序初始化调用一次
     *
     * @param context
     */
    public static void init(Context context) {
        NetUtils.context = context;
    }

    /**
     * 判断当前网络是否可用
     *
     * @return true if current network is available, else return false
     */
    public static final boolean hasNetWork() {
        if (context == null) {
            throw new IllegalArgumentException("context can be not null.");
        }
        ConnectivityManager connectionManager = getConnectivityManager();
        NetworkInfo currentNetInfo = connectionManager.getActiveNetworkInfo();
        return currentNetInfo != null && currentNetInfo.isAvailable();
    }

    /**
     * 是否是WIFI连接
     *
     * @return
     */
    public static final boolean isWifiConnected() {
        if (!hasNetWork()) {
            return false;
        }
        ConnectivityManager connectionManager = getConnectivityManager();
        NetworkInfo currentNetInfo = connectionManager.getActiveNetworkInfo();
        int type = currentNetInfo.getType();
        return type == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 是否是数据链接
     *
     * @return
     */
    public static final boolean isDataConnected() {
        if (!hasNetWork()) {
            return false;
        }
        ConnectivityManager connectionManager = getConnectivityManager();
        NetworkInfo currentNetInfo = connectionManager.getActiveNetworkInfo();
        int type = currentNetInfo.getType();
        return type == ConnectivityManager.TYPE_MOBILE;
    }


    /**
     * 获取链接管理器
     *
     * @return
     */
    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取当前的网络类型
     *
     * @return
     */
    public static int getNetWorkType() {
        if (hasNetWork()) {
            ConnectivityManager connectionManager = getConnectivityManager();
            NetworkInfo currentNetInfo = connectionManager.getActiveNetworkInfo();
            return currentNetInfo.getType();
        }
        return -1;
    }


    /**
     * 移除所有的代理
     */
    public static void removeAllProxies() {
        //Proxy.NO_PROXY
        System.getProperties().remove("http.proxyHost");
        System.getProperties().remove("http.proxyPort");
        System.getProperties().remove("https.proxyHost");
        System.getProperties().remove("https.proxyPort");
    }
}
