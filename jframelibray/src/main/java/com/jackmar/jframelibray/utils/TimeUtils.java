package com.jackmar.jframelibray.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * @author zrx
 * @Title:TimeUtils
 * @Description: 计时类
 * @date 2015-11-5 上午9:54:50
 */
public class TimeUtils extends CountDownTimer {
    private TextView checking;

    /**
     * @param millisInFuture    参数依次为总时长
     * @param countDownInterval 和计时的时间间隔
     * @param checked           界面展示时间控件
     */
    public TimeUtils(long millisInFuture, long countDownInterval, TextView checked) {
        super(millisInFuture, countDownInterval);
        checking = checked;
    }

    /**
     * 计时完毕时触发
     */
    @Override
    public void onFinish() {
        checking.setTextColor(Color.parseColor("#6990e3"));
        checking.setText("重新发送");
        checking.setEnabled(true);
    }

    /**
     * 计时过程显示
     */
    @Override
    public void onTick(long millisUntilFinished) {
        checking.setEnabled(false);
        checking.setTextColor(Color.parseColor("#6990e3"));
        checking.setText(millisUntilFinished / 1000 + "秒");
    }
}