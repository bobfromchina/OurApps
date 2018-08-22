package com.example.wangbo.ourapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wangbo.ourapp.R;
import com.example.wangbo.ourapp.bean.HotMealsBean;
import com.example.wangbo.ourapp.utils.FlowLayout;
import com.example.wangbo.ourapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wangbo on 2018/7/4.
 * <p>
 * 热门套餐的适配器
 */
public class HotMealsAdapter extends RecyclerListAdapter<HotMealsBean> {

    public HotMealsAdapter(List<HotMealsBean> datas, Context context) {
        super(datas, context);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.item_hot_meals, parent, false));
    }


    @Override
    public void handleData(int position, @NonNull BaseRecyclerViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        HotMealsBean bean = datas.get(position);

        // 名称
        viewHolder.tvName.setText(bean.getMealName());

        // 金额
        viewHolder.tvMoney.setText(mContext.getString(R.string.yuan, bean.getMealDiscountPrice()));

        // 折扣
        viewHolder.tvDiscount.setText(bean.getMealDiscount());

        // 原价
        viewHolder.tvOldMoney.setText(mContext.getString(R.string.MealOriginalPrice, bean.getMealOriginalPrice()));

        // 部门
        viewHolder.tvPart.setText(mContext.getString(R.string.OrganizName, bean.getOrganizName()));

        // 地址
        viewHolder.tvAddress.setText(bean.getOrganizDetailAddress());


        List<String> list = new ArrayList<>();
        list = bean.getMealLabel();
        List<String> listColor = Arrays.asList("#eca72c", "#e94429", "#0087e7");

        //  关键字排序
        if (viewHolder.flShowHot != null) {
            viewHolder.flShowHot.removeAllViews();
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    final String string = list.get(i);
                    View view = createView(string, listColor.get(i));
                    viewHolder.flShowHot.addView(view);
                }
            }
        }
    }

    /**
     * 实现一个方法  去加载这个流布局
     *
     * @param string 传递过来的文字
     * @return 封装后返回对象
     */
    private View createView(String string, String color) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewUtils.init(mContext);
        lp.leftMargin = ViewUtils.dp2pxF(10);

        TextView text = new TextView(mContext);
        text.setLayoutParams(lp);
        text.setTextSize(14);
        text.setMaxEms(15);
        text.setMaxLines(1);
        text.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        text.setPadding(25, 10, 25, 10);
        text.setText(string);
        //text.setTextColor(ContextCompat.getColor(mContext, R.color.btn_green_noraml));
        text.setTextColor(Color.parseColor(color));
        text.setBackground(ContextCompat.getDrawable(mContext, R.drawable.show_hot_bg));
        GradientDrawable gradientDrawable = (GradientDrawable) text.getBackground();
        gradientDrawable.setStroke(2, Color.parseColor(color));
        return text;
    }

    class ViewHolder extends BaseRecyclerViewHolder {

        /**
         * 名称
         */
        @BindView(R.id.tv_name)
        TextView tvName;

        /**
         * 金额
         */
        @BindView(R.id.tv_money)
        TextView tvMoney;

        /**
         * 折扣
         */
        @BindView(R.id.tv_discount)
        TextView tvDiscount;

        /**
         * 原价
         */
        @BindView(R.id.tv_old_money)
        TextView tvOldMoney;

        /**
         * 部门
         */
        @BindView(R.id.tv_part)
        TextView tvPart;

        /**
         * 地址
         */
        @BindView(R.id.tv_address)
        TextView tvAddress;

        /**
         * 展示热门搜索
         */
        @BindView(R.id.flow_layout)
        FlowLayout flShowHot;

        public ViewHolder(View rootView) {
            super(rootView);
        }
    }
}
