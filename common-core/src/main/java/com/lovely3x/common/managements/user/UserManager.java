package com.lovely3x.common.managements.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.lovely3x.common.beans.LoginStatusBean;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用户管理器
 * 用来管理用户的登陆退出更新
 * 不适用于跨进程
 * Created by lovely3x on 16-1-20.
 */
public class UserManager {

    private static final String TAG = "UserManager";

    /**
     * 用户变化监听器
     */
    private static final List<UserChangedListener> mUserChangedListener = Collections.synchronizedList(new ArrayList<UserChangedListener>());

    /**
     * 用户登陆状态变化监听器
     */
    private static final List<UserLoginStateChangedListener> mUserLoginStateChangedListener = Collections.synchronizedList(new ArrayList<UserLoginStateChangedListener>());

    /**
     * 用户管理器实例
     */
    private static final UserManager INSTANCE = new UserManager();

    /**
     * 操作成功的code值
     */
    private static final int CODE_OK = 0;

    /**
     * 工作线程池
     */
    private ExecutorService executors = Executors.newFixedThreadPool(1);

    /**
     * 主线程消息循环Handler
     */
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());

    private Context mContext;

    /**
     * 当前的用户
     * 用户管理器只会同时操作一个用户
     */
    private IUser mCurrentUser;

    /**
     * 登陆器
     */
    private IUserLander mLander;

    /**
     * 上一个用户
     */
    private static final String EXTRA_PRE_USER = "extra.pre.user";

    /**
     * 新用户
     */
    private static final String EXTRA_NEW_USER = "extra.new.user";

    /**
     * 谁发出的广播
     */
    private static final String EXTRA_FROM = "extra.from";

    /**
     * 通知用户发生变化后执行
     */
    private static final String ACTION_NOTIFY_USER_CHANGED = "user.manager.action.notify.user.changed";

    /**
     * 初始化钩子列表
     */
    private static final List<InitHook> mInitHooks = Collections.synchronizedList(new ArrayList<InitHook>());

    private UserManager() {

    }

    public static UserManager getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     * 多次调用无效，只可以初始化一次
     *
     * @param context 上下文
     */
    public void init(Context context) {
        if (mContext == null) {
            this.mContext = context;
        }
        TokenManager.getInstance().init(mContext);
        //注册用户变化同步监听器
        IntentFilter filter = new IntentFilter(ACTION_NOTIFY_USER_CHANGED);
        mContext.registerReceiver(new Receiver(), filter);

        readUserFromDatabases();

        //Call init hooks.
        for (InitHook hook : mInitHooks) hook.onInit(INSTANCE);
    }

    /**
     * 从数据库中读取用户数据
     */
    private void readUserFromDatabases() {
        initCheck();
        if (Config.DEBUG) ALog.d(TAG, "Read user from databases");
        if (mLander == null) throw new IllegalStateException("在登陆之前你应该线设置登陆器。");
        //从数据库中读取用户
        mCurrentUser = mLander.getLastUser();
    }

    /**
     * 胡群殴的当前的用户对象
     *
     * @return null或当前的对象
     */
    public IUser getCurrentUser() {
        return mCurrentUser;
    }


    /**
     * 初始化检查
     * 判断当前的用户管理器是否已经初始化
     * 如果未初始化抛出状态异常
     */
    @MainThread
    public void initCheck() {
//        ViewUtils.mainThreadChecker();
        if (mContext == null) throw new IllegalStateException();
    }

    /**
     * 登陆
     *
     * @param user 需要登陆的用户
     */
    @MainThread
    public void login(@NonNull final IUser user) {
        initCheck();
//        ViewUtils.mainThreadChecker();
        if (Config.DEBUG) ALog.d(TAG, "Start login: " + user);
        if (mLander == null) throw new IllegalStateException("在登陆之前你应该线设置登陆器。");
        final IUserLander logger = mLander;

        executors.execute(new Runnable() {
            @Override
            public void run() {
                //登陆
                final LoginStatusBean bean = logger.login(user);
                final int retCode = bean.getCode();
//                final int retCode = logger.login(user);
                final ArrayList<UserLoginStateChangedListener> backup = new ArrayList<>(mUserLoginStateChangedListener);

                switch (retCode) {
                    case CODE_OK: {//登陆成功
                        user.setState(IUser.STATE_ONLINE);

                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                //通知用户变化监听器
                                notifyUserChanged(user);

                                //回调登陆状态变化监听器
                                try {
                                    for (UserLoginStateChangedListener ulscl : backup) {
                                        if (ulscl != null) ulscl.onUserLoginSuccessful(user);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    break;
                    default: {//登录失败
                        user.setState(IUser.STATE_OFFLINE);

                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //回调登陆状态变化监听器

                                try {
                                    //通知用户变化监听器
                                    notifyUserChanged(user);
                                } catch (Exception e) {
                                    ALog.e(TAG, e);
                                }

                                try {

                                    for (UserLoginStateChangedListener ulscl : backup) {
                                        if (ulscl != null)
                                            ulscl.onUserLoginFailure(user, retCode,bean.getDesc());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                    break;
                }
            }
        });
    }

    /**
     * 登出
     */
    @MainThread
    public void logout() {
        initCheck();
        if (!isSigned()) return;
        ViewUtils.mainThreadChecker();
        if (mLander == null) throw new IllegalStateException("在登出之前你应该线设置登陆器。");
        executors.execute(new Runnable() {
            @Override
            public void run() {
                final int retCode = mLander.logout(mCurrentUser);
                final ArrayList<UserLoginStateChangedListener> backup = new ArrayList<>(mUserLoginStateChangedListener);

                //我想忽略退出成功与否，因为如果用户点击了退出，你给他一个退出失败是几个意思
                //这个就留给监听器实现者做吧
                switch (retCode) {
                    case CODE_OK: {//退出成功
                        mCurrentUser.setState(IUser.STATE_OFFLINE);
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // TODO: 2017/9/11
                                try {
                                    //回调登陆状态变化监听器
                                    for (UserLoginStateChangedListener ulscl : backup) {
                                        if (ulscl != null)
                                            ulscl.onUserLogoutSuccessful(mCurrentUser);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //通知用户变化监听器
                                notifyUserChanged(mCurrentUser);
                            }
                        });
                    }
                    break;
                    default: {//退出失败
                        mainLooperHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //回调登陆状态变化监听器
                                try {
                                    for (UserLoginStateChangedListener ulscl : backup) {
                                        if (ulscl != null)
                                            ulscl.onUserLogoutFailure(mCurrentUser, retCode);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //通知用户变化监听器
                                notifyUserChanged(mCurrentUser);
                            }
                        });
                    }
                    break;
                }
            }
        });
    }

    /**
     * 通知用户变化方法
     *
     * @param currentUser 当前的用户
     */
    @MainThread
    public void notifyUserChanged(@NonNull final IUser currentUser) {

        Runnable runner = new Runnable() {

            @Override
            public void run() {
                IUser preUser = null;

                if (mCurrentUser != null) preUser = mCurrentUser.clone();

                mCurrentUser = currentUser;

                realNotifyUserChanged(currentUser, preUser);
                sendNotifyUserChangedBroadcast(currentUser, preUser);

            }
        };

        if (ViewUtils.isMainThread()) {
            runner.run();
        } else {
            mainLooperHandler.post(runner);
        }
    }

    /**
     * 执行用户更新
     *
     * @param currentUser 当前的用户
     * @param preUser     上一个用户
     */
    private void realNotifyUserChanged(@NonNull IUser currentUser, IUser preUser) {
        //更新数据库的数据
        mLander.updateOrInsertUser(currentUser);

        ArrayList<UserChangedListener> backup = new ArrayList<>(mUserChangedListener);
        try {
            //通知监听器
            for (UserChangedListener userChangedListener : backup) {
                if (userChangedListener != null)
                    userChangedListener.onUserChanged(preUser, currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送用户发生变化广播
     *
     * @param newUser 当前用户
     * @param preUser 上一个用户
     */
    private void sendNotifyUserChangedBroadcast(IUser newUser, IUser preUser) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRE_USER, preUser);
        intent.putExtra(EXTRA_NEW_USER, newUser);
        intent.putExtra(EXTRA_FROM, hashCode());
        mContext.sendBroadcast(intent);
    }

    /**
     * 更新用户，用户如果发生了变化，可以在外部通过调用这个方法来更新当前的用户
     *
     * @param user 需要更新为的用户
     */
    @MainThread
    public void updateUser(@NonNull IUser user) {
        notifyUserChanged(user);
    }

    /**
     * 添加用户变化监听器
     *
     * @param listener    需要添加的用户变化监听器
     * @param callOnAdded 添加后立即回调当前的用户 如果为true 那么 回调的上一个对象总是null
     */
    @MainThread
    public void addUserChangedListener(UserChangedListener listener, boolean callOnAdded) {
        ViewUtils.mainThreadChecker();
        initCheck();
        mUserChangedListener.add(listener);
        if (callOnAdded) {
            listener.onUserChanged(null, mCurrentUser);
        }
    }

    /**
     * 添加用户变化监听器
     *
     * @param listener 需要添加的用户变化监听器
     */
    @MainThread
    public void addUserChangedListener(UserChangedListener listener) {
        ViewUtils.mainThreadChecker();
        initCheck();
        addUserChangedListener(listener, true);
    }

    /**
     * 移除用户变化监听器
     *
     * @param listener 需要移除的监听器
     */
    @MainThread
    public void removeUserChangedListener(UserChangedListener listener) {
        ViewUtils.mainThreadChecker();
        initCheck();
        mUserChangedListener.remove(listener);
    }

    /**
     * 添加用户状态监听器
     *
     * @param userLoginStateChangedListener 用户状态监听器,监听用户的登陆状态
     */
    public void addUserStateChangedListener(UserLoginStateChangedListener userLoginStateChangedListener) {
        if (userLoginStateChangedListener == null) return;
        if (!mUserLoginStateChangedListener.contains(userLoginStateChangedListener)) {
            mUserLoginStateChangedListener.add(userLoginStateChangedListener);
        }
    }

    /**
     * 添加用户状态监听器
     *
     * @param userLoginStateChangedListener 用户状态监听器,监听用户的登陆状态
     * @param callWhenAdd                   如果当前用户的登录状态为已经登录则立即回调 登录成功,否则回调登出成功
     */
    public void addUserStateChangedListener(UserLoginStateChangedListener userLoginStateChangedListener, boolean callWhenAdd) {
        if (userLoginStateChangedListener == null || mUserLoginStateChangedListener.contains(userLoginStateChangedListener))
            return;

        mUserLoginStateChangedListener.add(userLoginStateChangedListener);
        if (callWhenAdd) {
            if (isSigned()) {
                userLoginStateChangedListener.onUserLoginSuccessful(getCurrentUser());
            } else {
                userLoginStateChangedListener.onUserLogoutSuccessful(getCurrentUser());
            }

        }
    }

    /**
     * 移除用户状态变化监听器
     *
     * @param userLoginStateChangedListener 用户状态变化监听器
     */
    public void removeUserStateChangedListener(UserLoginStateChangedListener userLoginStateChangedListener) {
        if (userLoginStateChangedListener == null) return;
        mUserLoginStateChangedListener.remove(userLoginStateChangedListener);
    }

    /***
     * 当用户已经退出调用这个接口
     * 这个接口主要用于意外退出
     * 例如：有人抢登了账号，在服务器端发出了冲突信号，那么就可以调用这个接口
     */
    @MainThread
    public void onUserExited() {
        if (mCurrentUser != null) {
            IUser newUser = mCurrentUser.clone();
            newUser.setState(IUser.STATE_OFFLINE);
            notifyUserChanged(newUser);
        }
    }

    /**
     * 是否已经登陆
     *
     * @return
     */
    public boolean isSigned() {
        return mCurrentUser != null && mCurrentUser.getState() == IUser.STATE_ONLINE;
    }

    /**
     * 设置当前的登陆器
     *
     * @param mLander 登陆器
     */
    public void setLander(IUserLander mLander) {
        this.mLander = mLander;
    }

    /**
     * 获取当前的登陆器
     *
     * @return 登陆器 或null
     */
    public IUserLander getLander() {
        return mLander;
    }

    public void cancelLogin() {
        if (mLander != null) {
            mLander.cancelLogin();
        }
    }

    /**
     * 用户变化接收器
     */
    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                int from = intent.getIntExtra(EXTRA_FROM, UserManager.this.hashCode());
                if (from != UserManager.this.hashCode()) {
                    if (ACTION_NOTIFY_USER_CHANGED.equals(intent.getAction())) {
                        IUser preUser = intent.getParcelableExtra(EXTRA_PRE_USER);
                        IUser newUser = intent.getParcelableExtra(EXTRA_NEW_USER);
                        if (preUser != null && newUser != null)
                            realNotifyUserChanged(newUser, preUser);
                    }
                }
            }
        }
    }

    /**
     * 注册初始化钩子
     *
     * @param initHook
     */
    public void registerInitHook(InitHook initHook) {
        mInitHooks.add(initHook);
    }

    /**
     * 取消注册初始化钩子
     *
     * @param initHook
     */
    public void unregisterInitHook(InitHook initHook) {
        mInitHooks.remove(initHook);
    }

    /**
     * 初始化钩子
     * 可以注册到用户管理器中
     * 在用户管理器初始化阶段进行回调
     */
    public interface InitHook {
        /**
         * 在用户管理器初始化阶段进行回调
         *
         * @param userManager 用户管理器实例
         */
        void onInit(UserManager userManager);
    }
}
