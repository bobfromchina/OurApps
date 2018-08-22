package com.lovely3x.common.managements.pay;

/**
 * 初始化器
 * Created by lovely3x on 16/8/24.
 */
public interface Initializer<I, H> {

    /**
     * 初始化时调用
     *
     * @param i 当前的实例对象
     */
    void init(I i);

    /**
     * 注册初始化钩子
     *
     * @param h 钩子对象
     */
    void registerInitHook(InitHook<H> h);

    /**
     * 反注册初始化钩子
     *
     * @param h 钩子对象
     */
    void unregisterHook(InitHook<H> h);

}
