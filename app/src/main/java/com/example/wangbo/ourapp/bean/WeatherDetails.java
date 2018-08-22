package com.example.wangbo.ourapp.bean;

import java.util.List;

/**
 * Created by wangbo on 2018/7/27.
 */

public class WeatherDetails {


    private String weaid;
    private String days;
    private String week;
    private String cityno;
    private String citynm;
    private String temperature;
    private String temperature_curr;
    private String humidity;
    private String humi_high;
    private String humi_low;
    private String weather;
    private String weather_icon;
    private String weather_icon1;
    private String wind;
    private String winp;
    private String temp_high;
    private String temp_low;
    private String weatid;
    private String weatid1;

//           "temperature": "31℃/24℃", /*当日温度区间*/
//             "temperature_curr":"21℃", /*当前温度*/
//                     "humidity":"50%",/*湿度*/
//                     "aqi":"100",/*pm2.5 说明详见weather.pm25*/
//                     "weather":"多云转晴", /*天气*/
//                     "weather_icon":"http://api.k780.com/upload/weather/d/1.gif", /*气象图标 全部气象图标下载*/
//                     "weather_icon1":"", /*无意义不必理会*/
//                     "wind":"微风",/*风向*/
//                     "winp":"小于3级", /*风力*/
//                     "temp_high":"31", /*最高温度*/
//                     "temp_low":"24", /*最低温度*/
//                     "humi_high":"87.8", /*最大湿度*/
//                     "humi_low":"75.2", /*最小湿度*/
//                     "weatid":"2", /*天气ID，可对照weather.wtype接口中weaid*/
//                     "weatid1":"", /*无意义不必理会*/
//                     "windid":"1", /*风向ID(暂无对照表)*/
//                     "winpid":"2" /*风力ID(暂无对照表)*/
//                     "weather_iconid":"1"  /*气象图标编号,对应weather_icon 1.gif*/


    public WeatherDetails() {
    }

    public WeatherDetails(String weaid, String days, String week, String cityno, String citynm, String temperature, String temperature_curr, String humidity, String humi_high, String humi_low, String weather, String weather_icon, String weather_icon1, String wind, String winp, String temp_high, String temp_low, String weatid, String weatid1) {
        this.weaid = weaid;
        this.days = days;
        this.week = week;
        this.cityno = cityno;
        this.citynm = citynm;
        this.temperature = temperature;
        this.temperature_curr = temperature_curr;
        this.humidity = humidity;
        this.humi_high = humi_high;
        this.humi_low = humi_low;
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

    public String getTemperature_curr() {
        return temperature_curr;
    }

    public void setTemperature_curr(String temperature_curr) {
        this.temperature_curr = temperature_curr;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumi_high() {
        return humi_high;
    }

    public void setHumi_high(String humi_high) {
        this.humi_high = humi_high;
    }

    public String getHumi_low() {
        return humi_low;
    }

    public void setHumi_low(String humi_low) {
        this.humi_low = humi_low;
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
}
