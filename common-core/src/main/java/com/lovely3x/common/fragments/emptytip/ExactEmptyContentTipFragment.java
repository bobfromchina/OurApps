package com.lovely3x.common.fragments.emptytip;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.requests.BaseCodeTable;


/**
 * 精确指定空内容区域提示TipFragment
 * Created by lovely3x on 15-9-23.
 */
public abstract class ExactEmptyContentTipFragment extends AbstractEmptyContentTipFragment {

    /**
     * 空视图容器
     */
    protected ViewGroup emptyContainer;

    /**
     * 过渡视图
     */
    protected View transitionView;

    @Override
    public void handleEmptyContent(View.OnClickListener listener) {
        emptyContainerCheck();
        if (transitionView != null) {
            emptyContainer.removeView(transitionView);
        }
        transitionView = getLayoutInflater().inflate(R.layout.view_empty_data, emptyContainer, false);
        transitionView.findViewById(R.id.ll_view_empty_click_area).setOnClickListener(listener);
        emptyContainer.addView(transitionView);
    }

    @Override
    public void handleEmptyContent() {
        handleEmptyContent(null);
    }

    @Override
    public void handleLoadingContent() {
        emptyContainerCheck();
        if (transitionView != null) {
            emptyContainer.removeView(transitionView);
        }
        transitionView = getLayoutInflater().inflate(R.layout.view_loading, emptyContainer, false);
        emptyContainer.addView(transitionView);
    }

    @Override
    public void handleLoadSuccessful() {
        emptyContainerCheck();
        if (transitionView != null) {
            emptyContainer.removeView(transitionView);
        }
    }

    @Override
    public void handleLoadFailure(String errorMsg) {
        handleLoadFailure(errorMsg, null);
    }

    @Override
    public void handleLoadFailure(String errorMsg, View.OnClickListener retryListener) {
        emptyContainerCheck();
        //移除原有的过渡视图
        if (transitionView != null) {
            emptyContainer.removeView(transitionView);
        }
        //将现在的过渡视图添加进去
        transitionView = getLayoutInflater().inflate(R.layout.view_load_failure, emptyContainer, false);
        emptyContainer.addView(transitionView);
        //设置点击监听器
        transitionView.findViewById(R.id.ll_view_load_failure_click_area).setOnClickListener(retryListener);
        //设置提示文字
        ((TextView) transitionView.findViewById(R.id.loading_msg)).setText(errorMsg);
    }

    @Override
    public void handleLoadFailure(int errorCode) {
        if (errorCode == BaseCodeTable.getInstance().getEmptyDataCode()) {
            handleEmptyContent();
        } else {
            handleLoadFailure(BaseCodeTable.getInstance().getCodeDescription(errorCode));
        }

    }

    @Override
    public void handleLoadFailure(int errorCode, View.OnClickListener retryListener) {
        if (errorCode == BaseCodeTable.getInstance().getEmptyDataCode()) {
            handleEmptyContent(retryListener);
        } else {
            handleLoadFailure(BaseCodeTable.getInstance().getCodeDescription(errorCode), retryListener);
        }
    }

    /**
     * 获取空内容视图提示容器
     *
     * @return 空内容视图容器
     */
    protected abstract ViewGroup getEmptyContainerView();

    /**
     * 空视图容器检查
     */
    protected void emptyContainerCheck() {
        if (emptyContainer == null) {
            emptyContainer = getEmptyContainerView();
            emptyContainer.setClickable(true);
        }
        if (emptyContainer == null) {
            throw new IllegalStateException("你必须先设置一个空视图容器#getEmptyContainerView");
        }
    }
}
