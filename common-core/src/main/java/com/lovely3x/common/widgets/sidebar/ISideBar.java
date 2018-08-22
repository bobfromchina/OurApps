package com.lovely3x.common.widgets.sidebar;

import android.widget.TextView;

/**
 * sidebar罗
 * Created by lovely3x on 16/6/28.
 */
public interface ISideBar {

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener 设置触摸的word的监听器
     */
    void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener);


    /**
     * 设置文字显示器
     *
     * @param mTextDialog
     */
    void setTextView(TextView mTextDialog);

}
