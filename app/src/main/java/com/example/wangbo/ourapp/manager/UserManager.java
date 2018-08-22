package com.example.wangbo.ourapp.manager;

import android.app.Activity;
import android.content.Context;

import com.example.wangbo.ourapp.OurApplication;
import com.example.wangbo.ourapp.activity.LoginAct;
import com.example.wangbo.ourapp.bean.UserEntity;
import com.jackmar.jframelibray.utils.PreHelper;
import com.jackmar.jframelibray.utils.PreferenceKey;

/**
 * Created by JackMar on 2016/12/14.
 * <p>
 * 用户管理
 */
public class UserManager {

    private UserManager() {

    }

    private static UserManager single = new UserManager();

    public synchronized static UserManager getInstance() {
        return single;
    }

    /**
     * 检查登录状态
     *
     * @param toLogin 跳转到登陆
     */
    public boolean checkLogin(Context context, boolean toLogin) {
        boolean loginState = false;
        try {
            loginState = PreHelper.defaultCenter(context).getBooleanData(PreferenceKey.LOGIN_STATE);
            if (!loginState) {
                if (toLogin) {
                    LoginAct.Companion.start(context);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginState;
    }

    /**
     * 更新缓存得用户信息
     */
    public void updateUser(Activity context, UserEntity user, boolean needFinish) {
        if (user != null) {
            PreHelper.defaultCenter(context).putObject(PreferenceKey.USER_INFO, user);
            PreHelper.defaultCenter(context).setData(PreferenceKey.LOGIN_STATE, true);
            if (needFinish) {
                context.finish();
            }
        }
    }

    /**
     * 获取用户信息
     */
    public UserEntity getUser() {
        UserEntity infoBean = null;
        if (UserManager.getInstance().checkLogin(OurApplication.getInstance(), false)) {
            try {
                infoBean = PreHelper.defaultCenter(OurApplication.getInstance()).getObject(PreferenceKey.USER_INFO, UserEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return infoBean;
    }

    public String getUserId() {
        UserEntity infoBean = null;
        if (UserManager.getInstance().checkLogin(OurApplication.getInstance(), false)) {
            try {
                infoBean = PreHelper.defaultCenter(OurApplication.getInstance()).getObject(PreferenceKey.USER_INFO, UserEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert infoBean != null;
        return infoBean.getUser().getTokenId();
    }

    /**
     * 退出登陆
     */
    public void logOut(Context context) {
        PreHelper.defaultCenter(context).setData(PreferenceKey.USER_INFO, "");
        PreHelper.defaultCenter(context).putStringSet(PreferenceKey.COOKIE, null);
        PreHelper.defaultCenter(context).setData(PreferenceKey.LOGIN_STATE, false);
        LoginAct.Companion.start(context);
//        XActivityManager.getInstance().finishOtherActivity(LoginAct.class);
    }
}
