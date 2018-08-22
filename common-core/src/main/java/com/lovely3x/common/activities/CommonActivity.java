package com.lovely3x.common.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lovely3x.common.CommonApplication;
import com.lovely3x.common.R;
import com.lovely3x.common.beans.Img;
import com.lovely3x.common.dialogs.LoadingProgressDialog;
import com.lovely3x.common.image.camera.CameraActivity;
import com.lovely3x.common.image.camera.CameraActivity2;
import com.lovely3x.common.image.crop.CropImageActivity;
import com.lovely3x.common.image.displayer.ImagesDisplyerActivity;
import com.lovely3x.common.image.displayer.ImgBrowserPagerAdapter;
import com.lovely3x.common.image.picker.ImagePickerListActivity;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.BaseWeakHandler;
import com.lovely3x.common.utils.CompatUtils;
import com.lovely3x.common.utils.ConnectivityReceiver;
import com.lovely3x.common.utils.Event;
import com.lovely3x.common.utils.StorageUtils;
import com.lovely3x.common.utils.ViewUtils;
import com.lovely3x.common.utils.storage.JSONFile;
import com.lovely3x.common.versioncontroller.BackgroundVersionChecker;
import com.lovely3x.common.versioncontroller.impls.UpdateAlertDialog;
import com.lovely3x.jsonparser.model.JSONKey;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * 通用activity界面
 * Created by lovely3x on 15-8-15.
 */
public abstract class CommonActivity extends SwipeBackActivity implements Event, OnDismissListener, OnReshowListener, CommonApplication.ApplicationStateListener, DialogInterface.OnShowListener {
    /**
     * 图片资源类型 bitmap
     */
    public static final int IMG_SOURCE_TYPE_BITMAP = ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP;

    /**
     * 图片资源类型 网络地址
     */
    public static final int IMG_SOURCE_TYPE_URL = ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL;
    /**
     * 图片资源类型 文件地址
     */
    public static final int IMG_SOURCE_TYPE_FILE = ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_FILE;

    /**
     * 图片资源类型 assert文件资源
     */
    public static final int IMG_SOURCE_TYPE_ASSERT = ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_ASSERT;


    /**
     * act启动次数记录文件
     */
    private static final String ACT_LAUNCH_TIMES_RECORD = "act_launch_times";

    /**
     * 默认的对话框显示延迟时间
     */
    private static final long DEFAULT_DELAY_TIME = 500;

    public android.os.Handler UI_HANDLER = new android.os.Handler(Looper.getMainLooper());

    /**
     * 默认的图片裁切大小
     */
    public static final int DEFAULT_IMAGE_CROP_WIDTH_AND_HEIGHT = ViewUtils.getWidth();
    /**
     * 记录文件
     */
    public static final String RECORD_SHARE_PREFERENCES = "record.share.preferences";
    /**
     * 程序必须要用到的权限
     */
    public static final List<String> INTEGRANT_PERMISSIONS = new ArrayList<>();
    /**
     * 保存图片选择对话框是否正在显示的key
     */
    private static final String KEY_CHOICE_IMG_DIALOG_IS_SHOWING = "key.choice.img.dialog.is.showing";
    /**
     * 保存图片选择对话框请求id的key
     */
    private static final String KEY_CHOICE_IMG_DIALOG_REQUEST_ID = "key.choice.img.dialog.request.id";
    /**
     * 保存图片选择对话框标题的key
     */
    private static final String KEY_CHOICE_IMG_DIALOG_TITLE = "key.choice.img.dialog.title";
    /**
     * 保存图片选择对话框显示的内容的key
     */
    private static final String KEY_CHOICE_IMG_DIALOG_MESSAGE = "key.choice.img.dialog.message";
    /**
     * 保存请求码集合的key
     */
    private static final String KEY_REQUEST_CODE_TABLE = "key.request.code.table";
    /**
     * 保存是否裁切图片的key
     */
    private static final String KEY_IMG_NEED_CROP = "key.img.need.crop";
    /**
     * 请求权限
     */
    private static final int REQUEST_PERMISSIONS = 0x1239;
    private static final String FOREGROUND_RECORD = "share_pref_record";
    private static final String KEY_IS_FOREGROUND = "key_is_foreground";
    protected String TAG = "CommonActivity";
    protected Context mActivity;
    protected ViewPager imgBrowser;
    /**
     * 记录请求值的code表
     */
    private SparseArray<RequestType> requestCodeTable = new SparseArray<>();

    /**
     * 对话框操作动作队列
     */
    private final LinkedList<Runnable> dialogActionQueue = new LinkedList<>();

    /**
     * 图片选择框是否正在显示
     */
    private boolean mChoiceImgDialogIsShowing;
    /**
     * 图片选择的请求码
     */
    private int mChoiceImgRequestCode = -1;
    /**
     * 图片选择的对话框标题
     */
    private String mChoiceImgDialogTitle;
    /**
     * 图片选择的对话框内容
     */
    private String mChoiceImgDialogMessage;
    /**
     * 图片是否需要crop
     */
    private boolean mChoiceImgNeedCorp;
    /**
     * 图片选择对话框
     */
    private AlertDialog mChoiceImgDialog;
    /**
     * 默认的Tint对象
     */
    private Tint mDefaultTint;
    private InputMethodManager mInputMethod;
    private Toast mToast;
    private ProgressDialogInterface mProgressDialog;
    /**
     * 图片选择监听器列表
     */
    private final List<ImgSelectedListener> imgSelectedListeners = new ArrayList<>();

    /**
     * 进度取消监听器列表
     */
    private final List<ProgressCircleCancelListener> progressCircleCancelListeners = new ArrayList<>();

    /**
     * Activity Result 监听器
     */
    private final List<ActivityResultListener> activityResultListeners = new ArrayList<>();

    /**
     * 连接变化监听器
     */
    private final ConnectivityReceiver.ConnectivityListener CONNECTIVITY_LISTENER = new ConnectivityReceiver.ConnectivityListener() {
        @Override
        public void onConnectivityChanged(boolean hasNetwork, int type) {
            if (hasNetwork) onNetworkConnected(type);
            else onNetworkDisconnected();
        }

        @Override
        public void onHostAccessibilityChanged(boolean isConnected) {
            CommonActivity.this.onHostAccessibilityChanged(isConnected);
        }
    };
    private boolean mFirstEnterAct;
    private final ArrayList<BackPressClickedListener> mBackPressClickedListeners = new ArrayList<>();

    private boolean mWaitingShowDialog;
    private Runnable mWaitingShowDialogRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        //理论上应该放在launcher activity,放在基类中所有集成此库的app都可以避免此问题
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }

        //  设置沉浸式
        getWindow().getDecorView().setFitsSystemWindows(true);
        ActivityManager.launchReply();
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_CREATE);
        mActivity = this;
        if (ActivityManager.getActivities().size() == 1) {//如果仅仅存在一个
            Activity act = ActivityManager.getActivities().get(0);
            try {
                ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_ACTIVITIES);
                if (act != null && info.name.equals(act.getClass().getName())) {
                    onApplicationEnterForeground();
                }
            } catch (PackageManager.NameNotFoundException e) {
                ALog.e(TAG, e);
            }

        }
        restoreRecord();
        Bundle extras = getIntent().getExtras();
        if (extras == null) extras = new Bundle();
        onInitExtras(extras);

        applyTranslationStatusBarAndNavigationBar(getTint());
        if (savedInstanceState != null) restoreInstance(savedInstanceState);
        if (mChoiceImgDialogIsShowing && mChoiceImgRequestCode != -1) {
            mChoiceImgDialogIsShowing = false;
            showChoiceImageDialog(mChoiceImgNeedCorp, mChoiceImgRequestCode, mChoiceImgDialogTitle, mChoiceImgDialogMessage);
        }

        //寻找进度框,如果有
        final String tag = getProgressDialogTagIfIsFragment();
        if (tag != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment instanceof ProgressDialogInterface) {
                ((ProgressDialogInterface) fragment).setDismissListener(this);
                ((ProgressDialogInterface) fragment).setReshowListener(this);
            }
        }

        //注册连接监听器
        ConnectivityReceiver.addConnectivityListener(CONNECTIVITY_LISTENER);

        //Android开发之获取时间SystemClock
        long time = SystemClock.elapsedRealtime();

        //是否是第一次进入
        this.mFirstEnterAct = getLaunchTimes() == 0;

        //添加一次启动次数
        addLaunchTimes(1);

        ALog.d(TAG, "First use judge consumed time " + (SystemClock.elapsedRealtime() - time));

        //第一次进入,回调方法
        if (mFirstEnterAct) onFirstEnterAct();
    }

    /**
     * 是否是首次进入这个act
     *
     * @return true or false
     */
    public boolean isFirstEnterAct() {
        return mFirstEnterAct;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (ActivityManager.isFromBackgroundToForeground(this)) {
            onApplicationEnterForeground();
        }
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_RESTART);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_START);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(this);

        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_STOP);
        android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(2);
        if (runningTasks.size() > 1 && !runningTasks.get(0).topActivity.getPackageName().equals(getPackageName())) {
            onApplicationEnterBackground();
        }
        recordIdentifier();
    }

    @Override
    protected void onDestroy() {
        if (mChoiceImgDialog != null && mChoiceImgDialog.isShowing()) {
            mChoiceImgDialog.cancel();
            mChoiceImgDialog = null;
        }
        ConnectivityReceiver.removeConnectivityListener(CONNECTIVITY_LISTENER);
        super.onDestroy();
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_DESTROY);

        UI_HANDLER.removeCallbacksAndMessages(null);

        ButterKnife.unbind(this);
    }

    /**
     * 当连接到网络后执行
     *
     * @param type 连接的网络的类型
     */
    protected void onNetworkConnected(int type) {
        ALog.d(TAG, "Current network is connected ,connectivity type is " + type);
    }

    /**
     * 当网络连接断开后执行
     */
    protected void onNetworkDisconnected() {
        ALog.d(TAG, "Current network is disconnected.");
    }

    /**
     * 当主机的访问性发生变化后执行
     *
     * @param isConnected 是能够连接到主机
     */
    protected void onHostAccessibilityChanged(boolean isConnected) {
        ALog.d(TAG, String.format("HostAccessibilityChanged[%s]", isConnected));
    }

    /**
     * 等待对话框是否正在显示
     */
    private boolean mProgressDialogIsShowing;

    {
        //INTEGRANT_PERMISSIONS.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        INTEGRANT_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //INTEGRANT_PERMISSIONS.add(Manifest.permission.CALL_PHONE);
        INTEGRANT_PERMISSIONS.add(Manifest.permission.CAMERA);
        INTEGRANT_PERMISSIONS.add(Manifest.permission.SEND_SMS);
        INTEGRANT_PERMISSIONS.add(Manifest.permission.READ_CONTACTS);
    }

    public CommonActivity() {
        TAG = this.getClass().getSimpleName();
    }

    /**
     * 把指定的listView滚动到底部
     * <p>
     * 获取listView实例 去获取到适配器的长度 - 1  就可以滚动到最低部分
     *
     * @param listView 需要滚动的listview
     */
    public static void smoothListToBottom(final ListView listView) {
        if (listView != null) {
            final ListAdapter adapter = listView.getAdapter();
            if (adapter != null) {
                listView.setSelection(adapter.getCount() - 1);
            }
        }
    }

    /**
     * 把指定的listView滚动到顶部
     *
     * @param listView 需要滚动的listview
     */
    public static void smoothListToTop(final ListView listView) {
        if (listView != null) {
            final ListAdapter adapter = listView.getAdapter();
            if (adapter != null) {
                listView.setSelection(0);
            }
        }
    }

    /**
     * 是否显示状态栏和导航栏
     *
     * @param tint tint对象
     */
    public void applyTranslationStatusBarAndNavigationBar(Tint tint) {
        applyTranslationStatusBarAndNavigationBar(tint.mShowStatusBar, tint.mShowNavigationBar, tint.mStatusBarColor, tint.mNavigationBarColor, tint.tint, tint.dark);
    }

    /**
     * 是否显示状态栏和导航栏
     *
     * @param showStatusBar      是否显示状态栏
     * @param showNavigationBar  是否显示导航栏
     * @param navigationBarColor 导航栏颜色
     * @param statusBarColor     状态栏颜色
     */
    public void applyTranslationStatusBarAndNavigationBar(boolean showStatusBar, boolean showNavigationBar, int statusBarColor, int navigationBarColor, boolean tint, boolean darkMode) {
        ALog.d(TAG, "applyTranslationStatusBarAndNavigationBar " +
                " showStatusBar == " + showStatusBar +
                " showStatusBar == " + showStatusBar +
                " showNavigationBar == " + showNavigationBar +
                " statusBarColor == " + statusBarColor +
                " navigationBarColor == " + navigationBarColor
        );

        //    if(true)return;;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

        if (tint) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager;
            if (showStatusBar) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                tintManager = new SystemBarTintManager(this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintColor(statusBarColor);
                tintManager.setStatusBarDarkMode(darkMode, this);
            }

            if (showNavigationBar) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                tintManager = new SystemBarTintManager(this);
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setNavigationBarTintColor(navigationBarColor);
            }
        }
    }

    /**
     * 获取颜色
     *
     * @param colorRes 需要获取的颜色 资源文件
     * @param safe     主要是为了区分和 {@link Context#getColor} 可以随意传递
     * @return 该资源文件对应的颜色值
     */
    public
    @ColorInt
    int getColor(@ColorRes int colorRes, boolean safe) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return getResources().getColor(colorRes, getTheme());
        } else {
            return getResources().getColor(colorRes);
        }
    }

    public Drawable getDrawable(int res, boolean safe) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getDrawable(res);
        } else {
            return getResources().getDrawable(res);
        }
    }


    /**
     * 恢复记录文件
     */
    public void restoreRecord() {
        JSONFile recordFile = new JSONFile(com.lovely3x.common.utils.storage.StorageUtils.getInstance().getPrivateFile(RECORD_SHARE_PREFERENCES));
        Set<JSONKey> keys = recordFile.keySet();
        for (JSONKey key : keys) {
            String[] keyArray = key.getKey().split(":");
            if (keyArray.length == 2) {
                //className:identifier
                if (keyArray[0].equals(getClass().getName())) {
                    Record record = new Record();
                    record.identifier = keyArray[1];
                    record.object = recordFile.get(key).getString();
                    onRecordIdentifier(record);
                }
            }
        }
    }

    /***
     * 权限检查器
     * 会检查当前在{@link #INTEGRANT_PERMISSIONS} 中的权限当前是否已经获取到了
     * 如果没有获取到会尝试区申请
     * 申请的结果会在 {@link #onRequestPermissionsResult(int, String[], int[])} 调用
     */
    public void permissionChecker() {
        //在安卓M 上面我们需要请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> needPermissions = new ArrayList<>();
            for (int i = 0; i < INTEGRANT_PERMISSIONS.size(); i++) {
                //权限
                String permission = INTEGRANT_PERMISSIONS.get(i);
                if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    needPermissions.add(permission);
                }
            }

            //请求权限
            if (!needPermissions.isEmpty()) {
                String[] permissions = new String[needPermissions.size()];
                needPermissions.toArray(permissions);
                requestPermissions(permissions, REQUEST_PERMISSIONS);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Config.DEBUG) {
            ALog.d(TAG, "request permission request: " + Arrays.toString(permissions) + " : " + Arrays.toString(grantResults));
        }
    }

    /**
     * 当第一次进入这个act后执行
     */
    protected void onFirstEnterAct() {
    }

    /**
     * 获取启动次数
     *
     * @return 这个act的启动次数
     */
    protected int getLaunchTimes() {
        return getLaunchTimesRecordSP().getInt(this.getClass().getName(), 0);
    }

    /**
     * 获取启动次数记录文件
     *
     * @return 记录文件
     */
    protected SharedPreferences getLaunchTimesRecordSP() {
        return getSharedPreferences(ACT_LAUNCH_TIMES_RECORD, MODE_PRIVATE);
    }

    /**
     * 添加启动次数
     *
     * @param times 需要添加的启动次数
     */
    protected void addLaunchTimes(int times) {
        SharedPreferences sp = getLaunchTimesRecordSP();
        sp.edit().putInt(this.getClass().getName(), getLaunchTimes() + times).apply();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ActivityManager.launchReply();
        ActivityManager.updateState(this, ActivityManager.ACTIVITY_STATE_ON_CREATE);
    }

    /**
     * 获取用于当前界面着色的描述对象
     * 这个方法会在 当前activity的window获得焦点后执行,
     * 所以不推荐在这个方法中直接返回一个新建的对象
     * × 不缓存这个对象的原因是这样可以使得用户自由的变化这个Tint
     *
     * @return Tint对象
     */
    public Tint getTint() {
        if (mDefaultTint == null) {
            mDefaultTint = new Tint(getColor(R.color.colorPrimaryDark, true), 0, true, false);
        }
        mDefaultTint.mStatusBarColor = getColor(R.color.colorPrimaryDark, true);
        return mDefaultTint;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //ALog.exception(TAG, "onWindowFocusChanged :  " + hasFocus);
        if (hasFocus) {
            //  ALog.d(TAG, "start call applyTranslationStatusBarAndNavigationBar");
            applyTranslationStatusBarAndNavigationBar(getTint());
            //ALog.d(TAG, "applyTranslationStatusBarAndNavigationBar ended");
//            applyTranslationStatusBarAndNavigationBar(mShowStatusBar, mShowNavigationBar, mStatusBarColor, mNavigationBarColor);
        }
    }

    private void restoreInstance(Bundle savedInstanceState) {
        mChoiceImgDialogIsShowing = savedInstanceState.getBoolean(KEY_CHOICE_IMG_DIALOG_IS_SHOWING);
        mChoiceImgRequestCode = savedInstanceState.getInt(KEY_CHOICE_IMG_DIALOG_REQUEST_ID);
        mChoiceImgDialogTitle = savedInstanceState.getString(KEY_CHOICE_IMG_DIALOG_TITLE);
        mChoiceImgDialogMessage = savedInstanceState.getString(KEY_CHOICE_IMG_DIALOG_MESSAGE);
        requestCodeTable = savedInstanceState.getSparseParcelableArray(KEY_REQUEST_CODE_TABLE);
        mChoiceImgNeedCorp = savedInstanceState.getBoolean(KEY_IMG_NEED_CROP, mChoiceImgNeedCorp);
        requestCodeTable = (requestCodeTable == null) ? new SparseArray<RequestType>() : requestCodeTable;
    }

    /**
     * 记录标志
     */
    private void recordIdentifier() {
        //记录标志
        Record record = getRecordIdentifier();
        if (record != null) {
            JSONFile recordFile = new JSONFile(com.lovely3x.common.utils.storage.StorageUtils.getInstance().getPrivateFile(RECORD_SHARE_PREFERENCES));
            String key = getClass().getName() + ":" + record.identifier;
            recordFile.put(key, record.object);
            recordFile.commit();
        }
    }


    /**
     * 判断当前应用程序是否处于前台
     *
     * @return 是否处于前台
     */
    public boolean isForeground() {
        android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(Integer.MAX_VALUE);
        return runningTasks != null && !runningTasks.isEmpty() && runningTasks.get(0).topActivity.getPackageName().equals(getPackageName());
    }

    /**
     * 添加图片选择监听器
     *
     * @param imgSelectedListener 需要添加的图片选择监听器
     */
    public void addImgSelectedListener(ImgSelectedListener imgSelectedListener) {
        imgSelectedListeners.add(imgSelectedListener);
    }

    /**
     * 移除图片选择监听器
     *
     * @param imgSelectedListener 需要移除的图片选择监听器
     */
    public void removeImgSelectedListener(ImgSelectedListener imgSelectedListener) {
        imgSelectedListeners.remove(imgSelectedListener);
    }

    /**
     * 显示图片
     *
     * @param url            需要显示的url
     * @param loadingImg     加载中的图片
     * @param loadFailureImg 加载失败的图片
     */
    public void showImg(String url, Bitmap loadingImg, Bitmap loadFailureImg) {
        showImg(url, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL, loadingImg, loadFailureImg);
    }

    /**
     * 显示图片
     *
     * @param url                需要显示的uri
     * @param contentRequestType 需要显示的内容的类型,目前支持
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_ASSERT} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_BITMAP} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_FILE} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_URL}
     * @param loadingImg         加载中的图片
     * @param loadFailureImg     加载失败显示的图片
     */
    public void showImg(String url, int contentRequestType, Bitmap loadingImg, Bitmap loadFailureImg) {
        ArrayList<String> list = new ArrayList<>();
        list.add(url);
        showImgs(list, contentRequestType, loadingImg, loadFailureImg);
    }

    /**
     * 显示一组图片
     *
     * @param urls               需要显示的uri
     * @param contentRequestType 需要显示的内容的类型,目前支持
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_ASSERT} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_BITMAP} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_FILE} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_URL}
     * @param loadingImg         加载中显示的图片
     * @param loadingFailureImg  加载失败显示的图片
     */
    public void showImgs(List<String> urls, int contentRequestType, Bitmap loadingImg, Bitmap loadingFailureImg) {
        showImgs(urls, contentRequestType, true, loadingImg, loadingFailureImg, 0);
    }

    /**
     * 显示一组图片
     *
     * @param urls               需要显示的uri
     * @param contentRequestType 需要显示的内容的类型,目前支持
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_ASSERT} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_BITMAP} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_FILE} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_URL}
     * @param loadingImg         加载中显示的图片
     * @param loadingFailureImg  加载失败显示的图片
     * @param select             当前选中的条目
     */
    public void showImgs(List<String> urls, int contentRequestType, Bitmap loadingImg, Bitmap loadingFailureImg, int select) {
        showImgs(urls, contentRequestType, true, loadingImg, loadingFailureImg, select);
    }

    /**
     * 显示图片
     *
     * @param url            需要显示的图片地址
     * @param loadingImg     加载中的图片资源
     * @param loadFailureImg 记载失败的图片资源
     */
    public void showImg(String url, @DrawableRes int loadingImg, @DrawableRes int loadFailureImg) {
        showImg(url, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL, loadingImg, loadFailureImg);
    }

    /**
     * 显示图片
     *
     * @param bm 需要显示的bitmap对象
     */
    public void showImg(Bitmap bm) {
        ArrayList<Bitmap> bms = new ArrayList<>();
        bms.add(bm);
        showImgs(bms, R.drawable.icon_loading_failure);
    }

    public void showImg(String url) {
        showImg(url, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL, R.drawable.icon_loading, R.drawable.icon_loading_failure);
    }

    /**
     * 显示图片
     *
     * @param uri                需要显示的uri
     * @param contentRequestType 需要显示的内容的类型,目前支持
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_ASSERT} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_BITMAP} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_FILE} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_URL}
     * @param loadingImg         加载中的图片资源
     * @param loadFailureImg     加载失败显示的图片资源
     */
    public void showImg(String uri, int contentRequestType, @DrawableRes int loadingImg, @DrawableRes int loadFailureImg) {
        ArrayList<String> list = new ArrayList<>();
        list.add(uri);
        showImgs(list, contentRequestType, true, loadingImg, loadFailureImg, 0);
    }

    /**
     * 显示一组图片
     *
     * @param urls           需要显示的图片网络地址
     * @param loadingImg     加载中显示的图片
     * @param loadFailureImg 加载失败显示的图片
     */
    public void showImgs(List<String> urls, Bitmap loadingImg, Bitmap loadFailureImg) {
        showImgs(urls, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL, loadingImg, loadFailureImg);
    }

    /**
     * /**
     * 显示一组图片
     *
     * @param uris               需要显示的图片的uri's
     * @param contentRequestType 需要显示的内容的类型,目前支持
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_ASSERT} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_BITMAP} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_FILE} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_URL}
     * @param loadingImg         加载中显示的图片资源
     * @param loadFailureImg     加载失败显示的图片资源
     */
    public void showImgs(List<String> uris, int contentRequestType, @DrawableRes int loadingImg, @DrawableRes int loadFailureImg) {
        showImgs(uris, contentRequestType, true, loadingImg, loadFailureImg, 0);
    }

    /**
     * 显示一组图片
     *
     * @param uris               需要显示的图片的uri's
     * @param contentRequestType 需要显示的内容的类型,目前支持
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_ASSERT} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_BITMAP} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_FILE} ,
     *                           {@link ImgBrowserPagerAdapter#IMG_SOURCE_TYPE_URL}
     * @param loadingImg         加载中显示的图片资源
     * @param loadFailureImg     加载失败显示的图片资源
     * @param select             当前选中的条目的下标
     */
    public void showImgs(List<String> uris, int contentRequestType, @DrawableRes int loadingImg, @DrawableRes int loadFailureImg, int select) {
        showImgs(uris, contentRequestType, true, loadingImg, loadFailureImg, select);
    }

    /**
     * 显示一组图片
     *
     * @param bms           需要显示的bitmap对象
     * @param loafailureImg 加载失败显示的图片
     */
    public void showImgs(List<Bitmap> bms, Bitmap loafailureImg) {
        showImgs(bms, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP, true,
                CommonApplication.getInstance().getLoadingImgResId(),
                CommonApplication.getInstance().getLoadingFailureResId(), 0);
    }

    /**
     * 显示一组图片
     *
     * @param bms            需要显示的bitmap对象
     * @param loadFailureImg 加载失败显示的图片
     * @param select         当前选中的条目下标
     */
    public void showImgs(List<Bitmap> bms, Bitmap loadFailureImg, int select) {
        showImgs(bms, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP, true,
                CommonApplication.getInstance().getLoadingImgResId(),
                CommonApplication.getInstance().getLoadingFailureResId(), select);
    }

    /**
     * 显示一组图片
     *
     * @param bms            需要显示的图片
     * @param loadFailureImg 加载失败显示的图片资源
     */
    public void showImgs(List<Bitmap> bms, @DrawableRes int loadFailureImg) {
        showImgs(bms, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP, true,
                CommonApplication.getInstance().getLoadingImgResId(),
                CommonApplication.getInstance().getLoadingFailureResId(), 0);
    }

    /**
     * 显示一组图片
     *
     * @param bms            需要显示的图片
     * @param loadFailureImg 加载失败显示的图片资源
     */
    public void showImgs(List<Bitmap> bms, @DrawableRes int loadFailureImg, int select) {
        showImgs(bms, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_BITMAP, true,
                CommonApplication.getInstance().getLoadingImgResId(),
                CommonApplication.getInstance().getLoadingFailureResId(), select);
    }

    /**
     * 显示一组图片
     * 子类完全可以自己重写实现
     *
     * @param imgs               需要显示那是的图片集合
     * @param contentRequestType 需要显示的图片的类型
     * @param hasAim             是否使用动画(至于什么动画,自由发挥)
     * @param loadingImg         加载中图片
     * @param loadFailureImg     加载失败图片
     * @param select             选中的下标
     */
    protected void showImgs(List<? extends Object> imgs, int contentRequestType,
                            boolean hasAim, @DrawableRes int loadingImg,
                            @DrawableRes int loadFailureImg, int select) {
        coverView();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgBrowser = new ViewPager(this);
        imgBrowser.setBackgroundColor(0xFF000000);
        ImgBrowserPagerAdapter adapter = new ImgBrowserPagerAdapter(this, imgs, contentRequestType, hasAim, loadingImg, loadFailureImg);
        adapter.setOnItemClicked(new ImgBrowserPagerAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                onImgClicked(position);
            }
        });
        imgBrowser.setAdapter(adapter);
        imgBrowser.setCurrentItem(select);
        getContentViewFrame().addView(imgBrowser, lp);
    }

    /**
     * 当某一张图片被点击后执行
     */
    protected void onImgClicked(int position) {
        ViewGroup contentFrame = getContentViewFrame();
        contentFrame.removeView(imgBrowser);
        int count = contentFrame.getChildCount();
        for (int i = 0; i < count; i++) contentFrame.getChildAt(i).setVisibility(View.VISIBLE);
        imgBrowser = null;
    }

    /**
     * 显示一组图片
     * 子类完全可以自己重写实现
     *
     * @param imgs               需要喜爱那是的图片集合
     * @param contentRequestType 需要显示的图片的类型
     * @param hasAim             是否使用动画(至于什么动画,自由发挥)
     * @param loadingImg         加载中图片
     * @param loadFailureImg     加载失败图片
     * @param select             默认选中的条目
     */
    protected void showImgs(List<? extends Object> imgs,
                            int contentRequestType, boolean hasAim,
                            Bitmap loadingImg, Bitmap loadFailureImg, int select) {
        coverView();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgBrowser = new ViewPager(this);
        ImgBrowserPagerAdapter adapter = new ImgBrowserPagerAdapter(this, imgs, contentRequestType, hasAim, loadingImg, loadFailureImg);
        imgBrowser.setAdapter(adapter);
        imgBrowser.setCurrentItem(select);
        imgBrowser.setBackgroundColor(0xFF000000);
        getContentViewFrame().addView(imgBrowser, lp);
        adapter.setOnItemClicked(new ImgBrowserPagerAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                onImgClicked(position);
            }
        });
    }

    protected ViewGroup getContentViewFrame() {
        ViewGroup vg = (ViewGroup) getWindow().getDecorView();
        return (ViewGroup) ((ViewGroup) vg.getChildAt(0)).getChildAt(1);
    }

    @Override
    public void onBackPressed() {
        if (imgBrowser == null) {
            boolean consumed = false;
            for (BackPressClickedListener listener : mBackPressClickedListeners) {
                if (listener.onBackPressed()) {
                    consumed = true;
                    break;
                }
            }
            if (!consumed) super.onBackPressed();
        } else {
            onImgClicked(-1);
        }
    }

    /**
     * 覆盖当前正在显示的视图
     * 为显示图片浏览器做准备
     */
    protected void coverView() {
        ViewGroup contentViewFrame = getContentViewFrame();
        int count = contentViewFrame.getChildCount();
        for (int i = 0; i < count; i++)
            contentViewFrame.getChildAt(i).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onApplicationEnterForeground() {
        ALog.d(TAG, "On application enter foreground");
    }

    @Override
    public void onApplicationEnterBackground() {
        ALog.d(TAG, "On application enter background");
    }

    /**
     * 初始化附加值
     *
     * @param bundle 传递的类对象
     */
    protected void onInitExtras(@NonNull Bundle bundle) {

    }

    /**
     * 启动指定的界面
     *
     * @param compoundsClazz 需要启动的activity组件名
     * @param bundle         需要传递的数据
     *                       默认不清除其他的activity
     */
    public void launchActivity(Class<? extends Activity> compoundsClazz, Bundle bundle) {
        launchActivity(compoundsClazz, bundle, false);
    }

    /**
     * 启动指定的界面
     *
     * @param compoundsClazz         需要启动的activity组件名
     * @param bundle                 需要传递的数据
     * @param launchBeforeClearStack 启动之前先清除栈数据
     */
    public void launchActivity(Class<? extends Activity> compoundsClazz, Bundle bundle, boolean launchBeforeClearStack) {
        ActivityManager.launchActivity(this, compoundsClazz, bundle, launchBeforeClearStack);
    }

    /**
     * 启动指定的界面
     *
     * @param compoundsClazz 需要启动的activity组件名
     * @param params         需要传递的数据
     */
    public void launchActivity(Class<? extends Activity> compoundsClazz, Object... params) {
        launchActivity(compoundsClazz, buildBundle(params), false);
    }

    /**
     * 启动指定的界面
     *
     * @param compoundsClazz 需要启动的activity组件名
     */
    public void launchActivity(Class<? extends Activity> compoundsClazz) {
        launchActivity(compoundsClazz, null, false);
    }


    /**
     * 启动指定的界面
     *
     * @param compoundsClazz         需要启动的activity组件名
     * @param launchBeforeClearStack 启动之前先清除栈数据
     * @param arguments              需要传递的参数数组
     */
    public void launchActivity(Class<? extends Activity> compoundsClazz, boolean launchBeforeClearStack, Object... arguments) {
        launchActivity(compoundsClazz, buildBundle(arguments), launchBeforeClearStack);
    }

    /**
     * 启动一个activity 带请求码的
     *
     * @param compoundsClazz 需要启动的类
     * @param requestCode    请求码
     */
    public void launchActivityForResult(Class<? extends Activity> compoundsClazz, int requestCode) {
        launchActivityForResult(compoundsClazz, false, requestCode);
    }


    /**
     * 启动一个activity 带请求码的
     *
     * @param compoundsClazz 需要启动的类
     * @param requestCode    请求码
     */
    public void launchActivityForResult(Class<? extends Activity> compoundsClazz, boolean launchBeforeClearActivities, int requestCode) {
        launchActivityForResult(compoundsClazz, requestCode, launchBeforeClearActivities, new Bundle());
    }

    /**
     * 启动一个activity 带请求码及附加值
     *
     * @param compoundsClazz 需要启动的类
     * @param requestCode    请求码
     * @param params         传递的对象
     */
    public void launchActivityForResult(Class<? extends Activity> compoundsClazz, int requestCode, Object... params) {
        launchActivityForResult(compoundsClazz, requestCode, buildBundle(params));
    }

    public void launchActivityForResult(Class<? extends Activity> compoundsClazz, int requestCode, Bundle bundle) {
        launchActivityForResult(compoundsClazz, requestCode, false, bundle);
    }

    public void launchActivityForResult(Class<? extends Activity> compoundsClazz, int requestCode, boolean launchBeforeClearActivities, Object... params) {
        launchActivityForResult(compoundsClazz, requestCode, launchBeforeClearActivities, buildBundle(params));
    }

    public void launchActivityForResult(Class<? extends Activity> compoundsClazz, int requestCode, boolean launchBeforeClearActivities, Bundle bundle) {
        ActivityManager.launchActivityForResult(this, compoundsClazz, bundle, launchBeforeClearActivities, requestCode);
    }

    /**
     * 构建bundle对象
     *
     * @param arguments 需要构建的参数
     * @return 构建好的bundle对象
     */
    public Bundle buildBundle(Object... arguments) {
        if (arguments == null) return null;

        Bundle bundle = new Bundle();
        if (arguments.length % 2 == 0) {
            final int count = arguments.length / 2;
            for (int i = 0; i < count; i++) {
                String key = arguments[i * 2].toString();
                Object value = arguments[i * 2 + 1];
                if (key != null && value != null) {
                    if (value instanceof Byte) {
                        bundle.putByte(key, (Byte) value);
                    } else if (value instanceof Short) {
                        bundle.putShort(key, (Short) value);
                    } else if (value instanceof Integer) {
                        bundle.putInt(key, (Integer) value);
                    } else if (value instanceof Long) {
                        bundle.putLong(key, (Long) value);
                    } else if (value instanceof Float) {
                        bundle.putFloat(key, (Float) value);
                    } else if (value instanceof Double) {
                        bundle.putDouble(key, (Double) value);
                    } else if (value instanceof String) {
                        bundle.putString(key, (String) value);
                    } else if (value instanceof Bundle) {
                        bundle.putBundle(key, (Bundle) value);
                    } else if (value instanceof Parcelable) {
                        bundle.putParcelable(key, (Parcelable) value);
                    } else if (value instanceof ArrayList) {
                        bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                    } else if (value instanceof Collection) {
                        bundle.putParcelableArrayList(key, new ArrayList<>((Collection<? extends Parcelable>) value));
                    } else if (value instanceof Serializable) {
                        bundle.putSerializable(key, (Serializable) value);
                    } else {
                        ALog.e(TAG, "unsupported object");
                    }
                }
            }

        }
        return bundle;
    }


    /**
     * 使用相机拍摄图片
     *
     * @param requestCode 请求码
     *                    默认为裁切
     */
    public void captureImgFromCamera(int requestCode) {
        captureImgFromCamera(requestCode, true);
    }

    /**
     * 使用相机拍摄图片
     *
     * @param requestCode 请求码
     * @param needCrop    是否需要裁切
     */
    public void captureImgFromCamera(int requestCode, boolean needCrop) {
        if (requestCode == -1) throw new IllegalArgumentException("request code cant is -1");

        String title = getString(R.string.alert);
        String msg = getString(R.string.do_you_want_to_select_images_from_where);

        mChoiceImgRequestCode = requestCode;
        mChoiceImgDialogTitle = title;
        mChoiceImgDialogMessage = msg;
        mChoiceImgNeedCorp = needCrop;

        RequestType type = requestCodeTable.get(requestCode);
        if (type == null) {
            type = new RequestType();
            type.type = RequestType.TYPE_CAMERA;
        }

        if (type.type != RequestType.TYPE_CAMERA)
            throw new RuntimeException("多次请求类型必须一致.");

        type.times++;
        type.needCrop = needCrop;
        type.requestCode = requestCode;
        requestCodeTable.put(requestCode, type);
        launchActivityForResult(getCameraActivity(), requestCode);
    }


    /**
     * 从相册选择图片
     *
     * @param requestCode 请求码
     *                    默认为裁切
     */
    public void choiceImgFromGallery(int requestCode) {
        choiceImgFromGallery(requestCode, true);
    }

    /**
     * 从相册选择图片
     *
     * @param requestCode 请求码
     * @param needCrop    是否需要裁切
     */
    public void choiceImgFromGallery(int requestCode, boolean needCrop) {
        if (requestCode == -1) throw new IllegalArgumentException("request code cant is -1");

        String title = getString(R.string.alert);
        String msg = getString(R.string.do_you_want_to_select_images_from_where);

        mChoiceImgRequestCode = requestCode;
        mChoiceImgDialogTitle = title;
        mChoiceImgDialogMessage = msg;
        mChoiceImgNeedCorp = needCrop;

        RequestType type = requestCodeTable.get(requestCode);
        if (type == null) {
            type = new RequestType();
            type.type = RequestType.TYPE_GALLERY;
        }
        if (type.type != RequestType.TYPE_GALLERY)
            throw new RuntimeException("多次请求类型必须一致.");

        type.times++;
        type.requestCode = requestCode;
        type.needCrop = needCrop;
        requestCodeTable.put(requestCode, type);
        launchImageChoiceActivity(requestCode);
    }

    protected int getCropWidth(int requestCode) {
        return DEFAULT_IMAGE_CROP_WIDTH_AND_HEIGHT;
    }

    protected int getCropHeight(int requestCode) {
        return DEFAULT_IMAGE_CROP_WIDTH_AND_HEIGHT;
    }

    /**
     * 显示图像选择对话框
     * * 图片裁切完成后会调用 {@link #onImageCropped}
     *
     * @param requestCode 请求码,用来标识不同的请求
     */
    public void showChoiceImageDialog(final boolean needCrop, final int requestCode, String title, String msg) {
        if (requestCode == -1) throw new IllegalArgumentException("request code cant is -1");
        if (!mChoiceImgDialogIsShowing) {
            mChoiceImgDialogIsShowing = true;
            mChoiceImgRequestCode = requestCode;
            mChoiceImgDialogTitle = title;
            mChoiceImgDialogMessage = msg;
            mChoiceImgNeedCorp = needCrop;
            mChoiceImgDialog = new AlertDialog.Builder(this)
                    .setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestType type = requestCodeTable.get(requestCode);
                            if (type == null) {
                                type = new RequestType();
                                type.type = RequestType.TYPE_GALLERY;
                            }
                            if (type.type != RequestType.TYPE_GALLERY)
                                throw new RuntimeException("多次请求类型必须一致.");

                            type.times++;
                            type.requestCode = requestCode;
                            type.needCrop = needCrop;
                            requestCodeTable.put(requestCode, type);
                            launchImageChoiceActivity(requestCode);
                        }
                    })
                    .setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestType type = requestCodeTable.get(requestCode);
                            if (type == null) {
                                type = new RequestType();
                                type.type = RequestType.TYPE_CAMERA;
                            }

                            if (type.type != RequestType.TYPE_CAMERA)
                                throw new RuntimeException("多次请求类型必须一致.");

                            type.times++;
                            type.needCrop = needCrop;
                            type.requestCode = requestCode;
                            requestCodeTable.put(requestCode, type);
                            launchActivityForResult(getCameraActivity(), requestCode);
                        }
                    })
                    .setTitle(title).setMessage(msg)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //是在onDestroy处调用的
                            mChoiceImgDialogIsShowing = mChoiceImgDialog == null;
                            ALog.e(TAG, "onDismiss");
                        }
                    }).create();
            mChoiceImgDialog.show();
        }
    }

    /**
     * 启动图片界面
     *
     * @param requestCode 启动的请求码
     */
    protected void launchImageChoiceActivity(int requestCode) {
        launchActivityForResult(getImgChoiceActivity(), requestCode);
    }

    /**
     * 获取图片选择列表
     *
     * @return 扩展的ImagePickerListActivity类对象
     */
    protected Class<? extends ImagePickerListActivity> getImgChoiceActivity() {
        return ImagePickerListActivity.class;
    }

    /**
     * 获取相机界面
     *
     * @return 扩展的CameraActivity类对象
     */
    protected Class<? extends CameraActivity2> getCameraActivity() {
        return CameraActivity2.class;
    }

    /**
     * 显示图像选择对话框
     * 图片裁切完成后会调用 {@link #onImageCropped}
     *
     * @param requestCode 请求码,用来标识不同的请求
     */
    public void showChoiceImageDialog(final int requestCode) {
        showChoiceImageDialog(true, requestCode,
                getString(R.string.alert),
                getString(R.string.do_you_want_to_select_images_from_where));
    }

    /**
     * 显示图像选择对话框
     * 图片裁切完成后会调用 {@link #onImageCropped}
     *
     * @param needCrop    是否需要裁切
     * @param requestCode 请求码,用来标识不同的请求
     */
    public void showChoiceImageDialog(final int requestCode, boolean needCrop) {
        showChoiceImageDialog(needCrop, requestCode,
                getString(R.string.alert),
                getString(R.string.do_you_want_to_select_images_from_where));
    }

    /**
     * 裁切图片
     *
     * @param inUri       输入地址
     * @param outURi      输出地址
     * @param aspectX     x比重
     * @param aspectY     y比重
     * @param width       宽度
     * @param height      高度
     * @param requestCode 请求码
     */
    public void cropImage(Uri inUri, Uri outURi, int aspectX, int aspectY, int width, int height, int requestCode) {
        RequestType type = requestCodeTable.get(requestCode);
        if (type == null) {
            type = new RequestType();
            type.type = RequestType.TYPE_CROP;
        }

        if (type.type != RequestType.TYPE_CROP) throw new RuntimeException("多次请求类型必须一致.");

        type.times++;
        type.requestCode = requestCode;
        requestCodeTable.put(requestCode, type);

//        Intent intent = new Intent(this, CropImageActivity.class);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, outURi);
//        intent.setData(inUri);
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", width);
//        intent.putExtra("outputY", height);
//        intent.putExtra("scale", true);
//        intent.putExtra("scaleUpIfNeeded", true);
//        intent.putExtra("noFaceDetection", true);
//
//        startActivityForResult(intent, requestCode);

        CropImage.activity(inUri)
                .setOutputUri(outURi)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(aspectX, aspectY)
                .setRequestedSize(width, height)
                .start(this, getCropActivity(), requestCode);
    }

    protected Class<? extends Activity> getCropActivity() {
        return CropImageActivity.class;
    }


    /**
     * 裁切图片
     *
     * @param inUri       输入地址
     * @param outURi      输出地址
     * @param width       宽度
     * @param height      高度
     * @param requestCode 请求码
     */
    public void cropImage(Uri inUri, Uri outURi, int width, int height, int requestCode) {
        cropImage(inUri, outURi, 1, 1, width, height, requestCode);
    }

    /**
     * 显示toast
     *
     * @param stringResId 资源id
     */
    public void showToast(@StringRes int stringResId) {
        showToast(getString(stringResId), Toast.LENGTH_SHORT);
    }

    /**
     * 显示时间较长的toast
     *
     * @param string
     */
    public void showLongToast(final String string) {
        showToast(string, Toast.LENGTH_LONG);
    }


    /**
     * 显示时间较长的toast
     *
     * @param stringResId 资源id
     */
    public void showLongToast(@StringRes int stringResId) {
        showToast(getString(stringResId), Toast.LENGTH_LONG);
    }

    /**
     * 显示时间较短的toast
     *
     * @param string
     */
    public void showToast(final String string) {
        showToast(string, Toast.LENGTH_SHORT);
    }


    /**
     * 显示toast
     *
     * @param string 显示toast
     */
    public void showToast(final String string, final int len) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                    mToast = null;
                }
                mToast = Toast.makeText(CommonActivity.this, string, len);
                mToast.show();
            }
        };

        if (Looper.myLooper() != Looper.getMainLooper()) {
            UI_HANDLER.post(run);
        } else {
            run.run();
        }
    }

    /**
     * 当图片被裁切完成后执行
     *
     * @param imgUri      裁切完成的图片保存的地址
     * @param requestCode 发起照片裁切的请求码
     */
    public void onImageCropped(Uri imgUri, int requestCode) {
        ALog.e(TAG, "onImageCropped -> " + imgUri);
    }

    /**
     * 分发图片选择事件
     *
     * @param uri
     * @param requestCode
     */
    protected void dispatchImageSelectedFromCrop(Uri uri, int requestCode) {
        //Always call self.
        onImageCropped(uri, requestCode);

        if (imgSelectedListeners.isEmpty()) {
            onImageSelected(uri, requestCode);
        } else {
            boolean processed = false;
            for (ImgSelectedListener listener : imgSelectedListeners) {
                if (listener != null && listener.onImageSelected(uri, requestCode)) {
                    processed = true;
                }
            }
            if (!processed) onImageSelected(uri, requestCode);
        }
    }

    /**
     * 当从相册中选取照片后回调
     *
     * @param uri         照片保存的地址
     * @param requestCode 发起照片选择的请求码
     */
    public void onGalleryPicked(Uri uri, int requestCode, boolean needCorp) {
        if (needCorp) {
            cropImage(uri, Uri.fromFile(new File(
                            StorageUtils.getApplicationCacheDir(this),
                            String.valueOf(System.currentTimeMillis()).concat(".png"))),
                    getCropWidth(requestCode), getCropHeight(requestCode), requestCode);
        } else {
            dispatchImageSelected(uri, requestCode);
        }
    }

    /**
     * this
     */
    public void showDelayProgressCircle() {
        showDelayProgressCircle(getString(R.string.loading));
    }

    /**
     * this
     */
    public void showProgressCircle() {
        showProgressCircle(getString(R.string.loading));
    }

    public void showProgressCircle(boolean cancelable) {
        showProgressCircle(getString(R.string.loading), cancelable);
    }

    public void showDelayProgressCircle(boolean cancelable) {
        showDelayProgressCircle(getString(R.string.loading), cancelable);
    }

    /**
     * this
     */
    public void showProgressCircle(Parcelable tag) {
        showProgressCircle(getString(R.string.loading), tag);
    }

    /**
     * this
     */
    public void showDelayProgressCircle(Parcelable tag) {
        showDelayProgressCircle(getString(R.string.loading), tag);
    }

    /**
     * this
     */
    public void showProgressCircle(@StringRes int strRes) {
        showProgressCircle(getString(strRes));
    }

    /**
     * this
     */
    public void showDelayProgressCircle(@StringRes int strRes) {
        showDelayProgressCircle(getString(strRes));
    }

    /**
     * this
     */
    public void showProgressCircle(@StringRes int strRes, Parcelable tag) {
        showProgressCircle(getString(strRes), tag);
    }

    /**
     * this
     */
    public void showDelayProgressCircle(@StringRes int strRes, Parcelable tag) {
        showDelayProgressCircle(getString(strRes), tag);
    }

    /**
     * this
     */
    public void showProgressCircle(@StringRes int strRes, boolean cancelable) {
        showProgressCircle(getString(strRes), cancelable);
    }

    /**
     * this
     */
    public void showDelayProgressCircle(@StringRes int strRes, boolean cancelable) {
        showDelayProgressCircle(getString(strRes), cancelable);
    }

    /**
     * 显示等待对话框
     *
     * @param content 内容
     */
    public void showProgressCircle(String content) {
        showProgressCircle(getString(R.string.loading), content);
    }

    /**
     * 显示等待对话框
     *
     * @param content 内容
     */
    public void showDelayProgressCircle(String content) {
        showDelayProgressCircle(getString(R.string.loading), content);
    }

    /**
     * 显示等待对话框
     *
     * @param content 内容
     */
    public void showProgressCircle(String content, boolean cancelable) {
        showProgressCircle(getString(R.string.loading), content, cancelable);
    }


    /**
     * 显示等待对话框
     *
     * @param content 内容
     */
    public void showDelayProgressCircle(String content, boolean cancelable) {
        showDelayProgressCircle(getString(R.string.loading), content, cancelable);
    }

    /**
     * 显示等待对话框
     *
     * @param content 内容
     */
    public void showProgressCircle(String content, Parcelable tag) {
        showProgressCircle(getString(R.string.loading), content, tag, true);
    }


    /**
     * 显示等待对话框
     *
     * @param content 内容
     */
    public void showDelayProgressCircle(String content, Parcelable tag) {
        showDelayProgressCircle(getString(R.string.loading), content, tag, true);
    }

    /**
     * 显示等待对话框
     *
     * @param title   标题
     * @param content 内容
     */
    public void showProgressCircle(String title, String content) {
        showProgressCircle(title, content, true);
    }


    public void showDelayProgressCircle(String title, String content) {
        showDelayProgressCircle(title, content, true);
    }

    /*
            * 显示等待对话框
     *
             * @param title   标题
     * @param content 内容
     */
    public void showDelayProgressCircle(final String title, final String content, final boolean cancelable) {
        showDelayProgressCircle(title, content, null, cancelable);
    }

    /**
     * 显示等待对话框
     *
     * @param title   标题
     * @param content 内容
     */
    public void showProgressCircle(final String title, final String content, final boolean cancelable) {
        showProgressCircle(title, content, null, cancelable);
    }

    public void showDelayProgressCircle(final String title, final String content, final Parcelable tag, final boolean cancelable) {
        showDelayProgressCircle(title, content, tag, cancelable, DEFAULT_DELAY_TIME);
    }

    /**
     * 在一定的延迟后显示对话框
     * 这个通常用在等待时间不固定的情况下，很典型的就是网络请求，网络请求有快有慢。
     * 如果网络请求很快就返回了，也显示对话框这其实是不合理的，并且很有可能伴随着屏幕的闪动。
     * 实现原理为在发起请求后，设置固定的延迟，如果dismiss时小于延迟，那么就取消掉这个对话框的显示
     * 否则就显示出这个对话框
     *
     * @param title
     * @param content
     * @param tag
     * @param cancelable
     * @param waitingTime
     */
    public void showDelayProgressCircle(final String title, final String content, final Parcelable tag, final boolean cancelable, long waitingTime) {
        UI_HANDLER.postDelayed(this.mWaitingShowDialogRunner = new Runnable() {
            @Override
            public void run() {
                mWaitingShowDialogRunner = null;
                showProgressCircle(title, content, tag, cancelable);
            }
        }, waitingTime);
    }

    /**
     * 显示等待对话框
     *
     * @param title   标题
     * @param content 内容
     */
    public void showProgressCircle(final String title, final String content, final Parcelable tag, final boolean cancelable) {
        ALog.i(TAG, "showProgressCircle");

        dismissProgressCircle();
        //等待对话框存在,但是等待对话框正在准备显示出来
        //那么我们将这次创建对话框的操作放置到队列中,等待执行
        if ((mProgressDialog != null && !mProgressDialogIsShowing)) {
            dialogActionQueue.addLast(new Runnable() {
                private final String TAG = "Show Action";

                @Override
                public void run() {
                    showProgressCircle(title, content, tag, cancelable);
                }
            });
        } else {
            mProgressDialog = createProgressCircle(title, content, cancelable, tag);
            if (mProgressDialog != null) {
                mProgressDialog.setDismissListener(CommonActivity.this);
                mProgressDialog.setReshowListener(CommonActivity.this);
                mProgressDialog.setOnShowListener(CommonActivity.this);
            }
        }
    }

    /**
     * 创建等待对话框对象
     *
     * @param title   给定的标题
     * @param content 给定的内容
     * @return
     */
    protected ProgressDialogInterface createProgressCircle(String title, String content, boolean cancelable, Parcelable tag) {
//        return ProgressDialogWrapper.show(this, title, content);
        return LoadingProgressDialog.showProgressDialog(this, cancelable, content, tag);
    }

    /**
     * 获取进度框的tag如果这个对话框是使用fragment做的
     * 我们极力推荐使用能fragment做dialog,因为他可以很好的保存状态
     *
     * @return null或fragment的tag, 如果是null的话就表示无tag或不是fragment做的dialog
     */
    protected String getProgressDialogTagIfIsFragment() {
        return LoadingProgressDialog.class.getName();
    }

    /**
     * 读条完毕
     */
    public void dismissProgressCircle() {
        //如果有延迟显示对话框
        //那么就直接取消掉，并返回
        if (mWaitingShowDialogRunner != null) {
            UI_HANDLER.removeCallbacks(mWaitingShowDialogRunner);
            mWaitingShowDialogRunner = null;
            return;
        }
        ALog.i(TAG, "dismissProgressCircle");

        //如果进度条不为null,但是我们的对话框却没显示,说明这个对话框是正在加载中的.
        if (mProgressDialog != null) {
            if (!mProgressDialogIsShowing) {
                //那么我们将取消动作添加到队列中等待执行
                dialogActionQueue.addLast(new Runnable() {
                    private final String TAG = "Dismiss Action";

                    @Override
                    public void run() {
                        dismissProgressCircle();
                    }
                });
            } else {
                mProgressDialog.cancel();
                mProgressDialog.dismiss();
                mProgressDialog = null;
                mProgressDialogIsShowing = false;
            }
        }

    }


    /**
     * 当使用相机拍摄照片后回调
     *
     * @param uri         照片拍摄后保存的地址
     * @param requestCode 发起拍照获取图片的请求码
     * @param needCrop    是否需要裁切图片
     */
    public void onCameraTaken(Uri uri, int requestCode, boolean needCrop) {
        if (needCrop) {
            cropImage(uri, Uri.fromFile(new File(
                            StorageUtils.getApplicationCacheDir(this),
                            String.valueOf(System.currentTimeMillis()).concat(".png"))),
                    getCropWidth(requestCode), getCropHeight(requestCode), requestCode);
        } else {
            dispatchImageSelected(uri, requestCode);
        }
    }

    /**
     * 分发图片选择事件
     *
     * @param uri
     * @param requestCode
     */
    protected void dispatchImageSelected(Uri uri, int requestCode) {
        if (imgSelectedListeners.isEmpty()) {
            onImageSelected(uri, requestCode);
        } else {
            boolean processed = false;
            ImgSelectedListener[] listeners = imgSelectedListeners.toArray(new ImgSelectedListener[imgSelectedListeners.size()]);
            for (ImgSelectedListener listener : listeners) {
                if (listener != null && listener.onImageSelected(uri, requestCode)) {
                    processed = true;
                }
            }
            if (!processed) onImageSelected(uri, requestCode);
        }
    }

    /**
     * 当图片被选择后执行
     *
     * @param uri 选择的图片的地址
     */
    protected void onImageSelected(Uri uri, int requestCode) {
        if (Config.DEBUG) {
            ALog.d(TAG, "onImageSelected -> " + uri);
        }
    }

    /**
     * 版本更新检查
     *
     * @param background                  是否是在后台静默检查
     * @param installPackageStoreLocation 下载的安装包保存位置
     * @param checkURL                    服务端的版本文件存放位置 例如 http://192.168.1.254:8080/version/version.xml
     */
    protected void versionCheck(String checkURL, String installPackageStoreLocation, boolean background) {
        if (background) {
            BackgroundVersionChecker.getInstance(this).check(installPackageStoreLocation, checkURL);
        } else {
            UpdateAlertDialog dialog = new UpdateAlertDialog();
            dialog.setFileDownloadPath(installPackageStoreLocation);
            dialog.setCheckFileURL(checkURL);
            dialog.show(this.getFragmentManager(), String.format("%s%s", this.getClass().getName(), dialog.getClass().getName()));
        }
    }

    /**
     * 版本更新检查
     * 如果没有初始化 {@link com.lovely3x.common.utils.storage.StorageUtils} 请先初始化
     * 默认保存在 {@link com.lovely3x.common.utils.storage.StorageUtils#getPublicCacheFile(String)} (String)}
     *
     * @param background 是否是在后台静默检查
     * @param checkURL   服务端的版本文件存放位置 例如 http://192.168.1.254:8080/version/version.xml
     */
    public void versionCheck(String checkURL, boolean background) {
        File packageFile = com.lovely3x.common.utils.storage.StorageUtils.getInstance().getPublicCacheFile(getPackageName().concat(".apk"));
        if (packageFile != null) {
            versionCheck(checkURL, packageFile.getAbsolutePath(), background);
        }
    }

    /**
     * 后台静默检查新版本，发现新版本后提示用户更新
     * 默认保存在 {@link com.lovely3x.common.utils.storage.StorageUtils#getPublicCacheFile(String)} (String)}
     *
     * @param checkURL 需要检查的地址
     */
    public void silentVersionCheck(String checkURL) {
        File packageFile = com.lovely3x.common.utils.storage.StorageUtils.getInstance().getPublicCacheFile(getPackageName().concat(".apk"));
        if (packageFile != null) {
            versionCheck(checkURL, packageFile.getAbsolutePath(), true);
        }
    }

    /**
     * 提醒式检查新版本，发现新版本后提示用户更新
     * 默认保存在 {@link com.lovely3x.common.utils.storage.StorageUtils#getPublicCacheFile(String)} (String)}
     *
     * @param checkURL 需要检查的地址
     */
    public void versionCheck(String checkURL) {
        File packageFile = com.lovely3x.common.utils.storage.StorageUtils.getInstance().getPublicCacheFile(getPackageName().concat(".apk"));
        if (packageFile != null) {
            versionCheck(checkURL, packageFile.getAbsolutePath(), false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RequestType requestCodeRequestType = requestCodeTable.get(requestCode);
        if (requestCodeRequestType != null) {
            if (requestCodeRequestType.times > 0) {
                requestCodeRequestType.times--;
                if (requestCodeRequestType.times <= 0) requestCodeTable.remove(requestCode);

                if (requestCodeRequestType.type == RequestType.TYPE_CROP) {//裁切图片
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        dispatchImageSelectedFromCrop(result.getUri(), requestCode);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        ALog.w(TAG, "Crop img fail " + result.getError());
                    }

                } else if (requestCodeRequestType.type == RequestType.TYPE_GALLERY) {//从相册选择图片
                    if (data != null && data.getData() != null) {
                        onGalleryPicked(data.getData(), requestCode, requestCodeRequestType.needCrop);
                    }
                } else if (requestCodeRequestType.type == RequestType.TYPE_CAMERA) {
                    if (data != null && data.getData() != null) {
                        onCameraTaken(data.getData(), requestCode, requestCodeRequestType.needCrop);
                    }
                }
            }
        }
        dispatchOnActivityForResultEvent(requestCode, resultCode, data);
    }

    /**
     * 分发activity for result 事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void dispatchOnActivityForResultEvent(int requestCode, int resultCode, Intent data) {
        ArrayList<ActivityResultListener> activityResultListeners = new ArrayList<>(this.activityResultListeners);
        for (ActivityResultListener listener : activityResultListeners) {
            try {
                listener.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                ALog.e(TAG, e);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_CHOICE_IMG_DIALOG_IS_SHOWING, mChoiceImgDialogIsShowing);
        outState.putInt(KEY_CHOICE_IMG_DIALOG_REQUEST_ID, mChoiceImgRequestCode);
        outState.putString(KEY_CHOICE_IMG_DIALOG_TITLE, mChoiceImgDialogTitle);
        outState.putString(KEY_CHOICE_IMG_DIALOG_MESSAGE, mChoiceImgDialogMessage);
        outState.putSparseParcelableArray(KEY_REQUEST_CODE_TABLE, requestCodeTable);
        outState.putBoolean(KEY_IMG_NEED_CROP, mChoiceImgNeedCorp);
    }

    /**
     * 使用{@link #decodeBitmapUri} 解析完成后调用
     *
     * @param uri    解析的地址
     * @param bitmap 解析的结果,注意可能是null,因为可能解析失败,失败原因可能是指定的url无效或oom等
     */
    public void onBitmapUriDecode(Uri uri, Bitmap bitmap) {

    }

    /**
     * 解码图片uri(目前值支持解析文件uri)
     * 解析图片将在工作线程中执行,所以在解析工作完成后将调用 {@link #onBitmapUriDecode}
     *
     * @param uri 需要解析的地址
     */
    public void decodeBitmapUri(final Uri uri) {
        final DecodeImageHandler dih = new DecodeImageHandler(CommonActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                try {
                    bm = BitmapFactory.decodeFile(uri.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dih.obtainMessage(DecodeImageHandler.HANDLER_WHAT_DECODE_BITMAP, new Object[]{uri, bm}).sendToTarget();
                }
            }
        }).start();
    }

    /**
     * 在新的界面中显示图片
     *
     * @param imgs 需要显示的图片集合
     */
    public void showImgOnNewActivity(List<Img> imgs, int index) {
        if (imgs == null || imgs.isEmpty()) return;
        launchActivity(ImagesDisplyerActivity.class,
                ImagesDisplyerActivity.EXTRA_IMAGES, imgs, ImagesDisplyerActivity.EXTRA_INDEX, index);
    }

    /**
     * 在新的界面中显示图片
     *
     * @param imgs 图片集合
     * @param type 图片的类型，这里的类型应该不包括Drawable资源和Bitmap
     */
    public void showImgOnNewActivity(List<String> imgs, int type, int index) {
        if (imgs == null || imgs.isEmpty()) return;
        ArrayList<Img> images = new ArrayList<>();
        CommonApplication app = CommonApplication.getInstance();
        int failureResId = app.getLoadingFailureResId();
        int loadingResId = app.getLoadingFailureResId();
        for (int i = 0; i < imgs.size(); i++) {
            images.add(new Img(type, imgs.get(i), null, failureResId, loadingResId, null, null));
        }
        showImgOnNewActivity(images, index);
    }

    /**
     * 在新的界面中显示网络图片
     *
     * @param imgs 需要显示 的图片集合
     */
    public void showHTTPURLImgOnNewActivity(List<String> imgs, int index) {
        showImgOnNewActivity(imgs, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL, index);
    }

    /**
     * 在新的界面中显示网络图片
     *
     * @param imgs 需要显示 的图片集合
     */
    public void showHTTPURLImgOnNewActivity(List<String> imgs) {
        showImgOnNewActivity(imgs, ImgBrowserPagerAdapter.IMG_SOURCE_TYPE_URL, 0);
    }

    /**
     * 显示键盘
     *
     * @param view attach目标
     */
    public void showInputMethod(View view) {
        if (view == null) return;
        if (mInputMethod == null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            mInputMethod = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        mInputMethod.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param view from here detach
     */
    public void hiddenInputMethod(View view) {
        if (mInputMethod == null) {
            mInputMethod = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        mInputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    public void onEvent(int eventWhat, Object objects) {
        //On evnet
        if (Config.DEBUG) {
            ALog.d(TAG, "onEvent");
        }
    }

    /**
     * 显示提示按钮
     *
     * @param title   标题
     * @param content 内容
     */
    public AlertDialog showAlertDialog(String title, String content) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, null)
                .setTitle(title)
                .setMessage(content)
                .create();

        dialog.show();
        return dialog;
    }

    /**
     * 显示提示按钮
     *
     * @param title   标题
     * @param content 内容
     */
    public AlertDialog showConfirmDialog(String title, String content, DialogInterface.OnClickListener onClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, onClickListener)
                .setNegativeButton(R.string.cancel, null)
                .setTitle(title)
                .setMessage(content)
                .create();

        dialog.show();
        return dialog;
    }

    /**
     * 显示提示按钮
     *
     * @param title   标题
     * @param content 内容
     */
    public AlertDialog showAlertDialog(int title, int content) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, null)
                .setTitle(title)
                .setMessage(content).create();

        dialog.show();
        return dialog;
    }

    /**
     * 显示提示按钮
     *
     * @param content 内容
     */
    public Dialog showAlertDialog(int content) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, null)
                .setTitle(R.string.alert)
                .setMessage(content).create();
        dialog.show();
        return dialog;
    }

    /**
     * 显示提示按钮
     *
     * @param content 内容
     */
    public Dialog showAlertDialog(String content) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, null)
                .setTitle(R.string.alert)
                .setMessage(content).create();
        dialog.show();
        return dialog;
    }

    /**
     * 显示提示按钮
     *
     * @param content 内容
     */
    public Dialog showAlertDialog(String content, DialogInterface.OnClickListener onClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, onClickListener)
                .setTitle(R.string.alert)
                .setMessage(content).create();
        dialog.show();
        return dialog;
    }

    /**
     * 显示提示按钮
     *
     * @param content 内容
     */
    public Dialog showAlertDialog(int content, DialogInterface.OnClickListener onClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert)
                .setPositiveButton(R.string.ok, onClickListener)
                .setMessage(content).create();
        dialog.show();
        return dialog;
    }

    public AlertDialog showConfirmDialog(String title, String content, DialogInterface.OnClickListener negativeListener, DialogInterface.OnClickListener positiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.confirm, positiveListener)
                .setNegativeButton(R.string.cancel, negativeListener)
                .setTitle(title)
                .setMessage(content).create();
        dialog.show();
        return dialog;
    }

    /**
     * 显示确认对话框
     *
     * @param content          内容
     * @param positiveListener 确认按钮监听器
     */
    public AlertDialog showConfirmDialog(String content, DialogInterface.OnClickListener positiveListener) {
        return showConfirmDialog(getString(R.string.alert), content, null, positiveListener);
    }

    /**
     * 显示确认对话框
     *
     * @param contentRes       内容
     * @param positiveListener 确认按钮监听器
     */
    public AlertDialog showConfirmDialog(int contentRes, DialogInterface.OnClickListener positiveListener) {
        return showConfirmDialog(getString(R.string.alert), getString(contentRes), null, positiveListener);
    }


    /**
     * 显示重试对话框
     *
     * @param codeDescription
     * @param onClickListener
     */
    public AlertDialog showRetryDialog(String codeDescription, DialogInterface.OnClickListener onClickListener) {
        return showRetryDialog(codeDescription, onClickListener, false);
    }


    /**
     * 显示重试对话框
     *
     * @param codeDescription
     * @param onClickListener
     */
    public AlertDialog showRetryDialog(String codeDescription, DialogInterface.OnClickListener onClickListener, final boolean finishOnCancel) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setPositiveButton(com.lovely3x.common.R.string.retry, onClickListener)
                .setNegativeButton(com.lovely3x.common.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finishOnCancel) {
                            finish();
                        }
                    }
                })
                .setTitle(R.string.alert)
                .setMessage(codeDescription).create();
        dialog.show();
        return dialog;
    }


    /**
     * 当activity销毁时，会记录返回的这个对象
     *
     * @return 需要记录的对象
     */
    public Record getRecordIdentifier() {
        return null;
    }

    /**
     * 当activity创建时会返回这个class名记录的对象
     *
     * @param object 记录的对象
     */
    public void onRecordIdentifier(Record object) {

    }

    /**
     * 当对话框被关闭掉执行
     *
     * @param dialog The dialog that was dismissed will be passed into the
     */
    @Override
    public void onDismiss(ProgressDialogInterface dialog, boolean fromUser) {
        ALog.e(TAG, "Dialog " + dialog + "dismiss ,from user operation " + fromUser + " ,Tag is " + dialog.getTags());
        if (fromUser) onUserCancelProgressDialog();
    }


    /**
     * 当用户取消掉了对话框后执行
     */
    public void onUserCancelProgressDialog() {

    }

    @Override
    public void onReshow(ProgressDialogInterface dialog) {
        ALog.e(TAG, "Dialog " + dialog + "reshow ,Tag is " + dialog.getTags());
    }

    @Override
    public void onShow(DialogInterface dialog) {
        mProgressDialogIsShowing = true;
        Iterator<Runnable> it = new LinkedList<>(dialogActionQueue).descendingIterator();
        dialogActionQueue.clear();
        while (it.hasNext()) it.next().run();

    }

    /**
     * 添加对话框取消监听器
     *
     * @param progressCircleCancelListener
     * @return
     */
    public boolean addProgressCircleCancelListener(ProgressCircleCancelListener progressCircleCancelListener) {
        return progressCircleCancelListeners.add(progressCircleCancelListener);
    }

    /**
     * 移除对话框取消监听器
     *
     * @param progressCircleCancelListener
     * @return
     */
    public boolean removeProgressCircleCancelListener(ProgressCircleCancelListener progressCircleCancelListener) {
        return progressCircleCancelListeners.remove(progressCircleCancelListener);
    }

    /***
     * 添加activityresult 监听器
     * 添加后可以监听这个activity的{@link #onActivityResult(int, int, Intent)} 事件
     *
     * @param activityResultListener 需要添加的监听器
     * @return
     */
    public boolean addActivityResultListener(ActivityResultListener activityResultListener) {
        return activityResultListeners.add(activityResultListener);
    }

    /**
     * 移除activityresult 监听器
     *
     * @param activityResultListener 需要移除的监听器
     * @return
     */
    public boolean removeActivityResultListener(ActivityResultListener activityResultListener) {
        return activityResultListeners.remove(activityResultListener);
    }

    public boolean addBackPressClickedListener(BackPressClickedListener backPressClickedListener) {
        return !mBackPressClickedListeners.contains(backPressClickedListener) && this.mBackPressClickedListeners.add(backPressClickedListener);
    }

    public boolean removeBackPressClickedListener(BackPressClickedListener listener) {
        return mBackPressClickedListeners.remove(listener);
    }


    /**
     * Tint 用于对导航栏和状态栏处理的描述对象
     */
    public static class Tint {

        public boolean dark = false;

        public boolean tint = true;

        /**
         * 状态栏的颜色
         */
        public int mStatusBarColor = -1;


        /**
         * 导航栏的颜色
         */
        public int mNavigationBarColor = -1;


        /**
         * 是否显示状态栏
         */
        public boolean mShowStatusBar;

        /**
         * 是否显示导航栏
         */
        public boolean mShowNavigationBar;

        public Tint(int statusBarColor, int navigationBarColor, boolean showStatusBar, boolean showNavigationBar) {
            this.mStatusBarColor = statusBarColor;
            this.mNavigationBarColor = navigationBarColor;
            this.mShowStatusBar = showStatusBar;
            this.mShowNavigationBar = showNavigationBar;
        }

        public Tint() {
        }

    }

    /**
     * 发出申请的类型
     */
    public static class RequestType implements Parcelable {
        /**
         * 裁切图片
         */
        public static final int TYPE_CROP = 0x1;
        /**
         * 从相册选择图片
         */
        public static final int TYPE_GALLERY = 0x2;
        /**
         * 从相机选择
         */
        public static final int TYPE_CAMERA = 0x3;
        public static final Creator<RequestType> CREATOR = new Creator<RequestType>() {
            public RequestType createFromParcel(Parcel source) {
                return new RequestType(source);
            }

            public RequestType[] newArray(int size) {
                return new RequestType[size];
            }
        };
        /**
         * 请求码
         */
        public int requestCode;
        /**
         * 次数
         */
        public int times;
        /**
         * 类型
         */
        public int type = ~0;
        /**
         * 是否需要裁切
         */
        public boolean needCrop;

        public RequestType() {
        }

        protected RequestType(Parcel in) {
            this.requestCode = in.readInt();
            this.times = in.readInt();
            this.type = in.readInt();
            this.needCrop = in.readByte() != 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.requestCode);
            dest.writeInt(this.times);
            dest.writeInt(this.type);
            dest.writeByte(needCrop ? (byte) 1 : (byte) 0);
        }
    }

    /**
     * 解码图片handler消息处理
     */
    private static class DecodeImageHandler extends BaseWeakHandler<CommonActivity> {

        private static final int HANDLER_WHAT_DECODE_BITMAP = 0x100;

        /**
         * 当指定的这个类如果不存在,handler 的消息不会得到处理
         *
         * @param outClass 外部引用类
         */
        public DecodeImageHandler(CommonActivity outClass) {
            super(outClass);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_WHAT_DECODE_BITMAP) {
                if (msg.obj instanceof Object[]) {
                    Object[] obj = (Object[]) msg.obj;
                    getOutClass().onBitmapUriDecode((Uri) obj[0], (Bitmap) obj[1]);
                }
            }
        }
    }

    /**
     * 记录对象
     */
    public static class Record {
        /**
         * 标志
         */
        public String identifier;
        /**
         * 需要记录的对象
         */
        public String object;

        @Override
        public String toString() {
            return "Record{" +
                    "identifier='" + identifier + '\'' +
                    ", object='" + object + '\'' +
                    '}';
        }
    }

    /**
     * 进度等待条
     * 我们现在推荐使用 {@link LoadingProgressDialog}
     */
    @Deprecated
    public static class ProgressDialogWrapper extends android.app.ProgressDialog implements ProgressDialogInterface {

        private Parcelable mTag;
        private OnReshowListener mOnReshowListener;

        public ProgressDialogWrapper(Context context) {
            super(context);
        }

        public ProgressDialogWrapper(Context context, int theme) {
            super(context, theme);
        }


        public static ProgressDialogWrapper show(Context context, CharSequence title,
                                                 CharSequence message) {
            return show(context, title, message, false);
        }

        public static ProgressDialogWrapper show(Context context, CharSequence title,
                                                 CharSequence message, boolean indeterminate) {
            return show(context, title, message, indeterminate, false, null);
        }

        public static ProgressDialogWrapper show(Context context, CharSequence title,
                                                 CharSequence message, boolean indeterminate, boolean cancelable) {
            return show(context, title, message, indeterminate, cancelable, null);
        }

        public static ProgressDialogWrapper show(Context context, CharSequence title,
                                                 CharSequence message, boolean indeterminate,
                                                 boolean cancelable, OnCancelListener cancelListener) {
            ProgressDialogWrapper dialog = new ProgressDialogWrapper(context);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setIndeterminate(indeterminate);
            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(cancelListener);
            dialog.show();
            return dialog;
        }

        @Override
        public void setDismissListener(final com.lovely3x.common.activities.OnDismissListener dismissListener) {
            if (dismissListener == null) {
                super.setOnDismissListener(null);
            } else {
                super.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismissListener.onDismiss(ProgressDialogWrapper.this, true);
                    }
                });
            }
        }

        @Override
        public void setReshowListener(OnReshowListener reshowListener) {
            this.mOnReshowListener = reshowListener;
        }

        @Override
        public void setTags(Parcelable tag) {
            this.mTag = tag;
        }

        @Override
        public Parcelable getTags() {
            return mTag;
        }
    }

    /**
     * 进度条接口
     */
    public interface ProgressDialogInterface extends DialogInterface {


        /**
         * 当前是否正处于显示状态
         *
         * @return
         */
        boolean isShowing();

        /**
         * 设置对话款关闭监听器
         *
         * @param dismissListener 需要设置的监听器
         */
        void setDismissListener(com.lovely3x.common.activities.OnDismissListener dismissListener);

        /**
         * 设置对话框重新显示监听器
         *
         * @param reshowListener 需要设置的监听器
         */
        void setReshowListener(com.lovely3x.common.activities.OnReshowListener reshowListener);

        /**
         * 设置显示事件监听器
         *
         * @param onShowListener 需要设置的监听器
         */
        void setOnShowListener(OnShowListener onShowListener);

        /**
         * 设置一个标记
         *
         * @param tag 标记
         */
        void setTags(Parcelable tag);

        /**
         * 获取标记
         *
         * @return
         */
        Parcelable getTags();

    }

    /**
     * 图片选择监听器
     */
    public interface ImgSelectedListener {
        /**
         * 当图片被选择后执行
         *
         * @param uri         选择的图片的地址
         * @param requestCode 是否有处理 如果没有处理则会继续向下下分发
         */
        boolean onImageSelected(Uri uri, int requestCode);
    }

    /**
     * 进度条取消监听器
     */
    public interface ProgressCircleCancelListener {
        /**
         * 当进度条被取消后执行
         */
        void onCancel();
    }

    /***
     * ActivityResultListener listen 'onActivityResult' and forward this event to observer.
     */
    public interface ActivityResultListener {
        /**
         * Call when activity onActivityResult is called.
         *
         * @param requestCode
         * @param resultCode
         * @param data
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }


    public interface BackPressClickedListener {

        boolean onBackPressed();
    }



}
