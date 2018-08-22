package com.lovely3x.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lovely3x.common.R;
import com.lovely3x.imageloader.ils.ImageLoaderILImpl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 图片加载器
 * Created by lovely3x on 16-5-25.
 */
public class ImageLoader implements IL {

    private static final String TAG = "ImageLoader";
    private static final IL IMPL;

    /**
     * 检视器列表
     */
    private Inspector inspector;

    private final boolean DEBUG = true;

    static {
        IMPL = new ImageLoaderILImpl();
    }

    private static final ImageLoader INSTANCE = new ImageLoader();

    /**
     * context
     */
    private Context mContext;

    /**
     * 获取图片加载实例
     *
     * @return 图片加载实例
     */
    public static ImageLoader getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public void init(Context context) {
        if (context == null) throw new NullPointerException("Context == null");
        if (!isInitialized()) {
            if (context.getApplicationContext() != null)
                this.mContext = context.getApplicationContext();
            else this.mContext = context;
            IMPL.init(mContext);
        } else {
            if (DEBUG) Log.d(TAG, "Already initialized, ignore this init operation.");
        }
    }

    /**
     * 是否初始化
     */
    public boolean isInitialized() {
        return mContext != null;
    }

    /**
     * 设置检视器
     *
     * @param inspector 检视器
     */
    public void setInspector(Inspector inspector) {
        this.inspector = inspector;
    }

    /**
     * 显示图片
     *
     * @param view 需要显示的视图
     * @param url  需要显示的地址
     */
    public void display(View view, String url) {
        if (inspector != null && inspector.onBeforeLoading(IMPL, url, view, null)) {
            return;
        }
        IMPL.display(view, url);
    }

    /**
     * 显示图片
     *
     * @param iv  图片需要显示的视图
     * @param url 图片显示的地址
     */
    public void display(ImageView iv, String url) {
        if (inspector != null && inspector.onBeforeLoading(IMPL, url, iv, null)) {
            return;
        }
        IMPL.display(iv, url);
    }

    /**
     * 显示图片
     *
     * @param iv
     * @param url
     * @param isGif
     */
    public void display(ImageView iv, String url, boolean isGif) {
        if (!isGif) {
            display(iv, url);
        } else {
          //  displayGif(iv, url);
        }
    }

//    @Override
//    public void displayGif(ImageView iv, String url) {
//        IMPL.displayGif(iv, url);
//    }

    /**
     * 显示图片
     *
     * @param view    图片需要显示的视图
     * @param url     图片显示的地址
     * @param options 显示图片的选项
     */
    public void display(View view, String url, ImageDisplayOptions options) {
        if (inspector != null && inspector.onBeforeLoading(IMPL, url, view, options)) {
            return;
        }
        IMPL.display(view, url, options);
    }

    /**
     * 显示图片
     *
     * @param iv      图片显示的视图
     * @param url     图片显示的地址
     * @param options 显示图片的选项
     */
    public void display(ImageView iv, String url, ImageDisplayOptions options) {
        if (inspector != null && inspector.onBeforeLoading(IMPL, url, iv, options)) {
            return;
        }
        IMPL.display(iv, url, options);
    }
}

