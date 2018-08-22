package com.example.wangbo.ourapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.example.wangbo.ourapp.R;

import java.util.List;

/**
 * Created by wangbo on 2018/8/21.
 * <p>
 * 重写适配器
 */
public class MyAadapter extends DelegateAdapter.Adapter<MyAadapter.MainViewHolder> {

    private int mCount = 0;

    private LayoutHelper mLayoutHelper;

    private Context mContext;

    private VirtualLayoutManager.LayoutParams mLayoutParams;

    private List<String> datas;

    public MyAadapter(Context context, List<String> data, LayoutHelper layoutHelper1, int i) {
        super();
        this.datas = data;
        this.mContext = context;
        this.mLayoutHelper = layoutHelper1;
        mCount = i;
    }

    public MyAadapter(Context context, List<String> data, LayoutHelper layoutHelper1, int i, @Nullable @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
        super();
        this.datas = data;
        this.mContext = context;
        this.mLayoutHelper = layoutHelper1;
        mCount = i;
        this.mLayoutParams = layoutParams;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.v_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.itemView.setLayoutParams(mLayoutParams != null ? mLayoutParams : new VirtualLayoutManager.LayoutParams(new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
    }

    @Override
    protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
        ((TextView) holder.itemView.findViewById(R.id.tv_title)).setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    protected static class MainViewHolder extends RecyclerView.ViewHolder {

        public static volatile int existing = 0;
        public static int createdTimes = 0;

        public MainViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;
        }

        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
        }
    }
}
