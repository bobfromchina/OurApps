package com.lovely3x.common.cacher;

/**
 * 缓存管理器
 */
public interface ICacheManager extends Monitor {
    /**
     * 获取值
     *
     * @param name 根据名字获取缓存对象
     * @return 缓存对象或null
     */
    Cache get(String name);

    /**
     * 存放缓存对象到缓存中
     *
     * @param name  缓存名
     * @param cache 缓存对象
     * @return 是否缓存成功
     */
    boolean put(String name, Cache cache);

    /**
     * 清楚缓存
     *
     * @return 是否清楚成功
     */
    boolean clearCaches();

    /**
     * 清除指定key的缓存
     *
     * @param name key
     * @return 清除掉的Cache对象或null（如果没有清楚掉）
     */
    Cache clearCacheForKey(String name);


}