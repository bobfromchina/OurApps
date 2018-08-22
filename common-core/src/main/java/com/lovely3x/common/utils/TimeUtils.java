package com.lovely3x.common.utils;

import com.lovely3x.common.consts.Const;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by lovely3x on 16-3-12.
 */
public class TimeUtils {

    public static final int HOUR = 1000 * 60 * 60;
    public static final int MINUTE = 1000 * 60;
    public static final int SECOND = 1000;
    public static final int DAY = HOUR * 24;

    public static final StringBuilder sb = new StringBuilder();

    public static final SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static final SimpleDateFormat yyyyMMddFormator = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static final SimpleDateFormat yyyyMMddHHmmFormator = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

    public static final Calendar calendar = Calendar.getInstance();

    /**
     * 格式化时间
     *
     * @param remainTime 需要格式化的时间
     * @return 格式化结果像这样： 10:20:02
     */
    public static String format_Hours_Minute_Second(long remainTime) {

        int h = 0;
        int m = 0;
        int s = 0;

        if (remainTime >= HOUR) {
            h = (int) (remainTime / HOUR);
            remainTime -= h * HOUR;
        }
        if (remainTime >= MINUTE) {
            m = (int) (remainTime / MINUTE);
            remainTime -= m * MINUTE;
        }

        if (remainTime >= SECOND) {
            s = (int) (remainTime / SECOND);
        }
        synchronized (TimeUtils.class) {
            sb.delete(0, sb.length());
            if (h < 10) {
                sb.append('0');
            }
            sb.append(h);
            sb.append(':');
            if (m < 10) {
                sb.append('0');
            }
            sb.append(m);
            sb.append(':');
            if (s < 10) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }


    /**
     * 格式化时间
     *
     * @param remainTime 需要格式化的时间
     * @return 格式化结果像这样： 10小时20分02秒
     */
    public static String format_Hours_Minute_Second_Unit(long remainTime) {

        int h = 0;
        int m = 0;
        int s = 0;

        if (remainTime >= HOUR) {
            h = (int) (remainTime / HOUR);
            remainTime -= h * HOUR;
        }
        if (remainTime >= MINUTE) {
            m = (int) (remainTime / MINUTE);
            remainTime -= m * MINUTE;
        }

        if (remainTime >= SECOND) {
            s = (int) (remainTime / SECOND);
        }
        synchronized (TimeUtils.class) {
            sb.delete(0, sb.length());
            if (h > 0) {
                if (h < 10) {
                    sb.append('0');
                }
                sb.append(h);
                sb.append("时");
            }

            if (m > 0) {
                if (m < 10) {
                    sb.append('0');
                }
                sb.append(m);
                sb.append("分");
            }

            if (s < 10) {
                sb.append('0');
            }
            sb.append(s);
            sb.append("秒");
        }
        return sb.toString();
    }

    /**
     * 格式化时间为浮点型
     * 例如这种格式:2.59,2.34
     */
    public static float format_Float_Hour(float timeMillis) {
        return timeMillis / HOUR;
    }

    /**
     * 格式化时间字符串 类似于这样 2015-12-22 19:22:21
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String formatYYYYMMddHHmmss(long timeInMilliseconds) {
        return formator.format(timeInMilliseconds);
    }


    /**
     * 格式化时间字符串 类似于这样 2015-12-22
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String formatYYYYMMdd(long timeInMilliseconds) {
        return yyyyMMddFormator.format(timeInMilliseconds);
    }


    /**
     * 格式化时间字符串 类似于这样 2015-12-22 23:21
     *
     * @param timeInMilliseconds
     * @return
     */
    public static String formatYYYYMMddHHmm(long timeInMilliseconds) {
        return yyyyMMddHHmmFormator.format(timeInMilliseconds);
    }


    /**
     * 获取时间
     *
     * @param timeOfDay
     * @return
     */
    public static String getReadableTimeOfDay(long timeOfDay) {
        final long hour = timeOfDay / Const.HOUR;
        final long minute = (timeOfDay % Const.HOUR) / Const.MINUTE;
        return String.format(Locale.US, "%02d:%02d", hour, minute);
    }

    /**
     * 获取今日零点的时间戳
     *
     * @return 今日零时的时间戳
     */
    public static long getTodayZeroTimeStamp() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000 * 1000;
    }

    public static long getZeroTimeStamp(long time) {
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    /**
     * 根据格力高历获取这一天的的毫秒值
     *
     * @param date
     * @return
     */
    public static long getTimeInMillisSecondsFromFormatGregorian(String date) {
        String[] dates = date.split("-");
        String year = dates[0];
        String month = dates[1];
        String day = dates[2];

        try {
            return yyyyMMddFormator.parse(year + "-" + (Integer.parseInt(month) + 1) + "-" + day).getTime();
        } catch (ParseException e) {
            ALog.e("Get time in millis seconds from gregorian", e);
        }
        return 0;
    }


    /**
     * 根据格毫秒值获取格式化的格力高的格式化日期
     *
     * @param date
     * @return
     */
    public static String getFormatGregorianFromTimeInMillisSeconds(long date) {
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取目标时间和当前的时间,有多少天
     *
     * @param targetTimeMilliseconds 目标的时间
     * @return day
     */
    public static int days(long targetTimeMilliseconds) {
        long remain = targetTimeMilliseconds - System.currentTimeMillis();
        return (int) (remain / DAY);
    }
}
