package com.lovely3x.common.managements.location;

import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lovely3x.common.beans.LocationWrapper;
import com.lovely3x.common.utils.ALog;

/**
 * Created by lovely3x on 16/11/30.
 */
public class AMapLocationProvider extends LocationManager2.LocationProvider implements AMapLocationListener {

    private static final String PROVIDER_NAME = "AMap";
    private static final String TAG = "AMapLocationProvider";

    private LocationManager2.Configuration mConfiguration;
    private AMapLocationClient mAMapLocationManager;

    public AMapLocationProvider(LocationManager2 manager2) {
        super(manager2);
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public void onConfigurationChanged(LocationManager2.Configuration configuration) {
        this.mConfiguration = configuration;
    }

    @Override
    public void startLocation() {
        mAMapLocationManager = new AMapLocationClient(mManager.getContext());
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mAMapLocationManager.setLocationListener(this);
        //设置为高精度定位模式
        //  mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //mLocationOption.setGpsFirst(useGPS);

        mLocationOption.setInterval(1000);
        mLocationOption.setGpsFirst(false);

        //设置定位参数
        mAMapLocationManager.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mAMapLocationManager.startLocation();
    }

    @Override
    public void stopLocation() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.stopLocation();
            mAMapLocationManager = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        ALog.i(TAG, "onLocationChanged => " + aMapLocation);

        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            final LocationWrapper current = wrapLocationWrapper(aMapLocation);
            mManager.onLocationChanged(current);
        }
    }

    @NonNull
    private LocationWrapper wrapLocationWrapper(AMapLocation aMapLocation) {

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
        return current;
    }

}
