package com.jackmar.jframelibray.view.rvlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.utils.ViewUtil;
import com.jackmar.jframelibray.view.ErrorView;
import com.jackmar.jframelibray.view.rvlist.refresh.PtrClassicFrameLayout;
import com.jackmar.jframelibray.view.rvlist.refresh.PtrDefaultHandler;
import com.jackmar.jframelibray.view.rvlist.refresh.PtrFrameLayout;
import com.jackmar.jframelibray.view.rvlist.refresh.PtrHandler;


public class RefreshUtil implements JRecyclerView.JRecyclerViewListener, PtrHandler {

    private Context context;
    //recyclerView列表
    private JRecyclerView mRecyclerView;
    //下拉刷新包裹控件
    private PtrClassicFrameLayout mPtrLayout;
    //组合刷新监听
    private IOnRefreshListener onRefreshListener;

    private IOnErrorReLoadListener reloadListener;
    //是否是第一次刷新
    private boolean isFristRefresh = true;

    private View EmptyView;

    private ErrorView errorView;

    private boolean showEmpty = false;

    public RefreshUtil(Context context, PtrClassicFrameLayout mPtrLayout, JRecyclerView mRecyclerView) {
        this.context = context;
        this.mPtrLayout = mPtrLayout;
        this.mRecyclerView = mRecyclerView;
        EmptyView = LayoutInflater.from(context).inflate(R.layout.layout_list_empty_view, null);
        initPtr();
        initRecylerView(true);
    }

    public RefreshUtil(Context context, JRecyclerView mRecyclerView) {
        this.context = context;
        this.mRecyclerView = mRecyclerView;
        EmptyView = LayoutInflater.from(context).inflate(R.layout.layout_list_empty_view, null);
        initRecylerView(true);
    }

    public RefreshUtil(Context context, PtrClassicFrameLayout mPtrLayout, JRecyclerView mRecyclerView, boolean showEmpty) {
        this.context = context;
        this.mPtrLayout = mPtrLayout;
        this.mRecyclerView = mRecyclerView;
        this.showEmpty = showEmpty;
        EmptyView = LayoutInflater.from(context).inflate(R.layout.layout_list_empty_view, null);
        initPtr();
        initRecylerView(false);
    }

    public RefreshUtil(Context context, JRecyclerView mRecyclerView, boolean showEmpty) {
        this.context = context;
        this.mRecyclerView = mRecyclerView;
        this.showEmpty = showEmpty;
        EmptyView = LayoutInflater.from(context).inflate(R.layout.layout_list_empty_view, null);
        initRecylerView(true);
    }

    /**
     * 初始化recyclerView
     */
    private void initRecylerView(boolean needRefresh) {
        if (mRecyclerView != null) {
            //设置线性布局
            mRecyclerView.setLinearLayout();
            //设置recycleview下拉刷新不能用
            mRecyclerView.setSwipeRefreshEnable(needRefresh);
            mRecyclerView.setPullRefreshEnable(needRefresh);
            mRecyclerView.setOnRefreshListener(this);
            initError();
            mRecyclerView.setEmptyView(EmptyView);
        }
    }

    /**
     * 初始化ptr
     */
    private void initPtr() {
        if (mPtrLayout != null && mRecyclerView != null) {
            View header = LayoutInflater.from(context).inflate(R.layout.ptr_refresh_header, null);
            mPtrLayout.setHeaderView(header);
            mPtrLayout.setRatioOfHeaderHeightToRefresh(0.9f);
            // 设置更新时间
            mPtrLayout.setLastUpdateTimeRelateObject(context);
            // 设置与viewpage冲突
            mPtrLayout.disableWhenHorizontalMove(true);
            mPtrLayout.setResistance(3.5f);
            mPtrLayout.setDurationToCloseHeader(200);
            mPtrLayout.setPtrHandler(this);
            initError();
            mRecyclerView.setEmptyView(EmptyView);
        }
    }

    /**
     * 设置九宫格布局
     *
     */
    public void setGridLayout(int count) {
        mRecyclerView.setGridLayout(count);
    }

    /**
     * 初始化错误视图
     */
    private void initError() {
        if (EmptyView != null) {
            errorView = (ErrorView) EmptyView.findViewById(R.id.ev_view);
            errorView.setBackgroundResource(R.color.transparent);
        }
    }

    /**
     * 获取错误视图
     */
    public ErrorView getErrorView() {
        if (errorView != null)
            return errorView;
        return null;
    }

    public void setError(int res, String text) {
        if (errorView != null) {
            errorView.setError(res, text);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reloadListener != null) {
                        reloadListener.reloadNet();
                    }
                }
            });
        }
    }

    /**
     * 设置空试图
     */
    public void setEmptyView(View emptyView) {
        mRecyclerView.setEmptyView(emptyView);
    }


    /**
     * 设置默认的分割线
     */
    public void setDefaultDivider() {
        if (mRecyclerView != null) {
            //设置分割线
            mRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, 1, 0, 0, context.getResources().getColor(R.color.color_line)));
        }
    }

    /**
     * 设置默认的分割线
     */
    public void setHDivider(int height) {
        if (mRecyclerView != null) {
            int h = ViewUtil.dip2px(context, height);
            //设置分割线
            mRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, h, 0, 0, context.getResources().getColor(R.color.color_line)));
        }
    }

    /**
     * 设置默认的分割线
     */
    public void setDefaultWidthDivider() {
        if (mRecyclerView != null) {
            //设置分割线
            mRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL, 1, ViewUtil.dip2px(context, 15), ViewUtil.dip2px(context, 15), context.getResources().getColor(R.color.color_line)));
        }
    }

    /**
     * 设置网格分割线
     *
     * @param width 间隔宽度
     * @param count 列数
     */
    public void setGridDivider(int count, int width, boolean includeEdge) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(count, width, includeEdge));
        }
    }

    /**
     * 设置网格分割线有header 的情况
     *
     * @param width 间隔宽度
     * @param count 列数
     */
    public void setGridDivider(int count, int width, boolean hasHeader, boolean includeEdge,int headerCount ) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(count, width, includeEdge, hasHeader,headerCount));
        }
    }

    /**
     * 启动刷新
     */
    public void showRcListRefresh() {
        if (mRecyclerView != null && isFristRefresh) {
            mRecyclerView.setRefreshing(true);
            isFristRefresh = false;
        }
    }

    /**
     * RecyclerView是否需要加载
     *
     * @param enable
     */
    public void enableLoad(boolean enable) {
        if (mRecyclerView != null) {
            mRecyclerView.setLoadEnable(enable);
        }
    }

    /**
     * RecyclerView是否需要加载
     *
     * @param enable
     */
    public void enableRefresh(boolean enable) {
        if (mRecyclerView != null) {
            //设置recycleview下拉刷新不能用
            mRecyclerView.setSwipeRefreshEnable(enable);
            mRecyclerView.setPullRefreshEnable(enable);
        }
    }

    /**
     * 设置刷新监听
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(IOnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * 设置错误视图刷新
     *
     * @param reloadListener
     */
    public void setOnErrorReloadListener(IOnErrorReLoadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    /************************
     * RecyclerView 刷新加载的回掉
     ********************************************************/
    @Override
    public void onRefresh() {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }

    }

    @Override
    public void onLoadMore() {
        if (onRefreshListener != null) {
            onRefreshListener.onLoad();
        }
    }


    /************************
     * Ptr 刷新回掉
     ********************************************************/
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                mRecyclerView.getRecyclerView(), header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    /**
     * 完成刷新或者加载
     */
    public void completeRefreshAndLoad() {
        if (onRefreshListener != null) {
            if (mRecyclerView != null) {
                mRecyclerView.setRefreshCompleted();
                mRecyclerView.setRefreshing(false);
            }
            if (mPtrLayout != null) {
                mPtrLayout.refreshComplete();
            }
            //处理上拉加载根据分页判断是否需要继续加载
            if (mRecyclerView.getRecyclerView().getAdapter().getItemCount() < 10) {
                if (mRecyclerView != null) {
                    mRecyclerView.setLoadEnable(false);
                }
            } else {
                if (mRecyclerView != null) {
                    mRecyclerView.setLoadEnable(true);
                }
            }
        }
        mRecyclerView.showEmptyView(showEmpty);
    }

    public PtrClassicFrameLayout getmPtrLayout() {
        return mPtrLayout;
    }

    public JRecyclerView getmRecyclerView() {
        return mRecyclerView;
    }
}
