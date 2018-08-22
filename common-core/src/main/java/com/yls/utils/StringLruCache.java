package com.yls.utils;

import android.support.v4.util.LruCache;

/**
 * Created by lovely3x on 17/2/21.
 */
public class StringLruCache extends LruCache<String, String> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public StringLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, String value) {
        if (value == null) return 0;
        return value.length();
    }
}
