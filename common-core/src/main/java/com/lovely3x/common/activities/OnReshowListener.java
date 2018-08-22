package com.lovely3x.common.activities;

/**
 * 重新显示监听器
 */
public interface OnReshowListener {

    /**
     * 当这个对话框重新被显示后执行
     *
     * @param dialog
     */
    void onReshow(CommonActivity.ProgressDialogInterface dialog);
}
