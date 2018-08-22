package com.lovely3x.common.managements.user;

/**
 * 用户变化监听器
 */
public interface UserChangedListener {
    /**
     * 当用户发生变化后执行
     *
     * @param preUser     上一个用户
     * @param currentUser 当前用户
     */
    void onUserChanged(IUser preUser, IUser currentUser);
}
