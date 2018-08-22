package com.jackmar.jframelibray.fileutils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 文件工具
 * Created by lovely3x on 15-11-14.
 */
public class FileUtils {


    /**
     * 复制文件
     *
     * @param srcFile    源文件
     * @param dstFile    目标文件
     * @param bufferSize 缓冲区大小
     * @param needClose  是否需要关闭
     * @return 是否复制成功
     */
    public static boolean copy(File srcFile, File dstFile, int bufferSize, boolean needClose) {
        boolean result = true;
        try {
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(dstFile);
            result = StreamUtils.copy(fis, fos, bufferSize, needClose);
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 复制文件 使用默认的缓冲区大小 8 * 1024
     *
     * @param srcFile   源文件
     * @param dstFile   目标文件
     * @param needClose 是否需要关闭
     * @return 是否复制成功
     */
    public static boolean copy(File srcFile, File dstFile, boolean needClose) {
        boolean result = true;
        try {
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(dstFile);
            result = StreamUtils.copy(fis, fos, 8 * 1024, needClose);
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制文件
     *
     * @param srcFilePath 源文件路径
     * @param dstFilePath 目标文件路径
     * @param bufferSize  缓冲区大小
     * @param needClose   是否需要关闭
     * @return 是否复制成功
     */
    public static boolean copy(String srcFilePath, String dstFilePath, int bufferSize, boolean needClose) {
        boolean result = true;
        try {
            FileInputStream fis = new FileInputStream(srcFilePath);
            FileOutputStream fos = new FileOutputStream(dstFilePath);
            result = StreamUtils.copy(fis, fos, bufferSize, needClose);
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制文件 使用默认的缓冲区大小 8 * 1024
     *
     * @param srcFilePath 源文件路径
     * @param dstFilePath 目标文件路径
     * @param needClose   是否需要关闭
     * @return 是否赋值成功
     */
    public static boolean copy(String srcFilePath, String dstFilePath, boolean needClose) {
        boolean result = true;
        try {
            FileInputStream fis = new FileInputStream(srcFilePath);
            FileOutputStream fos = new FileOutputStream(dstFilePath);
            result = StreamUtils.copy(fis, fos, 1024 * 8, needClose);
        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除文件或文件夹,会递归删除
     *
     * @param file 需要删除的文件夹或文件
     */
    public static void delete(File file) {
        if (file != null) {
            if (file.isFile()) {
                file.delete();
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        delete(f);
                    }
                }
            }
        }
    }

    /**
     * 复制我的数据库数据到外部存储器
     * 外部存储器 dbs 里面存放的就是数据库数据
     *
     * @param context 上下文
     */
    public void copyMyDatabasesToExternal(Context context) {
        File[] files = new File(context.getCacheDir().getParentFile(), "databases").listFiles();
        File external = new File(Environment.getExternalStorageDirectory(), "dbs");
        FileUtils.delete(external);
        external.mkdirs();
        for (File f : files) {
            FileUtils.copy(f, new File(external, f.getName()), true);
        }
    }

    /**
     * 后去文件(夹)的大小
     *
     * @param file 需要获取的文件对象
     * @return 文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    long len = getFileSize(f);
                    if (len != -1) size += len;
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

}
