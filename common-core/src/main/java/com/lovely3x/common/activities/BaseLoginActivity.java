package com.lovely3x.common.activities;

import android.support.annotation.NonNull;

import com.lovely3x.common.managements.user.IUser;
import com.lovely3x.common.managements.user.UserManager;

/**
 * 默认的登陆界面处理
 * 这个类不包含界面处理，只有逻辑
 * 所以，你应该事先这个类，应将这个类的action设置为{@link #ACTION_LOGIN}
 * 这样方便其他的地方调用
 * Created by lovely3x on 16-1-21.
 */
public abstract class BaseLoginActivity extends BaseCommonActivity {

    /**
     * 登陆
     *
     * @param user 需要登陆的用户
     */
    public void login(@NonNull IUser user) {
        UserManager.getInstance().login(user);
    }

    /**
     * 当用户登陆登录失败后调用
     *
     * @param user      登陆失败的用户
     * @param errorCode 失败的原因
     * @param desc      失败的描述
     */
    public abstract void onLoginFailure(@NonNull IUser user, int errorCode, String desc);

    /**
     * 登陆成功
     *
     * @param user 登陆成功的用户
     */
    public abstract void onLoginSuccessful(@NonNull IUser user);


}
