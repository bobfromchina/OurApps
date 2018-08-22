package com.lovely3x.common.utils;

import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 弱引用handler
 * 你应该使用这个handler,因为这可以尽量的防止内存泄露发生
 * Created by lovely3x on 15-7-9.
 */
public class BaseWeakHandler<T> extends android.os.Handler {

    /**
     * 使用弱引用来持有外部类
     */
    protected WeakReference<T> outClassRef;

    /**
     * 当指定的这个类如果不存在,handler 的消息不会得到处理
     *
     * @param outClass 外部引用类
     */
    public BaseWeakHandler(T outClass) {
        super(Looper.getMainLooper());
        this.outClassRef = new WeakReference<T>(outClass);
    }


    @Override
    public void dispatchMessage(Message msg) {
        if (getOutClass() != null) {
            super.dispatchMessage(msg);
        }
    }

    /**
     * @return 获取外部类引用, 如果为 null 则表示 外部类已经销毁
     */
    public T getOutClass() {
        return outClassRef.get();
    }

}
