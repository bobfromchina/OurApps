package com.lovely3x.common.managements.pay;

/**
 * 初始化钩子
 * Created by lovely3x on 16/8/24.
 */
public interface InitHook<T> {

    void onInit(T t);
}
