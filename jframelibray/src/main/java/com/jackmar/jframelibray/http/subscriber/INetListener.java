package com.jackmar.jframelibray.http.subscriber;

/**
 * @Title
 * @Author luojiang
 * @Date 2016-05-23 17:14
 */
public interface INetListener<T> extends IOnNextListener<T> {
    void OnError(Throwable throwable);
}
