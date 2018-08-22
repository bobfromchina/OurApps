package com.lovely3x.common.managements.user;

/**
 * 登陆状态发生变化接口
 */
public interface UserLoginStateChangedListener {
    /**
     * 当用户登陆失败
     *
     * @param IUser 登录失败的用户
     * @return 是否需要从新登陆(如果在所有的监听器中有一个返回true, 则代表需要重新登陆)
     */
    boolean onUserLoginFailure(IUser IUser, int errorCode, String desc);

    /**
     * 当用户登陆成功后执行
     *
     * @param IUser 登陆成功的用户
     */
    void onUserLoginSuccessful(IUser IUser);

    /**
     * 当用户登出后调用
     *
     * @param IUser 退出的用户
     */
    void onUserLogoutSuccessful(IUser IUser);

    /**
     * 当用户登出失败后后调用
     *
     * @param IUser     退出失败的用户
     * @param errorCode 错误原因码
     */
    void onUserLogoutFailure(IUser IUser, int errorCode);


    class SimpleUserLoginStateChangedListener implements UserLoginStateChangedListener {

        @Override
        public boolean onUserLoginFailure(IUser IUser, int errorCode, String desc) {
            return false;
        }

        @Override
        public void onUserLoginSuccessful(IUser IUser) {

        }

        @Override
        public void onUserLogoutSuccessful(IUser IUser) {

        }

        @Override
        public void onUserLogoutFailure(IUser IUser, int errorCode) {

        }
    }
}
