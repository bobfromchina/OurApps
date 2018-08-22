package com.lovely3x.common.managements.pay;

import android.content.Context;

import com.lovely3x.common.utils.ALog;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 支付管理器
 * Created by lovely3x on 16/8/23.
 */
public class PaymentManager implements Initializer<Context, PaymentManager> {

    private static final String TAG = "PaymentManager";

    private static final PaymentManager INSTANCE = new PaymentManager();

    private final CopyOnWriteArrayList<IPayCallBack> mCallBacks = new CopyOnWriteArrayList<>();

    private final CopyOnWriteArrayList<InitHook<PaymentManager>> mHooks = new CopyOnWriteArrayList<>();

    private final CopyOnWriteArrayList<PayAction> mActions = new CopyOnWriteArrayList<>();

    /**
     * 支付请求锁
     */
    private static final AtomicBoolean LOCK = new AtomicBoolean(false);

    private boolean lockConcurrentRequest;

    private static Context mContext = null;

    private PaymentManager() {
    }

    public static PaymentManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        if (context == null) throw new NullPointerException();
        if (context.getApplicationContext() == null) {
            mContext = context;
        } else {
            mContext = context.getApplicationContext();
        }

        //回调钩子列表
        for (InitHook<PaymentManager> next : new ArrayList<>(mHooks)) {
            next.onInit(this);
        }
    }

    @Override
    public void registerInitHook(InitHook<PaymentManager> h) {
        if (!mHooks.contains(h)) mHooks.add(h);
    }

    @Override
    public void unregisterHook(InitHook<PaymentManager> h) {
        mHooks.remove(h);
    }

    /**
     * 通知支付结果
     *
     * @param payMethod 支付方式
     * @param payResult 支付结果
     */
    public void notifyPayResult(IPayCallBack.PayMethod payMethod, IPayCallBack.PayResult payResult) {
        if (LOCK.get() || !lockConcurrentRequest) {

            ALog.i(TAG, String.format(Locale.US, "%s Notify Pay Result ==> %s ", payMethod, payResult));
            for (IPayCallBack next : mCallBacks) {
                next.onPayResult(payMethod, payResult);
            }
            if (lockConcurrentRequest) LOCK.set(false);//解锁请求
        } else {
            //Ignored
            ALog.i(TAG, "没有在支付请求发出,忽略此次结果.");
        }

    }

    /**
     * 发送支付请求
     *
     * @param request   支付请求对象
     * @param payMethod 支付方式
     */
    public boolean sendPaymentRequest(PayRequest request, IPayCallBack.PayMethod payMethod) {
        if (!LOCK.get() || !lockConcurrentRequest) {//如果当前没有发起支付请求

            ALog.i(TAG, String.format(Locale.US, "%s Send Payment Request ==> %s ", payMethod, request));

            if (lockConcurrentRequest) LOCK.set(true);//锁住请求

            final ArrayList<PayAction> actions = new ArrayList<>(mActions);

            boolean used = false;

            for (int i = 0; i < actions.size(); i++) {
                PayAction item = actions.get(i);
                try {
                    if (item.pay(request, payMethod)) used = true;
                } catch (Exception e) {
                    ALog.e(TAG, e);
                }
            }

            if (!used && lockConcurrentRequest) LOCK.set(false);

            if (!used) {
                ALog.i(TAG, String.format(Locale.US, "Un-process %s Send Payment Request ==> %s ", payMethod, request));
            }

        } else {
            ALog.i(TAG, "已经有支付在请求了,忽略这个请求.");
        }
        return false;//已经有请求在执行了
    }

    /**
     * 注册支付结果回调接口
     *
     * @param callBack 需要注册的回调接口
     */
    public void registerPaymentCallBack(IPayCallBack callBack) {
        if (!mCallBacks.contains(callBack)) mCallBacks.add(callBack);
    }

    /**
     * 反注册 支付结果回调接口
     *
     * @param callBack 需要反注册的回调接口
     */
    public void unregisterPaymentCallBack(IPayCallBack callBack) {
        mCallBacks.remove(callBack);
    }

    /**
     * 注册支付动作
     *
     * @param payAction 需要注册的支付动作
     */
    public void registerPayAction(PayAction payAction) {
        if (payAction == null) return;
        if (!mActions.contains(payAction)) mActions.add(payAction);
    }

    /**
     * 反注册支付动作
     *
     * @param payAction 需要反注册的支付动作
     */
    public void unregisterPayAction(PayAction payAction) {
        if (payAction == null) return;
        mActions.remove(payAction);
    }

    public boolean isLockConcurrentRequest() {
        return lockConcurrentRequest;
    }

    public void setLockConcurrentRequest(boolean lockConcurrentRequest) {
        this.lockConcurrentRequest = lockConcurrentRequest;
    }

    public Context getContext() {
        return mContext;
    }
}
