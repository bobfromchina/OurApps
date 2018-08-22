package com.jackmar.jframelibray.managers;


import android.app.Activity;

import java.util.ArrayList;

public class XActivityManager {
    static XActivityManager single = null;
    private ArrayList<Activity> activityList = null;
    private Activity curryRunningActivity = null;

    public synchronized static XActivityManager getInstance() {
        if (single == null) {
            single = new XActivityManager();
        }
        return single;
    }

    public XActivityManager() {
        activityList = new ArrayList<>();
    }

    /**
     * 添加activity管理
     *
     * @param activity
     */
    public void addToActivityList(Activity activity) {
        synchronized (activityList) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
                //设置当前运行的activity
                this.setCurryRunningActivity(activity);
            }
        }
    }

    /**
     * 移除activity
     *
     * @param activity BaseActivity必须继承
     */
    public void removeToActivityList(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    /**
     * 设置当前运行的activity
     *
     * @param activity BaseActivity必须继承
     */
    public void setCurryRunningActivity(Activity activity) {
        if (activity != null) {
            curryRunningActivity = activity;
        }
    }

    /**
     * 获取指定的acitivity
     *
     * @param activity activity
     * @return BasessActivity
     */
    public Activity getActivityByClass(Class<? extends Activity> activity) {
        String name = activity.getSimpleName();
        for (Activity ac : activityList) {
            String name1 = ac.getClass().getSimpleName();
            if (name.equals(name1)) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 获取指定的acitivity
     *
     * @param activity activity
     * @return BasessActivity
     */
    public Activity getActivityByClassEnd(Class<? extends Activity> activity) {
        String name = activity.getSimpleName();
        int size = activityList.size();
        int end = -1;
        for (int i = 0; i < size; i++) {
            Activity ac = activityList.get(i);
            String name1 = ac.getClass().getSimpleName();
            if (name.equals(name1)) {
                end = i;
            }
        }
        if (end == -1) {
            return null;
        }
        return activityList.get(end);
    }

    /**
     * 设置当前运行的activity
     *
     * @return BasessActivity
     */
    public Activity getCurryRunningActivity() {
        return curryRunningActivity;
    }

    /**
     * finish掉指定的activity
     *
     * @param activity
     */
    public void finishActivity(Class<? extends Activity> activity) {
        Activity ac = getActivityByClassEnd(activity);
        if (ac != null) ac.finish();
    }

    /**
     * 删除除了当前act 以外的act
     *
     * @param activity
     */
    public void finishOtherActivity(Class<? extends Activity> activity) {
        Activity ac = getActivityByClassEnd(activity);
        for (Activity act : activityList) {
            if (act != null && ac != null && act != ac) {
                act.finish();
            }
        }
    }
}
