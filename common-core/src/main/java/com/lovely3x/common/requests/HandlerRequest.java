package com.lovely3x.common.requests;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.lovely3x.common.CommonApplication;

/**
 * 使用handler事先的请求器
 * Created by lovely3x on 16-1-25.
 */
public class HandlerRequest extends CommonRequests {
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    protected Handler mHandler;

    public HandlerRequest() {
        super(CommonApplication.getInstance(), BaseCodeTable.getInstance(), BaseURLConst.getInstance());
    }

    public HandlerRequest(Handler handler) {
        super(CommonApplication.getInstance(), BaseCodeTable.getInstance(), BaseURLConst.getInstance());
        this.mHandler = handler;
    }

    /**
     * 创建实例
     *
     * @param handler handler，通信用
     * @return HandlerRequest的实例
     */
    public static
    @NonNull
    <T> T newInstance(Class<T> clazz, Handler handler) {
        try {
            HandlerRequest obj = (HandlerRequest) Class.forName(String.format("%s$Impl", clazz.getName())).newInstance();
            obj.mHandler = handler;
            return (T) obj;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static Handler getMainHandler() {
        return MAIN_HANDLER;
    }
}
