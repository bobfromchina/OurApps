package com.lovely3x.common.utils.storage;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 存储工具
 * Created by lovely3x on 16-1-13.
 */
public class StorageUtils {

    private static final StorageUtils INSTANCE = new StorageUtils();
    private Context mContext;

    /**
     * 初始化检查
     */
    private void initCheck() {
        if (mContext == null) throw new IllegalStateException("Must call init method on before.");
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        if (context == null) throw new IllegalArgumentException("Context can't be null.");
        this.mContext = context;
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static StorageUtils getInstance() {
        return INSTANCE;
    }

    /**
     * 获取私有文件
     *
     * @param fileName 文件名
     * @return
     */
    public File getPrivateFile(String fileName) {
        initCheck();
        File file = new File(mContext.getFilesDir(), fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取一个公共缓存文件对象
     *
     * @param fileName 获取的缓存的文件的名字
     * @return null或缓存文件对象
     */
    public File getPublicCacheFile(String fileName) {
        initCheck();
        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir == null) return null;
        File cacheDir = new File(externalDir, mContext.getApplicationContext().getPackageName() + ".caches");
        if (!cacheDir.exists()) cacheDir.mkdirs();
        File file = new File(cacheDir, fileName);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
