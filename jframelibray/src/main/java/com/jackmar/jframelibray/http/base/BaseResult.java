package com.jackmar.jframelibray.http.base;

/**
 * 返回数据的基类
 * Created by JackMar on 2017/2/28.
 * 邮箱：1261404794@qq.com
 */

public class BaseResult<T> {


    private int Code;
    private String Msg;
    private boolean State;
    private T Data;
    private int status;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
