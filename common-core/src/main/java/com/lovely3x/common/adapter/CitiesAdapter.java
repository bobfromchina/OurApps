package com.lovely3x.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.beans.City;
import com.lovely3x.common.utils.CharacterParser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * 城市选择列表的适配器
 *
 * @author lovely3x
 * @version 1.0
 * @time 2015-4-28 上午11:01:31
 * @Description: {}
 */
public class CitiesAdapter extends ListAdapter<City> implements StickyListHeadersAdapter {

    private char[] mIndicator;

    public CitiesAdapter(List<City> datas, Context context) {
        super(datas, context);
    }


    @NonNull
    @Override
    public BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.view_city_list_item, parent, false));
    }

    @Override
    public void handleData(int position, @NonNull BaseViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        City city = datas.get(position);
        viewHolder.tvCityName.setText(city.getName());
        viewHolder.mRootView.setOnClickListener(new DefaultClickedListener(position, city));
    }

    /**
     * 默认的点击监听器
     *
     * @author lovely3x
     * @version 1.0
     * @time 2015-5-25 下午5:04:03
     * @Description: {}
     */
    private class DefaultClickedListener implements View.OnClickListener {

        private final int mPosition;
        private City mCity;

        public DefaultClickedListener(int position, City city) {
            this.mCity = city;
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onClicked(mPosition, mCity);
            }
        }

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View v = getLayoutInflater().inflate(R.layout.view_city_list_sticky_item, parent, false);
        TextView view = (TextView) v.findViewById(R.id.tv_view_city_list_sticky_item_name);
        view.setText(String.valueOf(mIndicator[position]));
        return v;
    }

    @Override
    public long getHeaderId(int position) {
        return mIndicator[position];
    }

    private char[] getHeaderViewByContent() {
        char[] indicator = new char[datas.size()];
        CharacterParser parser = CharacterParser.getInstance();
        for (int i = 0; i < datas.size(); i++) {
            String content = datas.get(i).getName();
            if (content != null && content.trim().length() > 0) {
                indicator[i] = (char) (parser.convert(String.valueOf(content.charAt(0))).charAt(0) - 32);
            } else {
                indicator[i] = '#';
            }
        }
        return indicator;
    }


    @Override
    public void setData(List<City> datas) {
        super.setData(datas);
        sort();
        mIndicator = getHeaderViewByContent();
        notifyDataSetChanged();
    }

    /**
     * 排个序
     */

    private void sort() {
        final CharacterParser parser = CharacterParser.getInstance();
        Collections.sort(datas, new Comparator<City>() {
            public int compare(City lhs, City rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();
                if (TextUtils.isEmpty(lhsName)) {
                    return -1;
                }
                if (TextUtils.isEmpty(rhsName)) {
                    return 1;
                }
                int lhsChar = parser.convert(String.valueOf(lhsName.charAt(0))).charAt(0);
                int rhsChar = parser.convert(String.valueOf(rhsName.charAt(0))).charAt(0);
                return lhsChar - rhsChar;
            }
        });
    }

    /**
     * 根据分类的首字母的Char ASCII值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < mIndicator.length; i++) {
            char firstChar = mIndicator[i];
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    class ViewHolder extends BaseViewHolder {

        TextView tvCityName;

        /**
         * please don't modify the constructor
         *
         * @param rootView the root view
         */
        public ViewHolder(View rootView) {
            super(rootView);
            tvCityName = (TextView) rootView.findViewById(R.id.tv_view_city_list_item_city_name);
        }
    }
}
