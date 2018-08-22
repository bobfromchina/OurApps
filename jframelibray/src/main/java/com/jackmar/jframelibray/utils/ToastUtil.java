package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Toast弹出框
 */
public class ToastUtil {
    private static ToastUtil instance;
    public synchronized static ToastUtil getInstance() {
        if (instance == null) {
            instance = new ToastUtil();
        }
        return instance;
    }
         Toast toast = null;
        /**
         * toast
         *
         * @param msg
         *            消息类容
         */
        public  void showToast(Context context, String msg) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }


}
