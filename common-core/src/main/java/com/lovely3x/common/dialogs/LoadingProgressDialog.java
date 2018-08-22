package com.lovely3x.common.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.activities.CommonActivity;
import com.lovely3x.common.activities.OnReshowListener;

import java.lang.reflect.Field;


/**
 * 加载对话框
 * Created by lovely3x on 16-4-19.
 */
public class LoadingProgressDialog extends DialogFragment implements CommonActivity.ProgressDialogInterface, DialogInterface.OnShowListener {

    /**
     * 显示的提示内容
     */
    private static final String EXTRA_TIP = "extra.tip";
    /**
     * 是否可以取消
     */
    private static final String EXTRA_CANCELABLE = "extra.cancelable";
    /**
     * dialog保存的tag值
     */
    private static final String EXTRA_TAG = "extra.tag";

    private static final String TAG = "LoadingProgressDialog";

    private TextView tvTip;
    private com.lovely3x.common.activities.OnDismissListener mDismissListener;
    private Parcelable mTag;
    private OnReshowListener mmOnReshowListener;
    private OnShowListener mOnShowListener;

    /**
     * 创建一个加载进度对话框示例
     *
     * @param cancelable 是否可以取消
     * @param tip        提示的内容
     * @return 实例
     */
    public static LoadingProgressDialog newInstance(boolean cancelable, String tip) {
        Bundle args = new Bundle();
        args.putString(EXTRA_TIP, tip);
        args.putBoolean(EXTRA_CANCELABLE, cancelable);
        LoadingProgressDialog fragment = new LoadingProgressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 创建一个加载进度对话框示例
     *
     * @param cancelable 是否可以取消
     * @param tip        提示的内容
     * @return 实例
     */
    public static LoadingProgressDialog newInstance(boolean cancelable, String tip, Parcelable tag) {
        Bundle args = new Bundle();
        args.putString(EXTRA_TIP, tip);
        args.putParcelable(EXTRA_TAG, tag);
        args.putBoolean(EXTRA_CANCELABLE, cancelable);
        LoadingProgressDialog fragment = new LoadingProgressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 显示加载对话框
     *
     * @param activity 显示的载体
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity) {
        LoadingProgressDialog lpd = newInstance(true, null);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }

    /**
     * 显示加载对话框
     *
     * @param activity   显示的载体
     * @param cancelable 是否能够取消
     * @param content    显示的内容
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, boolean cancelable, String content) {
        LoadingProgressDialog lpd = newInstance(cancelable, content);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }

    /**
     * 显示加载对话框
     *
     * @param activity   显示的载体
     * @param cancelable 是否能够取消
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, boolean cancelable) {
        LoadingProgressDialog lpd = newInstance(cancelable, null);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }

    /**
     * 显示加载对话框
     *
     * @param activity 显示的载体
     * @param content  显示的内容
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, String content) {
        LoadingProgressDialog lpd = newInstance(true, content);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }


    /**
     * 显示加载对话框
     *
     * @param activity 显示的载体
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, Parcelable tag) {
        LoadingProgressDialog lpd = newInstance(true, null, tag);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }

    /**
     * 显示加载对话框
     *
     * @param activity   显示的载体
     * @param cancelable 是否能够取消
     * @param content    显示的内容
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, boolean cancelable, String content, Parcelable tag) {
        LoadingProgressDialog lpd = newInstance(cancelable, content, tag);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }

    /**
     * 显示加载对话框
     *
     * @param activity   显示的载体
     * @param cancelable 是否能够取消
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, boolean cancelable, Parcelable tag) {
        LoadingProgressDialog lpd = newInstance(cancelable, null, tag);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Class<DialogFragment> clazz = DialogFragment.class;
            Field f = clazz.getDeclaredField("mDismissed");
            f.setAccessible(true);
            f.set(this, false);
            f = clazz.getDeclaredField("mShownByMe");
            f.setAccessible(true);
            f.set(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载对话框
     *
     * @param activity 显示的载体
     * @param content  显示的内容
     * @return 实例
     */
    public static LoadingProgressDialog showProgressDialog(FragmentActivity activity, String content, Parcelable tag) {
        LoadingProgressDialog lpd = newInstance(true, content, tag);
        lpd.show(activity.getSupportFragmentManager(), LoadingProgressDialog.class.getName());
        return lpd;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.transparentDialog);
        dialog.setContentView(R.layout.view_progress_bar);
        tvTip = (TextView) dialog.findViewById(R.id.tv_view_progress_bar_tip);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String tip = arguments.getString(EXTRA_TIP, getString(R.string.loading));
            boolean cancelable = arguments.getBoolean(EXTRA_CANCELABLE, true);
            mTag = arguments.getParcelable(EXTRA_TAG);
            setCancelable(cancelable);
            tvTip.setText(tip);
        }
        if (savedInstanceState != null && mmOnReshowListener != null) {
            mmOnReshowListener.onReshow(this);
        }
        return dialog;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        callbackOnceDismissListener(canceled);
    }

    /**
     * 是否已经取消
     */
    private boolean canceled = false;


    @Override
    public void onCancel(DialogInterface dialog) {
        canceled = true;
        super.onCancel(dialog);

    }

    void callbackOnceDismissListener(boolean fromUser) {
        if (mDismissListener != null) {
            mDismissListener.onDismiss(this, fromUser);
            mDismissListener = null;
        }
    }


    /**
     * 获取当前视图是否被销毁了
     *
     * @return true or false
     */
    public boolean viewDestroyed() {
        try {
            Field field = Class.forName(DialogFragment.class.getName()).getDeclaredField("mViewDestroyed");
            field.setAccessible(true);
            return field.getBoolean(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 设置对话款关闭监听器
     *
     * @param dismissListener 需要设置的监听器
     */
    public void setDismissListener(com.lovely3x.common.activities.OnDismissListener dismissListener) {
        this.mDismissListener = dismissListener;
    }

    @Override
    public void setReshowListener(OnReshowListener reshowListener) {
        this.mmOnReshowListener = reshowListener;
    }

    @Override
    public void setOnShowListener(OnShowListener onShowListener) {
        this.mOnShowListener = onShowListener;
    }

    @Override
    public void setTags(Parcelable tag) {
        this.mTag = tag;
    }

    public Parcelable getTags() {
        return mTag;
    }

    @Override
    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }


    @Override
    public void cancel() {
        if (getDialog() != null) {
            getDialog().cancel();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (mOnShowListener != null) {
            mOnShowListener.onShow(dialog);
        }
    }
}
