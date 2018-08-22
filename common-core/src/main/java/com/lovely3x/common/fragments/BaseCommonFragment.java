package com.lovely3x.common.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.lovely3x.common.activities.BaseCommonActivity;
import com.lovely3x.common.managements.user.UserManager;
import com.lovely3x.common.requests.BaseCodeTable;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.BaseWeakHandler;
import com.lovely3x.common.utils.Response;

/**
 * 基类Fragment
 * Created by lovely3x on 16-1-22.
 */
public abstract class BaseCommonFragment extends CommonFragment {

    public static final String NEW_INSTANCE_METHOD_NAME = "newInstance";

    protected FragmentWeakHandler mHandler;
    protected BaseCommonActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseCommonActivity) super.mActivity;
    }

    /**
     * 调用这个方法后将会创建一个Handler(如果没有创建)
     */
    protected BaseWeakHandler getHandler() {
        if (mHandler == null) {
            mHandler = new FragmentWeakHandler(this);
        }
        return mHandler;
    }

    /**
     * 处理消息
     *
     * @param msg 消息对象
     */
    public void handleMessage(Message msg) {
        if (msg.obj instanceof com.lovely3x.common.utils.Response) {
            handleResponseMessage(msg, (com.lovely3x.common.utils.Response) msg.obj);
        }
    }

    /**
     * 处理Response消息
     *
     * @param response response 对象
     */
    protected void handleResponseMessage(Message msg, com.lovely3x.common.utils.Response response) {
        if (isDetached() || getHost() == null) {
            discardResponse(response);
        } else {
            if (response.errorCode == BaseCodeTable.getInstance().getNotLoginCode()) {
                UserManager userManager = UserManager.getInstance();
                userManager.onUserExited();
                if ((mActivity != null && mActivity.isNeedLoginJudge())) {
                    mActivity.launchLoginActivity(null, false, true);
                }
            } else if (response.errorCode == BaseCodeTable.getInstance().getNotBindPhoneCode()) {
                mActivity.launchBindPhoneActivity(null, false, true);
            }

        }
    }

    /**
     * 丢弃服务端的响应对象
     *
     * @param response
     */
    protected void discardResponse(Response response) {
        ALog.d(TAG, "Discard Response " + response);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    /**
     * 默认的 fragment 弱引用handler
     */
    static class FragmentWeakHandler extends BaseWeakHandler<BaseCommonFragment> {
        public FragmentWeakHandler(BaseCommonFragment outClass) {
            super(outClass);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseCommonFragment outClass = getOutClass();
            if (outClass != null) {
                outClass.handleMessage(msg);
            }
        }
    }

    /**
     * 拨打电话
     *
     * @param phone 电话号码
     */
    public void callPhone(String phone) {
        callPhonePermission(phone);
    }

    /**
     * 拨打电话
     */
    private void callPhonePermission(String phone) {
        if (ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, android.Manifest.permission.CALL_PHONE)) {
                mActivity.showToast("您还未设置或已关闭拨打电话的权限，请设置权限信息。");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            }
        } else {
            // 已经获得授权，可以打电话
            CallPhone(phone);
        }
    }

    private void CallPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
