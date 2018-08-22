package com.lovely3x.common.fragments.refresh;

import android.support.annotation.NonNull;
import android.view.View;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.refresh.CanLoadMore;
import com.lovely3x.common.activities.refresh.CanRefresh;
import com.lovely3x.common.activities.refresh.RefreshAndLoadMore;
import com.lovely3x.common.adapter.ListAdapter;
import com.lovely3x.common.fragments.BaseCommonFragment;
import com.lovely3x.common.requests.BaseCodeTable;
import com.lovely3x.common.utils.Response;
import com.lovely3x.common.utils.ViewUtils;

import java.util.List;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreDefaultFooterView;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;

/**
 * 使用Ptr实现了下拉刷新和加载更多的Fragment
 * 实现类 如果需要刷新则继承CanRefresh
 * 如果需要加载更多则继承CanLoadMore
 * Created by lovely3x on 16-1-21.
 */
public abstract class PtrRefreshAndLoadMoreFragment extends BaseCommonFragment implements RefreshAndLoadMore, PtrHandler, LoadMoreHandler {

    /**
     * 默认的页码
     */
    public static final int DEFAULT_FIRST_PAGE = 0;
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
    private LoadMoreContainer mLoadMoreLayout;

    @Override
    protected void initViews() {
        //是否可以加载跟多和下拉刷新
        canLoadMore = this instanceof CanLoadMore;
        canRefresh = this instanceof CanRefresh;
        //初始化布局
        if (canRefresh) {
            this.mRefreshLayout = ButterKnife.findById(mRootView, getRefreshContainerLayoutId());
            if (mRefreshLayout == null) {

                throw new IllegalStateException("你虽然开启了下拉刷新，但是你却没有指定下拉刷新的布局的id,默认的下拉刷新布局id 是pull_refresh 你同样可以通过重写#getRefresLayoutId返回布局id");
            }
            initRefreshLayoutOptions();
        }
        if (canLoadMore) {
            this.mLoadMoreLayout = ButterKnife.findById(mRootView, getLoadMoreContainerLayoutId());
            if (mLoadMoreLayout == null)
                throw new IllegalStateException("你虽然开启了加更多，但是你却没有指定加载更多的布局的id,默认的加载更多布局id是load_more 你同样可以通过重写#getLoadMoreContainerLayoutId返回布局id");
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
        LoadMoreDefaultFooterView footerView = new LoadMoreDefaultFooterView(mActivity);
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
                    mLoadMoreLayout.loadMoreFinish(((List) response.obj).isEmpty(), false);
                }
            }
        }
    }

    @Override
    public void handleRefreshState(Response response, ListAdapter listAdapter) {
        handleRefreshState(response);
    }

    /**
     * 获取下拉刷新布局容器的id
     * 默认的id是{@link R.id#pull_refresh}
     *
     * @return 下拉刷新布局容器的id
     */
    protected int getRefreshContainerLayoutId() {
        return R.id.pull_refresh;
    }

    /***
     * 获取加载更多的容器的id
     * 默认的id是{@link R.id#load_more}
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
    protected int getFirstPageIndex() {
        return DEFAULT_FIRST_PAGE;
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
    public abstract boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header);

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onBeginRefresh();
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        onBeginLoadMore();
    }
}
