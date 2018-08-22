package com.lovely3x.common.versioncontroller.impls;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.versioncontroller.ApplicationUtils;
import com.lovely3x.common.versioncontroller.Result;
import com.lovely3x.common.versioncontroller.Version;
import com.lovely3x.common.versioncontroller.VersionControllerManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lovely3x on 15-8-25.
 * sample like this
 * <p/>
 * <application>
 * <!-- 版本名 -->
 * <versionName>11.0.0</versionName>
 * <!-- 版本号 -->
 * <versionCode>11</versionCode>
 * <!-- 更新的内容 -->
 * <content>
 * 修护内容：分享黑底、侧滑菜单的条目高度、商品详情查看大图白底变成黑底、添加收货地址提示文字颜色变浅！注：如本处不更新，登陆后可在消息中选择更新
 * </content>
 * <!-- 新版本发布时间 -->
 * <date>2014-12-30 22:15:45</date>
 * <!-- 是否需要强制更新,如不更新则无法使用! -->
 * <force>false</force>
 * <package>com.lovely3x.app</package>
 * <!-- 下载地址 -->
 * <address>http://192.168.1.132:8080/MiLi/version/milis.apk</address>
 * </application>
 * <p/>
 * 默认的版本更新实现
 */
public class VersionControllerManagerService extends VersionControllerManager {


    private static final long SETUP_VALUE = 1024 * 80;
    private static final String TAG = "VersionControllerManagerService";
    /**
     * 下载进度监听器
     */
    private DownloadProgressListener mDownloadListener;

    /*
     * 设置检查状态变化监听器
     */
    private CheckStateChangedListener mCheckStateChangedListener;

    /**
     * 版本检查url
     */
    private String versionCheckUrl;

    /**
     * 文件下载地址路劲
     */
    private String mFileDownloadPath;

    private Call call;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) try {
            call.cancel();
        } catch (Exception e) {
        }
    }

    @Override
    public Version getVersionFromService() {
        try {
            Thread.sleep(2000);
            return ApplicationUtils.parseVersion(new OkHttpClient()
                    .newCall(new Request.Builder().url(getVersionFileURL()).build()).execute().body().byteStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本检查静态文件下载地址
     *
     * @return 获取地址
     */
    public String getVersionFileURL() {
        return versionCheckUrl;
    }


    /**
     * 设置版本检查文件地址
     *
     * @param versionCheckUrl 地址
     */
    public void setVersionFileURL(String versionCheckUrl) {
        this.versionCheckUrl = versionCheckUrl;
    }


    @Override
    public void check() {

        if (!checkerRunning()) {
            super.check();
            if (mCheckStateChangedListener != null) mCheckStateChangedListener.onCheck();
        }
    }

    private void postProgress(final long max, final long progress) {
        if (Config.DEBUG) ALog.d(TAG, "download progress " + progress * 1.0f / max);
        if (mDownloadListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadListener.doProgress((int) max, (int) progress);
                    mDownloadListener.doProgress(1.0f * progress / max);
                }
            });
        }
    }

    /**
     * 设置文件下载地址
     *
     * @param absPath 文件保存的绝对路径
     */
    public void setFileDownloadPath(String absPath) {
        this.mFileDownloadPath = absPath;
    }


    @Override
    public Result download(final Version remoteVersion) {
        if (mDownloadListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadListener.onStart();
                }
            });
        }

        if (mCheckStateChangedListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCheckStateChangedListener.onObtaining();
                }
            });
        }

        File outFile = new File(mFileDownloadPath);
        final Result result = new Result(Uri.fromFile(outFile).toString());
        Version localFileVersion = null;
        if (outFile.exists() &&
                (localFileVersion = ApplicationUtils.getArchivePackageVersion(this, outFile.getPath())) != null &&
                localFileVersion.getPackageName().equalsIgnoreCase(remoteVersion.getPackageName()) &&
                localFileVersion.getVersionCode() == remoteVersion.getVersionCode()) {
            if (mDownloadListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadListener.onSuccessful(result);
                    }
                });
            }
            return result;
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            call = okHttpClient.newCall(new Request.Builder()
                    .url(remoteVersion.getUrl())
                    .method("GET", null).build());
            Response response = call.execute();
            String value = response.header("Content-Length");
            long length = -1;
            if (value != null && value.length() > 0) {
                length = Long.parseLong(value);
            }

            InputStream stream = response.body().byteStream();
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buf = new byte[1024 * 8];
            long progress = 0;
            long postProgress = 0;

            int len;
            while ((len = stream.read(buf)) != -1) {
                if (getInstance() == null) return null;
                fos.write(buf, 0, len);
                progress += len;
                if (progress - postProgress >= SETUP_VALUE) {
                    postProgress = progress;
                    postProgress(length, progress);
                }
            }
            fos.close();
            stream.close();
            if (mDownloadListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadListener.onSuccessful(result);
                    }
                });
            }
            return result;
        } catch (final IOException e) {
            e.printStackTrace();
            if (mDownloadListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadListener.onError(e);
                    }
                });
            }
            throw new RuntimeException(e);
        }
    }


    @Override
    public void isLatestedVersion() {
        super.isLatestedVersion();
        if (mCheckStateChangedListener != null) mCheckStateChangedListener.onLatestVersion();
    }

    @Override
    public void onError(final Throwable throwable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCheckStateChangedListener != null)
                    mCheckStateChangedListener.onError(throwable);
            }
        });
        super.onError(throwable);
    }

    @Override
    public boolean compareVersion(Version localVersion, Version remoteVersion) {
        if (localVersion.getPackageName().equals(remoteVersion.getPackageName())) {
            return localVersion.getVersionCode() < remoteVersion.getVersionCode();
        }
        return false;
    }

    @Override
    public void update(Result result) {
        super.update(result);
        if (mCheckStateChangedListener != null) mCheckStateChangedListener.onObtained(result);
    }

    @Override
    public void foundNewerVersion(Version localVersion, final Version remoteVersion) {
        if (mCheckStateChangedListener != null)
            mCheckStateChangedListener.onNewVersionFond(remoteVersion);
    }

    /**
     * 设置下载进度监听器
     *
     * @param listener 需要设置的监听器
     */
    public void setDownloadProgressListener(@Nullable DownloadProgressListener listener) {
        this.mDownloadListener = listener;
    }

    /**
     * 设置检查状态变化监听器
     *
     * @param listener 监听器对象
     */
    public void setCheckStateChangedListener(@Nullable CheckStateChangedListener listener) {
        this.mCheckStateChangedListener = listener;
    }
}
