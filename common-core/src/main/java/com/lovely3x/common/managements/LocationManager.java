package com.lovely3x.common.managements;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lovely3x.common.beans.LocationWrapper;
import com.lovely3x.common.consts.Const;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.LocationUtils;

import java.util.Vector;

/**
 * 位置管理器
 * Created by lovely3x on 15-9-10.
 */
public class LocationManager implements AMapLocationListener {

    /**
     * 默认的更新位置的时间间隔
     */
    public static final long DEFAULT_UPDATE_LOCATION_INTERVAL = 5 * 1000;
    /**
     * 跟新位置的最小距离
     */
    public static final float DEFAULT_UPDATE_LOCATION_MIN_DISTANCE = 1.0f;

    /*位置模拟模式*/
    public static final boolean MOCK_MODE = Const.MOCK_LOCATION;

    private static String TAG = "LocationManager";

    /**
     * 位置管理器实例
     */
    private static LocationManager mInstance;

    private final Object lock = new Object();

    /**
     * 默认的位置请求更新间隔时间 毫秒
     */
    private long updateLocationInterval = DEFAULT_UPDATE_LOCATION_INTERVAL;

    /**
     * 最低的位置请求更新距离 米
     */
    private float updateLocationMinDistance = DEFAULT_UPDATE_LOCATION_MIN_DISTANCE;

    /**
     * 当前的位置对象
     */
    private LocationWrapper mLocation;

    /**
     * 位置变化监听器们
     */
    private Vector<OnLocationChangeListener> mLocationChangeListeners = new Vector<>();

    private Context mContext;

    private AMapLocationClient mAMapLocationManager;

    /**
     * 是否使用gps
     */
    private boolean useGPS = false;

    private LocationManager() {
        TAG = LocationManager.class.getSimpleName();
    }

    /**
     * 获取位置管理器实例
     *
     * @return 位置管理器实例
     */
    public static LocationManager getInstance() {
        if (mInstance == null) {
            synchronized (LocationManager.class) {
                if (mInstance == null) {
                    mInstance = new LocationManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置请求位置更新的时间间隔
     *
     * @param milliseconds 位置更新的毫秒值
     */
    public LocationManager setUpdateLocationInterval(long milliseconds) {
        if (!isInit()) {
            updateLocationInterval = milliseconds;
            return this;
        }
        throw new IllegalStateException("This method must be called before the init");
    }

    /**
     * 设置是否使用gps
     *
     * @param useGPS 是否使用gps
     * @return 当前实例
     */
    public LocationManager setUseGPS(boolean useGPS) {
        if (!isInit()) {
            useGPS = useGPS;
            return this;
        }
        throw new IllegalStateException("This method must be called before the init");
    }

    /**
     * 设置更新位置的最小距离
     *
     * @param minDistance 最小距离
     * @return 当前实例
     */
    public LocationManager setUpdateLocationMinDistance(float minDistance) {
        if (!isInit()) {
            updateLocationMinDistance = minDistance;
            return this;
        }
        throw new IllegalStateException("This method must be called before the init");
    }


    /**
     * 是否初始化
     *
     * @return true 或false
     */
    private boolean isInit() {
        return mContext != null;
    }


    /**
     * 开始定位
     */
    public void start() {
        initCheck();
        mAMapLocationManager = new AMapLocationClient(mContext);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mAMapLocationManager.setLocationListener(this);
        //设置为高精度定位模式
        //  mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //mLocationOption.setGpsFirst(useGPS);
        mLocationOption.setInterval(updateLocationInterval);
        mLocationOption.setGpsFirst(false);
        //设置定位参数
        mAMapLocationManager.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mAMapLocationManager.startLocation();
    }

    /**
     * 停止定位
     */
    public void stop() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.stopLocation();
            mAMapLocationManager.unRegisterLocationListener(this);
            mAMapLocationManager = null;
        }
    }

    /**
     * 初始化操作
     *
     * @param context 上下文对象
     */
    public void init(Context context) {
        if (context == null) {
            throw new NullPointerException("context can be not null.");
        }
        if (this.mContext == null) {
            this.mContext = context.getApplicationContext();
        }
    }


    /**
     * 初始化检查
     */
    public void initCheck() {
        if (mContext == null) {
            throw new IllegalStateException("you don't call init method.");
        }
    }

    /**
     * 注册位置变化监听器
     *
     * @param onLocationChangeListener 位置变化监听器
     * @param callOnRegister           如果当前位置不为null,是否需要在注册的时候回调位置
     */
    public void registerOnLocationChangeListener(OnLocationChangeListener onLocationChangeListener, boolean callOnRegister) {

        ALog.i(TAG, "registerOnLocationChangeListener " + onLocationChangeListener + " retain " + mLocationChangeListeners.size());

        synchronized (lock) {
            initCheck();
            if (onLocationChangeListener != null) {
                mLocationChangeListeners.add(onLocationChangeListener);
                if (callOnRegister && mLocation != null && mLocation.getLatitude() != 0 && mLocation.getLongitude() != 0) {
                    onLocationChangeListener.onLocationChanged(Integer.MAX_VALUE, null, mLocation);
                }
            }
        }
    }

    /**
     * 注册位置变化监听器
     *
     * @param onLocationChangeListener 位置变化监听器
     */
    public void registerOnLocationChangeListener(OnLocationChangeListener onLocationChangeListener) {
        registerOnLocationChangeListener(onLocationChangeListener, true);
    }

    /**
     * 移除位置变化监听器
     *
     * @param onLocationChangeListener 需要移除的位置变化监听器
     */
    public void unRegisterOnLocationChangeListener(OnLocationChangeListener onLocationChangeListener) {

        ALog.i(TAG, "unregisterOnLocationChangeListener " + onLocationChangeListener + " retain " + mLocationChangeListeners.size());
        synchronized (lock) {
            initCheck();
            if (onLocationChangeListener != null) {
                mLocationChangeListeners.remove(onLocationChangeListener);
            }
        }
    }


    /**
     * 获取当前的位置
     *
     * @return 当前位置对象 or null 如果没有获取到位置对象
     */
    public LocationWrapper getCurrentLocation() {
        synchronized (lock) {
            initCheck();
            return mLocation;
        }
    }

    public void mockLocation(Location location) {
        if (!MOCK_MODE) return;

        AMapLocation aMapLocation;
        aMapLocation = new AMapLocation(location);

        synchronized (lock) {
            final LocationWrapper current = new LocationWrapper();
            current.setOriginalLocation(aMapLocation);

            current.setLatitude(aMapLocation.getLatitude());
            current.setLongitude(aMapLocation.getLongitude());

            current.setQualifiedName(aMapLocation.getAddress());
            current.setProvince(aMapLocation.getProvince());
            current.setCity(aMapLocation.getCity());
            current.setDistrict(aMapLocation.getDistrict());

            current.setCoorType(LocationWrapper.CoorType.GCJ_02);

            current.setTime(aMapLocation.getTime());
            current.setAlt(aMapLocation.getAltitude());
            current.setAccuracy(aMapLocation.getAccuracy());
            current.setProvider(aMapLocation.getProvider());

            if (current.getLatitude() != 0 && current.getLongitude() != 0) {
                double distance = 0;
                if (mLocation != null) {//如果上次的位置不为 null
                    distance = LocationUtils.getDistance(mLocation, current);
                    // if (distance >= updateLocationMinDistance) {
                    for (OnLocationChangeListener next : mLocationChangeListeners) {
                        if (next != null) {
                            next.onLocationChanged(distance, mLocation, current);
                        }
                        //  }
                    }
                } else {
                    for (OnLocationChangeListener next : mLocationChangeListeners) {
                        if (next != null) {
                            next.onLocationChanged(distance, null, current);
                        }
                    }
                }
                mLocation = current.clone();
            }

        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (MOCK_MODE) return;
        if (Config.DEBUG) {
            ALog.i(TAG, "aMapLocation.getErrorInfo() " + aMapLocation.getErrorInfo());
            ALog.i(TAG, "onLocationChanged-> " + aMapLocation.toStr(3));
        }
        synchronized (lock) {
            final LocationWrapper current = new LocationWrapper();
            current.setOriginalLocation(aMapLocation);

            current.setLatitude(aMapLocation.getLatitude());
            current.setLongitude(aMapLocation.getLongitude());

            current.setQualifiedName(aMapLocation.getAddress());
            current.setProvince(aMapLocation.getProvince());
            current.setCity(aMapLocation.getCity());
            current.setDistrict(aMapLocation.getDistrict());

            current.setCoorType(LocationWrapper.CoorType.GCJ_02);

            current.setTime(aMapLocation.getTime());
            current.setAlt(aMapLocation.getAltitude());
            current.setAccuracy(aMapLocation.getAccuracy());
            current.setProvider(aMapLocation.getProvider());

            if (current.getLatitude() != 0 && current.getLongitude() != 0 && !TextUtils.isEmpty(current.getCity())) {
                double distance = 0;
                if (mLocation != null) {//如果上次的位置不为 null
                    distance = LocationUtils.getDistance(mLocation, current);
                    for (OnLocationChangeListener next : mLocationChangeListeners) {
                        if (next != null) {
                            next.onLocationChanged(distance, mLocation, current);
                        }
                    }
                } else {
                    for (OnLocationChangeListener next : mLocationChangeListeners) {
                        if (next != null) {
                            next.onLocationChanged(distance, null, current);
                        }
                    }
                }
                mLocation = current;
            }
        }
    }


    /**
     * 请求更新一次位置
     * 可能导致发生位置偏移过大
     */
    @Deprecated
    public void requestUpdateLocation() {
       /* if (mLocation != null && mLocation.getOriginalLocation() != null) {
            onLocationChanged(new AMapLocation(mLocation.getOriginalLocation()));
        }*/
    }

    /**
     * 位置变化监听器
     */
    public interface OnLocationChangeListener {
        /**
         * 当位置发生变化后调用
         *
         * @param betweenDistance 本次和上次之间的距离
         * @param pre             上一次的位置或者null,如果为第一次调用这个方法将会返回null
         * @param cur             本次的位置
         */
        void onLocationChanged(double betweenDistance, LocationWrapper pre, LocationWrapper cur);

    }
}
