package com.lovely3x.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class LeftHorizontalScrollDelete extends HorizontalScrollView {


    public static final int DP = -1;
    public static final int PIX = -2;
    VelocityTracker mVelocityTracker;
    private int blackWidth = (int) (100 * getResources().getDisplayMetrics().density + 0.5);
    private boolean isOpend;
    private LeftSlidingMenuState state = LeftSlidingMenuState.open;

    public LeftHorizontalScrollDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeftHorizontalScrollDelete(Context context) {
        super(context);
    }

    public LeftHorizontalScrollDelete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param width
     * @param flag  表示传递的值是dp还是像素
     */
    public void setBlackWidth(float width, int flag) {
        switch (flag) {
            case DP:
                blackWidth = (int) (width * getResources().getDisplayMetrics().density + 0.5);
                break;
            case PIX:
                blackWidth = (int) width;
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ViewGroup direc = (ViewGroup) getChildAt(0);
        this.blackWidth = direc.getChildAt(1).getWidth();
    }

    public boolean onTouchEvent(MotionEvent ev) {

        switch (state) {//如果菜单状态是才不允许被打开,那么就直接消费事件
            case close:
                break;
            case open:
                super.onTouchEvent(ev);
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mVelocityTracker.computeCurrentVelocity(1000, Integer.MAX_VALUE);
                        float velocity = mVelocityTracker.getXVelocity();
                        Log.e("info", "velovity:" + velocity);
                        mVelocityTracker.clear();

                        int x = getScrollX();
                        Log.e("info", "当前值是x:" + x);
                        if (x < (blackWidth / 2)) {//没向右划过去
                            smoothScrollTo(0, getScrollY());
                            isOpend = false;
                        } else {
                            smoothScrollTo(blackWidth, getScrollY());//反正只能有一个方向，没左就是右
                            isOpend = true;
                        }
                        break;//抬起
                }
                mVelocityTracker.recycle();
                break;
        }

        return true;
    }

    private void initVelocityTrackerIfNotExists() {
        mVelocityTracker = VelocityTracker.obtain();
    }


    /**
     * 平滑显示滑块
     */
    public void showBlack() {
        smoothScrollTo(blackWidth, getScrollY());
        isOpend = true;
    }

    /**
     * 平滑关闭滑块的显示
     */
    public void closeBlack() {
        smoothScrollTo(0, getScrollY());
        isOpend = false;
    }

    /**
     * 立即关闭滑块的显示
     */
    public void promptlyCloseBlack() {
        scrollTo(0, getScrollY());
    }

    /**
     * 立即显示滑块
     */
    public void promptlyShowBlack() {
        scrollTo(blackWidth, getScrollY());
    }


    public boolean isShow() {
        return isOpend;
    }


    public void setMenuState(LeftSlidingMenuState state) {
        this.state = state;
    }


    /**
     * 左滑菜单 是否开启
     *
     * @author lovely3x
     */
    public enum LeftSlidingMenuState {
        close, /**
         * 关闭
         */
        open/**打开*/
    }

}
