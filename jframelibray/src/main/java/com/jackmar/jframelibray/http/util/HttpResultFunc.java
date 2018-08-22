package com.jackmar.jframelibray.http.util;

/**
 * Created by JackMar on 2017/2/28.
 * 邮箱：1261404794@qq.com
 */


import com.jackmar.jframelibray.http.base.BaseResult;

import rx.functions.Func1;

/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class HttpResultFunc<T> implements Func1<BaseResult<T>, T> {
    @Override
    public T call(BaseResult<T> tBaseResult) {
        return tBaseResult.getData();
    }
}