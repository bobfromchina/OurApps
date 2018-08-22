package com.lovely3x.common.utils;

/**
 * 数据接收器
 * Created by lovely3x on 16/8/10.
 */
public interface DataReceiver<T> {

    /**
     * 当有数据来到时调用
     *
     * @param t
     */
    void onReceive(T t);
}
