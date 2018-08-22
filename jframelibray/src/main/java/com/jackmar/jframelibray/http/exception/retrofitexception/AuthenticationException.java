package com.jackmar.jframelibray.http.exception.retrofitexception;

import okhttp3.Response;

/**
 * @title 验证异常
 * @author luojiang
 * @Date 2016-05-23 17:14
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(Response response, ApiException apiError) {
    }
}
