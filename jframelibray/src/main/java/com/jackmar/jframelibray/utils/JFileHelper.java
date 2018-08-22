/**
 * Android存储路径分 SDCard,手机内存
 * <p>
 * 手机内存一般存在/data/data/package/files目录下
 */
package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.os.Environment;

import com.jackmar.jframelibray.http.exception.BaseException;

import java.io.File;
import java.text.DecimalFormat;


/**
 * Android操作文件工具类
 * <p>
 * Created by luojiang on 2015/9/17.
 */
public class JFileHelper {

    /**
     * 检测 SD 是否可写
     *
     * @return
     */
    public static boolean sdcardIsReadyForWrite() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 检测 SD 是否可读
     *
     * @return
     */
    public static boolean sdcardIsReadyForRead() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SDCard的路径
     *
     * @return
     */
    public static String getSDCardPath() {
        if (!sdcardIsReadyForWrite()) {
            throw new BaseException("SDCard不是可读写模式");
        }

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

        return sdcard;

    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     *
     * @author luojiang
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 在SDCard 创建目录 或文件
     * <p>
     * 如果存在同名文件则删除文件后再创建,同名文件夹不做任何操作
     *
     * @param path 文件夹相对路径
     * @throws BaseException
     */
    public static String createFileOnSDCard(String path) {
        if (!sdcardIsReadyForWrite()) {
            throw new BaseException("SD卡不可写");
        }

        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getSDCardPath() + path);

        if (file.isFile() && file.exists()) {
            file.delete();
        }

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new BaseException("文件/文件夹" + path + "创建失败");
            }
        }

        return getSDCardPath() + path;
    }

    /**
     * 删除SDCard文件或文件夹
     *
     * @param path 文件路径
     */
    public static void deleteFileOnSDCard(String path) {
        if (!sdcardIsReadyForWrite()) {
            throw new BaseException("SD卡不可写");
        }

        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getSDCardPath() + path);

        if (file.isFile() && file.exists()) {
            if (!file.delete()) {
                throw new BaseException("删除文件" + path + "失败");
            }
        }
    }

    /**
     * 获取data/data/package/files目录
     * <p>
     * 该目录在卸载程序时自动删除
     *
     * @param context
     * @return
     */
    public static String getDataFolderPath(Context context) {

        String sdcard = context.getFilesDir().getAbsolutePath();

        return sdcard;
    }

    /**
     * 在data/data/package/files创建目录 或文件
     * <p>
     * 如果存在同名文件则删除文件后再创建,同名文件夹不做任何操作
     * <p>
     * 该目录在卸载程序时自动删除
     *
     * @param path 文件夹绝对路径
     * @throws BaseException
     */
    public static String createFileOnDataFolder(Context contenxt, String path) {
        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getDataFolderPath(contenxt) + path);

        if (file.isFile() && file.exists()) {
            file.delete();
        }

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new BaseException("文件/文件夹" + path + "创建失败");
            }
        }

        return getDataFolderPath(contenxt) + path;
    }

    /**
     * 删除data/data/package/files文件或文件夹
     * <p>
     * 该目录在卸载程序时自动删除
     *
     * @param path 文件路径
     */
    public static void deleteFileOnDataFolder(String path) {
        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getSDCardPath() + path);

        if (file.isFile() && file.exists()) {
            if (!file.delete()) {
                throw new BaseException("删除文件" + path + "失败");
            }
        }
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 递归删除指定文件夹下的所有文件（包括该文件夹）
     *
     * @param file
     * @author luojiang
     */
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteAll(f);// 递归删除每一个文件
                f.delete();// 删除该文件夹
            }
            file.delete();
        }
    }

    /**
     * 获取文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileS == 0) {
            fileSizeString = "";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " G";
        }
        return fileSizeString;
    }

    /**
     * 获取文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f) throws Exception {
        long size = 0;
        if (f.isDirectory()) {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                File file = flist[i];
                if (file.isDirectory()) {
                    size = size + getFileSize(file);
                } else {
                    size = size + file.length();
                }
            }
        } else {
            size = f.length();
        }
        return size;
    }

    /**
     * 获取文件MB
     *
     * @param path
     * @return
     */
    public static int getMB(String path) {
        int mb = 0;
        try {
            File file = new File(path);
            long size = getFileSize(file);
            mb = (int) (size / 1048576);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mb;
    }

    /**
     * 获取文件KB
     *
     * @param path
     * @return
     */
    public static int getKB(String path) {
        int mb = 0;
        try {
            File file = new File(path);
            long size = getFileSize(file);
            mb = (int) (size / 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mb;
    }

}
