package com.lovely3x.common.managements.location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lovely3x.common.beans.LocationWrapper;
import com.lovely3x.common.utils.ALog;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 位置管理器
 * Created by lovely3x on 15-9-10.
 */
public class LocationManager2 {

    private static final String TAG = "LocationManager2";

    private final List<LocationProvider> PROVIDERS = new Vector<>();
    private final List<LocationListener> LISTENER = new Vector<>();

    private final List<LocationListener> ONCE_LOCATION_LISTENERS = new Vector<>();

    private final Map<String, List<LocationListener>> SPECIAL_PROVIDER_LISTENER = new ConcurrentHashMap<>();

    /**
     * 正在用于定位的provider
     */
    private final List<LocationProvider> LOCATING_PROVIDERS = new Vector<>();

    private static LocationManager2 INSTANCE;

    private boolean mLocating;


    private LocationWrapper mCurrentLocation;


    /**
     * 是否为长定位
     */
    private boolean mLongLocating;

    private WeakReference<Context> mContext;

    public static LocationManager2 getInstance() {
        if (INSTANCE == null) {
            synchronized (LocationProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocationManager2();
                }
            }
        }
        return INSTANCE;
    }

    public void init(@NonNull Context context) {
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        this.mContext = new WeakReference<Context>(context);
    }

    public Context getContext() {
        return mContext == null ? null : mContext.get();
    }

    public boolean registerLocationProvider(@NonNull LocationProvider provider) {
        return !PROVIDERS.contains(provider) && PROVIDERS.add(provider);
    }

    public boolean unregisterLocationProvider(@NonNull LocationProvider provider) {
        return PROVIDERS.contains(provider) && PROVIDERS.remove(provider);
    }


    public boolean registerLocationListener(@NonNull LocationListener listener) {
        return !LISTENER.contains(listener) && LISTENER.add(listener);
    }

    public boolean registerLocationListener(@NonNull LocationListener listener, boolean callOnRegisterIfNeed) {
        boolean result = !LISTENER.contains(listener) && LISTENER.add(listener);
        if (callOnRegisterIfNeed && mCurrentLocation != null)
            listener.onLocationChanged(mCurrentLocation);
        return result;
    }


    public boolean unregisterLocationListener(@NonNull LocationListener provider) {
        return LISTENER.contains(provider) && LISTENER.remove(provider);
    }


    public void onLocationChanged(LocationWrapper location) {

        //更新当前的位置
        this.mCurrentLocation = location;

        //回调一次性监听器

        for (int i = 0; i < ONCE_LOCATION_LISTENERS.size(); i++) {
            LocationListener listener = ONCE_LOCATION_LISTENERS.get(i);
            try {
                listener.onLocationChanged(location);
            } catch (Exception e) {
                ALog.e(TAG, e);
            }
        }
        //回调一次性位置结束后，清除掉监听器列表
        ONCE_LOCATION_LISTENERS.clear();


        String provider = location.getProvider();
        if (!isEmpty(provider)) {
            List<LocationListener> listeners = SPECIAL_PROVIDER_LISTENER.get(provider);
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    LocationListener listener = listeners.get(i);
                    listener.onLocationChanged(location);
                }
                listeners.clear();
            }
            SPECIAL_PROVIDER_LISTENER.remove(provider);
        }


        //回调位置变化
        LocationListener[] listeners = new LocationListener[LISTENER.size()];
        listeners = LISTENER.toArray(listeners);

        for (LocationListener listener : listeners) {
            listener.onLocationChanged(location);
        }

        //如果当前的状态为非长时间定位
        //并且特定的监听器已经完成位置获取
        //那么在定位一次后则立即退出定位
        if (!mLongLocating && SPECIAL_PROVIDER_LISTENER.isEmpty()) {
            exitWatchLocationOnProviders();
            resetState(false);
        }
    }

    private boolean isEmpty(String provider) {
        return provider == null || provider.trim().length() == 0;
    }

    /**
     * 重置状态
     *
     * @param clearListeners 是否需要清除已经注册的监听器列表
     */
    private void resetState(boolean clearListeners) {
        mLongLocating = false;
        mLocating = false;

        if (clearListeners) {
            LISTENER.clear();
            ONCE_LOCATION_LISTENERS.clear();
        }
    }


    public void requestLocationOnce(@NonNull String provider, boolean forceRequest, LocationListener listener) {
        if (!PROVIDERS.isEmpty()) {
            boolean specialProviderIsWorking = false;

            for (LocationProvider locationProvider : LOCATING_PROVIDERS) {
                if (provider.equals(locationProvider.getProviderName())) {
                    specialProviderIsWorking = true;
                }
            }

            //指定的位置提供者已经正在工作了
            //无需重复添加开启该Provider的工作状态
            if (specialProviderIsWorking) {
                info("Special provider already tried to locating.");
                watchLocationOnSpecialProvider(provider, null, forceRequest, listener);
            } else {
                LocationProvider targetProvider = findLocationProvider(provider);
                if (targetProvider != null) {
                    watchLocationOnSpecialProvider(provider, targetProvider, forceRequest, listener);
                } else {
                    warring("Special location provider not found.");
                }
            }

        } else {
            warring("No available location source provider.");
        }
    }

    private void watchLocationOnSpecialProvider(String providerName, @Nullable LocationProvider provider, boolean forceRequest, LocationListener listener) {
        //添加监听器到列表中
        if (listener != null) {
            List<LocationListener> listeners = SPECIAL_PROVIDER_LISTENER.get(providerName);
            if (listeners == null) {
                listeners = new Vector<>();
                SPECIAL_PROVIDER_LISTENER.put(providerName, listeners);
            }
            listeners.add(listener);
        }

        if (!mLocating) {
            mLocating = true;
            if (!forceRequest && mCurrentLocation != null) {
                mLocating = false;
                //用户并没有强制要求新的位置，使用现有的位置。
                onLocationChanged(mCurrentLocation);
            } else {
                if (provider != null) {
                    //请求定位
                    LOCATING_PROVIDERS.add(provider);
                    provider.startLocation();
                }
            }
        } else {
            warring("Do not repeat requests for a location。");
        }
    }


    private LocationProvider findLocationProvider(String providerName) {
        for (LocationProvider provider : PROVIDERS) {
            if (providerName.equals(provider.getProviderName())) {
                return provider;
            }
        }
        return null;
    }

    public void requestLocationOnce(boolean forceRequest, LocationListener listener) {
        if (!PROVIDERS.isEmpty()) {
            //添加监听器到列表中
            if (listener != null) ONCE_LOCATION_LISTENERS.add(listener);

            if (!mLocating) {
                mLocating = true;
                if (!forceRequest && mCurrentLocation != null) {
                    mLocating = false;
                    //用户并没有强制要求新的位置，使用现有的位置。
                    onLocationChanged(mCurrentLocation);
                } else {
                    //请求定位
                    watchLocationOnProviders();
                }
            } else {
                warring("Do not repeat requests for a location。");
            }
        } else {
            warring("No available location source provider.");
        }
    }


    public void startWatchLocation() {
        //如果当前已经在定位
        if (mLocating) {
            //如果当前是短定位模式
            //那么就开启长定位模式
            mLongLocating = true;
        } else {
            //如果当前没有定位
            mLongLocating = true;
            mLocating = true;
        }
        //那么就开启长定位模式
        watchLocationOnProviders();
    }

    public void stopWatchLocation() {
        resetState(true);
    }


    private void watchLocationOnProviders() {
        for (LocationProvider provider : PROVIDERS) {
            if (!LOCATING_PROVIDERS.contains(provider)) {
                LOCATING_PROVIDERS.add(provider);
                provider.startLocation();
            }
        }
    }

    private void exitWatchLocationOnProviders() {
        for (LocationProvider provider : LOCATING_PROVIDERS) {
            provider.stopLocation();
        }
        LOCATING_PROVIDERS.clear();
    }


    private void warring(String msg) {
        System.err.println(TAG + ":" + msg);
    }

    private void error(String msg) {
        System.err.println(TAG + ":" + msg);
    }

    private void info(String msg) {
        System.out.println(TAG + ":" + msg);
    }

    public LocationWrapper getCurrentLocation() {
        return mCurrentLocation;
    }


    public static abstract class LocationProvider {

        protected final LocationManager2 mManager;

        public LocationProvider(LocationManager2 manager2) {
            this.mManager = manager2;
        }

        public abstract String getProviderName();

        public abstract void onConfigurationChanged(Configuration configuration);

        public abstract void startLocation();

        public abstract void stopLocation();


    }

    public interface LocationListener {

        void onLocationChanged(LocationWrapper locationWrapper);

    }

    public static class Configuration {

    }


}
