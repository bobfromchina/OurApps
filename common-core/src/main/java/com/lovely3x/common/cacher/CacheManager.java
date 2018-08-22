package com.lovely3x.common.cacher;

import android.content.Context;

/**
 * 缓存管理器
 * Created by lovely3x on 15-12-3.
 */
public class CacheManager {

    private static final String TAG = "CacheManager";

    private static final CacheManager mInstance = new CacheManager();

    private ICacheManager inner;

    private CacheManager() {
    }

    /**
     * 初始化
     * 使用默认的缓存策略管理器
     *
     * @param context 上下文
     */
    public synchronized void init(Context context) {
        inner = new DefaultCacheManagerImpl(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param police  缓存策略管理器
     */
    public synchronized void init(Context context, CachePolice police) {
        inner = new DefaultCacheManagerImpl(context, police);
    }

    /**
     * 使用扩展来处理缓存管理
     * 这会导致默认的缓存管理器失效，如果需要使用默认的管理器可以再次调用{@link #init(Context)}或{@link #init(Context, CachePolice)}
     *
     * @param iCacheManager 扩展的缓存管理器
     */
    public void useExtension(ICacheManager iCacheManager) {
        if (iCacheManager == null) {
            throw new NullPointerException("如果你想恢复默认的管理器，你可以再次调用#init");
        }
        this.inner = iCacheManager;
    }


    /**
     * 获取值
     *
     * @param name 根据名字获取缓存对象
     * @return 缓存对象或null
     */
    public Cache get(String name) {
        initCheck();
        return inner.get(name);
    }

    /**
     * 存放缓存对象到缓存中
     *
     * @param name  缓存名
     * @param cache 缓存对象
     * @return 是否缓存成功
     */
    public boolean put(String name, Cache cache) {
        initCheck();
        return inner.put(name, cache);
    }


    public void registerCacheMonitor(ICacheMonitor monitor) {
        inner.registerCacheMonitor(monitor);
    }


    public void unregisterCacheMonitor(ICacheMonitor monitor) {
        inner.unregisterCacheMonitor(monitor);

    }


    /**
     * 清除当前所有的缓存
     *
     * @return 是否清楚成功
     */
    public boolean clear() {
        return inner.clearCaches();
    }

    /**
     * 通过指定的key来清除缓存
     *
     * @param key 指定的key
     * @return 清除的对象或null
     */
    public Cache clearForKey(String key) {
        return inner.clearCacheForKey(key);
    }

    /**
     * 是否初始化了
     *
     * @return true or false
     */
    public boolean isInitialized() {
        return inner != null;
    }

    /**
     * 初始化检查
     */
    protected void initCheck() {
        if (!isInitialized()) {
            throw new IllegalStateException("请先调用 #init 方法初始化.");
        }
    }


    /**
     * 获取实例对象
     *
     * @return CacheManager对象
     */
    public static CacheManager getInstance() {
        return mInstance;
    }

    /**
     * 缓存管理器，内部实现
     */
    public static class DefaultCacheManagerImpl implements ICacheManager {

        private final CachePolice mCachePolice;
        private final Context mContext;

        protected DefaultCacheManagerImpl(Context context, CachePolice police) {
            this.mCachePolice = police;
            this.mContext = context;
        }

        protected DefaultCacheManagerImpl(Context context) {
            this(context, new CachePoliceImpl(context));
        }

        public synchronized boolean put(String key, Cache cache) {
            return mCachePolice.put(key, cache);
        }

        @Override
        public boolean clearCaches() {
            return mCachePolice.clear();
        }

        @Override
        public Cache clearCacheForKey(String name) {
            Cache cache = mCachePolice.get(name);
            mCachePolice.clearForKey(name);
            return cache;
        }

        @Override
        public void registerCacheMonitor(ICacheMonitor monitor) {
            mCachePolice.registerCacheMonitor(monitor);
        }

        @Override
        public void unregisterCacheMonitor(ICacheMonitor monitor) {
            mCachePolice.unregisterCacheMonitor(monitor);

        }

        public synchronized Cache get(String key) {
            return mCachePolice.get(key);
        }
    }

}
