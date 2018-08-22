package com.lovely3x.common.utils.geo;

public class RegeoResult {

    public String status;

    public String infocode;

    public String info;

    public Regeocode regeocode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegeoResult that = (RegeoResult) o;

        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (infocode != null ? !infocode.equals(that.infocode) : that.infocode != null)
            return false;
        if (info != null ? !info.equals(that.info) : that.info != null) return false;
        return !(regeocode != null ? !regeocode.equals(that.regeocode) : that.regeocode != null);

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (infocode != null ? infocode.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        result = 31 * result + (regeocode != null ? regeocode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RegeoResult{" +
                "info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", infocode='" + infocode + '\'' +
                ", regeocode=" + regeocode +
                '}';
    }

    public static class Regeocode {

        public String formattedAddress;

        public AddressComponent addressComponent;

        @Override
        public String toString() {
            return "Regeocode{" +
                    "addressComponent=" + addressComponent +
                    ", formattedAddress='" + formattedAddress + '\'' +
                    '}';
        }
    }

    public static class Neighborhood {

        public String type;

        public String name;

        @Override
        public String toString() {
            return "Neighborhood{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public static class AddressComponent {
        public String citycode;

        public String township;

        public String towncode;

        public String province;

        public String city;

        public String adcode;

        public String district;

        public String country;

        public Neighborhood neighborhood;

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            AddressComponent that = (AddressComponent) object;

            if (citycode != null ? !citycode.equals(that.citycode) : that.citycode != null)
                return false;
            if (township != null ? !township.equals(that.township) : that.township != null)
                return false;
            if (towncode != null ? !towncode.equals(that.towncode) : that.towncode != null)
                return false;
            if (province != null ? !province.equals(that.province) : that.province != null)
                return false;
            if (city != null ? !city.equals(that.city) : that.city != null) return false;
            if (adcode != null ? !adcode.equals(that.adcode) : that.adcode != null) return false;
            if (district != null ? !district.equals(that.district) : that.district != null)
                return false;
            if (country != null ? !country.equals(that.country) : that.country != null)
                return false;
            return !(neighborhood != null ? !neighborhood.equals(that.neighborhood) : that.neighborhood != null);

        }

        @Override
        public int hashCode() {
            int result = citycode != null ? citycode.hashCode() : 0;
            result = 31 * result + (township != null ? township.hashCode() : 0);
            result = 31 * result + (towncode != null ? towncode.hashCode() : 0);
            result = 31 * result + (province != null ? province.hashCode() : 0);
            result = 31 * result + (city != null ? city.hashCode() : 0);
            result = 31 * result + (adcode != null ? adcode.hashCode() : 0);
            result = 31 * result + (district != null ? district.hashCode() : 0);
            result = 31 * result + (country != null ? country.hashCode() : 0);
            result = 31 * result + (neighborhood != null ? neighborhood.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "AddressComponent{" +
                    "citycode='" + citycode + '\'' +
                    ", township='" + township + '\'' +
                    ", towncode='" + towncode + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", adcode='" + adcode + '\'' +
                    ", district='" + district + '\'' +
                    ", country='" + country + '\'' +
                    ", neighborhood=" + neighborhood +
                    '}';
        }
    }
}
