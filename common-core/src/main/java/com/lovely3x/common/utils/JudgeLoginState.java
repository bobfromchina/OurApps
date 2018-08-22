package com.lovely3x.common.utils;

/**
 * 可以判断登陆状态的接口
 * 实现了则表示可以处理登陆状态判断事件
 * Created by lovely3x on 16-1-18.
 */
public interface JudgeLoginState {
    /**
     * 获取是否是需要判断登陆状态的
     *
     * @return true or false 表示是否需要判断登陆状态
     */
    boolean isNeedLoginJudge();

    /**
     * 设置当前是否需要判断登陆状态
     *
     * @param isNeedJudgeLoginState 设置当前是否需要判断登陆状态
     */
    void setNeedLoginJudge(boolean isNeedJudgeLoginState);
}
