package com.jackmar.jframelibray.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jackmar.jframelibray.JFrameConfig;
import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.base.broadcasts.BroadcastManager;
import com.jackmar.jframelibray.base.broadcasts.IBroadcast;
import com.jackmar.jframelibray.managers.XActivityManager;
import com.jackmar.jframelibray.utils.ToastUtil;
import com.jackmar.jframelibray.utils.ViewUtil;
import com.jackmar.jframelibray.utils.imagepicker.ImagePickerUtils;
import com.jackmar.jframelibray.utils.statubars.StatusBarUtil;
import com.jackmar.jframelibray.view.ErrorView;
import com.jackmar.jframelibray.view.JTitleView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;

/**
 * 底层基类
 */

public abstract class JBaseAct extends FragmentActivity implements IBroadcast, EasyPermissions.PermissionCallbacks, SensorEventListener {

    /**
     * 标题栏
     */
    public JTitleView mJTitleView;

    public ErrorView errorView;
    /**
     * 内容布局
     */
    private RelativeLayout mContentLayout;
    /**
     * activity是否已经create
     */
    private boolean isCreate = false;
    /**
     * 上下文
     */
    protected Context context;
    /**
     * 配置文件
     */
    private JFrameConfig mFrameConfig;
    /**
     * 默认的页面跳转传值的key
     */
    public static String INTENT_KEY = "BundleKey";
    /**
     * 图片选择
     */
    private int CHOOSE_IMAGE_REQUEST = 1010;
    /**
     * 传感器模拟器
     */
    private SensorManager sensorManager;
    /**
     * 传感器
     */
    private Sensor sensor;

    private Vibrator vibrator;

    private boolean isShake;

    private Unbinder unbinder;

    private static final String TAG = "JBaseAct";

    /**
     * 启动页面方法
     *
     * @param tClass 目标页面
     * @param bundle 参数
     */
    public void launch(Class tClass, Bundle bundle) {
        Intent intent = new Intent(context, tClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 启动页面方法
     *
     * @param tClass 目标页面
     */
    public void launch(Class tClass) {
        Intent intent = new Intent(context, tClass);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XActivityManager.getInstance().addToActivityList(this);
        setStatusBar();
        context = this;
        setContentView(R.layout.act_jbase);
        initBaseView();
    }

    private void initBaseView() {
        mFrameConfig = JFrameConfig.getInstance(context);
        mJTitleView = (JTitleView) findViewById(R.id.jtv_title);
        mContentLayout = (RelativeLayout) findViewById(R.id.rl_content);
        errorView = new ErrorView(context);
        errorView.setVisibility(View.GONE);
        ViewUtil.setViewSize(errorView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mFrameConfig.isCreate()) {
            int backImage = mFrameConfig.getBackImage();
            boolean titleCanBack = mFrameConfig.getTitleCanBack();
            int titleBackgroundColorRes = mFrameConfig.getTitleBackgroundColorRes();
            int titleMoreTextColor = mFrameConfig.getTitleMoreTextColor();
            int titleMoreTextSize = mFrameConfig.getTitleMoreTextSize();
            int titleTextColor = mFrameConfig.getTitleTextColor();
            int titleTextSize = mFrameConfig.getTitleTextSize();
            //设置返回按钮
            if (backImage != 0) {
                mJTitleView.setBackImage(backImage);
            }
            //设置是否可以返回
            mJTitleView.setCanBack(titleCanBack);
            //设置标题背景
            if (titleBackgroundColorRes != 0) {
                mJTitleView.setBackgroundResource(titleBackgroundColorRes);
            }
            //设置右侧文字颜色
            if (titleMoreTextColor != 0) {
                mJTitleView.setMoreTextColor(titleMoreTextColor);
            }
            //设置标题颜色
            if (titleTextColor != 0) {
                mJTitleView.setTextColor(titleTextColor);
            }
            //设置标题文字大小
            if (titleTextSize != 0) {
                mJTitleView.setTextSize(titleTextSize);
            }
        }
    }

    public void initSensor() {
        vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SENSOR_DELAY_NORMAL);
    }

    /**
     * 设置内容布局 资源文件//带title
     *
     * @param layoutId 布局文件ID
     */
    public void setLyContent(int layoutId, boolean visible, String title) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(layoutId, null);
        mContentLayout.removeAllViews();
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mContentLayout.addView(view);
        mContentLayout.addView(errorView);
        unbinder = ButterKnife.bind(this);
        if (!visible) {
            mContentLayout.setVisibility(View.INVISIBLE);
        }
        mJTitleView.setTitleText(title);
        initView();
        initData();
    }

    /**
     * 设置内容布局 资源文件//带title
     *
     * @param layoutId 布局文件ID
     */
    public void setLyContentMore(int layoutId, boolean visible, String title, String more) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(layoutId, null);
        mContentLayout.removeAllViews();
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mContentLayout.addView(view);
        mContentLayout.addView(errorView);
        unbinder = ButterKnife.bind(this);
        if (!visible) {
            mContentLayout.setVisibility(View.INVISIBLE);
        }
        mJTitleView.setTitleText(title);
        mJTitleView.setMoreText(more);
        initView();
        initData();
    }

    /**
     * 设置内容布局文件//不带标题
     *
     * @param layoutId
     */
    public void setLyContentNoTitile(int layoutId, boolean visible) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(layoutId, null);
        mContentLayout.removeAllViews();
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mContentLayout.addView(view);
        mContentLayout.addView(errorView);
        unbinder = ButterKnife.bind(this);
        mJTitleView.setVisibility(View.GONE);
        if (!visible) {
            mContentLayout.setVisibility(View.INVISIBLE);
        }
        initView();
        initData();
    }

    /**
     * 设置页面可见
     */
    public void setPageVisible() {
        mContentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化布局//先执行
     */
    public abstract void initView();

    /**
     * 参数设置//后执行
     */
    public abstract void initData();

    /**
     * 设置状态栏沉浸式状态栏，默认为拉通到顶部状态栏
     */
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }


    /**
     * 注册广播接收器
     */
    public void regsiterRe() {
        BroadcastManager.getInstance().registerReceiver(this);
    }

    /**
     * 注销广播接收器
     */
    public void unRegsiterRe() {
        BroadcastManager.getInstance().unregisterReceiver(this);
    }

    /**
     * 设置editText的光标到最后
     *
     * @param editText
     */
    public void setEditTextCursor(EditText editText) {
        if (editText.getText() != null && editText.getText().length() > 0) {
            editText.setSelection(editText.getText().length());
        }
    }

    /**
     * 发送广播
     *
     * @param intent
     * @param filter
     */
    public void sendBroMessage(Intent intent, String filter) {
        BroadcastManager.getInstance().sendBroadcastMessage(this, intent, filter);
    }

    /**
     * 选择图片
     */
    public void chooseImage(int count) {
        ImagePickerUtils.setSelectCount(count);
        Intent intent = new Intent(context, ImageGridActivity.class);
        startActivityForResult(intent, CHOOSE_IMAGE_REQUEST);
    }

    /**
     * 图片返回成功
     *
     * @param images
     */
    public void OnImageSelected(ArrayList<ImageItem> images) {

    }

    /**
     * toast提示
     *
     * @param Msg
     */
    public void showToast(String Msg) {
        ToastUtil.getInstance().showToast(this, Msg);
    }


    @Override
    public void openBroadcastReceiver() {

    }

    @Override
    public void sendBroadcastMessage(String var1) {

    }

    @Override
    public void sendBroadcastMessage(Intent var1, String var2) {

    }

    @Override
    public void onBroadcastMessage(Context context, Intent intent, String filter) {

    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除activity
        XActivityManager.getInstance().removeToActivityList(this);

    }

    /**
     * 权限判断和申请
     *
     * @param Permissions
     * @param des
     * @param requestCode
     * @return
     */
    public boolean requestPermission(String[] Permissions, String des, int requestCode) {
        if (!EasyPermissions.hasPermissions(this, Permissions)) {
            EasyPermissions.requestPermissions(this, des, requestCode,
                    Permissions);
        } else {
            return true;
        }
        return false;
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST) {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                ArrayList<ImageItem> imageItems = new ArrayList<>();
                imageItems.addAll((Collection<? extends ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS));
                if (imageItems.size() > 0) {
                    OnImageSelected(imageItems);
                }
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                vibrator.vibrate(500);
                onShaked();
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) >= 15 || Math.abs(y) >= 15 || Math
                    .abs(z) >= 15) && !isShake) {
                isShake = true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            //开始震动
                            mHandler.obtainMessage(1).sendToTarget();
                            Thread.sleep(1000);
                            isShake = false;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 摇一摇回掉
     */
    public void onShaked() {

    }

    public void unRegisterSensor() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }

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
                        Log.e(TAG, "unsupported object");
                    }
                }
            }

        }
        return bundle;
    }


    public AlertDialog showAlert(String title, String message, String btn, String btn1, DialogInterface.OnClickListener onCancelListener, DialogInterface.OnClickListener onCancelListener1) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btn, onCancelListener)
                .setNegativeButton(btn1, onCancelListener1)
                .create();
        dialog.show();
        return dialog;
    }
}
