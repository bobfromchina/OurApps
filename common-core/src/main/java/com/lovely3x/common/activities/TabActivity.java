package com.lovely3x.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TabHost;

import com.lovely3x.common.widgets.FragmentTabHost;

/**
 * tab切换卡界面
 * Created by lovely3x on 16-1-21.
 */
public abstract class TabActivity extends BaseCommonActivity implements TabHost.OnTabChangeListener {

    /**
     * 页码的下标
     */
    public static final String EXTRA_TAB_INDEX = "EXTRA_TAB_INDEX";

    com.lovely3x.common.widgets.FragmentTabHost mFragmentHost;

    protected int tab = 0;

    @Override
    protected void onViewInitialized() {
        permissionChecker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initViews() {
        mFragmentHost = getTabHost();
        initTabHost();
    }

    @Override
    protected void onInitExtras(@NonNull Bundle bundle) {
        super.onInitExtras(bundle);
        tab = bundle.getInt(EXTRA_TAB_INDEX, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tab = intent.getIntExtra(EXTRA_TAB_INDEX, 0);
        if (mFragmentHost != null) {
            mFragmentHost.setCurrentTab(tab);
        }
    }

    public int setCurrentTab(int tab) {
        if (mFragmentHost != null) {
            mFragmentHost.setCurrentTab(tab);
        }
        return 0;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_TAB_INDEX, tab);
    }

    @Override
    public void restoreInstanceOnCreateBefore(@NonNull Bundle savedInstance) {
        tab = savedInstance.getInt(EXTRA_TAB_INDEX);
    }

    @Override
    public void restoreInstanceOnCreateAfter(@NonNull Bundle savedInstance) {

    }

    public abstract FragmentTabHost getTabHost();

    public abstract int getTabContainer();

    public abstract Class<? extends Fragment>[] getTabs();

    /**
     * 获取指定下标的视图
     *
     * @param index 下标
     * @return tab视图
     */
    public abstract View getTabItemView(int index);

    /**
     * 初始化tabHost
     */
    private void initTabHost() {
        mFragmentHost.setup(this, getSupportFragmentManager(), getTabContainer());
        mFragmentHost.getTabWidget().setDividerDrawable(null);
        //得到fragment的个数
        Class<? extends Fragment>[] fragments = getTabs();
        if (fragments == null) throw new IllegalArgumentException("Tab can't be null.");
        int count = fragments.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mFragmentHost.newTabSpec(String.valueOf(i)).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mFragmentHost.addTab(tabSpec, fragments[i], null);
        }
        mFragmentHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        tab = mFragmentHost.getCurrentTab();
    }
}
