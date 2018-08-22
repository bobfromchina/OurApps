package com.lovely3x.common.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;


/**
 * 位移动画辅助类
 * 一个我经常使用的动画位移动画辅助类
 * Created by lovely3x on 15-9-29.
 */
public class TranslationFromBottom implements View.OnClickListener {

    /**
     * 背景视图,在动画播放时对这个视图进行背景色的变化
     */
    private final View mBackgroundView;
    /**
     * 内容视图,在动画播放时的对这个视图进行位移
     */
    private final View mContentView;
    /**
     * 用来包含子背景视图和内容视图的容器视图
     * 在动画播放时会对他做可见不可见的处理
     */
    private final View mContainerView;

    /**
     * 背景开始的颜色
     */
    private int startColor = 0x00000000;

    /**
     * 背景停止的颜色
     */
    private int stopColor = 0x77000000;

    /**
     * 动画播放时长
     */
    private int mDuration = 300;
    /**
     * 动画关闭监听器
     */
    private AnimListener mInAnimListener;

    /**
     * 动画显示监听器
     */
    private AnimListener mOutAnimListener;

    private boolean isShowing;


    /**
     * @param containerView  内容视图和背景视图容器
     * @param backgroundView 背景市图
     * @param contentView    内容视图
     */
    public TranslationFromBottom(View containerView, View backgroundView, View contentView) {
        this.mBackgroundView = backgroundView;
        this.mContentView = contentView;
        this.mContainerView = containerView;
        mContainerView.setClickable(false);
    }

    public void setInAnimListener(AnimListener listener) {
        this.mInAnimListener = listener;
    }

    public void setOutAnimListener(AnimListener mOutAnimListener) {
        this.mOutAnimListener = mOutAnimListener;
    }

    /**
     * 播放显示动画
     *
     * @param duration 动画播放时间
     */
    public void out(int duration) {
        //背景色动画
        ObjectAnimator backgroundAnim = ObjectAnimator.ofInt(mBackgroundView, "backgroundColor", startColor, stopColor);
        backgroundAnim.setEvaluator(new AlphaEvaluator());
        //位移动画
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mContentView, "translationY", mContentView.getMeasuredHeight(), 0);
        AnimatorSet as = new AnimatorSet();
        as.playTogether(backgroundAnim, translationY);
        as.setDuration(duration);
        as.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mContainerView.setVisibility(View.VISIBLE);
                mContainerView.setClickable(true);
                mContainerView.setOnClickListener(TranslationFromBottom.this);
                isShowing = true;
                if (mOutAnimListener != null) mOutAnimListener.onAnimStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOutAnimListener != null) mOutAnimListener.onAnimEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mInAnimListener.onAnimOutStatus();
        as.start();
    }

    /**
     * 关闭动画
     *
     * @param duration 动画播放时间
     */
    public void in(int duration) {
        this.mDuration = duration;
        //背景色动画
        ObjectAnimator backgroundAnim = ObjectAnimator.ofInt(mBackgroundView, "backgroundColor", stopColor, startColor);
        backgroundAnim.setEvaluator(new AlphaEvaluator());
        //位移动画
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mContentView, "translationY", 0, mContentView.getMeasuredHeight());
        AnimatorSet as = new AnimatorSet();
        as.playTogether(backgroundAnim, translationY);
        as.setDuration(duration);
        as.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mInAnimListener != null) mInAnimListener.onAnimStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContainerView.setVisibility(View.INVISIBLE);
                mContainerView.setClickable(false);
                mContainerView.setOnClickListener(null);
                isShowing = false;
                if (mInAnimListener != null) mInAnimListener.onAnimEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mInAnimListener.onAnimInStatus();
        as.start();
    }

    /**
     * 关闭动画
     */
    public void in() {
        in(300);
    }

    /**
     * 没有动画的关闭
     */
    public void inNoAnim() {
        mContainerView.setVisibility(View.INVISIBLE);
        mContainerView.setClickable(false);
        mContainerView.setOnClickListener(null);
        if (mInAnimListener != null) mInAnimListener.onAnimEnd();
    }

    /**
     * 播放显示动画
     */
    public void out() {
        out(300);
    }

    @Override
    public void onClick(View v) {
        in(mDuration);
    }

    public int getStartColor() {
        return startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getStopColor() {
        return stopColor;
    }

    public void setStopColor(int stopColor) {
        this.stopColor = stopColor;
    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 动画监听器
     */
    public interface AnimListener {

        /**
         * 动画监听器
         */
        void onAnimEnd();

        /**
         * 动画开始
         */
        void onAnimStart();

        /**
         *  关闭状态
         */
        void onAnimInStatus();

        /**
         *  关闭状态
         */
        void onAnimOutStatus();
    }
}
