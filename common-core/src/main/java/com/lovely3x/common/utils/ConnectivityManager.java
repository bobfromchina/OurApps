package com.lovely3x.common.utils;

import android.content.Context;
import android.content.Intent;

import com.lovely3x.common.BuildConfig;
import com.lovely3x.common.requests.BaseURLConst;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 连接管理器
 * Created by lovely3x on 16/8/11.
 */
public class ConnectivityManager {
    private static final String TAG = "ConnectivityManager";

    /**
     * 主机访问性发生变化意图
     */
    public static final String HOST_ACCESSIBILITY_CHANGED_ACTION = BuildConfig.SERVER_HOST_ACCESSIBILITY_CHANGED_ACTION;

    /**
     * 主机和指定的端口访问性发生变化意图
     */
    public static final String HOST_AND_PORT_ACCESSIBILITY_CHANGED_ACTION = BuildConfig.SERVER_HOST_AND_PORT_ACCESSIBILITY_CHANGED_ACTION;

    public static final String EXTRA_HOST_IS_CONNECTED = "extra.host.is.connected";

    private static final ConnectivityManager INSTANCE = new ConnectivityManager();

    private static final int DEFAULT_CHECK_INTERVAL = 1000 * 60;

    private Context mContext;

    private static long connectivityCheckInterval = DEFAULT_CHECK_INTERVAL;

    private static ExecutorService pingThreadPool = Executors.newSingleThreadExecutor();

    private static Thread pingThread = null;

    /**
     * 是否可以连接到主机,默认为"是"
     */
    private boolean isConnected = true;

    private static final Runnable PING_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            try {
                pingThread = Thread.currentThread();
                while (true) {

                    try {
                        final String ipAddress = BaseURLConst.getInstance().getDomain();

                        ALog.d(TAG, "Try to ping " + ipAddress);

                        // Ping ip地址
                        Process p = Runtime.getRuntime().exec("ping -c 1 -w 5 " + ipAddress);

                        int status = p.waitFor();

                        if (status == 0) {
                            onPingResult(true);
                        } else {
                            onPingResult(false);
                        }
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        synchronized (pingThread) {
                            pingThread.wait(connectivityCheckInterval);
                        }
                    } catch (Exception e) {
                        ALog.e(TAG, e);
                    }
                }

            } catch (Exception e) {
                ALog.e(TAG, e);
            } finally {
                pingThread = null;
            }
        }
    };

    /**
     * ping主机的的结果
     *
     * @param hasConnected 是否连接到主机
     */
    private static void onPingResult(boolean hasConnected) {
        ALog.d(TAG, "Ping result => " + hasConnected);

        //连接状态发生变化,发送广播
        if (INSTANCE.isConnected != hasConnected) {
            INSTANCE.isConnected = hasConnected;

            //Send broadcast
            Intent pingResultIntent = new Intent(HOST_ACCESSIBILITY_CHANGED_ACTION);
            pingResultIntent.putExtra(EXTRA_HOST_IS_CONNECTED, hasConnected);
            INSTANCE.mContext.sendBroadcast(pingResultIntent);

        }
    }

    private Future<?> pingTask;

    public static ConnectivityManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        if (context == null) throw new NullPointerException("Context can be not null.");
        mContext = context;
        startCheck();
    }

    public long getConnectivityCheckInterval() {
        return connectivityCheckInterval;
    }

    public void setConnectivityCheckInterval(long connectivityCheckInterval) {
        ConnectivityManager.connectivityCheckInterval = connectivityCheckInterval;
        startCheck();
    }

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * 开始检查
     */
    private void startCheck() {
        stopCheck();
        pingTask = pingThreadPool.submit(PING_RUNNABLE);
    }

    /**
     * 停止检查
     */
    private void stopCheck() {
        if (pingThread != null && pingThread.isAlive()) {
            synchronized (pingThread) {
                pingThread.notifyAll();
            }
        }
        if (pingTask != null) pingTask.cancel(true);
    }
}