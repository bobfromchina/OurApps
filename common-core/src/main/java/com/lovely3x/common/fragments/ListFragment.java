package com.lovely3x.common.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lovely3x.common.R;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Simple list fragment
 * Created by lovely3x on 16-5-11.
 */
public abstract class ListFragment extends PLEFragment {


    protected ListView lvContentList;


    @Override
    protected ViewGroup getEmptyContainerView() {
        return ButterKnife.findById(getRootView(), R.id.fl_fragment_list_empty_container);
    }

    public ListView getListView() {
        return lvContentList;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initViews() {
        super.initViews();
        lvContentList = ButterKnife.findById(getRootView(), R.id.lv_fragment_List);
        lvContentList.setFooterDividersEnabled(false);
        lvContentList.setHeaderDividersEnabled(false);
    }


    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(com.lovely3x.common.adapter.ListAdapter adapter) {
        if (lvContentList != null) {
            lvContentList.setAdapter(adapter);
        } else {
            throw new IllegalStateException("ListView not initialized.");
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, lvContentList, header);
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public com.lovely3x.common.adapter.ListAdapter getAdapter() {
        ListAdapter adapter = lvContentList.getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return (com.lovely3x.common.adapter.ListAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        } else if (adapter instanceof com.lovely3x.common.adapter.ListAdapter) {
            return (com.lovely3x.common.adapter.ListAdapter) adapter;
        }
        return null;
    }
}
