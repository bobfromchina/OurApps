package com.lovely3x.common.versioncontroller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.versioncontroller.impls.CheckStateChangedListener;
import com.lovely3x.common.versioncontroller.impls.UpdateAlertDialog;
import com.lovely3x.common.versioncontroller.impls.VersionControllerManagerService;

import java.lang.ref.WeakReference;

/**
 * 后台版本更新检查器
 * <p/>
 * Created by lovely3x on 16-1-20.
 */
public class BackgroundVersionChecker implements CheckStateChangedListener {

    /**
     * activity 引用
     */
    private static final String TAG = "BackgroundVersionChecker";
    private static WeakReference<FragmentActivity> outClassRef;
    private static BackgroundVersionChecker INSTANCE = new BackgroundVersionChecker();


    /**
     * 是否正在检查
     * 如果正在检查，那么不再执行检查
     */
    private boolean isChecking;
    /**
     * 下载地址
     */
    private String mDownloadPath;
    /**
     * 版本检查服务器地址
     */
    private String mCheckURL;

    /**
     * @param activity
     * @return
     */
    public static BackgroundVersionChecker getInstance(FragmentActivity activity) {
        outClassRef = new WeakReference<>(activity);
        return INSTANCE;
    }


    /**
     * 获取实例
     *
     * @return 获取后台版本检查器
     */
    public BackgroundVersionChecker getINSTANCE() {
        return INSTANCE;
    }

    /**
     * 开始在后台检查更新
     * 发现新版本会启动一个对话框，并提醒用户更新
     *
     * @param checkURL     版本检查的地址
     * @param downloadPath 下载后文件保存地址
     */
    public void check(final String downloadPath, final String checkURL) {
        outClassRefCheck();
        if (!isChecking) {
            isChecking = true;
            this.mDownloadPath = downloadPath;
            this.mCheckURL = checkURL;
            VersionControllerManager vcm = VersionControllerManagerService.getInstance();
            if (vcm == null) {
                Context context = outClassRef.get();
                Intent intent = new Intent(context, VersionControllerManagerService.class);
                context.startService(intent);
            }
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        VersionControllerManagerService versionControllerManager = null;
                        do {
                            versionControllerManager = (VersionControllerManagerService) VersionControllerManagerService.getInstance();
                        } while (versionControllerManager == null);
                        versionControllerManager.setCheckStateChangedListener(BackgroundVersionChecker.this);
                        versionControllerManager.setVersionFileURL(checkURL);
                        versionControllerManager.setFileDownloadPath(downloadPath);
                        versionControllerManager.check();
                    } catch (Exception e) {
                        if (Config.DEBUG) {
                            ALog.w(TAG, e);
                        }

                    }/* finally {
                        isChecking = false;
                    }*/
                }
            }).start();
        }
    }

    /**
     * 外部类引用
     * 如果外部类引用类丢失会抛出异常
     */
    private void outClassRefCheck() {
        if (outClassRef.get() == null) throw new IllegalStateException();
    }

    @Override
    public void onCheck() {

    }

    @Override
    public void onNewVersionFond(Version version) {
        isChecking = false;
        //   VersionControllerManagerService.getInstance().setCheckRunning(false);
        UpdateAlertDialog dialog = new UpdateAlertDialog();
        dialog.setmNewVersion(version);
        dialog.setInitiativeToCheck(false);
        dialog.setFileDownloadPath(mDownloadPath);
        dialog.setCheckFileURL(mCheckURL);
        dialog.show(outClassRef.get().getFragmentManager(), String.format("%s%s", this.getClass().getName(), dialog.getClass().getName()));
    }

    @Override
    public void onLatestVersion() {
        isChecking = false;
        VersionControllerManagerService.getInstance().stopSelf();
    }

    @Override
    public void onObtaining() {

    }

    @Override
    public void onObtained(Result result) {

    }

    @Override
    public void onError(Throwable throwable) {
        isChecking = false;
        VersionControllerManagerService.getInstance().stopSelf();
    }
}
