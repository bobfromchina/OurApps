package com.jackmar.jframelibray.http.exception.retrofitexception;

import okhttp3.Response;

/**
 * @title 服务的异常
 * @author luojiang
 * @Date 2016-05-23 17:14
 */
public class ServerException extends RuntimeException {
    public ServerException(Response response) {
    }
}
