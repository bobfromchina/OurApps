/*package com.example.wangbo.ourapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangbo.ourapp.R;
import com.example.wangbo.ourapp.bean.ThreeBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wangbo on 2018/7/30.
 * <p>
 * 第三版
 */
public class ThreeAdapter extends RecyclerListAdapter<ThreeBean> {

    public ThreeAdapter(List<ThreeBean> datas, Context context) {
        super(datas, context);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder createViewHolder(int type, ViewGroup parent) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.item_three, parent, false));
    }

    @Override
    public void handleData(int position, @NonNull BaseRecyclerViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ThreeBean bean = datas.get(position);
        viewHolder.content.setText(bean.getDescss());
        viewHolder.time.setText(bean.getLastTime().substring(5, bean.getLastTime().length() - 3));

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/text_font.OTF");
        viewHolder.content.setTypeface(typeface);
        viewHolder.time.setTypeface(typeface);
        viewHolder.edit.setTypeface(typeface);

        switch (bean.getStatus()) {
            case 1:
                viewHolder.edit.setText("心情");
                break;
            case 2:
                viewHolder.edit.setText("记事");
                break;
            case 3:
                viewHolder.edit.setText("提醒");
                break;
            case 4:
                viewHolder.edit.setText("日程");
                break;
            case 5:
                viewHolder.edit.setText("日记");
                break;
            case 6:
                viewHolder.edit.setText("私密日记");
                break;
        }
    }

    class ViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.edit)
        TextView edit;

        public ViewHolder(View rootView) {
            super(rootView);
        }
    }
}*/
package com.example.wangbo.ourapp.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.ThreeBean

import butterknife.BindView
import com.example.wangbo.ourapp.base.BaseRecyclerViewHolder

/**
 * Created by wangbo on 2018/7/30.
 *
 *
 * 第三版
 */
class ThreeAdapter(datas: List<ThreeBean>, context: Context) : RecyclerListAdapter<ThreeBean>(datas, context) {

    override fun createViewHolder(type: Int, parent: ViewGroup): BaseRecyclerViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_three, parent, false))
    }

    override fun handleData(position: Int, holder: BaseRecyclerViewHolder) {
        val viewHolder = holder as ViewHolder
        val bean = datas[position]
        viewHolder.content.text = bean.descss
        viewHolder.time.text = bean.lastTime.substring(5, bean.lastTime.length - 3)

        val typeface = Typeface.createFromAsset(mContext.assets, "fonts/text_font.OTF")
        viewHolder.content.typeface = typeface
        viewHolder.time.typeface = typeface
        viewHolder.edit.typeface = typeface

        when (bean.status) {
            1 -> viewHolder.edit.text = "心情"
            2 -> viewHolder.edit.text = "记事"
            3 -> viewHolder.edit.text = "提醒"
            4 -> viewHolder.edit.text = "日程"
            5 -> viewHolder.edit.text = "日记"
            6 -> viewHolder.edit.text = "私密日记"
        }
    }

    internal inner class ViewHolder(rootView: View) : BaseRecyclerViewHolder(rootView) {

        @BindView(R.id.content)
        lateinit var content: TextView

        @BindView(R.id.time)
        lateinit var time: TextView

        @BindView(R.id.edit)
        lateinit var edit: TextView
    }
}
