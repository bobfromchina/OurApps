package com.example.wangbo.ourapp.base;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;

import com.example.wangbo.ourapp.R;
import com.example.wangbo.ourapp.utils.DynamicTimeFormat;
import com.jackmar.jframelibray.base.JBaseFg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;

/**
 * Created by wangbo on 2018/8/17.
 * <p>
 * 列表
 */
public abstract class PLEFragment extends JBaseFg {

    LoadMoreAndRefresh loadMoreAndRefresh;

    ClassicsHeader mClassicsHeader;

    Drawable mDrawableProgress;

    SmartRefreshLayout mRefreshLayout;

    @Override
    protected void initView() {
        initRefresh();
    }

    private void initRefresh() {

        this.mRefreshLayout = ButterKnife.findById(context, getContainer());

        int delta = new Random().nextInt(7 * 24 * 60 * 60 * 1000);
        mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis() - delta));
        mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM-dd HH:mm", Locale.CHINA));
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

        mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }
    }

    private int getContainer() {
        return R.id.refreshLayout;
    }

    public void setOnLoadData(LoadMoreAndRefresh loadMoreAndRefresh) {
        this.loadMoreAndRefresh = loadMoreAndRefresh;
    }

    public interface LoadMoreAndRefresh {

        void loadMoreData();

        void onRefreshData();

    }
}
