package com.lovely3x.common.utils;

import android.os.Parcelable;

/**
 * 实现这个类表示可以被显示
 * Created by lovely3x on 15-12-1.
 */
public interface Displayable extends Parcelable,Identity{
    /**
     * 显示的时候调用
     *
     * @return 返回用于显示的字符串
     */
    String display();
}
