package com.lovely3x.common.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.emptytip.ExactEmptyContentTipActivity;
import com.lovely3x.common.activities.refresh.CanLoadMore;
import com.lovely3x.common.activities.refresh.CanRefresh;
import com.lovely3x.common.activities.refresh.RefreshAndLoadMore;
import com.lovely3x.common.adapter.ListAdapter;
import com.lovely3x.common.adapter.RecyclerListAdapter;
import com.lovely3x.common.requests.BaseCodeTable;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.PageableData;
import com.lovely3x.common.utils.Response;
import com.lovely3x.common.utils.ViewUtils;

import java.util.List;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainerBase;
import in.srain.cube.views.ptr.loadmore.LoadMoreDefaultFooterView;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;

/**
 * 使用Ptr实现了下拉刷新和加载更多
 * 还有空内容提示界面
 * Created by lovely3x on 16-1-21.
 */
public abstract class PLEActivity extends ExactEmptyContentTipActivity implements RefreshAndLoadMore, PtrHandler, LoadMoreHandler {


    /*
     * 当前的下标
     */
    protected int currentIndex;
    /**
     * 是否启动下拉刷新
     */
    private boolean canRefresh;
    /**
     * 是否启动加载更多
     */
    private boolean canLoadMore;
    /**
     * 下拉刷新布局
     */
    private PtrFrameLayout mRefreshLayout;
    /**
     * 加载更多的布局
     */
    private LoadMoreContainerBase mLoadMoreLayout;

    @Override
    @CallSuper
    protected void initViews() {
        //是否可以加载跟多和下拉刷新
        canLoadMore = this instanceof CanLoadMore;
        canRefresh = this instanceof CanRefresh;
        //初始化布局
        if (canRefresh) {
            this.mRefreshLayout = ButterKnife.findById(this, getRefreshContainerLayoutId());
            if (mRefreshLayout == null)
                ALog.e(TAG, "你虽然开启了下拉刷新，但是你却没有给定下拉刷新的布局的id,你可以通过#getRefresLayoutId返回布局id");
            initRefreshLayoutOptions();
        }
        if (canLoadMore) {
            this.mLoadMoreLayout = ButterKnife.findById(this, getLoadMoreContainerLayoutId());
            if (mLoadMoreLayout == null)
                ALog.e(TAG, "你虽然开启了加更多，但是你却没有给定加载更多的布局的id,你可以通过#getLoadMoreContainerLayoutId返回布局id");
            initLoadMoreLayoutOptions();
        }
    }

    @Override
    public abstract void onBeginRefresh();

    @Override
    public abstract void onBeginLoadMore();

    /**
     * 初始化加载更多的参数
     */
    protected void initLoadMoreLayoutOptions() {
        LoadMoreDefaultFooterView footerView = new LoadMoreDefaultFooterView(this);

        if (mLoadMoreLayout.getChildCount() > 0 && mLoadMoreLayout.getChildAt(0) instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) mLoadMoreLayout.getChildAt(0)).getAdapter();
        }

        footerView.setVisibility(View.GONE);
        mLoadMoreLayout.setLoadMoreView(footerView);
        mLoadMoreLayout.setLoadMoreHandler(this);
        mLoadMoreLayout.setLoadMoreUIHandler(footerView);
        mLoadMoreLayout.loadMoreFinish(true, true);
    }

    /**
     * 初始化下拉刷新的参数
     */
    protected void initRefreshLayoutOptions() {
        // header
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(mActivity);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, ViewUtils.dp2pxF(8), 0, ViewUtils.dp2pxF(8));
        ///设置正在加载的最短时间
        mRefreshLayout.setLoadingMinTime(500);
        //设置关闭加载更多头部关闭需要消耗的时间
        mRefreshLayout.setDurationToCloseHeader(1000);
        //设置刷新的header
        mRefreshLayout.setHeaderView(header);
        //关闭横向滚动
        mRefreshLayout.disableWhenHorizontalMove(true);
        //设置ui变化操作器
        mRefreshLayout.addPtrUIHandler(header);
        //设置刷新操作监听器
        mRefreshLayout.setPtrHandler(this);
    }


    @Override
    public void handleRefreshState(@NonNull Response response) {
        if (response.obj instanceof PageableData) {
            handlePageableDataRefreshState(response, null);
        } else {


            if (canRefresh) {
                //当前获取的数据的页码为第一页
                if (response.addtional == getFirstPageIndex()) {
                    mRefreshLayout.refreshComplete();
                    if (canLoadMore) {
                        if (response.obj instanceof List) {
                            if (response.isSuccessful) {
                                if (((List) response.obj).isEmpty()) {
                                    mLoadMoreLayout.loadMoreFinish(true, false);
                                } else {
                                    mLoadMoreLayout.loadMoreFinish(false, true);
                                }
                            } else {
                                mLoadMoreLayout.loadMoreFinish(true, true);
                            }
                        } else {
                            mLoadMoreLayout.loadMoreFinish(false, true);
                        }
                    }
                }
            }
            if (canLoadMore) {
                if (response.addtional != DEFAULT_FIRST_PAGE) {
                    if (response.obj instanceof List) {
                        if (BaseCodeTable.getInstance().getSuccessfulCode() == response.errorCode) {
                            if (((List) response.obj).isEmpty()) {
                                mLoadMoreLayout.loadMoreFinish(true, false);
                            } else {
                                mLoadMoreLayout.loadMoreFinish(false, true);
                            }
                        } else if (BaseCodeTable.getInstance().getEmptyDataCode() == response.errorCode) {
                            mLoadMoreLayout.loadMoreFinish(true, false);
                        } else {//加载失败，恢复下标
                            currentIndex = response.addtional;
                            mLoadMoreLayout.loadMoreFinish(true, true);
                        }
                    } else {
                        boolean isEmptyResult = response.obj == null || (response.obj instanceof List && (((List) response.obj).isEmpty()));
                        mLoadMoreLayout.loadMoreFinish(isEmptyResult, false);
                    }
                }
            }
        }
    }

    @Override
    public void handleRefreshState(Response response, ListAdapter listAdapter) {
        if (response.obj instanceof PageableData) {
            handlePageableDataRefreshState(response, listAdapter);
        } else {
            handleRefreshState(response);
            if ((response.addtional == 0 && ((response.obj instanceof List && ((List) response.obj).isEmpty()) || response.obj == null))) {
                listAdapter.clear();
            }
        }
    }

    public void handlePageableDataRefreshState(Response response, ListAdapter listAdapter) {
        if (!(response.obj instanceof PageableData)) throw new IllegalArgumentException();

        PageableData pageableData = (PageableData) response.obj;

        if (canRefresh) {
            //当前获取的数据的页码为第一页
            if (pageableData.getNowpage() == getReturnFirstPageIndex()) {
                mRefreshLayout.refreshComplete();
                if (canLoadMore) {
                    if (response.isSuccessful) {
                        if (pageableData.getJSONArrayList() == null || pageableData.getJSONArrayList().length() == 0) {
                            mLoadMoreLayout.loadMoreFinish(true, false);
                        } else {
                            mLoadMoreLayout.loadMoreFinish(false, true);
                        }
                    } else {
                        mLoadMoreLayout.loadMoreFinish(true, true);
                    }
                }
            }
        }
        if (canLoadMore) {
            if (pageableData.getNowpage() != getReturnFirstPageIndex()) {
                if (BaseCodeTable.getInstance().getSuccessfulCode() == response.errorCode) {
                    if (pageableData.getJSONArrayList() == null || pageableData.getJSONArrayList().length() == 0) {
                        mLoadMoreLayout.loadMoreFinish(true, false);
                    } else {
                        mLoadMoreLayout.loadMoreFinish(false, true);
                    }
                } else if (BaseCodeTable.getInstance().getEmptyDataCode() == response.errorCode) {
                    mLoadMoreLayout.loadMoreFinish(true, false);
                } else {//加载失败，恢复下标
                    rollbackIndex(response, listAdapter);
                    mLoadMoreLayout.loadMoreFinish(true, true);
                }

            }
        }
        if (pageableData.getNowpage() == getReturnFirstPageIndex() && (pageableData.getJSONArrayList() == null || pageableData.getJSONArrayList().length() == 0)) {
            listAdapter.clear();
        }
    }

    /**
     * 恢复下标
     * 用于在加载更多失败的时候调用
     * 比如我们在加载第三的数据时失败，那么因为我们之前已经将index改变，所以现在需要回滚到之前的index
     *
     * @param response
     * @param listAdapter
     */
    protected void rollbackIndex(Response response, ListAdapter listAdapter) {
        if (response.obj instanceof PageableData) {
            currentIndex = ((PageableData) response.obj).getNowpage() - getReturnFirstPageIndex();
        } else {
            currentIndex = response.addtional--;
        }
    }


    public void handleRefreshState(Response response, RecyclerListAdapter listAdapter) {
        handleRefreshState(response);
        if ((response.addtional == 0 && ((response.obj instanceof List && ((List) response.obj).isEmpty()) || response.obj == null))) {
            listAdapter.clear();
        }
    }

    /**
     * 获取下拉刷新布局容器的id
     * 默认的id是{@link com.lovely3x.common.R.id#pull_refresh}
     *
     * @return 下拉刷新布局容器的id
     */
    protected int getRefreshContainerLayoutId() {
        return R.id.pull_refresh;
    }

    /***
     * 获取加载更多的容器的id
     * 默认的id是{@link com.lovely3x.common.R.id#load_more}
     *
     * @return 加载更多的容器id
     */
    protected int getLoadMoreContainerLayoutId() {
        return R.id.load_more;
    }

    /**
     * 是否实现了下拉刷新的标记
     *
     * @return true or false
     */
    public boolean isCanRefresh() {
        return canRefresh;
    }

    /**
     * 是否实现了加载更多的标记
     *
     * @return true or false
     */
    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    /**
     * 获取刷新布局对象
     *
     * @return 下拉刷新布局对象
     */
    public PtrFrameLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    /**
     * 获取加载更多布局对象
     *
     * @return 加载更多布局容器对象
     */
    public LoadMoreContainer getLoadMoreLayout() {
        return mLoadMoreLayout;
    }

    /**
     * 获取第一页的页码，默认为0
     *
     * @return 第一页的默认页码
     */
    @Deprecated
    protected int getFirstPageIndex() {
        return DEFAULT_POST_FIRST_PAGE;
    }

    /**
     * 获取提交数据时第一页的页码，默认为{@link #DEFAULT_POST_FIRST_PAGE}
     *
     * @return 提交数据时第一页的默认页码
     */
    protected int getPostFirstPageIndex() {
        return DEFAULT_POST_FIRST_PAGE;
    }

    /**
     * 获取返回的数据第一页的页码，默认为{@link #DEFAULT_POST_FIRST_PAGE}
     *
     * @return 第一页的默认页码
     */
    protected int getReturnFirstPageIndex() {
        return DEFAULT_RETURN_FIRST_PAGE;
    }

    /**
     * 下标自加1
     * 返回增 加后的值
     */
    protected int indexPlusPlus() {
        currentIndex++;
        return currentIndex;
    }

    @Override
    public void refreshComplete() {
        if (mRefreshLayout != null) mRefreshLayout.refreshComplete();
    }

    @Override
    public void loadMoreFinish(boolean emptyResult, boolean hasMore) {
        if (mLoadMoreLayout != null) mLoadMoreLayout.loadMoreFinish(emptyResult, hasMore);
    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {
        if (mLoadMoreLayout != null) mLoadMoreLayout.loadMoreError(errorCode, errorMessage);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return checkCanDoRefresh((ViewGroup) frame, content, header);
    }

    /**
     * 自雷可以通过重写这个方法来判断当前是否可以刷新
     *
     * @param refreshContainer 需要刷新的控件的容器
     * @param contentView      需要刷新的试图
     * @param header           刷新的头部
     * @return 是否可以刷新
     */
    public boolean checkCanDoRefresh(ViewGroup refreshContainer, View contentView, View header) {
        if (contentView instanceof LoadMoreContainerBase) {
            return !PtrDefaultHandler.canChildScrollUp(((LoadMoreContainerBase) contentView).getChildAt(0));
        } else {
            return !PtrDefaultHandler.canChildScrollUp(refreshContainer);
        }
    }

    @Override
    public void autoRefresh() {
        if (mRefreshLayout != null && canRefresh) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onBeginRefresh();
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        onBeginLoadMore();
    }
}
