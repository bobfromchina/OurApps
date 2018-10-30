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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangbo on 2018/8/21.
 * <p>
 * 重写适配器
 */
public class MyAdapter extends DelegateAdapter.Adapter<MyAdapter.ViewHolder> {

    private int mCount = 0;

    private LayoutHelper mLayoutHelper;

    private Context mContext;

    private VirtualLayoutManager.LayoutParams mLayoutParams;

    private List<String> datas;

    public MyAdapter(Context context, List<String> data, LayoutHelper layoutHelper1, int i) {
        super();
        this.datas = data;
        this.mContext = context;
        this.mLayoutHelper = layoutHelper1;
        mCount = i;
    }

    public MyAdapter(Context context, List<String> data, LayoutHelper layoutHelper1, int i, @Nullable @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.v_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setLayoutParams(mLayoutParams != null ? mLayoutParams : new VirtualLayoutManager.LayoutParams(new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
    }


    @Override
    protected void onBindViewHolderWithOffset(final ViewHolder holder, int position, int offsetTotal) {
        String data = datas.get(position);
        holder.tv_title.setText(data);

        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tv_title.setText("南非共和国");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;

        public ViewHolder(View view) {
            super(view);

            tv_title = view.findViewById(R.id.tv_title);

        }
    }
}
