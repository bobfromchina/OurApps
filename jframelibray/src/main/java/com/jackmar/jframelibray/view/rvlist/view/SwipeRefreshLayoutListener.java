package com.jackmar.jframelibray.view.rvlist.view;

import android.support.v4.widget.SwipeRefreshLayout;

import com.jackmar.jframelibray.view.rvlist.JRecyclerView;


public class SwipeRefreshLayoutListener implements SwipeRefreshLayout.OnRefreshListener {
    private JRecyclerView mJRecyclerView;

    public SwipeRefreshLayoutListener(JRecyclerView mJRecyclerView) {
        this.mJRecyclerView = mJRecyclerView;
    }

    @Override
    public void onRefresh() {
        if (!mJRecyclerView.isRefresh()&&!mJRecyclerView.isLoadMore()) {
            mJRecyclerView.setIsRefresh(true);
            mJRecyclerView.refresh();
        }
    }
}
