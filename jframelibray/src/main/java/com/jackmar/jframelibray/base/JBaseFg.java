package com.jackmar.jframelibray.base;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.adapter.ImgBrowserPagerAdapter;
import com.jackmar.jframelibray.base.broadcasts.BroadcastManager;
import com.jackmar.jframelibray.base.broadcasts.IBroadcast;
import com.jackmar.jframelibray.bean.Img;
import com.jackmar.jframelibray.utils.ToastUtil;
import com.jackmar.jframelibray.utils.imagepicker.ImagePickerUtils;
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
 * @Title fragment 基类
 * @Author JackMar
 * @Date 2016-05-23 17:14
 */
public abstract class JBaseFg extends Fragment implements IBroadcast, SensorEventListener {

    private static final String TAG = "JBaseFg";

    public Activity context;

    protected Dialog dialog;

    protected View rootView = null;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        regsiterRe();
    }

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

    public void initSensor() {
        vibrator = (Vibrator) getActivity().getSystemService(Activity.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SENSOR_DELAY_NORMAL);
    }

    public void unRegisterSensor() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }


    /**
     * 摇一摇回掉
     */
    public void onShaked() {

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
    }


    /**
     * 参数设置
     */
    protected abstract void initView();

    /**
     * 参数设置
     */
    protected abstract void initData();


    /**
     * 初始化layouy
     *
     * @return
     */
    protected abstract int getLayoutId();

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


    //权限判断和申请
    protected boolean requestPermission(String[] Permissions, String des, int requestCode) {
        if (!EasyPermissions.hasPermissions(getActivity(), Permissions)) {
            EasyPermissions.requestPermissions(this, des, requestCode,
                    Permissions);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unRegsiterRe();
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
     * 发送广播
     *
     * @param intent
     * @param filter
     */
    public void sendBroMessage(Intent intent, String filter) {
        BroadcastManager.getInstance().sendBroadcastMessage(this, intent, filter);
    }

    public void showToast(String Msg) {
        ToastUtil.getInstance().showToast(getActivity(), Msg);
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
        return getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST) {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                ArrayList<ImageItem> imageItems = new ArrayList<>();
                imageItems.addAll((Collection<? extends ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS));
                if (imageItems.size() != 0) {
                    OnImageSelected(imageItems);
                }
            }
        }
    }

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

    @Override
    public void onPause() {
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
        ActivityManager.launchActivity(context, compoundsClazz, bundle, launchBeforeClearStack);
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
        ActivityManager.launchActivityForResult(context, compoundsClazz, bundle, launchBeforeClearActivities, requestCode);
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
     * 在新的界面中显示图片
     *
     * @param imgs 图片集合
     * @param type 图片的类型，这里的类型应该不包括Drawable资源和Bitmap
     */
    public void showImgOnNewActivity(List<String> imgs, int type, int index) {
        if (imgs == null || imgs.isEmpty()) return;
        ArrayList<Img> images = new ArrayList<>();
        int failureResId = R.drawable.icon_loading;
        int loadingResId = R.drawable.icon_loading;
        for (int i = 0; i < imgs.size(); i++) {
            images.add(new Img(type, imgs.get(i), null, failureResId, loadingResId, null, null));
        }
        showImgOnNewActivity(images, index);
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
