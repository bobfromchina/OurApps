package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lovely3x on 16/7/29.
 */
public class HeightFollowWidthImageView extends ImageView {

    public HeightFollowWidthImageView(Context context) {
        super(context);
    }

    public HeightFollowWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightFollowWidthImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
