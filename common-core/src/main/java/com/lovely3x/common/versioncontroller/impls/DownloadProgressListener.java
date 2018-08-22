package com.lovely3x.common.versioncontroller.impls;


import com.lovely3x.common.versioncontroller.Result;

/**
 * Created by lovely3x on 15-8-25.
 * 下载进度监听器
 */
public interface DownloadProgressListener {

    /**
     * 开始下载
     */
    void onStart();

    /**
     * 发布的进度
     *
     * @param max      文件的总大小 如果无法获取 则为-1
     * @param progress 当前下载文件的进度 如果无法获取则为-1
     */
    void doProgress(int max, int progress);

    /**
     * 发布的百分比进度
     * 如果无法获取进度,返回-1
     *
     * @param progressPercent 当前的进度值
     */
    void doProgress(float progressPercent);

    /**
     * 下载遇到错误
     *
     * @param throwable 错误原因对象
     */
    void onError(Throwable throwable);

    /**
     * 下载成功
     *
     * @param result 下载的结果对象
     */
    void onSuccessful(Result result);

    /**
     * 默认的实现,减少代码
     */
    class SimpleDownloadProgressListener implements DownloadProgressListener {

        @Override
        public void onStart() {

        }

        @Override
        public void doProgress(int max, int progress) {

        }

        @Override
        public void doProgress(float progressPercent) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onSuccessful(Result result) {

        }
    }
}
