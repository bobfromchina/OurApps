package com.lovely3x.common.cacher;

import com.lovely3x.common.cacher.container.ICacheable;

/**
 * 缓存监视器
 * Created by lovely3x on 15-12-3.
 */
public interface ICacheMonitor {

    /**
     * 当一个缓存对象被缓存后执行
     *
     * @param cacheable         缓存容器
     * @param cache             缓存对象
     * @param maxCacheSize      最大的缓存大小限制
     * @param currentCachedSize 当前已经达到的缓存大小
     */
    void onCached(ICacheable cacheable, Cache cache, long maxCacheSize, long currentCachedSize);

    /**
     * 当缓存容器清除掉元素后执行
     *
     * @param cacheable         缓存对象容器
     * @param cache             被清除的缓存对象
     * @param maxCacheSize      最大的缓存大小限制
     * @param currentCachedSize 当前已经达到的缓存大小
     */
    void onTrimElement(ICacheable cacheable, Cache cache, long maxCacheSize, long currentCachedSize);

    /**
     * 当元素被移除后执行
     * 这个和{@link #onTrimElement(ICacheable, Cache, long, long)}不同
     * 这个主要是指用户主动删除，而并不是为了释放空间删除的
     *
     * @param cacheable         缓存容器对象
     * @param cache             缓存对象
     * @param maxCacheSize      最大的缓存大小限制
     * @param currentCachedSize 当前已经达到的缓存大小
     */
    void onRemovedElement(ICacheable cacheable, Cache cache, long maxCacheSize, long currentCachedSize);

    /**
     * 当缓存空间开始不足时调用
     *
     * @param cacheable 缓存容器对象
     * @param factor    当前的缓存比例因子，当因子越接近1.0则越饱和
     */
    void onLowCacheSpace(ICacheable cacheable, float factor);
}
