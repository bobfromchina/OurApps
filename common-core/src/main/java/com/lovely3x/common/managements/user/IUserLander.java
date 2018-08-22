package com.lovely3x.common.managements.user;

import android.support.annotation.NonNull;

import com.lovely3x.common.beans.LoginStatusBean;

import java.util.List;

/**
 * 用户登陆器
 * 实现用户的登陆退出功能
 * Created by lovely3x on 16-1-20.
 */
public interface IUserLander {

    /**
     * 登陆用户
     * 在登陆用户成功后，你可以将用户的信息保存在传递进来的这个用户上
     * 他会保存用户管理器中，但是如果你使用新的用户去替换，那么他将丢失
     * 所以你在更新用户的时候
     * 如果你使用{@link UserManager#notifyUserChanged(IUser)}
     * 这个方法来更新用户的话，那么你应该注意旧的用户数据会被新传递的用户所替换
     *
     * @param user 需要登陆的用户
     * @return 0 表示登陆成功，其他值标志登陆失败
     */
    LoginStatusBean login(@NonNull IUser user);
//    int login(@NonNull IUser user);

    /**
     * 登陆用户
     * 实现用户的登出功能
     *
     * @param user 需要登陆的用户
     * @return 0表示登出成功，其他表示登陆失败
     */
    int logout(@NonNull IUser user);

    /**
     * 从数据库中获取当前已经存在的用户
     *
     * @return 获取当前数据库中所有的用户
     */
    @NonNull
    List<? extends IUser> getAllUserFromDatabases();

    /**
     * 获取最新登陆的一个用户
     *
     * @return null或最后登陆的一个用户
     */
    IUser getLastUser();

    /**
     * 更新或者是插入一个用户对象到数据库中
     * 如果数据库中没有该对象则插入否则则是更新
     *
     * @param user 需要更新或插入的用户对象
     */
    void updateOrInsertUser(IUser user);

    /**
     * 取消登陆操作
     */
    void cancelLogin();

    /**
     * 取消登出操作
     */
    void cancelLogout();
}
