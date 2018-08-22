package com.lovely3x.common.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.lovely3x.common.BuildConfig;
import com.lovely3x.common.CommonApplication;
import com.lovely3x.common.R;
import com.lovely3x.common.managements.user.IUser;
import com.lovely3x.common.managements.user.UserLoginStateChangedListener;
import com.lovely3x.common.managements.user.UserManager;
import com.lovely3x.common.requests.BaseCodeTable;
import com.lovely3x.common.utils.BaseWeakHandler;
import com.lovely3x.common.utils.JudgeLoginState;
import com.lovely3x.imageloader.ImageDisplayOptions;

/**
 * 通用类的基础默认实现
 * Created by lovely3x on 16-1-21.
 */
public abstract class BaseCommonActivity extends CommonActivity implements JudgeLoginState {

    /**
     * 用户状态变化监听器
     */
    private final UserLoginStateChangedListener mStateChangedListener = new UserLoginStateChangedListener.SimpleUserLoginStateChangedListener() {
        @Override
        public void onUserLoginSuccessful(IUser IUser) {
            shouldLoadData();
        }

        @Override
        public void onUserLogoutSuccessful(IUser IUser) {
            shouldLoadData();
        }
    };

    /**
     * 是否需要判断登陆状态
     * <p/>
     * 在启动界面的时候 使用
     * {@link Intent#putExtra(String, boolean)}
     * {@link android.content.Context#startActivity(Intent)}
     * 如果指定为true则会在请求返回后判断是否登陆，如果用户未登陆，那么会启动登陆界面
     */
    public static final String EXTRA_NEED_JUDGE_LOGIN_STATE = "key.need_judge.login.state";

    /**
     * 默认的登陆操作动作
     * 一个activity声明了这个属性就被认为是可以登陆的
     */
    public static final String ACTION_LOGIN = BuildConfig.LOGIN_INTENT_ACTION;

    /**
     * 登陆成功后是否保留在登陆前的界面
     * 你想知道？那么就在登陆界面获取intent中的值，是Boolean型的
     */
    public static final String EXTRA_HOLD_HERE = "extra.hold.here";


    /**
     * 是否需要判断登陆状态
     * 默认为需要判断登陆状态
     */
    protected boolean mNeedJudgeLoginState = true;

    private ActivityWeakHandler mHandler;

    /**
     * 调用这个方法后将会创建一个Handler(如果没有创建)
     */
    protected BaseWeakHandler getHandler() {
        if (mHandler == null) {

            mHandler = new ActivityWeakHandler(this);
        }
        return mHandler;
    }

    @Override
    protected void onInitExtras(@NonNull Bundle bundle) {
        super.onInitExtras(bundle);
        this.mNeedJudgeLoginState = bundle.getBoolean(EXTRA_NEED_JUDGE_LOGIN_STATE, true);
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
        if (response.errorCode == BaseCodeTable.getInstance().getNotLoginCode()) {
            UserManager userManager = UserManager.getInstance();
            userManager.onUserExited();
            if (mNeedJudgeLoginState) launchLoginActivity(null, false, true);
        } else if (response.errorCode == BaseCodeTable.getInstance().getNotBindPhoneCode()) {
            launchBindPhoneActivity(null, false, true);
        }
    }

    /**
     * 启动登陆界面
     *
     * @param launchBeforeClearStack 启动之前先清除栈数据
     */
    public void launchLoginActivity(boolean launchBeforeClearStack) {
        launchLoginActivity(null, false, true);
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
        intent.putExtra(EXTRA_HOLD_HERE, holdHere);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            intent.setComponent(componentName);
            ActivityManager.launchActivity(this, intent, launchBeforeClear);
        }
        return componentName != null;
    }

    @Override
    public boolean isNeedLoginJudge() {
        return mNeedJudgeLoginState;
    }

    @Override
    public void setNeedLoginJudge(boolean isNeedJudgeLoginState) {
        this.mNeedJudgeLoginState = isNeedJudgeLoginState;
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
     * 获取登陆的动作，默认是 {@link #ACTION_LOGIN}
     *
     * @return 登陆动作
     */
    public String getLoginAction() {
        return ACTION_LOGIN;
    }

    /**
     * 获取绑定手机的动作，默认是 {@link BuildConfig#BIND_PHONE_ACTION}
     *
     * @return 登陆动作
     */
    public String getBindPhoneAction() {
        return BuildConfig.BIND_PHONE_ACTION;
    }

    /**
     * 启动绑定手机的界面
     *
     * @param holdHere          是否在绑定成功后依旧保留在这个页面，这取决与你的绑定手机界面的实现
     * @param launchBeforeClear 启动这个activity是否需要清除掉其他的activity
     * @param parameters        启动这个activity你需要传递什么参数
     * @return 是否找到这个绑定手机界面
     */
    public boolean launchBindPhoneActivity(Bundle parameters, boolean launchBeforeClear, boolean holdHere) {
        Intent intent = new Intent(getBindPhoneAction());
        if (parameters != null) intent.putExtras(parameters);
        //将是否保留在这个界面作为一个参数放到Intent中
        //方便登陆动作实现者取值
        intent.putExtra(BaseCommonActivity.EXTRA_HOLD_HERE, holdHere);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            intent.setComponent(componentName);
            ActivityManager.launchActivity(this, intent, launchBeforeClear);
        }
        return componentName != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) restoreInstanceOnCreateBefore(savedInstanceState);
        int contentView = getContentView();

        if (contentView > 0) {
            setContentView(getContentView());
            initViews();
            onViewInitialized();
            UserManager.getInstance().addUserStateChangedListener(mStateChangedListener, true);
        } else {
            View content = getInflatedContentView();
            if (content != null) {
                setContentView(content);
                initViews();
                onViewInitialized();
                UserManager.getInstance().addUserStateChangedListener(mStateChangedListener, true);
            }
        }

        if (savedInstanceState != null) restoreInstanceOnCreateAfter(savedInstanceState);
    }

    /**
     * 应该加载数据
     * 什么情况会回调?
     * 1,界面初始化完成后会调用一次(在调用{@link #onViewInitialized()} 调用后执行)
     * 2,用户登录状态(登出或登录成功)发生变化后会调用(每次变化都会调用)
     */
    protected void shouldLoadData() {
        //// TODO: 16/6/18
    }

    /**
     * 获取子类设置的布局id
     *
     * @return 子类设置的布局id
     */
    @LayoutRes
    protected abstract int getContentView();

    /**
     * 获取获取初始化完成的视图
     *
     * @return
     */
    protected View getInflatedContentView() {
        return null;
    }

    /**
     * 初始化视图
     */
    protected abstract void initViews();

    /**
     * 视图初始化完成
     * 这个方法执行完成之后将立即执行{@link #restoreInstanceOnCreateAfter(Bundle)}
     */
    protected abstract void onViewInitialized();


    /**
     * 恢复实例状态
     * 会在在onCreate之前调用
     *
     * @param savedInstance 保存实例状态的载体
     */
    public abstract void restoreInstanceOnCreateBefore(@NonNull Bundle savedInstance);

    /**
     * 恢复实例状态
     * 会在在onCreate执行完毕后调用
     *
     * @param savedInstance 保存实例状态的载体
     */
    public abstract void restoreInstanceOnCreateAfter(@NonNull Bundle savedInstance);

    /**
     * 保存实例状态
     *
     * @param outState 保存实例状态的载体
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 是否是要holderhere
     *
     * @return
     */
    protected boolean isHoldHere() {
        return getIntent().getBooleanExtra(EXTRA_HOLD_HERE, false);
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
            launchActivity(compoundsClazz, bundle, launchBeforeClearStack);
        } else {
            launchLoginActivity(null, false, true);
        }
    }

    /**
     * 启动登陆界面需要判断是否登陆
     */
    public void launchActivityNeedLogin(Class<? extends Activity> compoundsClazz) {
        if (UserManager.getInstance().isSigned()) {
            launchActivity(compoundsClazz);
        } else {
            launchLoginActivity(null, false, true);
        }
    }

    /**
     * 启动登陆界面需要判断是否登陆
     */
    public void launchActivityNeedLogin(Class<? extends Activity> compoundsClazz, Object... arguments) {
        if (UserManager.getInstance().isSigned()) {
            launchActivity(compoundsClazz, arguments);
        } else {
            launchLoginActivity(null, false, true);
        }
    }

    /**
     * 获取默认的imageloader加载选项
     *
     * @return
     */
    public ImageDisplayOptions getDefaultImageLoaderOptions() {
        return CommonApplication.getInstance().getImageLoaderDisplayOptionsBuilder().build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.getInstance().removeUserStateChangedListener(mStateChangedListener);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    /**
     * 默认的 fragment 弱引用handler
     */
    static class ActivityWeakHandler extends BaseWeakHandler<BaseCommonActivity> {
        public ActivityWeakHandler(BaseCommonActivity outClass) {
            super(outClass);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseCommonActivity outClass = getOutClass();
            if (outClass != null) {
                outClass.handleMessage(msg);
            }
        }
    }
}
