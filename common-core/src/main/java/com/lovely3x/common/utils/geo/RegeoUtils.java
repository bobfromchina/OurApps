package com.lovely3x.common.utils.geo;

import com.lovely3x.common.beans.LatLng;
import com.lovely3x.common.beans.LocationWrapper;
import com.lovely3x.common.utils.LatLngTransformUtils;
import com.lovely3x.common.utils.fileutils.StreamUtils;
import com.lovely3x.jsonparser.model.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

/**
 * 地理编码工具
 * Created by lovely3x on 16-4-6.
 */
public class RegeoUtils {

    /**
     * 由于使用的高德地图,素以需要高德地图的key
     */
    public static final String AMAP_RESET_API_KEY = "fd8dbbf31fc97166cbbcaba7d34c7a7f";

    /**
     * 逆地理编码地址
     */
    public static final String REGEO_URL = "http://restapi.amap.com/v3/geocode/regeo?key=%s&location=%s";

    /**
     * 地理编码地址
     */
    public static final String GEO_URL = "http://restapi.amap.com/v3/geocode/geo?key=%s&address=%s";

    /**
     * 建议设置的比较短,否则定位延迟高
     */
    public static final int CONNECT_TIME_OUT = 1000;
    public static final int READ_TIME_OUT = 2000;

    /**
     * 逆地理编码
     * 支持的坐标为wgs84
     * 逆向地理编码服务实现了将地球表面的地址坐标转换为标准地址的过程
     */
    public static void regeo(ExecutorService threadPool, final ReGeoListener geoListener, final LatLng latLng) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                geoListener.onReGeo(regeo(latLng));
            }
        };

        if (threadPool != null) threadPool.execute(run);
        else run.run();
    }


    /**
     * 逆地理编码
     * 支持的坐标为wgs84
     * 逆向地理编码服务实现了将地球表面的地址坐标转换为标准地址的过程
     */
    public static LocationWrapper regeo(final LatLng latLng) {
        LocationWrapper wrapper = null;
        try {
            //必须先将wgs84转换为gcj02
            LatLng transformed = LatLngTransformUtils.LatLng84_To_Gcj02(latLng.lat, latLng.lng);
            if (transformed == null) return null;
            String location = transformed.lng + "," + transformed.lat;
            String jsonResult = readConnection(String.format(REGEO_URL, AMAP_RESET_API_KEY, location));
            RegeoResult result = new JSONObject(jsonResult).createObject(RegeoResult.class);
            if (result != null && Integer.parseInt(result.status) == 1) {
                wrapper = new LocationWrapper();
                wrapper.setLatitude(latLng.lat);
                wrapper.setLongitude(latLng.lng);

                wrapper.setQualifiedName(result.regeocode.formattedAddress);

                wrapper.setProvider(result.regeocode.addressComponent.province);
                wrapper.setCity(result.regeocode.addressComponent.city);
                wrapper.setDistrict(result.regeocode.addressComponent.district);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return wrapper;
    }

    /**
     * 地理编码
     * 返回的结果为wgs84
     * 正向地理编码服务实现了将中文地址或地名描述转换为地球表面上相应位置的功能。
     */
    public static void geo(ExecutorService threadPool, final GeoListener reGeoListener, final String address) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                reGeoListener.onGeo(geo(address));
            }
        };
        if (threadPool != null) threadPool.execute(run);
        else run.run();
    }

    /**
     * 地理编码
     * 正向地理编码服务实现了将中文地址或地名描述转换为地球表面上相应位置的功能。
     * 返回的结果为wgs84
     */
    public static LatLng geo(final String address) {
        LatLng latLng = null;
        try {
            String result = readConnection(String.format(GEO_URL, AMAP_RESET_API_KEY, address));
            JSONObject jo = new JSONObject(result);
            if (Integer.parseInt(jo.getString("status")) == 1) {
                String loc = jo.getJSONArray("geocodes").getJSONOObject(0).getString("location");
                String[] synthLoc = loc.split(",");

                latLng = LatLngTransformUtils.gcj_To_84(Double.parseDouble(synthLoc[1]), Double.parseDouble(synthLoc[0]));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLng;
    }

    /**
     * 读取指定的连接
     *
     * @param url 需要读取的连接
     * @return 连接的结果
     */
    public static String readConnection(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(READ_TIME_OUT);
            InputStream is = conn.getInputStream();
            StreamUtils.readToString(is);
            StreamUtils.close(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 逆地理编码监听器
     * 逆向地理编码服务实现了将地球表面的地址坐标转换为标准地址的过程
     */
    public interface ReGeoListener {
        void onReGeo(LocationWrapper locationWrapper);
    }

    /**
     * 地理编码监听器
     * 正向地理编码服务实现了将中文地址或地名描述转换为地球表面上相应位置的功能。
     */
    public interface GeoListener {
        void onGeo(LatLng latLng);
    }
}
