package com.lovely3x.common.cacher;

import com.lovely3x.common.cacher.container.ICacheable;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 缓存策略实现者
 * Created by lovely3x on 15-12-3.
 */
public abstract class CachePolice implements Monitor {

    private static final java.lang.String TAG = "CachePolice";

    protected ArrayList<ICacheable<String, Cache>> cacheableList = new ArrayList<>();

    /**
     * 注册缓存容器
     *
     * @param cacheable 缓存容器
     */
    public synchronized void registerCacheContainerImpl(ICacheable<String, Cache> cacheable) {
        cacheableList.add(cacheable);
        Collections.sort(cacheableList, new Comparator<ICacheable>() {
            @Override
            public int compare(ICacheable lhs, ICacheable rhs) {
                if (lhs.getPriority() > rhs.getPriority()) {//大的拍前面
                    return -1;
                } else if (lhs.getPriority() < rhs.getPriority()) {//小的拍后面
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * 解除缓存实现容器
     *
     * @param cacheable 缓存实现容器
     */
    public synchronized void unregisterCacheContainerImpl(ICacheable<String, Cache> cacheable) {
        cacheableList.remove(cacheable);
    }

    /**
     * 放置数据到缓存中
     *
     * @param name  名字
     * @param cache 缓存对象
     * @return 是否放置成功
     */
    public boolean put(String name, Cache cache) {
        return getCacheable(cacheableList, name, cache).put(name, cache);
    }


    /**
     * 从缓存中获取缓存对象
     *
     * @param name 保存缓存对象的名字
     * @return 缓存对象
     */
    public Cache get(String name) {
        for (ICacheable<String, Cache> cacheable : cacheableList) {
            Cache cache;
            if ((cache = cacheable.get(name)) != null) {
                return cache;
            }
        }
        return null;
    }


    /**
     * 获取合适的缓存对象
     *
     * @param cacheableList 缓存实现者们
     * @param name          用于缓存的名字
     * @param cache         需要缓存的对象
     * @return 可缓存的容器对象
     */
    protected abstract ICacheable<String, Cache> getCacheable(List<ICacheable<String, Cache>> cacheableList, String name, Cache cache);


    /**
     * 清楚缓存
     *
     * @return 是否清楚成功
     */
    protected boolean clear() {
        try {
            for (ICacheable<String, Cache> cacheable : cacheableList) {
                if (cacheable != null) {
                    cacheable.clearCache();
                }
            }
        } catch (Exception e) {
            if (Config.DEBUG) {
                ALog.e(TAG, e);
            }
        }
        return true;
    }


    /**
     * @param key 需要清除的key
     * @return 是否清楚成功
     */
    public boolean clearForKey(String key) {

        try {
            for (ICacheable<String, Cache> cacheable : cacheableList) {
                if (cacheable != null) {
                    cacheable.remove(key);
                }
            }
        } catch (Exception e) {
            if (Config.DEBUG) {
                ALog.d(TAG, e);
            }
            return false;
        }
        return true;
    }

}
