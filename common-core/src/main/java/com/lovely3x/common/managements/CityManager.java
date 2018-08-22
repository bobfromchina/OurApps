package com.lovely3x.common.managements;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lovely3x.common.beans.City;
import com.lovely3x.common.beans.LocationWrapper;
import com.lovely3x.common.managements.location.LocationManager2;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理器
 * Created by lovely3x on 15-9-18.
 */
public class CityManager implements LocationManager2.LocationListener {


    /**
     * 城市模式 自动
     */
    public static final int MODEL_AUTO = 0x1;
    /**
     * 城市模式 手动
     */
    public static final int MODEL_USER_CHOICE = 0x2;
    private static final String CITY_PREFERENCES = "city.preferences";
    private static final String PREFERENCES_KEY_CITY_NAME = "preferences.key.city.name";
    private static final String PREFERENCES_KEY_MODEL = "preferences.key.model";
    private static final Object lock = new Object();
    private static CityManager mCityManager;
    private List<OnCityChangedListener> listeners = new ArrayList<>();
    /**
     * 模式 默认自动模式
     */
    private int model = MODEL_AUTO;
    /**
     * 当前的城市
     */
    private City mCity;
    private Context mContext;

    /**
     * 获取实例
     *
     * @return 实例对象
     */
    public static CityManager getInstance() {
        if (mCityManager == null) {
            synchronized (lock) {
                if (mCityManager == null) {
                    synchronized (lock) {
                        mCityManager = new CityManager();
                    }
                }
            }
        }
        return mCityManager;
    }

    public City getCity() {
        return mCity;
    }

    /**
     * 初始化操作
     *
     * @param context 上下文
     */
    public void init(Context context) {
        if (context == null) throw new IllegalArgumentException("Context can't be null.");
        ///保证只初始化一次
        if (mContext == null) {
            this.mContext = context.getApplicationContext();
            LocationManager2.getInstance().init(mContext);
            LocationManager2.getInstance().registerLocationListener(this, true);
            mCity = readCityFromSharePreferences();
            //回调
            update(mCity, model);
        }
    }

    /**
     * 从编号设置从读取城市数据
     *
     * @return
     */
    protected City readCityFromSharePreferences() {
        SharedPreferences sp = mContext.getSharedPreferences(CITY_PREFERENCES, Context.MODE_PRIVATE);
        String cityName = sp.getString(PREFERENCES_KEY_CITY_NAME, null);
        this.model = sp.getInt(PREFERENCES_KEY_MODEL, MODEL_AUTO);
        City city = new City();
        city.setName(cityName);
        return city;
    }

    /**
     * 写入当前的城市数据到偏好设置中
     */
    protected void writeCurrentCityToPreferences() {
        if (mCity != null) {
            SharedPreferences sp = mContext.getSharedPreferences(CITY_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(PREFERENCES_KEY_CITY_NAME, mCity.getName());
            editor.putInt(PREFERENCES_KEY_MODEL, model);
            editor.apply();
        }
    }

    /**
     * 初始化检查
     */
    private void initCheck() {
        if (mContext == null) {
            throw new RuntimeException("Did you forgot call init method.");
        }
    }

    /**
     * 更新 城市
     *
     * @param city 需要更新的城市
     */
    public void update(City city, int model) {
        initCheck();
        this.model = model;
        City temp = mCity == null ? null : mCity.clone();
        this.mCity = city;
        writeCurrentCityToPreferences();
        for (OnCityChangedListener listener : listeners) {
            if (listener != null) listener.onCityChanged(temp, city);
        }
    }

    /**
     * 注册城市变化监听器
     *
     * @param listener  监听器
     * @param onAddCall 当添加时回调监听器
     */
    public void registerListener(OnCityChangedListener listener, boolean onAddCall) {
        initCheck();
        if (listener != null) {
            listeners.add(listener);
            if (onAddCall) {
                listener.onCityChanged(null, mCity);
            }
        }
    }

    /**
     * 解除注册的监听器
     *
     * @param listener 监听器
     */
    public void unregisterListener(OnCityChangedListener listener) {
        initCheck();
        this.listeners.remove(listener);
    }

    @Override
    public void onLocationChanged(LocationWrapper cur) {
        if (cur != null) {
            if (mCity == null || TextUtils.isEmpty(mCity.getName()) || !mCity.getName().equals(cur.getCity())) {
                if (model == MODEL_AUTO) {//自动模式
                    //目前只是按照城市名来处理的
                    //可以加入id或者其他的
                    City city = new City();
                    city.setName(cur.getCity());
                    update(city, model);
                }
            }
        }
    }


    /**
     * 当城市变化后调用
     */
    public interface OnCityChangedListener {
        /**
         * 当前城市
         *
         * @param preCity     上一个城市
         * @param currentCity 当前的城市
         */
        void onCityChanged(City preCity, City currentCity);
    }

}
