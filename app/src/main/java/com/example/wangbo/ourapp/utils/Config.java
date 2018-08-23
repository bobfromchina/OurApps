package com.example.wangbo.ourapp.utils;

/**
 * 配置文件
 * Created by lovely3x on 15-11-16.
 */
public class Config {

    /**
     * 读取数据超时
     */
    public static final long READ_TIME_OUT = 20 * 1000;

    /**
     * 写入数据超时
     */
    public static final long WRITE_TIME_OUT = 20 * 1000;

    /**
     * 连接超时
     */
    public static final long CONNECT_TIME_OUT = 20 * 1000;

    /**
     * 是否开启debug模式
     */
    public static final boolean DEBUG = true;// BuildConfig.DEBUG;

    /**
     * 是否开启模拟数据
     */
    public static final boolean MOCK = false;
    /**
     * 默认的页码行
     */
    public static final int PAGE_ROWS = 10;

    /**
     * 动态的页码行
     */
    public static final int DYNAMIC_PAGE_ROWS = 20;

    /**
     * 图片的最大宽度
     */
    public static final int IMAGE_WIDTH = 1000;
    /**
     * 图片的最大高度
     */
    public static final int IMAGE_HEIGHT = 1000;

    /**
     * 图片的缩放因子
     */
    public static final float scaleFactor = 0.9f;

}
