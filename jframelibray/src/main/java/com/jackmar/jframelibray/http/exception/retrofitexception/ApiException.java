package com.jackmar.jframelibray.http.exception.retrofitexception;

import com.jackmar.jframelibray.utils.StrUtil;

/**
 * @Title 接口请求异常
 * @Author luojiang
 * @Date 2016-05-23 17:14
 */
public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 100;
    public static final int WRONG_PASSWORD = 101;
    public int code;
    private String msg;

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
        this.msg = detailMessage;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        return "";
    }

    public String getMessage() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}

