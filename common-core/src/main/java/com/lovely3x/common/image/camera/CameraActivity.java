package com.lovely3x.common.image.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.CommonActivity;
import com.lovely3x.common.image.displayer.ImgBrowserPagerAdapter;
import com.lovely3x.common.utils.StorageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;


public class CameraActivity extends CommonActivity implements CameraView.OnCameraSelectListener,
        View.OnClickListener {

    private Tint mDefaultTint;

    @Override
    public Tint getTint() {
        if (mDefaultTint == null) {
            mDefaultTint = new Tint(Color.parseColor("#FF202020"), 0, true, false);
        }
        return mDefaultTint;
    }

    /**
     * 相机权限请求
     */
    private static final int REQUEST_PERMISSION_CAMERA = 10023;

    DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.ARGB_8888)
            .imageScaleType(ImageScaleType.EXACTLY).considerExifParams(false)
            .cacheInMemory(false).cacheOnDisk(false).displayer(new FadeInBitmapDisplayer(0)).build();
    private CameraView cameraView;
    private RelativeLayout rlTop;

    private ImageButton ib_camera_change;
    private ImageButton ib_camera_flash;
    private ImageButton ib_camera_grid;
    private ImageButton ibTakePicture;

    private ImageView imgGrid;
    private ImageView imgPreview;
    private ImageView imgConfirm;
    /**
     * 拍摄的图片的路径
     */
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                initCamera();
            else
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        } else {
            initCamera();
        }
        initViews();
        showDCIM();
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        try {
            cameraView = new CameraView(this);
            cameraView.setDirPath(StorageUtils.getApplicationCacheDir(this).getAbsolutePath());
            cameraView.setOnCameraSelectListener(this);
            cameraView.setFocusView((FocusView) findViewById(R.id.sf_focus));
            cameraView.setCameraView((SurfaceView) findViewById(R.id.sf_camera), CameraView.MODE16T9);//4,3
            cameraView.setPicQuality(80);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_PERMISSION_CAMERA == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            initCamera();
        else finish();
    }

    private void initViews() {
        rlTop = $(R.id.rl_top);
        ib_camera_change = $(R.id.ib_camera_change);
        ib_camera_flash = $(R.id.ib_camera_flash);
        ib_camera_grid = $(R.id.ib_camera_grid);
        ibTakePicture = $(R.id.ib_camera_take_picture);
        imgGrid = $(R.id.img_grid);
        imgPreview = $(R.id.iv_activity_camera_preview);
        imgConfirm = $(R.id.iv_activity_camera_confirm);
    }

    private <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }


    /**
     * get first picture DCIM
     */
    private void showDCIM() {
        String columns[] = new String[]{
                MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DISPLAY_NAME
        };
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        boolean isOK = false;
        if (cursor != null) {
            cursor.moveToLast();
            String path = "";
            try {
                while (!isOK) {
                    int photoIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(photoIndex);
                    isOK = path.contains("DCIM/Camera"); //Is thie photo from DCIM folder ?
                    cursor.moveToPrevious(); //Add this so we don't get an infinite loop if the first image from
                    //the cursor is not from DCIM
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
            // ImageLoader.getInstance().displayImage("file://" + path, ibCameraPhotos, options);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ib_camera_change.setOnClickListener(this);
        ib_camera_flash.setOnClickListener(this);
        ib_camera_grid.setOnClickListener(this);
        ibTakePicture.setOnClickListener(this);
        imgPreview.setOnClickListener(this);
        imgConfirm.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && cameraView != null) {
            cameraView.onResume();
            cameraView.setTopDistance(rlTop.getHeight());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null)
            cameraView.onPause();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ib_camera_change) {
            cameraView.changeCamera();

        } else if (i == R.id.ib_camera_flash) {
            cameraView.changeFlash();

        } else if (i == R.id.ib_camera_grid) {
            if (imgGrid.getVisibility() == View.VISIBLE) {
                imgGrid.setVisibility(View.GONE);
                ib_camera_grid.setBackgroundResource(R.drawable.camera_grid_normal);
            }
            ib_camera_grid.setBackgroundResource(R.drawable.camera_grid_press);
            imgGrid.setVisibility(View.VISIBLE);

        } else if (i == R.id.ib_camera_take_picture) {
            deleteFileIfNeed();//拍摄前先删除
            cameraView.takePicture(false);

        } else if (i == R.id.iv_activity_camera_preview) {
            onPreviewClicked();
        } else if (i == R.id.iv_activity_camera_confirm) {
            onConfirmClicked();

        }
    }

    private void onConfirmClicked() {
        File file = null;
        if (mFilePath == null || !(file = new File(mFilePath)).exists()) {
        } else {
            Intent intent = new Intent();
            intent.setData(Uri.fromFile(file));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 预览图片被点击后执行
     */
    private void onPreviewClicked() {
        if (mFilePath != null && new File(mFilePath).exists()) {
            showImg(mFilePath, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_FILE, R.drawable.icon_loading, R.drawable.icon_loading_failure);
        }
    }

    @Override
    public void onShake(int orientation) {
        // you can rotate views here
    }

    @Override
    public void onTakePicture(final boolean success, final String filePath) {
        if (success) {
            this.mFilePath = filePath;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgConfirm.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage("file://" + filePath, imgPreview, options);
                }
            });
        }
    }

    @Override
    public void onChangeFlashMode(int flashMode) {
        switch (flashMode) {
            case CameraView.FLASH_AUTO:
                ib_camera_flash.setBackgroundResource(R.drawable.camera_flash_auto);
                break;
            case CameraView.FLASH_OFF:
                ib_camera_flash.setBackgroundResource(R.drawable.camera_flash_off);
                break;
            case CameraView.FLASH_ON:
                ib_camera_flash.setBackgroundResource(R.drawable.camera_flash_on);
                break;
        }
    }

    @Override
    public void onChangeCameraPosition(int camera_position) {

    }

    /**
     * 删除已经拍摄的文件
     */
    private void deleteFileIfNeed() {
        if (mFilePath != null) {
            File file = new File(mFilePath);
            if (file.exists()) {
                file.delete();
            }
        }
        imgPreview.setImageBitmap(null);
    }

    @Override
    public void onBackPressed() {
        if (imgBrowser == null) {
            deleteFileIfNeed();
        }
        System.gc();
        super.onBackPressed();
    }
}
