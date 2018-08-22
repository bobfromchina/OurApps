package com.example.wangbo.ourapp.utils;


public interface IDialogCallBack {
    /**
     * 点击确认
     *
     * @param tag
     */
    public void onPositiveCase(String tag);

    /**
     * 点击取消
     *
     * @param tag
     */
    public void onNegativeCase(String tag);

}
