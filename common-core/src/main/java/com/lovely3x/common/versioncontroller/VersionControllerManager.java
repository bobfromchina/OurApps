package com.lovely3x.common.versioncontroller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.lovely3x.common.versioncontroller.impls.VersionControllerManagerService;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Class VersionControllerManager
 * 版本控制,用于简化这个软件的检查更新功能
 * 你需要继承这个类来实现必要的逻辑
 * 我们提供了简易的实现{@link VersionControllerManagerService}
 */
abstract public class VersionControllerManager extends Service {

    private static final String TAG = "VersionCManager";
    /**
     * 检查器是否正在运行中
     */
    protected boolean checkerRunning;

    /**
     * handler
     */
    protected android.os.Handler mHandler;

    /**
     * 线程池
     */
    private ThreadPoolExecutor mFixThreadPool;

    /**
     * 服务端的版本对象
     */
    private Version remoteVersion;
    /**
     * 当前程序的版本对象
     */
    private Version currentVersion;

    /**
     * 当前服务对象
     */
    private static VersionControllerManager instance;

    /**
     * 当前检查的进度
     */
    private State state = State.none;

    /**
     * 下载的结果对象
     */
    private Result result;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static VersionControllerManager getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mHandler == null) {
            mHandler = new WeakHandler(this);
        }
        initWorkThreadIfNeed();
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 初始化工作线程,如果需要
     */
    private void initWorkThreadIfNeed() {
        if (mFixThreadPool == null) {
            mFixThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        }
    }


    public VersionControllerManager() {

    }

    /**
     * 服务器返回的远程版本对象
     *
     * @return null或远程版本对象, 如果为null则说明检查失败
     */
    public Version getRemoteVersion() {
        return remoteVersion;
    }

    /**
     * 获取版本的当前app版本对象
     *
     * @return null 或 当前app的版本对象 如果是null则说明检查失败
     */
    public Version getCurrentVersion() {
        return currentVersion;
    }

    /**
     * 获取打过钱的下载的结果对象
     *
     * @return null或下载结果对象 ,如果为null则说明下载失败,或没有下载完成
     */
    public Result getResult() {
        return result;
    }

    /**
     * 获取当前的状态
     *
     * @return 当前检查器进行的阶段
     */
    public State getState() {
        return state;
    }

    /**
     * 当前版本检查器是否正在运行
     *
     * @return true或 false
     */
    public boolean checkerRunning() {
        return checkerRunning;
    }

    /**
     * 使用者,通过这个方法发起检查更新操作.
     */
    public void check() {
        if (checkerRunning) {
            Log.e(TAG, "version checker already running.");
            return;
        }
        //设置检查器正在运行中
        checkerRunning = true;
        //色织当前的检查状态为正在检查
        state = State.checking;

        initWorkThreadIfNeed();
        mFixThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    remoteVersion = getVersionFromService();
                } catch (Exception e) {
                    onError(e);
                }
                currentVersion = ApplicationUtils.getCurrentPackageVersion(VersionControllerManager.this.getApplicationContext());
                if (remoteVersion == null || currentVersion == null) {
                    state = State.checkFailure;
                    onError(new NullPointerException("remote or local version is null."));
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            boolean needUpdate = compareVersion(currentVersion, remoteVersion);
                            if (needUpdate) {
                                //更新状态为等待用户确认下载新版本
                                state = State.waitingConfirm;
                                foundNewerVersion(currentVersion, remoteVersion);
                            } else {
                                //checkerRunning = false;
                                //更新状态为已经是最新版本
                                state = State.isLatest;
                                isLatestedVersion();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 用户通过在这个方法来确认下载
     *
     * @param remoteVersion 远程的版本
     */
    public void confirmDownload(final Version remoteVersion) {
        initWorkThreadIfNeed();
        mFixThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    state = State.downloading;
                    result = download(remoteVersion);
                    state = State.downloaded;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            update(result);
                        }
                    });
                } catch (Exception e) {
                    state = State.downloadFailure;
                    onError(e);
                }
            }
        });
    }

    /**
     * 下载
     *
     * @param remoteVersion 远程版本
     */
    @WorkerThread
    public abstract Result download(Version remoteVersion);


    /**
     * 实现类,从服务器获取服务器端的版本对象
     *
     * @return Version
     */
    @WorkerThread
    abstract public Version getVersionFromService();


    /**
     * @param localVersion  本地的版本对象
     * @param remoteVersion 服务器端存在的版本
     * @return boolean
     */
    @MainThread
    abstract public boolean compareVersion(Version localVersion, Version remoteVersion);


    /**
     * 告诉实现类已经是最新的版本了,无需更新.
     */
    @MainThread
    public void isLatestedVersion() {
    }


    /**
     * @param localVersion  本地的版本
     * @param remoteVersion 服务端的版本对象
     */
    @MainThread
    abstract public void foundNewerVersion(Version localVersion, Version remoteVersion);


    /**
     * @param result 在发现新版本的方法中返回的Result对象
     */
    @MainThread
    public void update(Result result) {

    }

    /**
     * 当发生异常后执行
     *
     * @param throwable 异常对象
     */
    public void onError(Throwable throwable) {

    }

    public void setCheckRunning(boolean checkRunning) {
        this.checkerRunning = checkRunning;
    }


    /**
     * weakHandler
     */
    private static class WeakHandler extends android.os.Handler {
        WeakReference<VersionControllerManager> outClassRef;

        public WeakHandler(VersionControllerManager t) {
            outClassRef = new WeakReference<>(t);
        }

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            VersionControllerManager outClassObj = outClassRef.get();
            if (outClassObj != null) {
                super.dispatchMessage(msg);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void onDestroy() {
        instance = null;
        checkerRunning = false;
        state = State.none;
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 当前检查器运行的阶段
     */
    public enum State {
        /**
         * 整在检查是否新版本
         */
        checking,
        /**
         * 检查失败
         */
        checkFailure,
        /**
         * 发现新版本,等待确认
         */
        waitingConfirm,
        /**
         * 已经是最新版本了
         */
        isLatest,
        /**
         * 下载中
         */
        downloading,
        /**
         * 下载失败
         */
        downloadFailure,
        /**
         * 下载完成
         */
        downloaded,
        errorState, /*
         * 什么状态都没有
         * 没有开始,或已经结束检查
         */
        none
    }
}

