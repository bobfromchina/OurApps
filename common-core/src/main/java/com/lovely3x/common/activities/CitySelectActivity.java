package com.lovely3x.common.activities;

import android.Manifest;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.adapter.BaseViewHolder;
import com.lovely3x.common.adapter.CitiesAdapter;
import com.lovely3x.common.adapter.ListAdapter;
import com.lovely3x.common.beans.City;
import com.lovely3x.common.beans.LocationWrapper;
import com.lovely3x.common.managements.CityManager;

import com.lovely3x.common.managements.location.LocationManager2;
import com.lovely3x.common.utils.Response;
import com.lovely3x.common.utils.ViewUtils;
import com.lovely3x.common.widgets.sidebar.ISideBar;
import com.lovely3x.common.widgets.sidebar.OnTouchingLetterChangedListener;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


/**
 * 城市选择activity
 *
 * @author lovely3x
 * @version 1.0
 * @time 2015-4-24 下午4:15:27
 */
public abstract class CitySelectActivity extends TitleActivity implements OnTouchingLetterChangedListener, View.OnClickListener, CityManager.OnCityChangedListener {

    private static final int HANDLER_WHAT_GET_CITIES_DATA = 0X1;
    private static final int HANDLER_WHAT_GET_HOT_CITIES = 0x2;
    private static final int PERMISSION_REQUEST_CODE_LOC = 0x11;
    /**
     * 获取的城市列表原始数据
     */
    protected ArrayList<City> originalCities = new ArrayList<City>();

    /**
     * 城市列表
     */
    protected StickyListHeadersListView slhCitys;

    protected View header;

    /**
     * 热门城市
     */
    protected GridView gridHotCities;
    protected EditText editSearch;
    /**
     * 侧边字母View
     */
    protected ISideBar mSidBar;
    /**
     * 选中侧边字母中显示的textView
     */
    protected TextView mSidBarCenterTxt;

    protected CheckedTextView ctvCurrentCity;

    protected HotCitiesAdapter mHotCitiesAdapter;

    protected CitiesAdapter mCitiesAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_city_select;
    }

    /**
     * 设置适配器
     */
    protected void setAdapter() {
        mCitiesAdapter = new CitiesAdapter(null, this);
        mHotCitiesAdapter = new HotCitiesAdapter(null, this);

        slhCitys.setAdapter(mCitiesAdapter);
        gridHotCities.setAdapter(mHotCitiesAdapter);
    }

    protected void initViews() {
        slhCitys = (StickyListHeadersListView) findViewById(R.id.slh_activity_city_select_citys);
        mSidBar = (ISideBar) findViewById(R.id.sb_activity_city_select_bar);
        mSidBarCenterTxt = (TextView) findViewById(R.id.tv_activity_city_select_pop);
        mSidBar.setTextView(mSidBarCenterTxt);

        header = getLayoutInflater().inflate(R.layout.view_city_select_list_header, slhCitys.getWrappedList(), false);
        gridHotCities = (GridView) header.findViewById(R.id.grid_vity_select_list_header_hot_cities);
        editSearch = (EditText) header.findViewById(R.id.edit_view_city_select_list_item_search);
        gridHotCities.setSelector(new ColorDrawable());
        ctvCurrentCity = (CheckedTextView) header.findViewById(R.id.tv_view_city_select_list_header_current_city);
        ctvCurrentCity.setOnClickListener(this);
        slhCitys.addHeaderView(header);

    }

    @Override
    public void restoreInstanceOnCreateBefore(@NonNull Bundle savedInstance) {

    }

    @Override
    public void restoreInstanceOnCreateAfter(@NonNull Bundle savedInstance) {

    }


    protected void onViewInitialized() {
        setAdapter();
        initListener();
        initData();


        CityManager.getInstance().registerListener(this, true);

        if (PermissionChecker.checkSelfPermission(this, Manifest.permission_group.LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            LocationManager2.getInstance().requestLocationOnce(false, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission_group.LOCATION}, PERMISSION_REQUEST_CODE_LOC);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission_group.LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            LocationManager2.getInstance().requestLocationOnce(false, null);
        }
    }

    protected void initListener() {

        mSidBar.setOnTouchingLetterChangedListener(this);
        editSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                search(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        mCitiesAdapter
                .setOnItemClickedListener(new ListAdapter.OnItemClickedListener<City>() {
                    @Override
                    public void onClicked(int position, City city) {
                        onCitySelected(city, CityManager.MODEL_USER_CHOICE);
                    }
                });

        mHotCitiesAdapter
                .setOnItemClickedListener(new ListAdapter.OnItemClickedListener<City>() {
                    @Override
                    public void onClicked(int position, City city) {
                        onCitySelected(city, CityManager.MODEL_USER_CHOICE);
                    }
                });

    }

    protected void initData() {
        LocationWrapper wrapper = LocationManager2.getInstance().getCurrentLocation();
        ctvCurrentCity.setText(wrapper == null ? getString(R.string.obtaing) : wrapper.getCity());

        showProgressCircle();

        getCitiesData(HANDLER_WHAT_GET_CITIES_DATA);
    }

    /**
     * 获取城市数据
     *
     * @param what 消息标识
     */
    protected abstract void getCitiesData(int what);

    /**
     * 获取热门城市数据
     *
     * @param what
     */
    protected abstract void getHotCities(int what);

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_view_city_select_list_header_current_city) {
            LocationWrapper cur = LocationManager2.getInstance().getCurrentLocation();
            City city = null;
            if (cur != null) {
                city = new City(-1, cur.getCity(), -1);
            } else {
                city = new City(-1, getString(R.string.not_set), -1);
            }
            onCitySelected(city, CityManager.MODEL_AUTO);
        }
    }

    @Override
    public void onCityChanged(City preCity, City currentCity) {
        LocationWrapper wrapper = LocationManager2.getInstance().getCurrentLocation();
        ctvCurrentCity.setText(wrapper == null ? getString(R.string.obtaing) : wrapper.getCity());
    }

    /**
     * 热门城市
     *
     * @author lovely3x
     * @version 1.0
     * @time 2015-4-28 下午4:13:16
     */
    protected class HotCitiesAdapter extends ListAdapter<City> {

        public HotCitiesAdapter(ArrayList<City> datas, Context context) {
            super(datas, context);
        }

        @NonNull
        @Override
        public BaseViewHolder createViewHolder(int position, ViewGroup parent) {
            ViewHolder vh = null;
            CheckedTextView view = new CheckedTextView(mContext);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(-2, -2);
            view.setLayoutParams(lp);
            view.setBackgroundResource(R.drawable.cbx_city_item_selector);
            view.setTextColor(
                    mContext.getResources().getColorStateList(R.color.color_city_item_selector));
            view.setPadding(
                    ViewUtils.dp2pxF(15),
                    ViewUtils.dp2pxF(6),
                    ViewUtils.dp2pxF(15),
                    ViewUtils.dp2pxF(6));
            vh = new ViewHolder(view);
            vh.tvName = view;
            return vh;
        }

        @Override
        public void handleData(int position, @NonNull BaseViewHolder holder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvName.setText(datas.get(position).getName());
            viewHolder.mRootView.setOnClickListener(new DefaultOnSelectedListener(datas.get(position), position));
        }

        /**
         * 城市选择监听器
         *
         * @author lovely3x
         * @version 1.0
         * @time 2015-5-25 下午4:50:43
         */
        private class DefaultOnSelectedListener implements View.OnClickListener {
            private final int mPosition;
            private City mCity;

            public DefaultOnSelectedListener(City city, int position) {
                this.mCity = city;
                this.mPosition = position;
            }

            public void onClick(View v) {
                if (mOnItemClickedListener != null) {
                    mOnItemClickedListener.onClicked(mPosition, mCity);
                }
            }
        }

    }

    /**
     * 当成是被选择后执行
     *
     * @param city  城市
     * @param model 模式
     */
    protected void onCitySelected(City city, int model) {
        CityManager.getInstance().update(city, model);
        slhCitys.setSelection(0);
    }

    @Override
    protected void handleResponseMessage(Message msg, Response response) {
        super.handleResponseMessage(msg, response);
        switch (msg.what) {
            case HANDLER_WHAT_GET_CITIES_DATA:
                if (response.isSuccessful) {
                    mCitiesAdapter.setData((List<City>) response.obj);
                    originalCities = new ArrayList<>(mCitiesAdapter.getDatas());
                } else {
                    mCitiesAdapter.setData(null);
                }
                //Always try to fetch hot cities.
                getHotCities(HANDLER_WHAT_GET_HOT_CITIES);
                break;
            case HANDLER_WHAT_GET_HOT_CITIES://城市选择
                dismissProgressCircle();
                if (response.isSuccessful) {
                    mHotCitiesAdapter.setData((List<City>) response.obj);
                } else {
                    mHotCitiesAdapter.setData(null);
                }
                break;
        }
    }

    protected static class ViewHolder extends BaseViewHolder {
        CheckedTextView tvName;

        /**
         * please don't modify the constructor
         *
         * @param rootView the root view
         */
        public ViewHolder(View rootView) {
            super(rootView);
        }
    }

    /**
     * 当右边的选择条上的文字变化后回调
     */
    @Override
    public void onTouchingLetterChanged(String s) {
        if (mCitiesAdapter != null && s != null && s.length() > 0) {
            int position = mCitiesAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                slhCitys.setSelection(position + 1);
            }
        }
    }

    /**
     * 搜索城市
     *
     * @param str 需要搜索的城市
     */
    protected void search(String str) {
        if (TextUtils.isEmpty(str)) {
            mCitiesAdapter.setData(originalCities);
            return;
        }

        // 过滤完成的城市
        ArrayList<City> filter = new ArrayList<City>();
        for (int i = 0; i < originalCities.size(); i++) {
            City city = originalCities.get(i);
            if (city == null || TextUtils.isEmpty(city.getName())
                    || city.getName().length() < str.length()) {
                continue;
            }

            if (city.getName().substring(0, str.length()).equalsIgnoreCase(str)) {
                filter.add(city);
            }
        }
        mCitiesAdapter.setData(filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CityManager.getInstance().unregisterListener(this);
    }
}
