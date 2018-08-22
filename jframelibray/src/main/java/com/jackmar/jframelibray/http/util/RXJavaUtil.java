package com.jackmar.jframelibray.http.util;

import com.jackmar.jframelibray.base.ALog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JackMar on 2017/2/28.
 * 邮箱：1261404794@qq.com
 */
public class RXJavaUtil {


    public static final String TAG = "RXJavaUtil ---->";

    /**
     * 线程调度，将网络请求放在后台线程操作，在主线程上界面显示
     */
    public static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io())
                .subscribe(s);

        ALog.e(TAG, o);
    }
}
