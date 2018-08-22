package com.example.wangbo.ourapp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 支持添加头视图和尾视图的RecyclerView
 * 在添加头尾视图前需要先设置布局管理器
 * Created by lovely3x on 17/1/10.
 */
public class RecyclerViewHeaderAndFooter extends RecyclerView {

    private static final String TAG = "RecyclerViewHAF";

    private final ArrayList<View> mFooterViews = new ArrayList<>();
    private final ArrayList<View> mHeaderViews = new ArrayList<>();

    private View mEmptyView;

    private boolean mEmptyViewIsShowing;

    /**
     * 将空视图所固定在一屏内
     */
    private boolean mFitHeightInScreen = true;
    private boolean mSizeable;

    public void setFitHeightInScreen(boolean fitHeightInScreen) {
        this.mFitHeightInScreen = fitHeightInScreen;
    }

    public void getFitHeight(boolean fitHeight) {
        this.mFitHeightInScreen = fitHeight;
    }

    protected int getFooterViewCount() {
        return mFooterViews.size();
    }

    protected int getHeaderViewCount() {
        return mHeaderViews.size();
    }

    private final HeaderAndFooterRecyclerViewAdapter mWrappedAdapter = new HeaderAndFooterRecyclerViewAdapter();

    protected boolean shouldShowEmptyView() {
        return (mAdapter == null ? 0 : mAdapter.getItemCount()) == 0;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        mWrappedAdapter.notifyDataSetChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 数据变化监听器
     * 用于监听数据的变化，在无数据时，如果有空内容视图就显示空内容视图
     */
    private final AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            triggerEmptyView();
        }
    };

    private final AdapterDataObserver mReflationInvokeObserver = new ReflationInvokeObserver();

    private void triggerEmptyView() {

        if (shouldShowEmptyView() && mEmptyView != null && !mEmptyViewIsShowing && mSizeable) {
            mEmptyViewIsShowing = true;

            if (mFitHeightInScreen) {

                int headerViewTotalHeight = getTotalHeaderHeight();
                int height = getMeasuredHeight();

                ViewGroup.LayoutParams lp = mEmptyView.getLayoutParams();
                if (lp == null) {
                    lp = getLayoutManager().generateDefaultLayoutParams();
                }
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = height - headerViewTotalHeight;

            }

            if (!hasHeader(mEmptyView)) {
                addHeaderView(mEmptyView);
            }

            //1, 数据集发生变化
            //2, 是否需要展示空内容提示视图
            //3, 展示空内容提示视图

        } else {
            if (!shouldShowEmptyView() && mEmptyViewIsShowing && mEmptyView != null) {
                mEmptyViewIsShowing = false;
                if (hasHeader(mEmptyView)) {
                    removeHeaderView(mEmptyView);
                }
            }
        }
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        mSizeable = true;
        triggerEmptyView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSizeable = false;
    }

    protected int getTotalHeaderHeight() {
        int totalHeight = 0;

        for (int i = 0; i < mHeaderViews.size(); i++) {
            View view = mHeaderViews.get(i);
            if (mEmptyViewIsShowing && view == mEmptyView) continue;
            view.measure(getMeasuredWidth(), getMeasuredHeight());
            totalHeight += view.getMeasuredHeight();
        }

        return totalHeight;
    }


    private Adapter mAdapter;

    public RecyclerViewHeaderAndFooter(Context context) {
        this(context, null);
    }

    public RecyclerViewHeaderAndFooter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewHeaderAndFooter(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mReflationInvokeObserver);
            mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
        }
        this.mAdapter = adapter;
        if (mAdapter == null) {
            super.setAdapter(null);
        } else {
//            super.setAdapter(null);
            //设置布局器，如果没有设置的话
            if (getLayoutManager() == null) {
                setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }

            mAdapter.registerAdapterDataObserver(mReflationInvokeObserver);
            mAdapter.registerAdapterDataObserver(mAdapterDataObserver);

            super.setAdapter(mWrappedAdapter);
        }

    }

    public void addFooterView(View view) {
        addFooterView(view, mFooterViews.size());
    }

    public void addFooterView(View view, int index) {
        if (!hasFooter(view)) {
            int realAdapterCount = mAdapter == null ? 0 : mAdapter.getItemCount();
            mFooterViews.add(index < 0 ? 0 : index, view);
//            mWrappedAdapter.notifyItemInserted(realAdapterCount + index);
            mWrappedAdapter.notifyDataSetChanged();
            Log.d(TAG, "Add footer view " + view + " at index " + index);
        }
    }

    public void addHeaderView(View view) {
        addHeaderView(view, mHeaderViews.size());
    }

    public void removeFooterView(View view) {
        int index = mFooterViews.indexOf(view);
        if (index >= 0) {
            removeFooterView(index);
        }
    }

    public View removeFooterView(int index) {
        View view = mFooterViews.remove(index);

        if (view != null) {
            int realAdapterCount = mAdapter == null ? 0 : mAdapter.getItemCount();
//            mWrappedAdapter.notifyItemRemoved(realAdapterCount + index);
            mWrappedAdapter.notifyDataSetChanged();
            Log.d(TAG, "Remove footer view " + view + " at index " + index);
        }

        return view;
    }

    public View removeHeaderView(int index) {

        View view = mHeaderViews.remove(index);

        if (view != null) {
            mWrappedAdapter.notifyDataSetChanged();
        }

        return view;
    }

    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOf(view);
        if (index >= 0) removeHeaderView(index);
    }

    public void addHeaderView(View view, int index) {
        if (!hasHeader(view)) {
            mHeaderViews.add(index < 0 ? 0 : index, view);
            mWrappedAdapter.notifyDataSetChanged();
        }
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFooterViews.size() - (mEmptyView != null ? 1 : 0);
    }

    public boolean hasFooter(View view) {
        return mFooterViews.contains(view);
    }

    public boolean hasHeader(View view) {
        return mHeaderViews.contains(view);
    }


    private final class HeaderAndFooterRecyclerViewAdapter extends Adapter {

        private int HEADER_VIEW_OFFSET = Integer.MAX_VALUE >> 2;
        private int FOOTER_VIEW_OFFSET = Integer.MAX_VALUE >> 1;


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType >= HEADER_VIEW_OFFSET && viewType < FOOTER_VIEW_OFFSET) {
                HeaderAndFooterViewHolder holder = new HeaderAndFooterViewHolder(mHeaderViews.get(viewType - HEADER_VIEW_OFFSET));
                holder.setIsRecyclable(false);
                return holder;
            } else if (viewType >= FOOTER_VIEW_OFFSET) {
                int pos = viewType - FOOTER_VIEW_OFFSET;
                int footerIndex = pos - mHeaderViews.size() - (mAdapter == null ? 0 : mAdapter.getItemCount());
                HeaderAndFooterViewHolder holder = new HeaderAndFooterViewHolder(mFooterViews.get(footerIndex));
                holder.setIsRecyclable(false);
                return holder;
            } else {
                return mAdapter == null ? null : mAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position < mHeaderViews.size()) {//Header
                final int headerViewIndex = position;

            } else if (position >= mHeaderViews.size() + (mAdapter == null ? 0 : mAdapter.getItemCount())) {//Footer
                final int footerViewIndex = position - mHeaderViews.size() - (mAdapter == null ? 0 : mAdapter.getItemCount());
            } else {
                if (mAdapter != null) {
                    mAdapter.onBindViewHolder(holder, position - mHeaderViews.size());
                }
            }
        }


        @Override
        public int getItemCount() {

            final int realCount = mAdapter == null ? 0 : mAdapter.getItemCount();
            final int headerSize = mHeaderViews.size();
            final int footerSize = mFooterViews.size();

            return realCount + footerSize + headerSize;

        }

        @Override
        public int getItemViewType(int position) {
            final int headerViewCount = mHeaderViews.size();
            if (position < headerViewCount) {
                return HEADER_VIEW_OFFSET + position;
            } else {
                int realAdapterSize = mAdapter == null ? 0 : mAdapter.getItemCount();
                final int max = headerViewCount + realAdapterSize;
                if (position >= max) {
                    return FOOTER_VIEW_OFFSET + position;
                } else {
                    if (mAdapter != null) {
                        int type = mAdapter.getItemViewType(position - headerViewCount);
                        if (type >= FOOTER_VIEW_OFFSET) {
                            throw new IllegalArgumentException("Type index must less then " + FOOTER_VIEW_OFFSET);
                        }
                        return type;
                    }
                }
            }

            return super.getItemViewType(position);
        }
    }

    private static class HeaderAndFooterViewHolder extends ViewHolder {

        HeaderAndFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ReflationInvokeObserver extends AdapterDataObserver {

        AdapterDataObserver real = null;

        ReflationInvokeObserver() {
            try {
                Field observerField = RecyclerView.class.getDeclaredField("mObserver");
                observerField.setAccessible(true);
                real = (AdapterDataObserver) observerField.get(RecyclerViewHeaderAndFooter.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onChanged() {
            real.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            real.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            real.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            real.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            real.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            real.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    }
}
