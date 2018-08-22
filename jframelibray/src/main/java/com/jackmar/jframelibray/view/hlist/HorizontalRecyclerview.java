package com.jackmar.jframelibray.view.hlist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by JackMar on 2016/11/8.
 * 邮箱：1261404794@qq.com
 */

public class HorizontalRecyclerview extends HorizontalBaseRecyclerview {
    private ViewGroup mView;

    public void setParentView(ViewGroup view) {
        this.mView = view;
    }

    public HorizontalRecyclerview(Context context) {
        super(context);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public HorizontalRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public HorizontalRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (this.mView != null && e.getAction() != MotionEvent.ACTION_UP) {
            this.mView.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (this.mView != null && e.getAction() != MotionEvent.ACTION_UP) {
            this.mView.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (this.mView != null && e.getAction() != MotionEvent.ACTION_UP) {
            this.mView.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
    }
}
