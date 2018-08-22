package com.lovely3x.imageloader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * 图片加载器接口
 * 实现则表示可以实现显示图片
 */
public interface IL {
    /**
     * 显示图片
     *
     * @param view 需要显示的视图
     * @param url  需要显示的地址
     */
    void display(View view, String url);

    /**
     * 显示图片
     *
     * @param iv  图片需要显示的视图
     * @param url 图片显示的地址
     */
    void display(ImageView iv, String url);

    /**
     * 显示图片
     *
     * @param view    图片需要显示的视图
     * @param url     图片显示的地址
     * @param options 显示图片的选项
     */
    void display(View view, String url, ImageDisplayOptions options);

    /**
     * 显示图片
     *
     * @param iv      图片显示的视图
     * @param url     图片显示的地址
     * @param options 显示图片的选项
     */
    void display(ImageView iv, String url, ImageDisplayOptions options);

    /**
     * 初始化
     *
     * @param context 上下文
     */
    void init(Context context);

    /**
     * 显示一张gif的图片
     *
     * @param iv  图片显示载体
     * @param url 图片显示的地址
     */
   // void displayGif(ImageView iv, String url);
}
