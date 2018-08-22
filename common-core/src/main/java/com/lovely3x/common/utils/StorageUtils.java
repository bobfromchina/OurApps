package com.lovely3x.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by lovely3x on 15-9-11.
 * 存储管理工具
 */
public class StorageUtils {

    /**
     * 是否存在外部存储
     *
     * @return true or false
     */
    public static boolean hasExternalStorage() {
        String state = Environment.getExternalStorageState();
        return !Environment.MEDIA_REMOVED.equals(state);
    }

    /**
     * 外部存储是否是可用的
     *
     * @return true or false
     */
    public static boolean externalStorageIsAvailable() {
        if (hasExternalStorage()) {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }
        return false;
    }

    /**
     * 获取外部存储文件夹
     *
     * @return null 或 外部文件对象
     */
    public static File getExternalDir() {
        return Environment.getExternalStorageDirectory();
    }


    /**
     * 根据传递的文件名 在外部存储根目录创建一个文件对象
     *
     * @param fileName 文件名
     * @return null 或file对象
     */
    public static File getExternalFile(String fileName) {
        if (externalStorageIsAvailable()) {
            return new File(getExternalDir(), fileName);
        }
        return null;
    }

    /**
     * 获取程序缓存文件夹
     *
     * @param context 上下文对象
     * @return 程序缓存文件夹对象
     */
    public static File getApplicationCacheDir(Context context) {
        File external = context.getExternalCacheDir();
        return external == null ? context.getCacheDir() : external;
    }
}
