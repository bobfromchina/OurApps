package com.lovely3x.common.cacher;

/**
 * 实现这个类，表示可以注册监视器
 * Created by lovely3x on 15-12-3.
 */
public interface Monitor {
    /**
     * 注册一个缓存监视器
     *
     * @param monitor 需要注册的缓存监视器
     */
    void registerCacheMonitor(ICacheMonitor monitor);

    /**
     * 解除注册的缓存监视器
     *
     * @param monitor 监视器
     */
    void unregisterCacheMonitor(ICacheMonitor monitor);
}
