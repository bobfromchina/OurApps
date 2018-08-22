package com.lovely3x.common.activities;

public interface OnDismissListener {
    /**
     * This method will be invoked when the dialog is dismissed.
     *
     * @param dialog   The dialog that was dismissed will be passed into the
     *                 method.
     * @param fromUser 关闭的原因是否时因为用户的操作
     */
    void onDismiss(CommonActivity.ProgressDialogInterface dialog, boolean fromUser);
}
