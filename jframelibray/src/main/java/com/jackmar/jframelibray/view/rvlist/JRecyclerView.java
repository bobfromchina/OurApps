package com.jackmar.jframelibray.view.rvlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.view.rvlist.view.RecyclerViewOnScroll;
import com.jackmar.jframelibray.view.rvlist.view.SwipeRefreshLayoutListener;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;


public class JRecyclerView extends LinearLayout {
    private Context mContext;
    private View view;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView loadMoreText;
    private LinearLayout loadMoreLayout;
    private View mFooterView;
    private FrameLayout mErrorContent;
    //是否还可以加载
    private boolean hasMore = true;
    //是否在刷新
    private boolean isRefresh = false;
    //是否正在加载
    private boolean isLoadMore = false;
    //是否可以刷新
    private boolean refreshEnable = true;
    //是否可以加载
    private boolean loadEnable = true;
    //刷新回掉接口
    private JRecyclerViewListener mRefreshListener;

    public JRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public JRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public JRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.jrecyclerview_layout, this, true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutListener(this));
        //初始化RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //不显示滑动bar
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnTouchListener(new onTouchRecyclerView());
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll(this));
        //加载布局
        loadMoreLayout = (LinearLayout) view.findViewById(R.id.loadMoreLayout);
        //加载更多文字
        loadMoreText = (TextView) view.findViewById(R.id.loadMoreText);
//        //底部加载视图
        mFooterView = view.findViewById(R.id.footer);
        //错误视图
        mErrorContent = (FrameLayout) view.findViewById(R.id.error_view);
        mFooterView.setVisibility(View.GONE);
        mErrorContent.setVisibility(View.GONE);
        this.setOverScrollMode(OVER_SCROLL_NEVER);

    }

    /**
     * 获取recyclerview
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * LinearLayoutManager
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridLayout(int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }


    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    /**
     * 获取LayoutManager
     *
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * 设置item动画
     *
     * @param animator
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    /**
     * 添加分割线根据位置
     *
     * @param decor
     * @param index
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        mRecyclerView.addItemDecoration(decor, index);
    }

    /**
     * 添加分割线
     *
     * @param decor
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecyclerView.addItemDecoration(decor);
    }

    /**
     * 滑倒顶部
     */
    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * 设置空视图
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mErrorContent.removeAllViews();
        mErrorContent.addView(emptyView);
    }

    /**
     * 显示空试图
     */
    public void showEmptyView(boolean showEmpty) {
        RecyclerView.Adapter oldAdapter = mRecyclerView.getAdapter();
        if (oldAdapter != null && mErrorContent.getChildCount() != 0) {
            if (oldAdapter instanceof HeaderAndFooterWrapper) {
                int itemCount = oldAdapter.getItemCount() - ((HeaderAndFooterWrapper) oldAdapter).getHeadersCount() - ((HeaderAndFooterWrapper) oldAdapter).getFootersCount();
                if (itemCount == 0 && showEmpty) {
                    mFooterView.setVisibility(View.GONE);
                    mErrorContent.setVisibility(VISIBLE);
                } else {
                    mErrorContent.setVisibility(GONE);
                }
            } else {
                if (oldAdapter.getItemCount() == 0) {
                    mFooterView.setVisibility(View.GONE);
                    mErrorContent.setVisibility(VISIBLE);
                } else {
                    mErrorContent.setVisibility(GONE);
                }
            }

        }
    }

    /**
     * 设置允许刷新
     *
     */
    public void setPullRefreshEnable(boolean enable) {
        refreshEnable = enable;
        setSwipeRefreshEnable(enable);
    }

    /**
     * 获取是否允许刷新状态
     *
     * @return
     */
    public boolean getPullRefreshEnable() {
        return refreshEnable;
    }

    /**
     * 设置SwipeRefreshLayout刷新状态
     *
     * @param enable
     */
    public void setSwipeRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    /**
     * 获取自带刷新状态
     *
     * @return
     */
    public boolean getSwipeRefreshEnable() {
        return mSwipeRefreshLayout.isEnabled();
    }

    /**
     * 设置自带刷新的颜色值
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

    }

    /**
     * 获取刷新布局
     *
     * @return
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * 设置刷新
     *
     * @param isRefreshing
     */
    public void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                if (refreshEnable)
                    mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });

    }

    /**
     * 获取加载允许
     *
     * @return
     */
    public boolean getLoadEnable() {
        return loadEnable;
    }

    /**
     * 设置允许加载状态
     *
     * @param loadEnable
     */
    public void setLoadEnable(boolean loadEnable) {
        this.loadEnable = loadEnable;
    }

    /**
     * 获取底部试图
     *
     * @return
     */
    public LinearLayout getFooterViewLayout() {
        return (LinearLayout) mFooterView;
    }

    /**
     * 设置底部试图的背景色
     *
     * @param color
     */
    public void setFooterViewBackgroundColor(int color) {
        loadMoreLayout.setBackgroundColor(ContextCompat.getColor(mContext, color));
    }

    /**
     * 设置底部视图文字
     *
     * @param text 设置文字
     */
    public void setFooterViewText(CharSequence text) {
        loadMoreText.setText(text);
    }

    /**
     * 设置底部视图文字
     *
     * @param resid 设置文字资源
     */
    public void setFooterViewText(int resid) {
        loadMoreText.setText(resid);
    }

    /**
     * 设置底部文字的颜色
     *
     * @param color
     */
    public void setFooterViewTextColor(int color) {
        loadMoreText.setTextColor(ContextCompat.getColor(mContext, color));
    }

    /**
     * 刷新状态回掉
     */
    public void refresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        if (mRefreshListener != null && hasMore) {
            setPullRefreshEnable(false);
            mFooterView.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();
            invalidate();
            mRefreshListener.onLoadMore();

        }
    }

    /**
     * 设置刷新完成
     */
    public void setRefreshCompleted() {
        isRefresh = false;
        refreshEnable = true;
        loadEnable = true;
        setRefreshing(false);
        isLoadMore = false;
        mFooterView.animate()
                .translationY(mFooterView.getHeight())
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }

    /**
     * 设置加载更多监听
     */
    public void setOnRefreshListener(JRecyclerViewListener listener) {
        this.mRefreshListener = listener;
    }

    /**
     * 是否是加载更多
     */
    public boolean isLoadMore() {
        return isLoadMore;
    }

    /**
     * 设置是否加载更多
     */
    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    /**
     * 是否是刷新
     *
     * @return
     */
    public boolean isRefresh() {
        return isRefresh;
    }

    /**
     * 设置刷新
     *
     * @param isRefresh
     */
    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    /**
     * 是否还有要加载的
     *
     * @return
     */
    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * 设置是否可以加载更多
     *
     * @param hasMore
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public interface JRecyclerViewListener {
        void onRefresh();

        void onLoadMore();
    }


    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (isRefresh || isLoadMore) {
                return true;
            } else {
                return false;
            }
        }
    }

}
