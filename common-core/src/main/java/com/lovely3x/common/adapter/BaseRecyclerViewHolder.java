package com.lovely3x.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * base view holder hold the view
 * Created by lovely3x on 15-7-9.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    /**
     * please don't modify the constructor
     *
     * @param rootView the root view
     */
    public BaseRecyclerViewHolder(View rootView) {
        super(rootView);
        ButterKnife.bind(this, rootView);
    }
}
