package com.example.wangbo.ourapp.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * base view holder hold the view
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View rootView) {
        super(rootView);
        ButterKnife.bind(this, rootView);
    }

}
