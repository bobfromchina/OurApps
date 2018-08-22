package com.lovely3x.common.versioncontroller.impls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;

import com.lovely3x.common.BuildConfig;

import java.io.File;

/**
 * 由于在通知栏必须显式的指定跳转的activity
 * 所以就必须要有一个跳台
 * Created by lovely3x on 15-8-28.
 */
public class ClickNotificationToInstallActivity extends Activity {


    public static void launchMe(Context activity, File apkFile) {
        Intent intent = makeLaunchIntent(activity, apkFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static Intent makeLaunchIntent(Context activity, File apkFile) {

        Intent intent = new Intent(activity, ClickNotificationToInstallActivity.class);
        //Fix Nougat 'file://' bug.
        if (Build.VERSION.SDK_INT >= 24) {//Nougat
            intent.setData(FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", apkFile));
        } else {
            intent.setData(Uri.fromFile(apkFile));
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();

        try {
            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
            promptInstall.setDataAndType(data, "application/vnd.android.package-archive");
            startActivity(promptInstall);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (VersionControllerManagerService.getInstance() != null)
            VersionControllerManagerService.getInstance().stopSelf();
        finish();
    }

}
