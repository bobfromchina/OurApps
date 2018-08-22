package com.lovely3x.common.utils;

/**
 * 事件接口
 * Created by lovely3x on 15-12-17.
 */
public interface Event<T> {

    /**
     * 当事件被触发了执行
     *
     * @param eventWhat 事件标示
     * @param objects   对象
     */
    void onEvent(int eventWhat, T objects);

}
