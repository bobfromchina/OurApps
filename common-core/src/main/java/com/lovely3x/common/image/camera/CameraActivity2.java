package com.lovely3x.common.image.camera;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.lovely3x.cameraview.GoogleCameraActivity;
import com.lovely3x.common.beans.Img;
import com.lovely3x.common.image.displayer.ImagesDisplyerActivity;
import com.lovely3x.common.image.displayer.ImgBrowserPagerAdapter;
import com.lovely3x.common.utils.ViewUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.ArrayList;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * 相机拍摄界面
 * 用来拍摄照片，并返回给调用者
 * Created by lovely3x on 17/2/26.
 */
public class CameraActivity2 extends GoogleCameraActivity {

    public static final int ANIM_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SwipeBackLayout layout = new SwipeBackLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.attachToActivity(this);

        getIvConfirm().setAlpha(0F);
        getIvImg().setAlpha(0F);
    }

    @Override
    protected void playImageDismissAnim() {

        ObjectAnimator imgAnim = ObjectAnimator.ofFloat(getIvImg(), View.ALPHA.getName(), getIvImg().getAlpha(), 0);
        ObjectAnimator confirmAnim = ObjectAnimator.ofFloat(getIvConfirm(), View.ALPHA.getName(), getIvConfirm().getAlpha(), 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(imgAnim, confirmAnim);
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.start();
    }

    @Override
    protected void playImageTakenComingAnim() {

        ObjectAnimator imgAnim = ObjectAnimator.ofFloat(getIvImg(), View.ALPHA.getName(), getIvImg().getAlpha(), 1);
        ObjectAnimator confirmAnim = ObjectAnimator.ofFloat(getIvConfirm(), View.ALPHA.getName(), getIvConfirm().getAlpha(), 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(imgAnim, confirmAnim);
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.start();
    }

    @Override
    public void onImageSaved(File file) {
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath()), getIvImg(), new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(ViewUtils.dp2pxF(50))).build());
    }

    @Override
    public void onImageClicked(File file) {
        ArrayList<Img> arrays = new ArrayList<>();
        arrays.add(new Img(ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_FILE, ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath())));

        Intent intent = new Intent(this, ImagesDisplyerActivity.class);

        intent.putParcelableArrayListExtra(ImagesDisplyerActivity.EXTRA_IMAGES, arrays);
        intent.putExtra(ImagesDisplyerActivity.EXTRA_INDEX, 0);

        startActivity(intent);

    }

    @Override
    public void onConfirmClicked(File file) {
        if (file != null && file.exists()) {
            Intent intent = new Intent();
            intent.setData(Uri.fromFile(file));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
