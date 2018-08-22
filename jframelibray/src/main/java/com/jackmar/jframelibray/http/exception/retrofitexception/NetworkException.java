package com.jackmar.jframelibray.http.exception.retrofitexception;

/**
 * @title 网络异常
 * @author luojiang
 * @Date 2016-05-23 17:14
 */
public class NetworkException extends RuntimeException {

    public NetworkException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }
    public NetworkException(int code,String detailMessage) {
        this(getApiExceptionMessage(code));
    }
    public NetworkException(Throwable t) {

    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code){
        String message = "";
//        switch (code) {
//            case USER_NOT_EXIST:
//                message = "该用户不存在";
//                break;
//            case WRONG_PASSWORD:
//                message = "密码错误";
//                break;
//            default:
//    }
             message = "网络连接错误";
    return message;
    }
}
