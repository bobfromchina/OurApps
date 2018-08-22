package com.jackmar.jframelibray.view.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * @Title
 * @Author luojiang
 * @Date 2016-05-23 17:14
 */
public class ProgressCircleDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Dialog  dialog;

    private Context context;
    private boolean cancelable;
    private DialogMaker.DialogCallBack mProgressCancelListener;

    public ProgressCircleDialogHandler(Context context, DialogMaker.DialogCallBack mProgressCancelListener,
                                       boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){
        if (null == dialog) {
            dialog = DialogMaker.showCommenWaitDialog(context, "", mProgressCancelListener, cancelable, null);
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void dismissProgressDialog(){
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
