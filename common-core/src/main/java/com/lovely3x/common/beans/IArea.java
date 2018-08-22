package com.lovely3x.common.beans;

/**
 * 地区的抽象
 * Created by lovely3x on 15-11-25.
 */
public interface IArea {


    /**
     * 获取描述码
     *
     * @return 描述码
     */
    int getCode();


    /**
     * 获取显示的名字
     *
     * @return 显示的名字
     */
    String getName();

    /**
     * 获取父级的id
     *
     * @return 父级id
     */
    int getParentCode();

}
