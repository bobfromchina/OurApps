package com.lovely3x.common.cacher;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.lovely3x.common.cacher.container.FIFODiskCacheContainer;
import com.lovely3x.common.cacher.container.ICacheable;
import com.lovely3x.common.cacher.container.LruMemoryCacheContainer;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.CommonUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认的缓存策略器实现，内部直接使用内存和磁盘作为缓存
 * 内存缓存使用LRU机制，磁盘缓存使用FIFO机制
 * Created by lovely3x on 15-12-3.
 */
public class CachePoliceImpl extends CachePolice {

    public static final List<ICacheable<String, Cache>> needUpdateCacheAbles = new CopyOnWriteArrayList<>();
    private final long mMemoryCacheSize;
    private final long mFileCacheSize;
    private final Context mContext;
    private final CacheWorker mFileCacheWorker;
    private final Handler mPushCacheHandler;

    /**
     * 使用指定的参数创建策略器
     *
     * @param context         上下文
     * @param cacheFileDirs   缓存文件夹
     * @param fileCacheSize   文件缓存大小
     * @param memoryCacheSize 内存缓存大小
     */
    public CachePoliceImpl(Context context, String cacheFileDirs, long fileCacheSize, int memoryCacheSize) {
        this.mContext = context;
        this.mFileCacheSize = fileCacheSize;
        this.mMemoryCacheSize = memoryCacheSize;
        registerCacheContainerImpl(new LruMemoryCacheContainer(memoryCacheSize));
        registerCacheContainerImpl(new FIFODiskCacheContainer(cacheFileDirs, context, fileCacheSize));
        this.mFileCacheWorker = new CacheWorker(this);
        mPushCacheHandler = new Handler(mFileCacheWorker.getLooper());
    }

    /**
     * 使用默认的缓存数据创建缓存策略器
     *
     * @param mContext 上下文
     */
    public CachePoliceImpl(Context mContext) {
        this(mContext, mContext.getCacheDir().getAbsolutePath(), 1000 * 1024, CommonUtils.getHeapMemorySize(mContext) / 8);
    }

    @Override
    public Cache get(final String name) {
        //最新的缓存对象
        Cache lastTimeCache = null;
        for (int i = 0; i < cacheableList.size(); i++) {
            final ICacheable<String, Cache> cacheable = cacheableList.get(i);
            Cache cache = cacheable.get(name);
            if (cache == null) {
                needUpdateCacheAbles.add(cacheable);
            } else {
                if (lastTimeCache == null) {
                    lastTimeCache = cache;
                } else {
                    if (lastTimeCache.getTime() < cache.getTime()) lastTimeCache = cache;
                }
            }
        }
        if (lastTimeCache == null) {
            needUpdateCacheAbles.clear();
        } else {
            if (!needUpdateCacheAbles.isEmpty()) {
                final Cache finalLastTimeCache = lastTimeCache;
                mPushCacheHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (ICacheable<String, Cache> internal : needUpdateCacheAbles) {
                            internal.put(finalLastTimeCache.getName(), finalLastTimeCache);
                        }
                        needUpdateCacheAbles.clear();
                    }
                });
            }
        }


        return lastTimeCache;
    }

    /**
     * 这里直接默认就是内存缓存实现，然后抛到磁盘缓存中区
     *
     * @param cacheableList 缓存实现者列表
     * @param name          用于缓存的名字
     * @param cache         需要缓存的对象
     * @return 最好的缓存实现者
     */
    @Override
    protected ICacheable<String, Cache> getCacheable(final List<ICacheable<String, Cache>> cacheableList, final String name, final Cache cache) {
        final ICacheable<String, Cache> first = cacheableList.get(0);
        mPushCacheHandler.post(new Runnable() {
            @Override
            public void run() {
                //缓存到其他的容器中
                for (ICacheable<String, Cache> cacheable : cacheableList) {
                    if (cacheable != null && cacheable != first) {
                        cacheable.put(name, cache);
                    }
                }
            }
        });

        return first;
    }

    @Override
    public void registerCacheMonitor(ICacheMonitor monitor) {
        for (ICacheable cacheable : cacheableList) {
            cacheable.registerCacheMonitor(monitor);
        }
    }

    @Override
    public void unregisterCacheMonitor(ICacheMonitor monitor) {
        for (ICacheable cacheable : cacheableList) {
            cacheable.unregisterCacheMonitor(monitor);
        }

    }

    /**
     * 缓存工作线程
     */
    private static final class CacheWorker extends Thread {

        private static final java.lang.String TAG = "CacheWorker";
        private final CachePoliceImpl mCachePolice;
        private Looper mLooper;

        public CacheWorker(CachePoliceImpl cachePolice) {
            this.mCachePolice = cachePolice;
            start();
            try {
                synchronized (mCachePolice) {
                    cachePolice.wait();
                }
            } catch (InterruptedException e) {
                if (Config.DEBUG) {
                    ALog.e(TAG, e);
                }
            }
        }

        @Override
        public void run() {
            Looper.prepare();
            this.mLooper = Looper.myLooper();
            try {
                synchronized (mCachePolice) {
                    mCachePolice.notify();
                }
            } catch (Exception e) {
                if (Config.DEBUG) {
                    ALog.e(TAG, e);
                }
            }
            Looper.loop();
        }

        public Looper getLooper() {
            return mLooper;
        }
    }

}
