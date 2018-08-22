package com.lovely3x.common.image.displayer;

import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.CommonActivity;
import com.lovely3x.common.beans.Img;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.CommonUtils;
import com.lovely3x.common.utils.fileutils.FileUtils;
import com.lovely3x.common.utils.fileutils.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 调用这个界面需要传递几个必要的参数
 * <br/>
 * {@link #EXTRA_IMAGES }
 * <br/>
 * {@link #EXTRA_INDEX }
 * <br/>
 * 图片显示界面
 * <br/>
 * Created by lovely3x on 15-12-8.
 */
public class ImagesDisplyerActivity extends CommonActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    /**
     * 默认选择的下标
     * 请使用{@link Intent#putExtra(String, int)}
     */
    public static final String EXTRA_INDEX = "extra.index";
    /**
     * 图片集合
     * 请使用{@link Intent#putParcelableArrayListExtra(String, ArrayList)}
     * 放置数据
     */
    public static final String EXTRA_IMAGES = "extra.images";
    private static final String TAG = "ImagesDisplyerActivity";
    private ViewPager mViewPager;
    private FrameLayout mTitleBar;
    /**
     * 文件保存路径
     * 默认在图片目录下
     */
    private File fileCacheDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private boolean mTitleIsHidden;
    private int mIndex;
    /**
     * 图片集合
     */
    private ArrayList<Img> mImages;
    /**
     * 该图片的描述信息
     */
    private TextView tvDesc;
    /**
     * 标题，用于显示 0/0 这个的
     */
    private TextView tvTitle;
    private Tint mDefaultTint;
    private ImgBrowserPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_gallery);
        this.mViewPager = ButterKnife.findById(this, R.id.vp_activity_common_gallery);
        mViewPager.setSoundEffectsEnabled(false);
        mViewPager.addOnPageChangeListener(this);
        ButterKnife.findById(this, R.id.view_common_gallery_title_bar_back).setOnClickListener(this);
        ButterKnife.findById(this, R.id.view_common_gallery_title_bar_save).setOnClickListener(this);
        mTitleBar = ButterKnife.findById(this, R.id.fl_activity_common_gallery_title_bar);
        tvDesc = ButterKnife.findById(this, R.id.tv_activity_common_gallery_desc);
        tvTitle = ButterKnife.findById(this, R.id.view_common_gallery_title_bar_title);
        Bundle bundle = getIntent().getExtras();

        mImages = bundle.getParcelableArrayList(EXTRA_IMAGES);
        mIndex = bundle.getInt(EXTRA_INDEX, 0);

        if (Config.DEBUG) {
            ALog.d(TAG, "mIndex == " + mIndex + " , mImages == " + mImages);
        }

        if (mImages != null && mIndex < mImages.size()) {
            onPageSelected(mIndex);
            showImgs();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.view_common_gallery_title_bar_back) {
            onBackPressed();
        } else if (i == R.id.view_common_gallery_title_bar_save) {
            onSaveClicked(v);
        }
    }

    /**
     * 显示图片
     */
    protected void showImgs() {
        mAdapter = new ImgBrowserPagerAdapter(this, mImages);
        mAdapter.setOnItemClicked(new ImgBrowserPagerAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                onImgClicked(position);
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mIndex);
    }


    /**
     * 当保存按钮被点击后执行
     *
     * @param view 被点击的这个视图
     */
    protected void onSaveClicked(View view) {
        if (mAdapter != null && mViewPager != null) {
            int currentItem = mViewPager.getCurrentItem();
            if (currentItem < mAdapter.getCount()) {
                Img item = mAdapter.getImages().get(currentItem);
                if (item != null) {
                    switch (item.getImgType()) {
                        case ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_ASSERT:
                            saveAssetImage(item);
                            break;
                        case ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP:
                            saveBitmap(item);
                            break;
                        case ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_FILE:
                            saveFile(item);
                            break;
                        case ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL:
                            saveURL(item);
                            break;
                    }
                }
            }
        }
    }

    /**
     * 保存asset文件
     *
     * @param img
     */
    protected void saveAssetImage(Img img) {
        final AssetManager manager = getAssets();
        String fileName = img.getName();
        final Uri uri = Uri.parse(img.getImg());
        if (TextUtils.isEmpty(fileName)) fileName = uri.getPath();
        final File f = new File(fileCacheDir, fileName);
        if (f.exists()) {//file already exists
            new AlertDialog.Builder(this).setTitle(R.string.alert).setMessage(R.string.img_alread_exists_you_want_to_overwite)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InputStream is = null;
                            try {
                                is = manager.open(uri.getPath());
                                boolean isSuccessful = StreamUtils.copy(is, new FileOutputStream(f), 1024 * 8, true);
                                showToast(isSuccessful ? R.string.save_successful : R.string.save_failure);
                            } catch (IOException e) {
                                e.printStackTrace();
                                showToast(R.string.save_failure);
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        } else {
            InputStream is = null;
            try {
                is = manager.open(uri.getPath());
                boolean isSuccessful = StreamUtils.copy(is, new FileOutputStream(f), 1024 * 8, true);
                showToast(isSuccessful ? R.string.save_successful : R.string.save_failure);
            } catch (IOException e) {
                e.printStackTrace();
                showToast(R.string.save_failure);
            }
        }
    }

    /**
     * 保存bitmap图片
     *
     * @param img 需要保存的图片对象
     */
    protected void saveBitmap(Img img) {
        final Bitmap bm = img.getImgBitmap();

        String fileName = img.getName();
        if (TextUtils.isEmpty(fileName))
            fileName = String.valueOf(System.currentTimeMillis()).concat(".jpg");

        final File f = new File(fileCacheDir, fileName);
        if (bm != null) {
            if (f.exists()) {//file already exists
                new AlertDialog.Builder(this).setTitle(R.string.alert).setMessage(R.string.img_alread_exists_you_want_to_overwite)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileOutputStream fos = null;
                                try {
                                     fos = new FileOutputStream(f);
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                    StreamUtils.close(fos);
                                    showToast(R.string.save_successful);
                                } catch (Exception e) {
                                    showToast(R.string.save_failure);
                                }finally {
                                    if (fos != null){
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, null).show();
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    StreamUtils.close(fos);
                    showToast(R.string.save_successful);
                } catch (Exception e) {
                    showToast(R.string.save_failure);
                }
            }
        }
    }

    private long realDownload(String path, String fileCacheDir, String fileName) {
        DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 发出通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        // 设置下载后文件存放的位置
        request.setDestinationInExternalPublicDir(fileCacheDir, fileName);
        request.setTitle(getString(R.string.app_name));
        return manager.enqueue(request);
    }

    /**
     * 保存图片
     *
     * @param img
     */
    protected void saveURL(final Img img) {
        String name = img.getName();
        if (TextUtils.isEmpty(name)) {
            name = CommonUtils.getUriName(img.getImg());
        }
        if (new File(fileCacheDir, name).exists()) {
            final String finalName = name;
            new AlertDialog.Builder(this).setTitle(R.string.alert).setMessage(R.string.img_alread_exists_you_want_to_overwite)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            realDownload(img.getImg(), fileCacheDir.getName(), finalName);
                            showToast(R.string.downloading_please_wait_a_mount);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        } else {
            realDownload(img.getImg(), fileCacheDir.getName(), name);
            showToast(R.string.downloading_please_wait_a_mount);
        }
    }

    /**
     * 保存文件
     *
     * @param img 需要保存的图片对象
     */
    protected void saveFile(final Img img) {
        String fileName = img.getName();
        final Uri uri = Uri.parse(img.getImg());
        if (TextUtils.isEmpty(fileName)) fileName = new File(uri.getPath()).getName();
        final File f = new File(fileCacheDir, fileName);
        if (f.exists()) {//file already exists
            new AlertDialog.Builder(this).setTitle(R.string.alert).setMessage(R.string.img_alread_exists_you_want_to_overwite)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtils.copy(new File(uri.getPath()), f, true);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        } else {
            FileUtils.copy(new File(uri.getPath()), f, true);
        }
    }


    @Override
    protected void onImgClicked(int position) {
        if (mTitleIsHidden) {
            mTitleIsHidden = false;
            showTitle();
        } else {
            mTitleIsHidden = true;
            hiddenTitle();
        }
    }

    /**
     * 显示标题栏
     */
    protected void showTitle() {
        ObjectAnimator translationIn = ObjectAnimator.ofFloat(mTitleBar, View.TRANSLATION_Y, -mTitleBar.getMeasuredHeight(), 0);
        translationIn.setDuration(200);
        translationIn.setInterpolator(new DecelerateInterpolator());
        translationIn.start();
    }

    /**
     * 隐藏标题栏
     */
    protected void hiddenTitle() {
        ObjectAnimator translationOut = ObjectAnimator.ofFloat(mTitleBar, View.TRANSLATION_Y, 0, -mTitleBar.getMeasuredHeight());
        translationOut.setDuration(200);
        translationOut.setInterpolator(new DecelerateInterpolator());
        translationOut.start();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (tvTitle != null && mImages != null && position < mImages.size()) {
            Img img = mImages.get(position);
            if (TextUtils.isEmpty(img.getDesc())) {
                //没有任何描述时，不显示这个控件
                //因为使用了padding的，但是颜色不是纯透明
                //所以必须设置不可见
                tvDesc.setText("");
                tvDesc.setVisibility(View.GONE);
            } else {
                //有描述时显示这个控件
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(mImages.get(position).getDesc());
            }
            tvTitle.setText(String.format("%d/%d", position + 1, mImages.size()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public Tint getTint() {
        if (mDefaultTint == null) {
            mDefaultTint = new Tint(Color.parseColor("#FF202020"), 0, true, false);
        }
        return mDefaultTint;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            long downId = bundle.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri filepath = downloadManager.getUriForDownloadedFile(downId);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                if (filepath != null) {
                    Toast.makeText(context, "下载成功,文件保存在:" + filepath.getPath(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
}
