package com.lovely3x.common.fragments.refresh;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.lovely3x.common.activities.refresh.RefreshAndLoadMore;
import com.lovely3x.common.adapter.ListAdapter;
import com.lovely3x.common.fragments.BaseCommonFragment;
import com.lovely3x.common.utils.Response;

/**
 * 使用支持包中的SwipeRefreshFragment
 * Created by lovely3x on 16-1-22.
 */
public class SwipeRefreshAndMoreFragment extends BaseCommonFragment implements RefreshAndLoadMore {
    @Override
    protected int getContentView() {
//        SwipeRefreshLayout
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void restoreInstanceOnCreateBefore(Bundle savedInstanceState) {

    }

    @Override
    protected void restoreInstanceOnCreateAfter(Bundle savedInstanceState) {

    }

    @Override
    protected void onViewInitialized() {

    }

    @Override
    public void onBeginRefresh() {

    }

    @Override
    public void onBeginLoadMore() {

    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {

    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {

    }

    @Override
    public void handleRefreshState(@NonNull Response response) {

    }

    @Override
    public void handleRefreshState(Response response, ListAdapter listAdapter) {

    }

    @Override
    public void autoRefresh() {

    }
}
