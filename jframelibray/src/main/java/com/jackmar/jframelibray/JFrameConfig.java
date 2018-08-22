package com.jackmar.jframelibray;

import android.content.Context;

import com.jackmar.jframelibray.utils.LogUtil;
import com.jackmar.jframelibray.utils.ScreenUtil;
import com.jackmar.jframelibray.utils.imagepicker.ImagePickerUtils;

/**
 *
 */

public class JFrameConfig {
    static JFrameConfig single;
    //标题栏的背景色
    private int titleBackgroundColorRes;
    //title 文字颜色
    private int titleTextColor;
    //title 文字大小
    private int titleTextSize;
    //title 可返回
    private boolean titleCanBack;
    //title 右侧文字大小
    private int titleMoreTextSize;
    //title 右侧文字颜色
    private int titleMoreTextColor;
    //title 右侧返回图标资源
    private int backImage;
    private Context context;
    //是否已经初始化配置文件
    private boolean isCreate = false;
    //设置网络是否是调试状态
    private boolean isDebug = false;
    //服务器地址
    private String HostUrl;

    private JFrameConfig(Context context) {
        this.context = context;
    }

    public synchronized static JFrameConfig getInstance(Context context) {
        if (single == null) {
            single = new JFrameConfig(context);
        }
        return single;
    }

    public JFrameConfig init() {
        ImagePickerUtils.initImagePicker();
        ScreenUtil.defaultCenter().init(context);
        return this;
    }

    public JFrameConfig setTitleBackgroundColorRes(int titleBackgroundColorRes) {
        this.titleBackgroundColorRes = titleBackgroundColorRes;
        return this;
    }

    public JFrameConfig setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    public JFrameConfig setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public JFrameConfig setTitleCanBack(boolean titleCanBack) {
        this.titleCanBack = titleCanBack;
        return this;
    }

    public JFrameConfig setTitleMoreTextSize(int titleMoreTextSize) {
        this.titleMoreTextSize = titleMoreTextSize;
        return this;
    }

    public JFrameConfig setBackImage(int backImage) {
        this.backImage = backImage;
        return this;
    }

    public JFrameConfig setTitleMoreTextColor(int titleMoreTextColor) {
        this.titleMoreTextColor = titleMoreTextColor;
        return this;
    }

    public JFrameConfig setDebug(boolean debug) {
        isDebug = debug;
        LogUtil.setDebug(isDebug);
        return this;
    }

    public JFrameConfig setHostUrl(String hostUrl) {
        HostUrl = hostUrl;
        return this;
    }


    /**
     * 初始化build
     */
    public void build() {
        isCreate = true;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public String getHostUrl() {
        return HostUrl;
    }

    public int getTitleBackgroundColorRes() {
        return titleBackgroundColorRes;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public boolean getTitleCanBack() {
        return titleCanBack;
    }

    public int getTitleMoreTextSize() {
        return titleMoreTextSize;
    }

    public int getTitleMoreTextColor() {
        return titleMoreTextColor;
    }

    public int getBackImage() {
        return backImage;
    }

    public boolean isTitleCanBack() {
        return titleCanBack;
    }

    public boolean isCreate() {
        return isCreate;
    }
}
