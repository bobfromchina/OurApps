package com.jackmar.jframelibray.http.subscriber;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.jackmar.jframelibray.http.exception.retrofitexception.ApiException;
import com.jackmar.jframelibray.http.exception.retrofitexception.AuthenticationException;
import com.jackmar.jframelibray.view.rvlist.CHandler;
import com.jackmar.jframelibray.view.rvlist.RefreshUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by JackMar on 2017/2/28.
 * 邮箱：1261404794@qq.com
 */

public class RcListSubscriber<T> extends Subscriber<T> {
    private IOnNextListener onNextListener;
    private Context context;
    private RefreshUtil refreshUtil;
    private CHandler mChandler;

    public RcListSubscriber(IOnNextListener onNextListener, Context context) {
        this.onNextListener = onNextListener;
        this.context = context;
    }

    public RcListSubscriber(IOnNextListener onNextListener, Context context, RefreshUtil refreshUtil) {
        this.onNextListener = onNextListener;
        this.context = context;
        this.refreshUtil = refreshUtil;
        mChandler = new CHandler(refreshUtil);
    }

    public RcListSubscriber() {

    }

    private void completeRefreshAndLoad() {
        if (refreshUtil != null) {
            refreshUtil.completeRefreshAndLoad();
            mChandler.obtainMessage(CHandler.DISMISS_PROGRESS);
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (refreshUtil != null) {
            mChandler.obtainMessage(CHandler.SHOW_PROGRESS);
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        completeRefreshAndLoad();
        // Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.i("onError", e.getMessage());
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ApiException) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (e instanceof AuthenticationException) {
            //跳转到登录
//            context.startActivity(new Intent(context, LogingActivity.class));
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        completeRefreshAndLoad();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (onNextListener != null) {
            onNextListener.onNext(t);
        }
        completeRefreshAndLoad();
    }
}
