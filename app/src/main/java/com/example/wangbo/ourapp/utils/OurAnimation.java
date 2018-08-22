package com.example.wangbo.ourapp.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.wangbo.ourapp.R;

/**
 * Created by wangbo on 2018/8/22.
 * <p>
 * 一个列表加载小动画
 */
public class OurAnimation {

    /**
     * top
     */
    public static void runLayoutAnimation(final RecyclerView recylerView) {
        final Context context = recylerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context
                , R.anim.layout_animation_fall_down);
        recylerView.setLayoutAnimation(controller);
        recylerView.getAdapter().notifyDataSetChanged();
        recylerView.scheduleLayoutAnimation();
    }

    /**
     * bottom
     */
    public static void runLayoutAnimationFromBottom(final RecyclerView recylerView) {
        final Context context = recylerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context
                , R.anim.layout_animation_from_bottom);
        recylerView.setLayoutAnimation(controller);
        recylerView.getAdapter().notifyDataSetChanged();
        recylerView.scheduleLayoutAnimation();
    }

    /**
     * right
     */
    public static void runLayoutAnimationFromRight(final RecyclerView recylerView) {
        final Context context = recylerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context
                , R.anim.layout_animation_slide_right);
        recylerView.setLayoutAnimation(controller);
        recylerView.getAdapter().notifyDataSetChanged();
        recylerView.scheduleLayoutAnimation();
    }

}
