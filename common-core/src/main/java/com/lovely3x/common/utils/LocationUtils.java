package com.lovely3x.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import com.lovely3x.common.beans.LocationWrapper;

/**
 * 计算两点的之间的距离
 *
 * @author Administrator
 */
public class LocationUtils {

    private static double EARTH_RADIUS = 6378137;// 地球半径 米

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 获取两点的距离 m
     *
     * @param originalLocation
     * @param nowlocation
     * @return
     */
    public static double getDistance(Location originalLocation, Location nowlocation) {
        double lat1 = originalLocation.getLatitude();
        double lng1 = originalLocation.getLongitude();
        double lat2 = nowlocation.getLatitude();
        double lng2 = nowlocation.getLongitude();
        return getWGS84Distance(lat1, lng1, lat2, lng2);
    }

    /**
     * 获取两点的距离 m
     *
     * @param originalLocation
     * @param nowlocation
     * @return
     */
    public static double getDistance(LocationWrapper originalLocation, LocationWrapper nowlocation) {
        double lat1 = originalLocation.getLatitude();
        double lng1 = originalLocation.getLongitude();
        double lat2 = nowlocation.getLatitude();
        double lng2 = nowlocation.getLongitude();
        return getWGS84Distance(lat1, lng1, lat2, lng2);
    }


    /**
     * 获取地球坐标
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getWGS84Distance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = s * 10000 / 10000;
        return s;
    }

    /**
     * @param context 给予程序访问环境的能力
     * @return 设备是否存在gps定位设备
     */
    public static boolean hasGPSDevice(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    /**
     * @param context 给予程序访问环境的能力
     * @return 设备是否存在网络定位设置
     */
    public static boolean hasNetDevice(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);
    }
}
