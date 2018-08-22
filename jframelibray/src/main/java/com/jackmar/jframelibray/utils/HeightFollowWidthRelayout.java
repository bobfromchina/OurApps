package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by wangbo on 2018/8/15.
 */

public class HeightFollowWidthRelayout extends RelativeLayout {
    public HeightFollowWidthRelayout(Context context) {
        super(context);
    }

    public HeightFollowWidthRelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightFollowWidthRelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
