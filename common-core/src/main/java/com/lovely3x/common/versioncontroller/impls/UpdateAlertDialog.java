package com.lovely3x.common.versioncontroller.impls;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.CommonActivity;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.versioncontroller.Result;
import com.lovely3x.common.versioncontroller.Version;
import com.lovely3x.common.versioncontroller.VersionControllerManager;

import java.io.File;


/**
 * Created by lovely3x on 15-8-27.
 * 更新对话框
 */
public class UpdateAlertDialog extends DialogFragment implements DownloadProgressListener, CheckStateChangedListener, View.OnClickListener, DialogInterface.OnKeyListener {

    private static final String TAG = "UpdateAlertDialog";

    /**
     * 通知id
     */
    private static final int ID_NOTIFY = 0x8754;
    private static final int REQUEST_CODE_LAUNCH_INSTALL = 0x39;
    /**
     * 内容容器
     */
    FrameLayout flContentContainer;
    /**
     * 标题
     */
    TextView tvTitle;
    /**
     * 左边的按钮
     */
    TextView btnNegative;
    /**
     * 右边的按钮
     */
    TextView btnPositive;
    private Activity mActivity;
    private Dialog mDialog;
    private VersionControllerManagerService mUpdateService;
    /**
     * 服务器返回的新版本对象
     */
    private Version mNewVersion;
    /**
     * 下载结果
     */
    private Result mDownloadResult;
    /**
     * 是否是运行在后台的
     */
    private boolean runningBackground;

    private NotificationManager mNotificationManager;

    private String mVersionCheckFileURL;

    private String mFilDownloadPath;
    private String downloadFilePathKey = "downloadFilePathKey";
    private String versionCheckFileURLKey = "versionCheckFileURLKey";

    /**
     * 启动这个dialog是否主动检查，默认为 true
     */
    private boolean initiativeToCheck = true;

    public boolean isInitiativeToCheck() {
        return initiativeToCheck;
    }

    public void setInitiativeToCheck(boolean initiativeToCheck) {
        this.initiativeToCheck = initiativeToCheck;
    }

    @Override
    public void onAttach(Activity activity) {
        this.mActivity = activity;
        super.onAttach(activity);
    }

    public Version getmNewVersion() {
        return mNewVersion;
    }

    public void setmNewVersion(Version mNewVersion) {
        this.mNewVersion = mNewVersion;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        Fragment old = manager.findFragmentByTag(tag);
        if (old != null) return;
        super.show(manager, tag);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFilDownloadPath = savedInstanceState.getString(downloadFilePathKey);
            mVersionCheckFileURL = savedInstanceState.getString(versionCheckFileURLKey);
        }
        mDialog = new Dialog(mActivity, R.style.transparentDialog);
        mDialog.setContentView(R.layout.dialog_update_alert);
        btnNegative = (TextView) mDialog.findViewById(R.id.tv_dialog_update_alert_negative);
        btnPositive = (TextView) mDialog.findViewById(R.id.tv_dialog_update_alert_postive);
        tvTitle = (TextView) mDialog.findViewById(R.id.tv_dialog_update_alert_title);

        flContentContainer = (FrameLayout) mDialog.findViewById(R.id.fm_dialog_update_alert_content);
        btnNegative.setOnClickListener(this);
        btnPositive.setOnClickListener(this);

        mDialog.setOnKeyListener(this);

        getNotificationManager().cancel(ID_NOTIFY);
        adjustDisplayByServiceState();
        startCheck();
        return mDialog;
    }

    /**
     * 调整显示
     */
    private void adjustDisplayByServiceState() {
        if (VersionControllerManagerService.getInstance() != null) {
            if (VersionControllerManagerService.getInstance().checkerRunning()) {
                switch (VersionControllerManagerService.getInstance().getState()) {
                    case checking:
                        displayCheckingView();
                        break;
                    case checkFailure:
                        displayCheckErrorViews();
                        break;
                    case isLatest:
                        displayIsLatestedVersionView();
                        break;
                    case waitingConfirm:
                        displayFoundNewVersionView(VersionControllerManagerService.getInstance().getRemoteVersion());
                        break;
                    case downloading:
                        displayDownloadingView();
                        break;
                    case downloadFailure:
                        displayDownloadErrorViews();
                        break;
                    case downloaded://
                        displayDownloadSuccessfulView();
                        break;
                    case none:
                        displayCheckingView();
                        break;
                }
            } else {
                displayCheckingView();
            }
        } else {
            displayCheckingView();
        }
    }


    public void startCheck() {

        if (VersionControllerManagerService.getInstance() == null) {
            final Intent intent = new Intent(mActivity, VersionControllerManagerService.class);
            new Thread() {
                @Override
                public void run() {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.startService(intent);
                        }
                    });
                    while (VersionControllerManagerService.getInstance() == null) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            ALog.e(TAG, e);
                        }
                    }
                    attachListenerAndCheck();
                }
            }.start();
        } else {
            attachListenerAndCheck();
        }
    }

    /**
     * 为更新服务添加监听器并启动检查
     */
    private void attachListenerAndCheck() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUpdateService = (VersionControllerManagerService) VersionControllerManagerService.getInstance();
                mUpdateService.setDownloadProgressListener(UpdateAlertDialog.this);
                mUpdateService.setCheckStateChangedListener(UpdateAlertDialog.this);

                //主动检查才去检查
                if (initiativeToCheck) {
                    mUpdateService.setVersionFileURL(mVersionCheckFileURL);
                    mUpdateService.setFileDownloadPath(mFilDownloadPath);
                    mUpdateService.check();
                }
            }
        });
    }

    /**
     * 获取通知管理器
     *
     * @return
     */
    protected NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @Override
    public void doProgress(int max, int progress) {
        ALog.e(TAG, max + ", " + progress);
        if (runningBackground) {
            Notification notification = new NotificationCompat
                    .Builder(mActivity)
                    .setContentTitle(mActivity.getString(R.string.downloading))
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setProgress(max, progress, false).build();
            getNotificationManager().notify(ID_NOTIFY, notification);
        }

    }

    /**
     * 显示错误检查视图
     */
    public void displayCheckErrorViews() {
        flContentContainer.removeAllViews();
        //设置右边的按钮可见
        btnPositive.setVisibility(View.VISIBLE);
        ///设置左边的按钮为 '取消'
        btnPositive.setText(R.string.retry);


        //设置左边的按钮为可见
        btnNegative.setVisibility(View.VISIBLE);

        ///设置左边的按钮为 '取消'
        btnNegative.setText(R.string.cancel);

        //设置标题为 '检查更新'
        tvTitle.setText(R.string.version_check);

        //将属于检查更更新的视图添加到对话框中
        mActivity.getLayoutInflater().inflate(R.layout.view_update_check_failure_content, flContentContainer, true);
    }


    @Override
    public void doProgress(float progressPercent) {
        ALog.e(TAG, "progresspercent" + progressPercent);

    }

    /**
     * 显示正在检查的视图
     */
    protected void displayCheckingView() {
        flContentContainer.removeAllViews();
        //设置右边的按钮不可见
        btnPositive.setVisibility(View.GONE);

        //设置左边的按钮为可见
        btnNegative.setVisibility(View.VISIBLE);
        ///设置左边的按钮为 '取消'
        btnNegative.setText(R.string.cancel);

        //设置标题为 '检查更新'
        tvTitle.setText(R.string.version_check);
        //将属于检查更更新的视图添加到对话框中
        mActivity.getLayoutInflater().inflate(R.layout.view_update_checking_content, flContentContainer, true);
    }

    /**
     * 显示发信新版本视图
     *
     * @param version 找到的新版本
     */
    protected void displayFoundNewVersionView(Version version) {
        flContentContainer.removeAllViews();
        //设置右边的按钮为可见
        btnPositive.setVisibility(View.VISIBLE);
        //设置右边的按钮的文字为 '更新'
        btnPositive.setText(R.string.update);
        //设置左边的按钮为可见
        btnNegative.setVisibility(View.VISIBLE);
        //设置左边文字为 '取消'
        btnNegative.setText(R.string.cancel);

        tvTitle.setText(mActivity.getString(R.string.found_new_version).concat(" ").concat(version.getVersionName()));
        mActivity.getLayoutInflater().inflate(R.layout.view_update_found_new_version_content, flContentContainer, true);
        TextView tvContent = (TextView) flContentContainer.findViewById(R.id.tv_view_update_found_new_version_content);
        tvContent.setText(version.getDescription());
    }

    /**
     * 显示最新版本视图
     */
    protected void displayIsLatestedVersionView() {
        flContentContainer.removeAllViews();
        //设置右边的按钮为不可见
        btnPositive.setVisibility(View.GONE);
        //设置左边的按钮为可见
        btnNegative.setVisibility(View.VISIBLE);
        //设置左边文字为 '确认'
        btnNegative.setText(R.string.confirm);

        tvTitle.setText(R.string.version_check);
        mActivity.getLayoutInflater().inflate(R.layout.view_update_is_latested_content, flContentContainer, true);
    }

    /**
     * 显示下载进度视图
     */
    protected void displayDownloadingView() {
        flContentContainer.removeAllViews();
        //设置右边的按钮为可见
        btnPositive.setVisibility(View.VISIBLE);
        //设置右边的按钮的文字为 '后台'
        btnPositive.setText(R.string.background);
        //设置左边的按钮为可见
        btnNegative.setVisibility(View.VISIBLE);
        //设置左边文字为 '取消'
        btnNegative.setText(R.string.cancel);
        //设置标题为 '下载中'
        tvTitle.setText(mActivity.getString(R.string.downloading));
        mActivity.getLayoutInflater().inflate(R.layout.view_update_downloading_content, flContentContainer, true);
    }


    /**
     * 显示下载成功的视图
     */
    protected void displayDownloadSuccessfulView() {
        flContentContainer.removeAllViews();
        //设置右边的按钮为可见
        btnPositive.setVisibility(View.VISIBLE);
        //设置右边的按钮的文字为 '安装'
        btnPositive.setText(R.string.install);

        //设置左边的按钮为不可见
        btnNegative.setVisibility(View.GONE);
        //设置左边文字为 '删除'
        btnNegative.setText(R.string.delete);

        //设置标题为 '下载完成'
        tvTitle.setText(mActivity.getString(R.string.download_successful));
        mActivity.getLayoutInflater().inflate(R.layout.view_update_download_successful_content, flContentContainer, true);
    }


    @Override
    public void onCheck() {
        displayCheckingView();
        ALog.e(TAG, "onCheck");
    }

    @Override
    public void onNewVersionFond(Version version) {
        this.mNewVersion = version;
        ALog.e(TAG, "onNewVersionFond");
        displayFoundNewVersionView(version);
    }

    @Override
    public void onLatestVersion() {
        ALog.e(TAG, "onLatestVersion");
        displayIsLatestedVersionView();
    }

    public void setCheckFileURL(String url) {
        this.mVersionCheckFileURL = url;
    }

    /**
     * 设置文件下载地址
     *
     * @param path 文件的绝对路径
     */
    public void setFileDownloadPath(String path) {
        this.mFilDownloadPath = path;
    }


    @Override
    public void onObtaining() {
        ALog.e(TAG, "onObtaining");
        if (runningBackground) {
            Notification notification = new NotificationCompat.Builder(mActivity)
                    .setContentTitle(mActivity.getString(R.string.downloading)).setSmallIcon(R.drawable.ic_launcher).setProgress(100, 0, false).build();
            getNotificationManager().notify(ID_NOTIFY, notification);
        } else {
            displayDownloadingView();
        }
    }

    @Override
    public void onObtained(Result result) {
        ALog.e(TAG, "onObtained");
        if (runningBackground) {
            String uri = mDownloadResult == null ? VersionControllerManagerService.getInstance().getResult().getURL() : mDownloadResult.getURL();

            File apkPath = new File(Uri.parse(uri).getPath());
            Intent installIntent = ClickNotificationToInstallActivity.makeLaunchIntent(mActivity, apkPath);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent installPendingIntent =
                    PendingIntent.getActivity(mActivity, REQUEST_CODE_LAUNCH_INSTALL,
                            installIntent, 0);
            Notification notification = new NotificationCompat
                    .Builder(mActivity)
                    .setContentTitle(mActivity.getString(R.string.download_successful))
                    .setProgress(1, 1, false)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(installPendingIntent)
                    .build();
            getNotificationManager().notify(ID_NOTIFY, notification);
        } else {
            this.mDownloadResult = result;
            mUpdateService.setCheckRunning(false);
            displayDownloadSuccessfulView();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        ALog.e(TAG, throwable);
        mUpdateService.setCheckRunning(false);
        VersionControllerManager instance = VersionControllerManagerService.getInstance();
        if (instance != null) {
            VersionControllerManager.State errorState = instance.getState();
            if (errorState == VersionControllerManager.State.checkFailure) {
                displayCheckErrorViews();
            } else {
                displayDownloadErrorViews();
            }
        } else {
            displayDownloadErrorViews();
        }
    }

    /**
     * 显示下载失败视图
     */
    private void displayDownloadErrorViews() {
        flContentContainer.removeAllViews();
        //设置右边的按钮可见
        btnPositive.setVisibility(View.VISIBLE);
        ///设置左边的按钮为 '取消'
        btnPositive.setText(R.string.retry);


        //设置左边的按钮为可见
        btnNegative.setVisibility(View.VISIBLE);

        ///设置左边的按钮为 '取消'
        btnNegative.setText(R.string.cancel);

        //设置标题为 '检查更新'
        tvTitle.setText(R.string.version_check);

        //将属于检查更更新的视图添加到对话框中
        mActivity.getLayoutInflater().inflate(R.layout.view_update_download_failure_content, flContentContainer, true);
    }

    @Override
    public void onSuccessful(Result result) {
        ALog.e(TAG, "onSuccessful");
    }


    /**
     * 安装application
     */
    protected void installApplication() {
        startActivity(getInstallIntent());
    }

    private Intent getInstallIntent() {
        if (mDownloadResult == null)
            mDownloadResult = VersionControllerManagerService.getInstance().getResult();
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setDataAndType(Uri.parse(mDownloadResult.getURL()), "application/vnd.android.package-archive");
        return promptInstall;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_dialog_update_alert_negative) {//左边的按钮
            String text = btnNegative.getText().toString();
            if (text.equals(mActivity.getString(R.string.cancel))) {//取消
                //如果正在下载,需要停止下载
                stopService();
                //关闭对话框
                dismiss();
            } else if (text.equals(mActivity.getString(R.string.confirm))) {//确认,已经是最新版本了
                stopService();
                dismiss();
            }
        } else if (v.getId() == R.id.tv_dialog_update_alert_postive) {//右边的按钮
            String text = btnPositive.getText().toString();
            if (text.equals(mActivity.getString(R.string.update))) {//更新
                mUpdateService.confirmDownload(mNewVersion);
            } else if (text.equals(mActivity.getString(R.string.install))) {//安装
                installApplication();
                stopService();
                dismiss();
            } else if (text.equals(mActivity.getString(R.string.background))) {//切换到后台运行
                dismiss();
                gotoBackRunning();
            } else if (text.equalsIgnoreCase(mActivity.getString(R.string.retry))) {//重试
                //如果是检查失败
                //则重新检查
                VersionControllerManager.State failState = VersionControllerManagerService.getInstance().getState();
                if (failState == VersionControllerManager.State.checkFailure) {
                    VersionControllerManagerService.getInstance().setCheckRunning(false);
                    attachListenerAndCheck();
                } else if (failState == VersionControllerManager.State.downloadFailure) {//重新下载
                    VersionControllerManagerService.getInstance().confirmDownload(VersionControllerManagerService.getInstance().getRemoteVersion());
                }
            }
        }
    }

    /**
     * 停止服务
     */
    public void stopService() {
        //停止服务
        boolean hasStop = mActivity.stopService(new Intent(mActivity, VersionControllerManagerService.class));
        ALog.e(TAG, "hasStop == " + hasStop);
    }

    /**
     * 切换到后台运行(下载)
     */
    protected void gotoBackRunning() {
        this.runningBackground = true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(downloadFilePathKey, mFilDownloadPath);
        outState.putString(versionCheckFileURLKey, mVersionCheckFileURL);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

        //如果用户点击了返回键 没有通过对话框操作
        //如果当前正在检查,则取消检查
        //如果当前正在下载,将转换到后台下载
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (VersionControllerManagerService.getInstance() != null) {
                VersionControllerManager.State state = VersionControllerManagerService.getInstance().getState();
                if (state == VersionControllerManager.State.none ||
                        state == VersionControllerManager.State.waitingConfirm ||
                        state == VersionControllerManager.State.downloadFailure ||
                        state == VersionControllerManager.State.downloaded ||
                        state == VersionControllerManager.State.checking ||
                        state == VersionControllerManager.State.checkFailure ||
                        state == VersionControllerManager.State.isLatest) {
                    dismiss();
                    stopService();
                    //下载中
                } else if (state == VersionControllerManager.State.downloading) {
                    dismiss();
                    gotoBackRunning();
                }
            }
            return true;
        }
        return false;
    }
}
