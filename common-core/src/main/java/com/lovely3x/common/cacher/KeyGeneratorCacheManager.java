package com.lovely3x.common.cacher;

import android.content.Context;

/**
 * JSON缓存管理器
 * Created by lovely3x on 15-12-3.
 */
public class KeyGeneratorCacheManager extends CacheManager.DefaultCacheManagerImpl {


    public KeyGeneratorCacheManager(Context context, CachePolice police) {
        super(context, police);
    }

    public KeyGeneratorCacheManager(Context context) {
        super(context);
    }

    @Override
    public Cache get(String name) {
        return super.get(name);
    }

    @Override
    public boolean put(String name, Cache cache) {
        cache.setName(name);
        return super.put(name, cache);
    }

    @Override
    public boolean clearCaches() {
        return super.clearCaches();
    }

    @Override
    public Cache clearCacheForKey(String name) {
        return super.clearCacheForKey(name);
    }


}
