package com.lovely3x.common.cacher.container;

import android.support.v4.util.LruCache;

import com.lovely3x.common.cacher.Cache;
import com.lovely3x.common.cacher.ICacheMonitor;
import com.lovely3x.common.cacher.Monitor;
import com.lovely3x.common.utils.ALog;

import java.util.ArrayList;
import java.util.List;

/**
 * lru 缓存实现
 * Created by lovely3x on 15-12-3.
 */
public class LruMemoryCacheContainer implements ICacheable<String, Cache> {

    private static final String TAG = "LruMemoryCacheContainer";

    /**
     * 内部的lruCache实现
     */
    private final InternalLruCache internalLruCache;

    /**
     * 创建一个lru缓存容器
     *
     * @param cacheSize 缓存的大小
     */
    public LruMemoryCacheContainer(int cacheSize) {
        this.internalLruCache = new InternalLruCache(cacheSize);
    }

    @Override
    public boolean put(String name, Cache cache) {
        Cache previous = internalLruCache.put(name, cache);
        if (previous != null) {
            ALog.d(TAG, "本次放入的对象导致上一个对象被移除，上一个对象是：" + previous);
        }
        return true;
    }

    @Override
    public Cache get(String name) {
        return internalLruCache.get(name);
    }

    @Override
    public boolean clearCache() {
        internalLruCache.trimToSize(0);
        return true;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Cache remove(String key) {
        return internalLruCache.remove(key);
    }

    @Override
    public long maxSize() {
        return internalLruCache.maxSize();
    }

    @Override
    public long currentSize() {
        return internalLruCache.size();
    }

    @Override
    public void registerCacheMonitor(ICacheMonitor monitor) {
        internalLruCache.registerCacheMonitor(monitor);
    }

    @Override
    public void unregisterCacheMonitor(ICacheMonitor monitor) {
        internalLruCache.unregisterCacheMonitor(monitor);
    }

    /**
     * 内部的lru缓存实现
     */
    private class InternalLruCache extends LruCache<String, Cache> implements Monitor {

        private static final String TAG = "InternalLruCache";

        /**
         * 监视器
         */
        private final List<ICacheMonitor> monitors = new ArrayList<>();

        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public InternalLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Cache oldValue, Cache newValue) {
            if (evicted && oldValue != null && oldValue.getName() != null) {
                for (ICacheMonitor monitor : monitors)
                    monitor.onTrimElement(LruMemoryCacheContainer.this, oldValue, maxSize(), currentSize());

            } else {
                for (ICacheMonitor monitor : monitors)
                    monitor.onRemovedElement(LruMemoryCacheContainer.this, oldValue, maxSize(), currentSize());
            }
        }

        @Override
        protected Cache create(String key) {
            return null;
        }

        @Override
        public void trimToSize(int maxSize) {
            float factor;
            if ((factor = size() / maxSize()) > 0.7) {
                for (ICacheMonitor monitor : monitors)
                    monitor.onLowCacheSpace(LruMemoryCacheContainer.this, factor);
            }
            super.trimToSize(maxSize);
        }

        @Override
        protected int sizeOf(String key, Cache value) {
            if (value != null) {
                if (value.getData() != null) return value.getData().length;
            }
            return super.sizeOf(key, value);
        }

        @Override
        public synchronized void registerCacheMonitor(ICacheMonitor monitor) {
            monitors.add(monitor);
        }

        @Override
        public synchronized void unregisterCacheMonitor(ICacheMonitor monitor) {
            monitors.remove(monitor);
        }
    }

}
