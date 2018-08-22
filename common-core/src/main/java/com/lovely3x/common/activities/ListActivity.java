package com.lovely3x.common.activities;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lovely3x.common.R;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * ListView activity
 * Created by lovely3x on 16/7/28.
 */
public abstract class ListActivity extends PLEActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_list;
    }


    protected ListView lvContentList;


    @Override
    protected ViewGroup getEmptyContainerView() {
        return ButterKnife.findById(getRootView(), R.id.fl_activity_list_empty_container);
    }

    public ListView getListView() {
        return lvContentList;
    }


    @Override
    protected void initViews() {
        super.initViews();
        lvContentList = ButterKnife.findById(getRootView(),R.id.lv_activity_List);
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
        return (com.lovely3x.common.adapter.ListAdapter) lvContentList.getAdapter();
    }
}
