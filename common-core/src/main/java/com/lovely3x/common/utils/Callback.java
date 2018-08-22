package com.lovely3x.common.utils;

/**
 * 回调
 *
 * @param <T>
 */
public interface Callback<T> {
    /**
     * 成功
     */
    public int CODE_SUCCESSFUL = 0;
    /**
     * 失败
     */
    public int CODE_FAILURE = -1;

    void onCallback(T t, int code);
}
