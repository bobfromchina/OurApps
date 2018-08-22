package com.lovely3x.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.support.multidex.MultiDex;

import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.CommonUtils;
import com.lovely3x.common.utils.ViewUtils;
import com.lovely3x.imageloader.ImageDisplayOptions;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;

/**
 * Common Application
 * Created by lovely3x on 15-9-1.
 */
public class CommonApplication extends Application/* DefaultApplicationLike **/ {

    private static final String TAG = "CommonApplication";

    public static final String WORK_HANDLER_THREAD_NAME = "BackgroundHandlerThread";

    private static final HandlerThread mHandlerThread;

    private static OkHttpClient mOkHttpClient;

    public static Handler BACKGROUND_HANDLER;

    private volatile static CommonApplication mInstance;

    private volatile AtomicBoolean mIsInitialized = new AtomicBoolean(false);

    /**
     * {@link #initOnBackground()} 是否执行完毕
     *
     * @return true or false
     */
    public boolean isInitialized() {
        return mIsInitialized.get();
    }

    private final Runnable BACKGROUND_TASK_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            if (mInstance != null) {
                long startTime = SystemClock.elapsedRealtime();
                initOnBackground();

                mIsInitialized.set(true);
                if (mInitializeCallBack != null) {
                    mInitializeCallBack.onInitialized(SystemClock.elapsedRealtime() - startTime);
                }
            }
        }

    };

    static {
        mHandlerThread = new HandlerThread(WORK_HANDLER_THREAD_NAME) {
            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
                BACKGROUND_HANDLER = new Handler(getLooper());
                synchronized (mHandlerThread) {
                    if (mInstance == null) {
                        mHandlerThread.notifyAll();
                    }
                }
            }
        };
        mHandlerThread.start();
    }

    private InitializeCallBack mInitializeCallBack;

    /**
     * 获取application实例
     *
     * @return application实例 或null(如果还没有attachContext)
     */
    public static CommonApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        long startTime = SystemClock.elapsedRealtime();
        initUrgentEvent();
        ALog.i(TAG, "initUrgentEvent consuming time " + (SystemClock.elapsedRealtime() - startTime) + "[ms]");
        startTime = SystemClock.elapsedRealtime();
        initSlowEvent();

        ALog.i(TAG, "initSlowEvent consuming time " + (SystemClock.elapsedRealtime() - startTime) + "[ms]");
        startTime = SystemClock.elapsedRealtime();
        prepareExecInitBackground();
        ALog.i(TAG, "prepareExecInitBackground consuming time " + (SystemClock.elapsedRealtime() - startTime) + "[ms]");

        ALog.d(TAG, "Mobile phone density" + ViewUtils.density());
        ALog.d(TAG, "Mobile phone scaled density" + ViewUtils.scaledDensity());
    }

    /**
     * 准备执行后台任务初始化
     */
    private void prepareExecInitBackground() {
        if (BACKGROUND_HANDLER == null) {
            try {
                synchronized (mHandlerThread) {
                    mHandlerThread.wait();
                }
            } catch (InterruptedException e) {
                ALog.e(TAG, "Exception => " + e);
            }
        }

        BACKGROUND_HANDLER.removeCallbacks(BACKGROUND_TASK_RUNNABLE);
        BACKGROUND_HANDLER.post(BACKGROUND_TASK_RUNNABLE);
    }

    /**
     * 在后台初始化耗时任务
     */
    protected void initOnBackground() {
        ALog.d(TAG, "InitOnBackground on thread => " + Thread.currentThread().getName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        mInstance = this;
    }

    /**
     * 在这里初始化,需要立即初始化的不能在拖延的
     */
    public void initUrgentEvent() {
        com.lovely3x.common.utils.ViewUtils.init(this);
        com.lovely3x.common.utils.NetUtils.init(this);
        com.lovely3x.common.utils.storage.StorageUtils.getInstance().init(this);
    }


    /**
     * 在这里初始化时不那么紧急的事件
     * 推荐在程序的启动页调用
     */
    public void initSlowEvent() {
        initImageLoader();
    }

    /**
     * 初始化图片加载器
     */
    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        int maxHeapSizeBytes = CommonUtils.getHeapMemorySize(getApplicationContext());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(10).denyCacheImageMultipleSizesInMemory()
                // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY)

//                .writeDebugLogs()
                // You can pass your own memory cache
                // implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(maxHeapSizeBytes / 4)
                .diskCacheSize(100 * 1024 * 1024)
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO).diskCacheFileCount(3000)
                // 缓存的文件数量
                .diskCache(new UnlimitedDiscCache(cacheDir))
                // 自定义缓存路径

                .defaultDisplayImageOptions(getDisplayOptionsBuilder()
                        .cacheInMemory(true).cacheOnDisk(true).build())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))
                .build();// 构建
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取显示选项
     *
     * @return 显示选项
     */
    public DisplayImageOptions.Builder getDisplayOptionsBuilder() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(getLoadingImgResId())
                .showImageOnFail(getEmptyURIImgResId())
                .showImageOnLoading(getLoadingFailureResId());
    }


    /**
     * 获取长方形显示选项
     *
     * @return 显示选项
     */
    public DisplayImageOptions.Builder getRectangleDisplayOptionsBuilder() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageOnFail(getRectangleLoadingFailureResId())
                .showImageOnLoading(getRectangleResIdLoadingImgResId());
    }

    /**
     * 获取图片加载显示选项
     *
     * @return 显示选项
     */
    public ImageDisplayOptions.Builder getImageLoaderDisplayOptionsBuilder() {
        return new ImageDisplayOptions.Builder()
                .cacheInMemory(true).cacheInDisk(true)
                .loadFailImgResource(getLoadingFailureResId())
                .loadingImgResource(getLoadingImgResId());
    }

    /**
     * 获取长方形图片加载显示选项
     *
     * @return 显示选项
     */
    public ImageDisplayOptions.Builder getRectangleImageLoaderDisplayOptionsBuilder() {
        return new ImageDisplayOptions.Builder()
                .cacheInMemory(true).cacheInDisk(true)
                .loadFailImgResource(getRectangleLoadingFailureResId())
                .loadingImgResource(getRectangleResIdLoadingImgResId());
    }


    /**
     * 获取正在加载中显示的图片
     *
     * @return 正在加载中显示的图片的资源id
     */
    public int getLoadingImgResId() {
        return R.drawable.icon_loading;
    }

    /**
     * 获取地址为空显示的图片
     *
     * @return 地址为空显示的图片的资源id
     */
    public int getEmptyURIImgResId() {
        return R.drawable.icon_loading_failure;
    }

    /**
     * 获取加载失败显示的图片
     *
     * @return 加载失败显示的图片的资源id
     */
    public int getLoadingFailureResId() {
        return R.drawable.icon_loading_failure;
    }


    /**
     * 获取长方形的空内容提示提示占位图
     */
    public int getRectangleEmptyURIImageResId() {
        return R.drawable.icon_loading_failure;
    }

    /**
     * 获取长方形的加载失败提示提示占位图
     */
    public int getRectangleLoadingFailureResId() {
        return R.drawable.icon_loading_failure;
    }

    /**
     * 获取长方形的加载中提示提示占位图
     *
     * @return
     */
    public int getRectangleResIdLoadingImgResId() {
        return R.drawable.icon_loading;
    }

    /**
     * 初始化 okHttp
     */
    protected OkHttpClient initOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(Config.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Config.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Config.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS).build();
    }

    /**
     * 获取okHttpClient对象
     * 不要对这个对象进行set操作
     * 如果你需要对这个对象set操作,那么请使用这个对象clone一个新的对象
     * 对新对象设置参数,因为你的所有操作都有可能影响到全局的网络请求操作
     *
     * @return okHttpClient
     */
    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) mOkHttpClient = initOkHttpClient();
        return mOkHttpClient;
    }

    /**
     * 程序状态监听器
     */
    public interface ApplicationStateListener {

        /**
         * 当程序进入前台后执行
         */
        void onApplicationEnterForeground();

        /**
         * 当程序进入后台后执行
         */
        void onApplicationEnterBackground();
    }

    /**
     * Runnable
     */
    public static abstract class ActivityRunnable implements Runnable {
        protected final Activity mActivity;

        public ActivityRunnable(Activity activity) {
            this.mActivity = activity;
        }
    }

    public static Handler getBackgroundHandler() {
        getInstance().getPackageManager();
        return BACKGROUND_HANDLER;
    }

    public void setInitializeCallBack(InitializeCallBack initializeCallBack) {
        mInitializeCallBack = initializeCallBack;
    }

    public static interface InitializeCallBack {

        @WorkerThread
        void onInitialized(long howLong);
    }
}
