package com.lovely3x.common.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * base view holder hold the view
 * Created by lovely3x on 15-7-9.
 */
public class BaseViewHolder {

    private static final java.lang.String TAG = "BaseViewHolder";
    public final View mRootView;
    private int type;

    /**
     * please don't modify the constructor
     *
     * @param rootView the root view
     */
    public BaseViewHolder(View rootView) {
        this.mRootView = rootView;
        ButterKnife.bind(this, rootView);
    }

    public BaseViewHolder(View mRootView, int type) {
        this.mRootView = mRootView;
        this.type = type;
        ButterKnife.bind(this,mRootView);
    }

    /**
     * 获取 类型
     *
     * @return 获取类型
     */
    public int getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(int type) {
        this.type = type;
    }


    public static class SimpleViewHolder extends BaseViewHolder {

        public SimpleViewHolder(View rootView) {
            super(rootView);
        }
    }
}
