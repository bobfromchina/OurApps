package com.lovely3x.common.managements.user;

import android.os.Parcelable;

/**
 * 用户实体
 * 用于描述一个用户对象
 * 我将用户定义为一个服务器的用户和标识描述对象
 * 这个类定义的属性可能并不完全
 * 你可以自己继承扩展，我们也正希望如此
 * Created by lovely3x on 16-1-20.
 */
public interface IUser extends Cloneable,Parcelable{

    /**
     * 用户的状态：离线
     */
    int STATE_OFFLINE = 0;
    /**
     * 用户的状态：在线
     */
    int STATE_ONLINE = 1;


    /**
     * 设置当前这个用户的状态
     *
     * @param state 需要设置的状态
     */
    void setState(int state);

    /**
     * 获取当前用户的状态
     *
     * @return 用户的状态
     */
    int getState();

    /**
     * 深复制一个当前用户对象
     *
     * @return 复制的对象
     */
    IUser clone();

}
