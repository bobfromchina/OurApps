package com.lovely3x.imageloader;

import android.view.View;

/**
 * 检视器
 * Created by lovely3x on 16-5-26.
 */
public interface Inspector {

    /**
     * 当准备加载图片
     *
     * @param url     地址
     * @param view    视图
     * @param options 图片加载选项
     * @param loader  加载器
     * @return 是否处理了本次操作，如果处理了这次操作，loader将不会继续加载图片
     */
    boolean onBeforeLoading(IL loader, String url, View view, ImageDisplayOptions options);
}
