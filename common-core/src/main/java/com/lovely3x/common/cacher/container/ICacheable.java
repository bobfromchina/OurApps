package com.lovely3x.common.cacher.container;

import com.lovely3x.common.cacher.Monitor;

/**
 * 实现该类，表示可以事先缓存
 * Created by lovely3x on 15-12-3.
 */
public interface ICacheable<K, V> extends Monitor {

    /**
     * 放置数据缓存
     *
     * @param k 缓存的key
     * @param v 缓存的value
     * @return 是否加入成功
     */
    boolean put(K k, V v);

    /**
     * 获取数据
     *
     * @param k 获取数据的key
     * @return 根据key获取到的值或null
     */
    V get(K k);


    /**
     * 清楚缓存
     *
     * @return 缓存
     */
    boolean clearCache();


    /**
     * 获取优先级，优先级越大就代表者首先考虑的缓存实现者
     *
     * @return 优先级
     */
    int getPriority();

    /**
     * 通过指定的key来清除对象
     *
     * @param k key
     * @return 被清除的对象或null
     */
    V remove(K k);

    /**
     * 获取最大的缓存数据
     *
     * @return 获取最大的缓存大小
     */
    long maxSize();

    /**
     * 当前已经缓存的大小
     *
     * @return 获取当前已经缓存的大小
     */
    long currentSize();

}
