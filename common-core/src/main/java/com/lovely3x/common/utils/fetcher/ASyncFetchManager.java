package com.lovely3x.common.utils.fetcher;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 异步数据获取器
 * 用于异步的去获取数据
 * 至于如何获取数据就需要看具体的{@link Fetcher}如何实现的了
 * 极简
 * 快速
 * Created by lovely3x on 17/3/6.
 */
public class ASyncFetchManager {

    /**
     * 错误原因：未知
     */
    public static final int ERROR_UNKNOWN = -1;

    /**
     * 错误原因：用户已经取消
     */
    private static final int ERROR_USER_CANCELED = -2;

    /**
     * 错误原因：空结果
     */
    private static final int ERROR_EMPTY_RESULT = -3;

    private Random mRandom = new Random();

    public static final int DEFAULT_THREAD_NUM = 3;

    private static ExecutorService sExecutor;

    private final SparseArray<Fetcher> mFetcherArray = new SparseArray<>();

    private final SparseArray<Future> mCalls = new SparseArray<>();

    private final ConcurrentHashMap<Object, Integer> ids = new ConcurrentHashMap<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private ASyncFetchManager() {
    }

    public void init(int threadNum) {
        sExecutor = Executors.newFixedThreadPool(threadNum);
    }

    public static ASyncFetchManager getAsyncFM() {
        return Holder.sInstance;
    }

    public synchronized Fetcher addFetcher(int flag, Fetcher fetcher) {
        Fetcher old = mFetcherArray.get(flag);
        if (old == null || old != fetcher) {
            mFetcherArray.put(flag, fetcher);
        }
        return old;
    }

    public synchronized Fetcher getFetcher(int flag){
        return mFetcherArray.get(flag);
    }

    public synchronized Fetcher removeFetcher(int flag) {
        Fetcher value = mFetcherArray.get(flag);
        if (value != null) {
            mFetcherArray.remove(flag);
        }
        return value;
    }

    public synchronized <S> void fetch(final S source, int flag, final int id, Rule rule, final CallBack<S> callBack) {
        if (sExecutor == null) throw new IllegalStateException();
        final Fetcher fetcher = mFetcherArray.get(flag);
        if (fetcher != null) {
            try {
                final Future oldCall = getCall(id);

                //这种类型的任务正在执行中
                if (oldCall != null && !oldCall.isDone() && !oldCall.isCancelled()) {
                    if (rule == Rule.noOperation) {
                        return;
                    } else {
                        oldCall.cancel(false);
                        removeCall(id);
                    }
                }

                //快速获取
                //并且在主线程中执行
                Object fastResult = fetcher.tryToFastFetch(source);

                if (fastResult != null) {
                    if (callBack != null) {
                        try {
                            callSuccess(id, callBack, source, fastResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }

                Future<?> future = sExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        Future cCall = getCall(id);
                        if (cCall == null || cCall.isCancelled()) {
                            if (cCall != null) {
                                removeCall(id);
                            }

                            if (callBack != null) {
                                callError(id, callBack, source, ERROR_USER_CANCELED, "User canceled");
                            }
                        } else {
                            try {
                                Object result = fetcher.process(source);
                                if (result != null) {
                                    if (callBack != null) {
                                        try {
                                            callSuccess(id, callBack, source, result);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    if (callBack != null) {
                                        callError(id, callBack, source, ERROR_EMPTY_RESULT, "Empty result");

                                    }
                                }
                            } catch (final Exception e) {
                                e.printStackTrace();
                                if (callBack != null) {
                                    callError(id, callBack, source, ERROR_UNKNOWN, e.getMessage());
                                }
                            } finally {
                                removeCall(id);
                            }
                        }

                    }
                });

                mCalls.put(id, future);

            } catch (final Exception e) {
                e.printStackTrace();
                callError(id, callBack, source, ERROR_UNKNOWN, e.getMessage());
            }
        }
    }

    private void callSuccess(final int id, final CallBack callBack, final Object source, final Object result) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            if (mCalls != null) callBack.onResult(id, source, result);
            return;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) callBack.onResult(id, source, result);
            }
        });
    }

    private void callError(final int id, final CallBack callBack, final Object source, final int errorCode, final String errorMsg) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onError(id, source, errorCode, errorMsg);
                }
            }
        });

    }

    public synchronized <S> void fetch(final S source, int flag, Rule rule, final CallBack<S> callBack) {
        Integer id = ids.get(source);
        if (id == null) {
            id = generateID();
            ids.put(source, id);
        }
        fetch(source, flag, id, rule, callBack);
    }

    private synchronized Future getCall(int key) {
        return mCalls.get(key);
    }

    private synchronized void removeCall(int key) {
        mCalls.remove(key);
    }

    private synchronized void putCall(int key, Future call) {
        mCalls.put(key, call);
    }

    private Integer generateID() {
        Collection<Integer> value = ids.values();
        int id = 0;
        do {
            id = mRandom.nextInt();
        } while (value.contains(id));
        return id;
    }

    public static interface CallBack<S> {

        void onError(int id, S s, int errorCode, String errorMsg);

        void onResult(int id, S s, Object object);

    }

    private static class Holder {
        private static final ASyncFetchManager sInstance = new ASyncFetchManager();
    }

    public static enum Rule {

        /**
         * 如果当前有任务则不继续处理任务任务
         */
        noOperation,

        /**
         * 取消执行的重新执行
         */
        reExecution;
    }
}
