package com.jackmar.jframelibray.http.exception.retrofitexception;

import okhttp3.Response;

/**
 * @title 带请求的异常
 * @author luojiang
 * @Date 2016-05-23 17:14
 */
public class ClientException extends RuntimeException {
    public ClientException(Response response, ApiException apiError) {
    }
}
