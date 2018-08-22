package com.lovely3x.common.viewcache;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 视图缓存对象池
 * Created by lovely3x on 15-12-5.
 */
public class ViewPool {

    /**
     * 视图缓存池
     */
    private final static SparseArray<List<ViewWrapper>> mViewCachePool = new SparseArray<>();


    /**
     * 从当前的列表中生车公一个唯一的视图类型
     * 这个正在某些情况下是无效的，如果你缓存的视图类型存在负数
     * 那么这将会导致这个方法异常，这也是不推荐使用负数的原因
     *
     * @return 唯一的视图类型 或 -1（如果生成失败）
     */
    public static int findUniquenessViewType() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            int key = mViewCachePool.indexOfKey(i);
            if (key < 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 缓存视图到缓存池中
     *
     * @param wrapper 需要缓存的缓存池
     * @return 是否加入成功，加入失败的情况一般为缓存池中已经存在该视图
     */
    public static boolean pushViewToCachePool(ViewWrapper wrapper) {
        List<ViewWrapper> list = mViewCachePool.get(wrapper.type);
        if (list == null) {
            list = new ArrayList<>();
        }
        mViewCachePool.put(wrapper.type, list);
        return !list.contains(wrapper) && list.add(wrapper);
    }


    /**
     * 通过指定的视图和类型寻找视图包装器
     *
     * @param view 需要匹配的视图对象
     * @return null或寻找到的视图
     */
    public static ViewWrapper findWrapperByView(View view) {
        final int cachePoolSize = mViewCachePool.size();
        for (int i = 0; i < cachePoolSize; i++) {
            List<ViewWrapper> wrappers = mViewCachePool.valueAt(i);
            if (wrappers != null) {
                for (int j = 0; j < wrappers.size(); j++) {
                    ViewWrapper wrapper = wrappers.get(j);
                    if (wrapper != null && wrapper.view == view) {
                        return wrapper;
                    }
                }
            }
        }
        return null;
/*
        for (int i = 0; i < cachePoolSize; i++) {
            List<ViewWrapper> result = mViewCachePool.get(mViewCachePool.keyAt(i));
            if (result != null && !result.isEmpty()) {
                ViewWrapper wrapper = findWrapperByView(view, result.get(0).type);
                if (wrapper != null) {
                    return wrapper;
                }
            }
        }
        return null;*/
    }

    /**
     * 通过指定的视图和类型寻找视图包装器
     *
     * @param view 需要匹配的视图对象
     * @param type 需要匹配的类型
     * @return null或寻找到的视图
     */
    public static ViewWrapper findWrapperByView(View view, int type) {
        List<ViewWrapper> results = mViewCachePool.get(type);
        if (results != null) {
            int index = Collections.binarySearch(results, new ViewWrapper(view, type));
            if (index > -1) {
                return results.get(index);
            }
        }
        return null;
    }


    /**
     * 在缓存池中寻找可以使用的视图
     *
     * @param type 需要查找的缓存视图类型
     * @return null或缓存视图对象
     */
    public static ViewWrapper findRecycledViewInCachePool(int type) {
        List<ViewWrapper> results = mViewCachePool.get(type);
        if (results != null && !results.isEmpty()) {
            for (ViewWrapper wrapper : results) {
                //如果对象已经回收，代表着可以重新被使用
                if (wrapper != null && wrapper.isRecycled) {
                    //重新设置标志位为正在使用
                    wrapper.isRecycled = false;
                    return wrapper;
                }
            }
        }
        return null;
    }

    /**
     * 视图包装对象
     */
    public static class ViewWrapper implements Comparable<ViewWrapper> {
        /**
         * 被包装的视图对象
         */
        public View view;
        /**
         * 被包装的视图的类型或者说标志
         * 不推荐使用负数作为类型
         */
        public int type;

        /**
         * 当前这个视图是否已经被回收，被回收了意味着可以重新被使用
         */
        public boolean isRecycled;

        /**
         * @param view 需要缓存的视图对象
         * @param type 类型，不推荐使用负数作为类型
         */
        public ViewWrapper(View view, int type) {
            this.view = view;
            this.type = type;
            isRecycled = true;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            ViewWrapper that = (ViewWrapper) object;

            if (type != that.type) return false;
            return !(view != null ? !view.equals(that.view) : that.view != null);

        }

        @Override
        public int hashCode() {
            int result = view != null ? view.hashCode() : 0;
            result = 31 * result + type;
            return result;
        }

        @Override
        public int compareTo(@NonNull ViewWrapper another) {
            if (view == another.view) {
                return 0;
            }
            return -1;
        }
    }
}
