package com.jackmar.jframelibray.view.rvlist.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jackmar.jframelibray.view.rvlist.JRecyclerView;

/**
 * Created by WuXiaolong on 2015/7/7.
 */
public class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
    private JRecyclerView mJRecyclerView;
    private static final String TAG = "RecyclerViewOnScroll";

    public RecyclerViewOnScroll(JRecyclerView mJRecyclerView) {
        this.mJRecyclerView = mJRecyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastCompletelyVisibleItem = 0;
        int firstVisibleItem = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            //Position to find the final item of the current LayoutManager
            lastCompletelyVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            firstVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
//            Log.e(TAG, "onScrolled: ---------" + firstVisibleItem);
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
            lastCompletelyVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//            Log.e(TAG, "onScrolled: ---------" + firstVisibleItem);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
            // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
            // this array into an array of position and then take the maximum value that is the last show the position value
            int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
            lastCompletelyVisibleItem = findMax(lastPositions);
            firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
//            Log.e(TAG, "onScrolled: ---------" + firstVisibleItem);
        }
        if (firstVisibleItem == 0 || firstVisibleItem == RecyclerView.NO_POSITION) {
            if (mJRecyclerView.getPullRefreshEnable())
                mJRecyclerView.setSwipeRefreshEnable(true);
        } else {
            mJRecyclerView.setSwipeRefreshEnable(false);
        }
        if (mJRecyclerView.getLoadEnable() &&
                !mJRecyclerView.isRefresh()
                && mJRecyclerView.isHasMore()
                && (lastCompletelyVisibleItem == totalItemCount - 1)
                && !mJRecyclerView.isLoadMore()
                && (dx > 0 || dy > 0)) {
            mJRecyclerView.getFooterViewLayout().setVisibility(View.VISIBLE);
            mJRecyclerView.setIsLoadMore(true);
            mJRecyclerView.loadMore();
        }

    }


    //To find the maximum value in the array
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            //       int max    = Math.max(lastPositions,value);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
