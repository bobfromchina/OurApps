package com.lovely3x.common.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 边缘滑动ViewPager
 * 只有当在ViewPager的边缘才能滑动
 * Created by lovely3x on 16-3-8.
 */
public class EdgeViewPager extends ViewPager {

    public int EDGE_WIDTH = 0;

    public EdgeViewPager(Context context) {
        this(context, null);
    }

    public EdgeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        EDGE_WIDTH = (int) (context.getResources().getDisplayMetrics().density * 10);
    }

    public int getEdgeWidth() {
        return EDGE_WIDTH;
    }

    public void setEdgeWidth(int edge_width) {
        this.EDGE_WIDTH = edge_width;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return ev.getX() < EDGE_WIDTH || ev.getX() > (getWidth() - EDGE_WIDTH);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
