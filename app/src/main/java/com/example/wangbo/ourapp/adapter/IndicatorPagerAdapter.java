package com.example.wangbo.ourapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务详情适配器
 * Created by lovely3x on 15-9-14.
 */
public class IndicatorPagerAdapter extends FragmentPagerAdapter {

    private final List<String> mTitleList;
    private List<Fragment> mFragmentList = new ArrayList<>();

    public IndicatorPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);

        this.mFragmentList.addAll(fragments);
        this.mTitleList = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }


}
