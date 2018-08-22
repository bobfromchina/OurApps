package com.example.wangbo.ourapp.bean;

public class WeatherBean {

//    private String wendu;
//    private String ganmao;
//    private String date;
//    private List<WeatherDetails> forecast;
    private String weaid;
    private String days;
    private String week;
    private String cityno;
    private String citynm;
    private String temperature;
    private String weather;
    private String weather_icon;
    private String weather_icon1;
    private String wind;
    private String winp;
    private String temp_high;
    private String temp_low;
    private String weatid;
    private String weatid1;

    public WeatherBean() {
    }

    public WeatherBean(String weaid, String days, String week, String cityno, String citynm, String temperature, String weather, String weather_icon, String weather_icon1, String wind, String winp, String temp_high, String temp_low, String weatid, String weatid1) {
        this.weaid = weaid;
        this.days = days;
        this.week = week;
        this.cityno = cityno;
        this.citynm = citynm;
        this.temperature = temperature;
        this.weather = weather;
        this.weather_icon = weather_icon;
        this.weather_icon1 = weather_icon1;
        this.wind = wind;
        this.winp = winp;
        this.temp_high = temp_high;
        this.temp_low = temp_low;
        this.weatid = weatid;
        this.weatid1 = weatid1;
    }

    public String getWeaid() {
        return weaid;
    }

    public void setWeaid(String weaid) {
        this.weaid = weaid;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getCityno() {
        return cityno;
    }

    public void setCityno(String cityno) {
        this.cityno = cityno;
    }

    public String getCitynm() {
        return citynm;
    }

    public void setCitynm(String citynm) {
        this.citynm = citynm;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeather_icon() {
        return weather_icon;
    }

    public void setWeather_icon(String weather_icon) {
        this.weather_icon = weather_icon;
    }

    public String getWeather_icon1() {
        return weather_icon1;
    }

    public void setWeather_icon1(String weather_icon1) {
        this.weather_icon1 = weather_icon1;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWinp() {
        return winp;
    }

    public void setWinp(String winp) {
        this.winp = winp;
    }

    public String getTemp_high() {
        return temp_high;
    }

    public void setTemp_high(String temp_high) {
        this.temp_high = temp_high;
    }

    public String getTemp_low() {
        return temp_low;
    }

    public void setTemp_low(String temp_low) {
        this.temp_low = temp_low;
    }

    public String getWeatid() {
        return weatid;
    }

    public void setWeatid(String weatid) {
        this.weatid = weatid;
    }

    public String getWeatid1() {
        return weatid1;
    }

    public void setWeatid1(String weatid1) {
        this.weatid1 = weatid1;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "weaid='" + weaid + '\'' +
                ", days='" + days + '\'' +
                ", week='" + week + '\'' +
                ", cityno='" + cityno + '\'' +
                ", citynm='" + citynm + '\'' +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                ", weather_icon='" + weather_icon + '\'' +
                ", weather_icon1='" + weather_icon1 + '\'' +
                ", wind='" + wind + '\'' +
                ", winp='" + winp + '\'' +
                ", temp_high='" + temp_high + '\'' +
                ", temp_low='" + temp_low + '\'' +
                ", weatid='" + weatid + '\'' +
                ", weatid1='" + weatid1 + '\'' +
                '}';
    }
}
