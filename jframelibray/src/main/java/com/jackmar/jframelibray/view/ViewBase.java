package com.jackmar.jframelibray.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class ViewBase extends RelativeLayout {
    private Context context;

    public ViewBase(Context context) {
        super(context);
        this.context = context;
    }

    public ViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 启动页面方法
     *
     * @param tClass 目标页面
     * @param bundle 参数
     */
    public void launch(Class tClass, Bundle bundle) {
        Intent intent = new Intent(context, tClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 启动页面方法
     *
     * @param tClass 目标页面
     */
    public void launch(Class tClass) {
        Intent intent = new Intent(context, tClass);
        context.startActivity(intent);
    }
}
