package com.lovely3x.common.utils;

import android.graphics.Bitmap;
import android.support.annotation.MainThread;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Bitmap共享池
 * 用于同进程内的Bitmap共享
 * Created by lovely3x on 17/3/1.
 */
public class BitmapSharePool {

    private static final Map<String, WeakReference<Bitmap>> sSharePool = new HashMap<>();

    @MainThread
    public static Bitmap get(String key) {
        WeakReference<Bitmap> ref = sSharePool.get(key);
        return ref == null ? null : ref.get();
    }

    @MainThread
    public static Bitmap put(String key, Bitmap bitmap) {
        WeakReference<Bitmap> value = sSharePool.put(key, new WeakReference<>(bitmap));
        return value == null ? null : value.get();
    }
}
