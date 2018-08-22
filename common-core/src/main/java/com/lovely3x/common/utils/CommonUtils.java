package com.lovely3x.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lovely3x.common.consts.Const;


import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * 一些常用的工具方法
 * Created by lovely3x on 15-9-22.
 */
public class CommonUtils {

    /**
     * 电话号码正则表达式
     */
    private static final Pattern PHONE_RE = Pattern.compile("^1[34578]\\d{9}$");

    /**
     * @param string        需要处理的字符串
     * @param start         处理开始的位置
     * @param end           处理结束的位置
     * @param replaceLength 星号区域的长度,-1表示和替换的区域长度相同
     * @return 处理之后的字符串
     */
    public static String getAsteriskString(String string, int start, int end, int replaceLength) {
        if (TextUtils.isEmpty(string) || string.length() < 11) {
            return string;
        }
        int asteriskLen = end - start + 1;
        asteriskLen = replaceLength != -1 ? replaceLength : asteriskLen;
        StringBuilder asterisk = new StringBuilder();
        for (int i = 0; i < asteriskLen; i++) asterisk.append('*');
        StringBuilder sb = new StringBuilder().append(string);
        sb.replace(start, end, asterisk.toString());
        return sb.toString();
    }

    /**
     * 将指定的时间转换为 多少天多少时 例如 12日30天6时
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {

        long second = 1;//one SECOND
        long minute = second * 60;//one MINUTE
        long hour = minute * 60;//one HOUR
        long day = hour * 24;//one day
        String result;
        time /= 1000;
        if (time < minute) {
            result = String.format(Locale.US, "%d秒", time / second);
        } else if (time < hour) {
            int m = (int) (time / minute);
            int s = (int) ((time % minute) / second);
            result = String.format(Locale.US, "%d分%d秒", m, s);
        } else if (time < day) {
            int h = (int) (time / hour);
            int m = (int) ((time % hour) / minute);
            int s = (int) ((time % hour % minute) / second);
            result = String.format(Locale.US, "%d时%d分%d秒", h, m, s);
        } else {
            int d = (int) (time / day);
            int h = (int) ((time % day) / hour);
            int m = (int) (time % day % hour / minute);//
            int s = (int) ((time % day % hour % minute) / second);
            result = String.format(Locale.US, "%d天%d时%d分%d秒", d, h, m, s);
        }
        return result;
    }

    /**
     * 获取堆内存大小
     *
     * @param context 上下文
     * @return 堆内存大小
     */
    public static int getHeapMemorySize(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return manager.getMemoryClass() * 1024 * 1024;
    }

    public static void smoothScrollToPosition(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    /**
     * 判断一个字段是否能够修改
     *
     * @param field 需要判断的字段
     * @return
     */
    public static boolean fieldCanBeModify(Field field) {
        return field != null && ((field.getModifiers() & Modifier.FINAL) == 0);
    }

    /**
     * 获取uri的名字部分
     *
     * @param uri 需要获取的uri
     * @return 名字
     */
    public static String getUriName(String uri) {
        if (!TextUtils.isEmpty(uri)) {
            if (uri.contains("/")) {
                int index = uri.lastIndexOf("/");
                if (index < uri.length() - 2) {
                    uri = uri.substring(index + 1, uri.length());
                }
            }
        }
        return uri;
    }

    /**
     * 解析一个uri的路径，包含scheme
     * 作用是将一个uri中encode的字符decode
     *
     * @param uri 需要解析的文件uri
     * @return 解析完成的字符串
     */
    public static String parsePath(Uri uri) {
        return "file://".concat(new File(uri.getPath()).getAbsolutePath());
    }

    /**
     * application是否运行在前台
     *
     * @param context 上下文
     * @return true or false
     */
    public static boolean appIsRunningForeground(Context context) {
        ActivityManager acm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> aps = acm.getRunningAppProcesses();
        for (int i = 0; i < aps.size(); i++) {
            ActivityManager.RunningAppProcessInfo rap = aps.get(i);
            //当前程序，
            if (rap.pid == android.os.Process.myPid() && (
                    rap.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                            rap.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
                    ) {
                return true;//前台运行
            }
            ALog.d("appIsRunningForground ", String.valueOf(rap));
        }
        return false;
    }

    /**
     * 获取当前进程的名字
     *
     * @param context 上下文，访问ActivityManager服务
     * @return null 或者当前进程名
     */
    public static String getProcessName(Context context) {
        ActivityManager acm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> rapis = acm.getRunningAppProcesses();
        if (rapis != null) {
            for (ActivityManager.RunningAppProcessInfo rapi : rapis) {
                if (rapi.pid == Process.myPid()) {
                    return rapi.processName;
                }
            }
        }
        return null;
    }

    /**
     * 是否是移动手机号码
     *
     * @param phone 需要判断的移动手机号码
     * @return true or false
     */
    public static boolean isMobilePhone(String phone) {
        return phone != null && PHONE_RE.matcher(phone).find();
    }

    /**
     * 判断给定的字符串是否是正确的密码
     *
     * @param str 需要判断的字符串
     * @return
     */
    public static boolean isCorrectPassword(String str) {
        return isCorrectPassword(str, true);
    }

    /**
     * 判断给定的字符串是否是正确的密码
     *
     * @param str        需要判断的字符串
     * @param digitsAble 是否只能是数字
     * @return
     */
    public static boolean isCorrectPassword(String str, boolean digitsAble) {
        return !TextUtils.isEmpty(str) && (!digitsAble || !TextUtils.isDigitsOnly(str)) && str.length() >= Const.PASSWORD_MIN_LEN && str.length() <= Const.PASSWORD_MAX_LEN;
    }

    /**
     * 是否是正确的身份证号码
     *
     * @param idCardNumber 需要判断的身份证号码
     * @return true or false
     */
    public static boolean isCorrectIDCardNumber(String idCardNumber) {
        return !TextUtils.isEmpty(idCardNumber) && Pattern.matches("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])", idCardNumber);
    }

    /**
     * 根据生日获取年龄
     *
     * @param timeTimeMillis
     * @return
     */
    public static int getAge(long timeTimeMillis) {
        Calendar flightCal = Calendar.getInstance();
        flightCal.setTimeInMillis(System.currentTimeMillis());
        Calendar birthCal = Calendar.getInstance();
        birthCal.setTimeInMillis(timeTimeMillis);

        int y = flightCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
        int m = flightCal.get(Calendar.MONTH) - birthCal.get(Calendar.MONTH);
        int d = flightCal.get(Calendar.DATE) - birthCal.get(Calendar.DATE);


        if (y < 0) {
            return 0;
        }

        if (m < 0) {
            y--;
        }

        if (m >= 0 && d < 0) {
            y--;
        }

        return y;
    }

    /**
     * 应用指定路径的字体效果到指定的TextView
     *
     * @param textView 需要应用字体效果的textView
     * @param fontPath 字体的路径，字体存放在 asset 下
     */
    public static void applyFont(TextView textView, String fontPath) {
        Typeface fontFace = Typeface.createFromAsset(textView.getContext().getAssets(), fontPath);
        textView.setTypeface(fontFace);
    }


    /**
     * 给我们好评
     *
     * @param context 上下文
     * @return 异常对象 如果异常步为null则表示启动市场成功，否则则为失败的原因
     */
    public static Exception giveMeSugar(Context context) {
        try {
            String pName = context.getPackageName();
            Uri uri = Uri.parse(String.format("market://details?id=%s", pName));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    /**
     * 比较两个对象
     */
    public static <V extends Comparable> int compare(final V v, final V v2) {
        if (v == null) {
            if (v2 == null) {
                return 0;
            }
            return -1;
        } else {
            if (v2 == null) {
                return 1;
            }
            return v.compareTo(v2);
        }
    }

    /**
     * 判断 两个对象是否相等
     */
    public static boolean isEquals(final Object o, final Object o2) {
        if (o != o2) {
            if (o != null) {
                if (o.equals(o2)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 将对象转换为string 如果对象为null 使用 空串代替
     */
    public static String nullStrToEmpty(final Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof String) {
            return (String) o;
        }
        return o.toString();
    }

    /**
     * 将Integer array转换为int array
     */
    public static int[] transformIntArray(final Integer[] array) {
        final int[] array2 = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }

    /**
     * Int Integer array
     */
    public static Integer[] transformIntArray(final int[] array) {
        final Integer[] array2 = new Integer[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }

    /**
     * 将Long array转换为long array
     */
    public static long[] transformLongArray(final Long[] array) {
        final long[] array2 = new long[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }

    /**
     * 将long array转换为Long array
     */
    public static Long[] transformLongArray(final long[] array) {
        final Long[] array2 = new Long[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i];
        }
        return array2;
    }

    public static Object[] transformIntToObjectArr(int[] arr) {
        if (arr == null) return null;
        Object[] obj = new Object[arr.length];
        for (int index = 0; index < arr.length; index++) {
            int i = arr[index];
            obj[index] = i;
        }
        return obj;
    }

    /**
     * 判断字段是否可以修改
     */
    public static boolean fieldIsInvalid(Field field) {
        return (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) || field.isSynthetic();
    }

    /**
     * 在指定的类和超类中获取指定的字段名
     *
     * @param clazz     需要查找的类
     * @param fieldName 需要查找的字段名
     * @return null 或查找到的字段
     */
    public static Field getFieldUntilObject(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClazz = clazz.getSuperclass();
            if (superClazz != Object.class) {
                return getFieldUntilObject(superClazz, fieldName);
            }
            return null;
        }
    }

    /**
     * 从manifest中读取meta数据
     *
     * @param context 上下文
     * @param metaKey 元素key
     * @return null或读取到的数据
     */
    public static String readDataFromMeta(Context context, String metaKey) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return String.valueOf(appInfo.metaData.get(metaKey));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建指定的类对象实例
     *
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T> List<T> createRangeData(Class<T> clazz, int value) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < value; i++) {
            try {
                list.add(clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    public static double getScreenSizeOfDevice(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        double x = Math.pow(width, 2);
        double y = Math.pow(height, 2);
        double diagonal = Math.sqrt(x + y);

        int dens = dm.densityDpi;
        double screenInches = diagonal / (double) dens;

        return screenInches;
    }

    /**
     * 获取actionBar高度
     *
     * @param context 获取actionBar的高度
     * @return
     */
    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarSize = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarSize = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarSize;
    }


    public static String[] getSupportABIS() {
        String[] abis = new String[]{};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        return abis;
    }

    public static boolean supportArm() {
        String[] abis = getSupportABIS();
        for (String abi : abis) {
            if (abi != null && abi.toUpperCase().contains("ARM")) return true;
        }
        return false;
    }

    public static boolean supportX86() {
        String[] abis = getSupportABIS();
        for (String abi : abis) {
            if (abi != null && abi.toUpperCase().contains("X86")) return true;
        }
        return false;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight =  context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}