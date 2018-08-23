package com.example.wangbo.ourapp.utils;

import android.os.Handler;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求响应
 *
 * @author lenovo
 */
public class Response {
    /**
     * 是否请求成功
     */
    public boolean isSuccessful;

    /**
     * 请求返回携带的数据
     */
    public Object obj;

    /**
     * 错误原因
     */
    public String errorMsg;


    public boolean success;

    /**
     * 错误的代码
     */
    public int errorCode;

    /**
     * 附加值
     */
    public int addtional;

    /**
     * 异常对象
     */
    public Exception exception;

    /**
     * 请求的参数数组
     */
    public Object[] parameters;

    public Response() {
    }

    private Map<String, Object> pair;

    /**
     * 根据可以获取参数名
     * @param key 需要获取的参数的key
     * @return
     */
    public Object getParameterByKey(String key) {
        if (pair == null) {
            pair = new HashMap<>();
            for (int i = 0; i < parameters.length; i += 2) {
                pair.put(String.valueOf(parameters[i]), parameters[i + 1]);
            }
        }
        return pair.get(key);
    }


    public Response(boolean isSuccessful, Object obj, String errorMsg, boolean success, int errorCode, int addtional, Exception exception, Object[] parameters, Map<String, Object> pair) {
        this.isSuccessful = isSuccessful;
        this.obj = obj;
        this.errorMsg = errorMsg;
        this.success = success;
        this.errorCode = errorCode;
        this.addtional = addtional;
        this.exception = exception;
        this.parameters = parameters;
        this.pair = pair;
    }

    /**
     * 模拟结果
     *
     * @param what       handler的what值
     * @param code       服务端的响应码
     * @param additional 附加值
     * @param object     附加的对象
     */
    public static void mockResult(Handler mHandler, int what, int code, int additional, Object object) {
        if (mHandler != null) {
            Response response = new Response();
            response.addtional = additional;
            response.errorCode = code;
            response.obj = object;
            response.isSuccessful = code == 1;
            mHandler.obtainMessage(what, response).sendToTarget();
        }
    }

    @Override
    public String toString() {
        return "Response{" +
                "isSuccessful=" + isSuccessful +
                ", obj=" + obj +
                ", errorMsg='" + errorMsg + '\'' +
                ", success=" + success +
                ", errorCode=" + errorCode +
                ", addtional=" + addtional +
                ", exception=" + exception +
                ", parameters=" + Arrays.toString(parameters) +
                ", pair=" + pair +
                '}';
    }
}
