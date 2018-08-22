package com.lovely3x.common.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.CommonUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by lovely3x on 15-7-30.
 * activity管理器
 * 主要用来记录启动了的activity,和她的状态
 */
public class ActivityManager {

    /**
     * 启动状态 等待回复
     */
    public static final int LAUNCH_STATE_WAITING_REPLY = 0x1;
    /**
     * 启动状态 启动成功
     */
    public static final int LAUNCH_STATE_OK = 0x2;
    /**
     * 等待activity 响应超时 ,不管他(如果可以就将它清除掉,可惜碰不到它
     */
    public static final int LAUNCH_STATE_TIME_OUT = 0x3;
    /**
     * 下面的7个状态对应了 activity中相应的状态
     */
    public static final int ACTIVITY_STATE_ON_CREATE = 0X1;
    public static final int ACTIVITY_STATE_ON_START = 0X2;
    public static final int ACTIVITY_STATE_ON_RESUME = 0X3;
    public static final int ACTIVITY_STATE_ON_RESTART = 0X4;
    public static final int ACTIVITY_STATE_ON_PAUSE = 0X5;
    public static final int ACTIVITY_STATE_ON_STOP = 0X6;
    public static final int ACTIVITY_STATE_ON_DESTROY = 0X7;
    /**
     * 所有的activity表
     */
    public static final LinkedHashMap<WeakReference<Activity>, Integer> sAllActivityStatus = new LinkedHashMap<>();
    private static final String TAG = "ActivityManager";
    /**
     * 启动超时
     */
    private static final long TIME_OUT = 1000 * 2;

    /**
     * 上一个更新状态的activity
     */
    public static ActivityWrapper previousActivity;

    /**
     * 当前的状态
     */
    private static int currentState = LAUNCH_STATE_OK;
    /**
     * 计时器
     */
    private static CountDownTimer countDownTimer;

    private static Link sLink = new Link();

    /**
     * 更新activity的状态
     *
     * @param activity 需要更新的activity
     * @param state    需要更新的状态
     */
    public static void updateState(Activity activity, Integer state) {
        if (Config.DEBUG) {
            ALog.d(TAG, String.format("update state [%s] for [%s]", stateToString(state), activity));
        }
        synchronized (ActivityManager.class) {
            updateStopRecord(activity, state);
            switch (state) {
                case ACTIVITY_STATE_ON_CREATE:
                case ACTIVITY_STATE_ON_START:
                case ACTIVITY_STATE_ON_RESTART:
                case ACTIVITY_STATE_ON_RESUME: {
                    sLink.previousAct = sLink.current;
                    sLink.current = new WeakReference<>(activity);
                    sAllActivityStatus.put(new ActivityWeakReference(activity), state);
                    break;
                }
                case ACTIVITY_STATE_ON_PAUSE:
                case ACTIVITY_STATE_ON_STOP:
                case ACTIVITY_STATE_ON_DESTROY: {
                    if (sLink.previousAct != null) sLink.previousAct.clear();
                    sLink.previousAct = null;
                    sAllActivityStatus.put(new ActivityWeakReference(activity), state);
                    break;
                }
            }
        }
    }

    /**
     * 更新停止记录
     *
     * @param activity 被停止的activity
     */
    static void updateStopRecord(Activity activity, int state) {
        synchronized (ActivityManager.class) {
            previousActivity = new ActivityWrapper(activity, state);
        }
    }

    /**
     * 是否是从后台进入到前台,你可能需要在onReStart处中调用
     * 但是,这个方法无法判断应用第一次进入从后台转入前台的这种情况
     * <p/>
     * 如果是第一次的话应该判断是否activity栈中只有一个activity
     *
     * @param activity 执行判断的activity
     * @return true if the activity from background to foreground,false otherwise
     */
    public static boolean isFromBackgroundToForeground(Activity activity) {
        synchronized (ActivityManager.class) {
            String currentName = activity.getClass().getName();
            return previousActivity != null && currentName.equals(previousActivity.activity) && (previousActivity.state == ACTIVITY_STATE_ON_STOP || previousActivity.state == ACTIVITY_STATE_ON_DESTROY);
        }
    }

    /**
     * 获取指定的activity的状态
     *
     * @param activity 需要获取状态的activity
     * @return ACTIVITY_STATE_INVALID 如果没有该activity 否则 范湖对应的状态
     */
    public static Integer getActivityState(Activity activity) {
        synchronized (ActivityManager.class) {
            Integer state = sAllActivityStatus.get(activity);
            return state == null ? ACTIVITY_STATE_ON_DESTROY : state;
        }
    }

    /**
     * 启动指定的界面
     *
     * @param compoundsClazz         需要启动的activity组件名
     * @param bundle                 需要传递的数据
     * @param launchBeforeClearStack 启动之前先清除栈数据
     */
    public static void launchActivity(Activity context, Class<? extends Activity> compoundsClazz, Bundle bundle, boolean launchBeforeClearStack) {
        synchronized (ActivityManager.class) {
            switch (currentState) {
                case LAUNCH_STATE_OK:
                case LAUNCH_STATE_TIME_OUT:

                    if (Config.DEBUG) {
                        ALog.d(TAG, String.format("Launch activity [%s]", compoundsClazz));
                    }

                    Intent intent = new Intent(context, compoundsClazz);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }

                    //更新状态
                    currentState = LAUNCH_STATE_WAITING_REPLY;
                    initCountDownTimer();
                    context.startActivity(intent);
                    if (launchBeforeClearStack) {
                        finishAllExceptThis(compoundsClazz);
                    }
                    break;
                case LAUNCH_STATE_WAITING_REPLY:
                    if (Config.DEBUG) {
                        ALog.d(TAG, "Waiting activity reply,so can't launch other activity");
                    }
                    break;
            }
        }
    }


    /**
     * 启动界面
     *
     * @param context                activity
     * @param compoundsClazz         需要启动的activity组件
     * @param bundle                 需要传递的数据过去
     * @param launchBeforeClearStack 启动前,是否清除掉activity栈
     * @param requestCode            请求码
     */
    public static void launchActivityForResult(Activity context, Class<? extends Activity> compoundsClazz, Bundle bundle, boolean launchBeforeClearStack, int requestCode) {
        synchronized (ActivityManager.class) {
            switch (currentState) {
                case LAUNCH_STATE_OK:
                case LAUNCH_STATE_TIME_OUT:

                    if (Config.DEBUG) {
                        ALog.d(TAG, String.format("Launch activity [%s]", compoundsClazz));
                    }

                    Intent intent = new Intent(context, compoundsClazz);
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }

                    //更新状态
                    currentState = LAUNCH_STATE_WAITING_REPLY;

                    if (launchBeforeClearStack) {
                        finishAllExceptThis(compoundsClazz);
                    }

                    initCountDownTimer();
                    context.startActivityForResult(intent, requestCode);

                    break;
                case LAUNCH_STATE_WAITING_REPLY:
                    if (Config.DEBUG) {
                        ALog.d(TAG, "Waiting activity reply,so can't launch other activity");
                    }
                    break;
            }
        }
    }

    /**
     * 启动界面
     *
     * @param activity               activity
     * @param intent                 intent意图
     * @param launchBeforeClearStack 启动前,是否清除掉activity栈
     * @param requestCode            请求码
     */
    public static void launchActivityForResult(Activity activity, Intent intent, boolean launchBeforeClearStack, int requestCode) {
        synchronized (ActivityManager.class) {
            switch (currentState) {
                case LAUNCH_STATE_OK:
                case LAUNCH_STATE_TIME_OUT:

                    if (Config.DEBUG) {
                        ALog.d(TAG, String.format("Launch activity for result [%s]", intent.getComponent()));
                    }


                    //更新状态
                    currentState = LAUNCH_STATE_WAITING_REPLY;
                    if (launchBeforeClearStack) {
                        try {
                            finishAllExceptThis((Class<? extends Activity>) Class.forName(intent.getComponent().getClassName()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    initCountDownTimer();
                    activity.startActivityForResult(intent, requestCode);


                    break;
                case LAUNCH_STATE_WAITING_REPLY:
                    if (Config.DEBUG) {
                        ALog.d(TAG, "Waiting activity reply,so can't launch other activity");
                    }
                    break;
            }
        }
    }

    /**
     * 启动界面
     *
     * @param activity               activity
     * @param intent                 intent意图
     * @param launchBeforeClearStack 启动前,是否清除掉activity栈
     */
    public static void launchActivity(Activity activity, Intent intent, boolean launchBeforeClearStack) {
        synchronized (ActivityManager.class) {
            switch (currentState) {
                case LAUNCH_STATE_OK:
                case LAUNCH_STATE_TIME_OUT:
                    if (Config.DEBUG) {
                        ALog.d(TAG, String.format("Launch activity [%s]", intent.getComponent()));
                    }
                    //更新状态
                    currentState = LAUNCH_STATE_WAITING_REPLY;

                    if (launchBeforeClearStack) {
                        try {
                            finishAllExceptThis((Class<? extends Activity>) Class.forName(intent.getComponent().getClassName()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    initCountDownTimer();
                    activity.startActivity(intent);
                    break;
                case LAUNCH_STATE_WAITING_REPLY:
                    if (Config.DEBUG) {
                        ALog.d(TAG, "Waiting activity reply,so can't launch other activity");
                    }
                    break;
            }
        }
    }


    /**
     * 启动界面
     *
     * @param activity               activity
     * @param intent                 intent意图
     * @param launchBeforeClearStack 启动前,是否清除掉activity栈
     */
    public static void launchActivity(Context activity, Intent intent, boolean launchBeforeClearStack) {
        synchronized (ActivityManager.class) {
            switch (currentState) {
                case LAUNCH_STATE_OK:
                case LAUNCH_STATE_TIME_OUT:
                    if (Config.DEBUG) {
                        ALog.d(TAG, String.format("Launch activity [%s]", intent.getComponent()));
                    }
                    //更新状态
                    currentState = LAUNCH_STATE_WAITING_REPLY;

                    if (launchBeforeClearStack) {
                        try {
                            finishAllExceptThis((Class<? extends Activity>) Class.forName(intent.getComponent().getClassName()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    initCountDownTimer();
                    activity.startActivity(intent);

                    break;
                case LAUNCH_STATE_WAITING_REPLY:
                    if (Config.DEBUG) {
                        ALog.d(TAG, "Waiting activity reply,so can't launch other activity");
                    }
                    break;
            }
        }
    }


    /**
     * activity被启动后的回执
     */
    public static void launchReply() {
        synchronized (ActivityManager.class) {
            currentState = LAUNCH_STATE_OK;
            if (Config.DEBUG) {
                ALog.d(TAG, String.format(Locale.US, "[%s] replied.", previousActivity == null ? null : previousActivity.activity));
            }
            if (countDownTimer != null) countDownTimer.cancel();
        }
    }

    /**
     * activity启动超时后执行
     */
    public static void launchTimeOut() {
        synchronized (ActivityManager.class) {
            if (Config.DEBUG) {
                ALog.d(TAG, "Update current state launch timeout");
            }
            currentState = LAUNCH_STATE_TIME_OUT;
            if (countDownTimer != null) countDownTimer.cancel();
        }
    }

    /**
     * 获取当前所有的activity
     *
     * @return activities
     */
    public static List<Activity> getActivities() {
        ArrayList<Activity> acts = new ArrayList<>();

        for (WeakReference<Activity> next : sAllActivityStatus.keySet()) {
            if (next != null) {
                if (next.get() != null) acts.add(next.get());
            }
        }
        return acts;
    }

    /**
     * 初始化计时器
     */
    private static void initCountDownTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(TIME_OUT, TIME_OUT) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                launchTimeOut();
            }
        };
        countDownTimer.start();
    }

    /**
     * 结束掉所有的activity
     */
    public static void finishAll() {
        finishAllExceptThis(null);
    }

    /**
     * finish掉除指定的activity外的所有activity
     *
     * @param activity 不需要移除的对象
     */
    public static void finishAllExceptThis(Class<? extends Activity> activity) {
        synchronized (ActivityManager.class) {
            Set<WeakReference<Activity>> keySet = sAllActivityStatus.keySet();
            Iterator<WeakReference<Activity>> it = keySet.iterator();
            while (it.hasNext()) {
                WeakReference<Activity> next = it.next();
                Activity act = null;
                if (next != null && (act = next.get()) != null) {
                    if (activity != null) {
                        if (!activity.getName().equals(act.getClass().getName())) {
                            it.remove();
                            ALog.i(TAG, "Finish " + act.getClass().getSimpleName());
                            act.finish();
                        }
                    } else {
                        it.remove();
                        ALog.i(TAG, "Finish " + act.getClass().getSimpleName());
                        act.finish();
                    }
                }
            }
        }
    }

    /**
     * 是否运行在后台
     *
     * @return true or false
     */
    public static boolean isRunningBackground(Context context) {
        return !CommonUtils.appIsRunningForeground(context);
    }

    private static String stateToString(int state) {
        switch (state) {
            case ACTIVITY_STATE_ON_CREATE:
                return "onCreate";
            case ACTIVITY_STATE_ON_RESUME:
                return "onResume";
            case ACTIVITY_STATE_ON_START:
                return "onStart";
            case ACTIVITY_STATE_ON_PAUSE:
                return "onPause";
            case ACTIVITY_STATE_ON_STOP:
                return "onStop";
            case ACTIVITY_STATE_ON_DESTROY:
                return "onDestroy";
            default:
                return "Unknown";
        }
    }

    /**
     * 获取当前处于的前台Activity
     *
     * @return null或获取到的当前处于前台的activity
     */
    public static Activity getFrontActivity() {
        return sLink.current != null ? sLink.current.get() : null;
    }


    /**
     * activity 包装类
     */
    private static class ActivityWrapper {
        private final int state;
        private final String activity;

        ActivityWrapper(Activity activity, int state) {
            this.activity = activity.getClass().getName();
            this.state = state;
        }
    }


    private static class ActivityWeakReference extends WeakReference<Activity> {

        private final WeakReference<Activity> mActivity;

        ActivityWeakReference(Activity referent) {
            super(referent);
            this.mActivity = new WeakReference<>(referent);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            ActivityWeakReference that = (ActivityWeakReference) object;

            if (mActivity.get() != null) {
                return that.get() != null && that.get() == mActivity.get();
            } else {
                return that.get() == mActivity.get();
            }
        }

        @Override
        public int hashCode() {
            return mActivity.get() != null ? mActivity.get().hashCode() : 0;
        }
    }


    private static class Link {
        WeakReference<Activity> previousAct;
        WeakReference<Activity> current;
    }

}
