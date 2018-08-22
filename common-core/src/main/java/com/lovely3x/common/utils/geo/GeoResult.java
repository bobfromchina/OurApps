package com.lovely3x.common.utils.geo;

/**
 * geo 结果
 * Created by lovely3x on 16-4-6.
 */
public class GeoResult {

    public String status;

    public String infocode;

    public String info;

    public double lat;

    public double lng;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoResult geoResult = (GeoResult) o;

        if (Double.compare(geoResult.lat, lat) != 0) return false;
        if (Double.compare(geoResult.lng, lng) != 0) return false;
        if (status != null ? !status.equals(geoResult.status) : geoResult.status != null)
            return false;
        if (infocode != null ? !infocode.equals(geoResult.infocode) : geoResult.infocode != null)
            return false;
        return !(info != null ? !info.equals(geoResult.info) : geoResult.info != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = status != null ? status.hashCode() : 0;
        result = 31 * result + (infocode != null ? infocode.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "GeoResult{" +
                "info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", infocode='" + infocode + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
