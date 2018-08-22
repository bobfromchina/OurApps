package com.lovely3x.common.versioncontroller.impls;


import com.lovely3x.common.versioncontroller.Result;
import com.lovely3x.common.versioncontroller.Version;

/**
 * 检查更新进度变化监听器
 * Created by lovely3x on 15-8-27.
 */
public interface CheckStateChangedListener {

    /**
     * 检查
     */
    void onCheck();

    /**
     * 发现新版本
     *
     * @param version 新的版本对象
     */
    void onNewVersionFond(Version version);

    /**
     * 已经是最新版本了
     */
    void onLatestVersion();

    /**
     * 正在下载新版本
     */
    void onObtaining();

    /**
     * 新版本下载完成
     *
     * @param result 下载结果对象
     */
    void onObtained(Result result);

    /**
     * 下载过程中出现错误
     *
     * @param throwable 错误原因
     */
    void onError(Throwable throwable);

}
