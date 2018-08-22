package com.lovely3x.common.widgets;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 这个视图中应该仅拦截 "垂直滑动" 事件
 * 这个视图应该有且仅有 "两个状态"
 * 状态一:头部为可见(展开状态) 内容区为半可见或不可见状态
 * 状态二:头部为不可见状态(收缩状态) 内容区为全可见状态
 * Created by lovely3x on 16/8/14.
 */
public class HeaderViewPager extends ViewGroup {

    private static final String TAG = "HeaderViewPager";

    private View mContentView;
    private View mHeaderView;
    private int mTouchSlop;
    private int childTopAndBottomOffset;

    /**
     * 头部的高度偏移量
     */
    private int mHeaderHeightOffset;

    private static final int STATE_HEADER_VISIBLE = 1;
    private static final int STATE_HEADER_INVISIBLE = 2;

    private int state = STATE_HEADER_VISIBLE;
    private Scroller mScroller;
    private ScrollListener mScrollListener;
    private boolean mForceReLayout;
    private int mActivePointerId;

    public HeaderViewPager(Context context) {
        this(context, null);
    }

    public HeaderViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int childCount = getChildCount();//应该是有且仅有 2 个
        if (childCount != 2) throw new IllegalArgumentException("Must have two child's.");

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight(), MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom(), MeasureSpec.getMode(heightMeasureSpec));

        //第一个视图的高
        int headerViewHeightMeasureSpec = 0;

        //第二个视图的高
        int contentViewHeightMeasureSpec = 0;

        //头部视图
        final View headerView = this.mHeaderView = getChildAt(0);
        //内容视图
        final View contentView = this.mContentView = getChildAt(1);

        //计算第一个视图的高度
        LayoutParams headerLayoutParameters = headerView.getLayoutParams();
        headerLayoutParameters = headerLayoutParameters == null ? generateDefaultLayoutParams() : headerLayoutParameters;

        if (headerLayoutParameters.height == LayoutParams.WRAP_CONTENT) {//
            headerViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
        } else if (headerLayoutParameters.height == LayoutParams.MATCH_PARENT) {//精确指定
            headerViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        } else if (headerLayoutParameters.height > 0) {//精确指定
            headerViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(headerLayoutParameters.height, MeasureSpec.EXACTLY);
        }

        //计算第二个视图的高度
        LayoutParams contentLayoutParams = contentView.getLayoutParams();
        contentLayoutParams = contentLayoutParams == null ? generateDefaultLayoutParams() : contentLayoutParams;

        if (contentLayoutParams.height == LayoutParams.WRAP_CONTENT) {//
            contentViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - mHeaderHeightOffset, MeasureSpec.AT_MOST);
        } else if (contentLayoutParams.height == LayoutParams.MATCH_PARENT) {//精确指定
            contentViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - mHeaderHeightOffset, MeasureSpec.EXACTLY);
        } else if (contentLayoutParams.height > 0) {//精确指定
            contentViewHeightMeasureSpec = MeasureSpec.makeMeasureSpec(contentLayoutParams.height - mHeaderHeightOffset, MeasureSpec.EXACTLY);
        }

        int height = 0;
        int width = 0;

        //测量第一个视图
        headerView.measure(widthMeasureSpec, headerViewHeightMeasureSpec);
        height += headerView.getMeasuredHeight();

        //测量第二个视图
        contentView.measure(widthMeasureSpec, contentViewHeightMeasureSpec);
        height += contentView.getMeasuredHeight();

        //宽度取两个视图中大的那一个
        width = Math.max(headerView.getMeasuredWidth(), contentView.getMeasuredWidth());

        //设置测量结果
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height + getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int marginLeft = 0;
        int marginRight = 0;
        int marginTop = 0;
        int marginBottom = 0;

        if (getLayoutParams() instanceof MarginLayoutParams) {
            marginLeft = ((MarginLayoutParams) getLayoutParams()).leftMargin;
            marginRight = ((MarginLayoutParams) getLayoutParams()).rightMargin;

            marginTop = ((MarginLayoutParams) getLayoutParams()).topMargin;
            marginBottom = ((MarginLayoutParams) getLayoutParams()).bottomMargin;
        }

        l -= marginLeft;
        r -= marginRight;

        t -= marginTop;
        b -= (marginBottom);

        //为第一个视图布局
        mHeaderView.layout(l + getPaddingLeft(), t + getPaddingTop(), r - getPaddingRight(), t + mHeaderView.getMeasuredHeight());

        //为第二个视图布局
        mContentView.layout(l + getPaddingLeft(), t + mHeaderView.getMeasuredHeight(), r - getPaddingRight(), b - getPaddingBottom());


        if (mForceReLayout) {
            mForceReLayout = false;
            reportOnScrollListener(-childTopAndBottomOffset);
            childTopAndBottomOffset = 0;
        } else {
            final int temp = childTopAndBottomOffset;
            childTopAndBottomOffset = 0;
            reportOnScrollListener(-temp);
            offsetAllChildTopAndBottom(temp);
        }

    }

    /**
     * 初始化
     */
    private void init() {
        ViewConfiguration config = ViewConfiguration.get(getContext());
        this.mTouchSlop = config.getScaledTouchSlop() / 2;
        this.mScroller = new Scroller(getContext(), new ViscousFluidInterpolator());
    }

    private int lastX = -1;
    private int lastY = -1;
    private boolean mIsBeginDrag;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN://按下
                    lastX = (int) ev.getX();
                    lastY = (int) ev.getY();
                    mActivePointerId = ev.getPointerId(0);
                    break;
                case MotionEvent.ACTION_MOVE://移动
                    int index = ev.findPointerIndex(mActivePointerId);
                    if (index == -1) {
                        return super.onInterceptTouchEvent(ev);
                    }
                    final int x = (int) ev.getX(index);
                    final int y = (int) ev.getY(index);

                    final int yDelta = y - lastY;
                    final int xDelta = x - lastX;

                    if (!mIsBeginDrag) {
                        if (Math.abs(yDelta) >= mTouchSlop && Math.abs(yDelta) > Math.abs(xDelta)) {
                            if (yDelta > 0 && !contentViewCanScrollDown(x, y)) {//往下滑动
                                mIsBeginDrag = true;
                            } else if (yDelta < 0 && state == STATE_HEADER_VISIBLE) {//往上滑动
                                mIsBeginDrag = true;
                            }
                        }
                    } else {
                        if (yDelta > 0) {//往下滑
                            //想要将头部视图拉出来,就必须要 当前头部视图不可见
                            //并且内容视图不能再向下滑动
                            if (!contentViewCanScrollDown(x, y)) {
                                mIsBeginDrag = tryToMove(yDelta);
                            }
                        } else if (yDelta < 0) {//往上滑
                            mIsBeginDrag = tryToMove(yDelta);
                        }
                    }

                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_UP://弹起
                    lastX = -1;
                    lastY = -1;
                    mIsBeginDrag = false;
                    break;
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return mIsBeginDrag;
    }

    /**
     * 尝试移动
     *
     * @param yDelta
     * @return
     */
    private boolean tryToMove(int yDelta) {
        boolean isMoved = false;
        if (yDelta < 0) {//往上推
            if (mHeaderView.getTop() - mHeaderHeightOffset <= -mHeaderView.getHeight()) {
                state = STATE_HEADER_INVISIBLE;
            } else {
                requestDisallowInterceptTouchEvent(true);
                offsetAllChildTopAndBottom(Math.max(yDelta, -(mHeaderView.getHeight() - mHeaderHeightOffset) - mHeaderView.getTop()));
                isMoved = true;
            }
        } else if (yDelta > 0) {//往下拉
            if (mHeaderView.getTop() >= 0) {
                state = STATE_HEADER_VISIBLE;
            } else {
                requestDisallowInterceptTouchEvent(true);
                offsetAllChildTopAndBottom(Math.min(yDelta, 0 - mHeaderView.getTop()));
                isMoved = true;
            }
        }
        return isMoved;
    }

    public void offsetAllChildTopAndBottom(int offset) {
        final int N = getChildCount();
        for (int i = 0; i < N; i++) {
            View child = getChildAt(i);
            child.offsetTopAndBottom(offset);
        }
        childTopAndBottomOffset += offset;

        reportOnScrollListener(offset);
    }

    public int getHeaderHeightOffset() {
        return mHeaderHeightOffset;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getContentView() {
        return mContentView;
    }

    public int getTotalScrollOffset() {
        return childTopAndBottomOffset;
    }

    public void reportOnScrollListener(int delta) {
        if (mScrollListener != null) mScrollListener.onScroll(delta);
    }

    public void setScrollListener(ScrollListener listener) {
        this.mScrollListener = listener;
    }

    public ScrollListener getScrollListener() {
        return mScrollListener;
    }

    public boolean headerIsVisible() {
        return state == STATE_HEADER_VISIBLE;
    }

    /**
     * 内容视图是否还能下滑
     *
     * @return
     */
    public boolean contentViewCanScrollDown(int x, int y) {
        return canScroll(mContentView, true, 1, x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN://按下
                    mActivePointerId = event.getPointerId(0);
                    lastX = (int) event.getX();
                    lastY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN: {
                    final int index = MotionEventCompat.getActionIndex(event);
                    mActivePointerId = event.getPointerId(index);

                    lastX = (int) event.getX(index);
                    lastY = (int) event.getY(index);
                    break;
                }
                case MotionEvent.ACTION_MOVE://移动
                {

                    final int pointerIndex = event.findPointerIndex(mActivePointerId);
                    if (pointerIndex == -1) {
                        mActivePointerId = -1;
                        break;
                    }
                    final int x = (int) event.getX(pointerIndex);
                    final int y = (int) event.getY(pointerIndex);

                    final int yDelta = y - lastY;
                    final int xDelta = x - lastX;

                    if (!mIsBeginDrag) {
                        if (Math.abs(yDelta) >= mTouchSlop && Math.abs(yDelta) > Math.abs(xDelta)) {
                            if (yDelta > 0 && !contentViewCanScrollDown(x, y)) {//往下滑动
                                mIsBeginDrag = true;
                            } else if (yDelta < 0 && state == STATE_HEADER_VISIBLE) {//往上滑动
                                mIsBeginDrag = true;
                            }
                        }
                    } else {
                        if (yDelta > 0) {//往下滑
                            //想要将头部视图拉出来,就必须要 当前头部视图不可见
                            //并且内容视图不能再向下滑动
                            if (!contentViewCanScrollDown(x, y)) {
                                mIsBeginDrag = tryToMove(yDelta);
                            }
                        } else if (yDelta < 0) {//往上滑
                            mIsBeginDrag = tryToMove(yDelta);
                        }
                    }

                    lastX = x;
                    lastY = y;
                    break;
                }
                case MotionEvent.ACTION_UP://弹起
                    lastX = -1;
                    lastY = -1;
                    mIsBeginDrag = false;
                    adjustViewOffset();
                    break;
                case MotionEvent.ACTION_POINTER_UP: {
                    onSecondaryPointerUp(event);
                    int index = event.findPointerIndex(mActivePointerId);
                    lastX = (int) event.getX(index);
                    lastY = (int) event.getY(index);
                    break;
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return mIsBeginDrag;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            lastX = (int) ev.getX(newPointerIndex);
            lastY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    /**
     * 参考 {@link android.support.v4.view.ViewPager#canScroll(View, boolean, int, int, int)}
     * <p>
     * Tests scrollability within child views of v given a delta of dx.
     *
     * @param v      View to test for horizontal scrollability
     * @param checkV Whether the view v passed should itself be checked for scrollability (true),
     *               or just its children (false).
     * @param dy     Delta scrolled in pixels
     * @param x      X coordinate of the active touch point
     * @param y      Y coordinate of the active touch point
     * @return true if child views of v can be scrolled by delta of dx.
     */
    protected boolean canScroll(View v, boolean checkV, int dy, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
                        y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
                        canScroll(child, true, dy, x + scrollX - child.getLeft(),
                                y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollVertically(v, -dy);
    }

    /**
     * 调整当前的视图的状态
     */
    protected void adjustViewOffset() {
        if (-mHeaderView.getTop() <= mHeaderView.getHeight() / 2) {
            state = STATE_HEADER_VISIBLE;
            //弹出整个头视图
            mScroller.startScroll(0, 0, 0, 0 - mHeaderView.getTop());
            postInvalidateOnAnimation();
        } else {//收缩整个头视图
            state = STATE_HEADER_INVISIBLE;
            mScroller.startScroll(0, 0, 0, -(mHeaderView.getTop() + mHeaderView.getHeight() - mHeaderHeightOffset));
            postInvalidateOnAnimation();
        }

    }

    private int preY = 0;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            int delta = mScroller.getCurrY() - preY;
            preY = mScroller.getCurrY();
            offsetAllChildTopAndBottom(delta);
            postInvalidateOnAnimation();
        } else {
            preY = 0;
        }
    }


    public void setHeaderHeightOffset(int headerHeightOffset) {
        this.mHeaderHeightOffset = headerHeightOffset;
        mForceReLayout = true;
        requestLayout();
    }


    public interface ScrollListener {
        void onScroll(int delta);
    }


    public static class ViscousFluidInterpolator implements Interpolator {
        /**
         * Controls the viscous fluid effect (how much of it).
         */
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        private static final float VISCOUS_FLUID_NORMALIZE;
        private static final float VISCOUS_FLUID_OFFSET;

        static {

            // must be set to 1.0 (used in viscousFluid())
            VISCOUS_FLUID_NORMALIZE = 1.0f / viscousFluid(1.0f);
            // account for very small floating-point error
            VISCOUS_FLUID_OFFSET = 1.0f - VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f);
        }

        private static float viscousFluid(float x) {
            x *= VISCOUS_FLUID_SCALE;
            if (x < 1.0f) {
                x -= (1.0f - (float) Math.exp(-x));
            } else {
                float start = 0.36787944117f;   // 1/e == exp(-1)
                x = 1.0f - (float) Math.exp(1.0f - x);
                x = start + x * (1.0f - start);
            }
            return x;
        }

        @Override
        public float getInterpolation(float input) {
            final float interpolated = VISCOUS_FLUID_NORMALIZE * viscousFluid(input);
            if (interpolated > 0) {
                return interpolated + VISCOUS_FLUID_OFFSET;
            }
            return interpolated;
        }
    }
}
