package com.lovely3x.common.image.crop;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.lovely3x.common.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * 图片裁切界面
 * Created by lovely3x on 17/3/3.
 */
public class CropImageActivity extends com.theartofdev.edmodo.cropper.CropImageActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "CropImageActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        findViewById(R.id.iv_crop_image_activity_rotate).setOnClickListener(this);
        findViewById(R.id.tv_crop_image_activity_save).setOnClickListener(this);
        findViewById(R.id.iv_crop_image_activity_back).setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));
            tintManager.setStatusBarDarkMode(false, this);
        }

    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.iv_crop_image_activity_rotate) {
            rotateImage(-getOptions().rotationDegrees);
        } else if (id == R.id.tv_crop_image_activity_save) {
            cropImage();
        } else if (id == R.id.iv_crop_image_activity_back) {
            onBackPressed();
        }
    }
}
