package com.example.wangbo.ourapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.example.wangbo.ourapp.R;
import com.example.wangbo.ourapp.adapter.MyAdapter;
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter;
import com.jackmar.jframelibray.base.JBaseAct;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wangbo on 2018/8/21.
 * <p>
 * <p>
 * VLayout 布局测试类
 */
public class VLayoutAct extends JBaseAct {

    @BindView(R.id.common_list)
    RecyclerViewHeaderAndFooter list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLyContent(R.layout.v_layout, true, "VLayoutTest");
    }

    @Override
    public void initView() {

        //  初始化管理器
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(this);
        list.setLayoutManager(virtualLayoutManager);

        //  设置缓存数量
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        list.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        // 初始化适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager, false);
        list.setAdapter(delegateAdapter);

        List<String> data = Arrays.asList("埃及", "菲律宾", "印度尼西亚", "新加坡", "阿富汗", "泰国", "赞比亚");

        // 线性布局
        LinearLayoutHelper layoutHelper1 = new LinearLayoutHelper();
        layoutHelper1.setBgColor(Color.GREEN);
        layoutHelper1.setAspectRatio(2.0f);
        layoutHelper1.setMargin(20, 10, 20, 10);
        layoutHelper1.setPadding(20, 10, 20, 10);
        delegateAdapter.addAdapter(new MyAdapter(this, data, layoutHelper1, 3));

        // 网格布局
        GridLayoutHelper helper = new GridLayoutHelper(3);
        helper.setMargin(7, 7, 7, 7);
        helper.setPadding(10, 10, 10, 10);
        helper.setBgColor(0xff87e543);
        helper.setGap(3);
        helper.setHGap(3);
        helper.setVGap(3);
        helper.setAutoExpand(true);
        MyAdapter myAdapter = new MyAdapter(this, data, helper, 6);
        delegateAdapter.addAdapter(myAdapter);

        // 固定布局
        delegateAdapter.addAdapter(new MyAdapter(this, data, new FixLayoutHelper(20, 20), 1));
        delegateAdapter.addAdapter(new MyAdapter(this, data, new FixLayoutHelper(FixLayoutHelper.TOP_RIGHT, 20, 20), 1));

        // 浮动布局
        VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(150, 150);

        List<String> floatData = Arrays.asList("墨尔本");
        FloatLayoutHelper floatLayoutHelper = new FloatLayoutHelper();
        floatLayoutHelper.setBgColor(Color.YELLOW);
        delegateAdapter.addAdapter(new MyAdapter(this, floatData, floatLayoutHelper, 1, layoutParams));

        // 列表布局，1行可显示多列item
        ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
        columnLayoutHelper.setWeights(new float[]{40.0f, 30.0f, 30.0f});
        delegateAdapter.addAdapter(new MyAdapter(this, data, columnLayoutHelper, 3));

        // 单行布局
        List<String> dataSingle = Arrays.asList("单行布局");
        delegateAdapter.addAdapter(new MyAdapter(this, dataSingle, new SingleLayoutHelper(), 1));

        //一拖N布局（N最大为4）,即OnePlusNLayoutHelper最多只能有5个item。
        OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper();
        onePlusNLayoutHelper.setColWeights(new float[]{20.0f});//设置左边一栏占10%
        onePlusNLayoutHelper.setRowWeight(30.0f);//设置右边上半部分占30%
        onePlusNLayoutHelper.setBgColor(Color.YELLOW);
        delegateAdapter.addAdapter(new MyAdapter(this, data, onePlusNLayoutHelper, 5));

        // 吸附布局 true  == top ? top : bottom
        delegateAdapter.addAdapter(new MyAdapter(this, data, new StickyLayoutHelper(true), 1));
        delegateAdapter.addAdapter(new MyAdapter(this, data, new StickyLayoutHelper(false), 1));

        // 瀑布流
        StaggeredGridLayoutHelper staggeredGridLayoutHelper = new StaggeredGridLayoutHelper(3, 0);// 显示3行，行距0
        staggeredGridLayoutHelper.setBgColor(Color.YELLOW);
        staggeredGridLayoutHelper.setGap(3);
        staggeredGridLayoutHelper.setHGap(3);
        staggeredGridLayoutHelper.setVGap(3);
        List<String> dataMore = Arrays.asList("埃及", "菲律宾", "印度尼西亚", "新加坡", "阿富汗", "泰国", "赞比亚",
                "埃及", "菲律宾", "印度尼西亚", "新加坡", "阿富汗", "泰国", "赞比亚",
                "埃及", "菲律宾", "印度尼西亚", "新加坡", "阿富汗", "泰国", "赞比亚",
                "埃及", "菲律宾", "印度尼西亚", "新加坡");
        delegateAdapter.addAdapter(new MyAdapter(this, dataMore, staggeredGridLayoutHelper, 25) {
            @Override
            public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);

                if (position % 2 == 0) {
                    layoutParams.mAspectRatio = 1.0f;
                } else {
                    layoutParams.height = 340 + position % 7 * 20;
                }
                holder.itemView.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
