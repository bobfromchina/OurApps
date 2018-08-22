package com.lovely3x.common.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.lovely3x.common.BuildConfig;
import com.lovely3x.common.activities.ActivityManager;
import com.lovely3x.common.activities.BaseCommonActivity;
import com.lovely3x.common.managements.user.UserManager;
import com.lovely3x.common.requests.BaseCodeTable;
import com.lovely3x.common.utils.BaseWeakHandler;

/**
 * 纯 handler Fragment
 * Created by lovely3x on 16/8/31.
 */
public abstract class PureHandlerFragment extends CommonFragment {

    /**
     * 是否需要判断登陆状态
     * 默认为需要判断登陆状态
     */
    protected boolean mNeedJudgeLoginState = true;

    private FragmentWeakHandler mHandler;

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
        if (mNeedJudgeLoginState && response.errorCode == BaseCodeTable.getInstance().getNotLoginCode()) {
            UserManager userManager = UserManager.getInstance();
            userManager.onUserExited();
            launchLoginActivity(null, false, true);
        }
    }


    /**
     * 是否是要holderhere
     *
     * @return
     */
    protected boolean isHoldHere() {
        return mActivity.getIntent().getBooleanExtra(BaseCommonActivity.EXTRA_HOLD_HERE, false);
    }

    /**
     * 启动登陆界面需要判断是否登陆
     *
     * @param compoundsClazz
     * @param bundle
     * @param launchBeforeClearStack
     */
    public void launchActivityNeedLogin(Class<? extends Activity> compoundsClazz, Bundle bundle, boolean launchBeforeClearStack) {
        if (UserManager.getInstance().isSigned()) {
            mActivity.launchActivity(compoundsClazz, bundle, launchBeforeClearStack);
        } else {
            launchLoginActivity(null, false, true);
        }
    }

    /**
     * 启动登陆界面需要判断是否登陆
     */
    public void launchActivityNeedLogin(Class<? extends Activity> compoundsClazz) {
        if (UserManager.getInstance().isSigned()) {
            mActivity.launchActivity(compoundsClazz);
        } else {
            launchLoginActivity(null, false, true);
        }
    }

    /**
     * 启动登陆界面
     *
     * @param holdHere          是否在登陆成功后依旧保留在这个页面，这取决与你的登陆界面的实现
     * @param launchBeforeClear 启动这个activity是否需要清除掉其他的activity
     * @param parameters        启动这个activity你需要传递什么参数
     * @return 是否找到这个登陆界面
     */
    public boolean launchLoginActivity(Bundle parameters, boolean launchBeforeClear, boolean holdHere) {
        Intent intent = new Intent(getLoginAction());
        if (parameters != null) intent.putExtras(parameters);
        //将是否保留在这个界面作为一个参数放到Intent中
        //方便登陆动作实现者取值
        intent.putExtra(BaseCommonActivity.EXTRA_HOLD_HERE, holdHere);
        ComponentName componentName = intent.resolveActivity(mActivity.getPackageManager());
        if (componentName != null) {
            intent.setComponent(componentName);
            ActivityManager.launchActivity(mActivity, intent, launchBeforeClear);
        }
        return componentName != null;
    }


    /**
     * 启动登陆界面,默认不清楚栈数据
     *
     * @param
     */
    public void launchLoginActivity() {
        launchLoginActivity(null, false, true);
    }

    /**
     * 获取登陆的动作，默认是 {@link BaseCommonActivity#ACTION_LOGIN}
     *
     * @return 登陆动作
     */
    public String getLoginAction() {
        return BuildConfig.LOGIN_INTENT_ACTION;
    }


    /**
     * 启动登陆界面需要判断是否登陆
     */
    public void launchActivityNeedLogin(Class<? extends Activity> compoundsClazz, Object... arguments) {
        if (UserManager.getInstance().isSigned()) {
            mActivity.launchActivity(compoundsClazz, arguments);
        } else {
            launchLoginActivity(null, false, true);
        }
    }


    /**
     * 默认的 fragment 弱引用handler
     */
    static class FragmentWeakHandler extends BaseWeakHandler<PureHandlerFragment> {
        public FragmentWeakHandler(PureHandlerFragment outClass) {
            super(outClass);
        }

        @Override
        public void handleMessage(Message msg) {
            final PureHandlerFragment outClass = getOutClass();
            if (outClass != null) {
                outClass.handleMessage(msg);
            }
        }
    }
}
