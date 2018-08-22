package com.lovely3x.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接监听器
 * Created by lovely3x on 16/6/19.
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String TAG = "ConnectivityReceiver";

    /**
     * 网络变化监听器
     */
    private static final List<ConnectivityListener> CONNECTIVITY_LISTENERS = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION://网络连接性变化
            {
                ArrayList<ConnectivityListener> backup = new ArrayList<>(CONNECTIVITY_LISTENERS);
                boolean hasNetwork = NetUtils.hasNetWork();
                int type = NetUtils.getNetWorkType();

                for (ConnectivityListener listener : backup) {
                    try {
                        listener.onConnectivityChanged(hasNetwork, type);
                    } catch (Exception e) {
                        ALog.w(TAG, e);
                    }
                }

            }
            break;
            case com.lovely3x.common.utils.ConnectivityManager.HOST_ACCESSIBILITY_CHANGED_ACTION://主机访问性变化
            {
                ArrayList<ConnectivityListener> backup = new ArrayList<>(CONNECTIVITY_LISTENERS);
                boolean isConnected = intent.getBooleanExtra(com.lovely3x.common.utils.ConnectivityManager.EXTRA_HOST_IS_CONNECTED, false);
                for (ConnectivityListener listener : backup) {
                    try {
                        listener.onHostAccessibilityChanged(isConnected);
                    } catch (Exception e) {
                        ALog.w(TAG, e);
                    }
                }
            }
            break;
        }

    }

    /**
     * 添加连接变化监听器
     *
     * @param connectivityListener 需要添加的连接变化监听器
     * @return 是否添加成功
     */
    public static boolean addConnectivityListener(ConnectivityListener connectivityListener) {
        return connectivityListener != null && CONNECTIVITY_LISTENERS.add(connectivityListener);
    }

    /**
     * 移除连接变化监听器
     *
     * @param connectivityListener 需要移除的连接监听器
     * @return 是否移除成功
     */
    public static boolean removeConnectivityListener(ConnectivityListener connectivityListener) {
        return connectivityListener != null && CONNECTIVITY_LISTENERS.remove(connectivityListener);
    }

    /**
     * 连接变化监听器
     */
    public interface ConnectivityListener {
        /**
         * 当网络变化后调用
         *
         * @param hasNetwork 当前是否有网络
         * @param type       当前的网络类型
         */
        void onConnectivityChanged(boolean hasNetwork, int type);

        /**
         * 当和服务器的连接状态发生变化后调用
         *
         * @param isConnected 是否可以连接到服务器
         */
        void onHostAccessibilityChanged(boolean isConnected);
    }
}
