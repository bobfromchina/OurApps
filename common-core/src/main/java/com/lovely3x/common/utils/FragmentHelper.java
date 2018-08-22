package com.lovely3x.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lovely3x.common.fragments.BaseCommonFragment;

import java.util.List;

/**
 * Fragment helper
 * Created by lovely3x on 16/7/29.
 */
public class FragmentHelper {

    private static final java.lang.String TAG = "FragmentHelper";

    /**
     * 显示Fragment
     *
     * @param fragmentClass       Fragment类
     * @param fragmentContainerId fragment容器id
     */
    public static Fragment showFragment(FragmentManager manager, Class<? extends Fragment> fragmentClass, int fragmentContainerId) {

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = manager.findFragmentByTag(fragmentClass.getName());

        List<Fragment> fragments = manager.getFragments();

        //Hidden all fragment
        if (fragments != null) {
            for (Fragment f : fragments) if (f != null) transaction.hide(f);
        }

        if (fragment == null) {
            try {
                fragment = (Fragment) fragmentClass.getMethod(BaseCommonFragment.NEW_INSTANCE_METHOD_NAME).invoke(null);
            } catch (Exception e) {
                try {
                    fragment = fragmentClass.newInstance();
                } catch (Exception e1) {
                    ALog.e(TAG, e1);
                }
            }
            transaction.add(fragmentContainerId, fragment, fragmentClass.getName());
        }

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();

        return fragment;
    }

    public static Fragment showFragment(FragmentManager manager, Class<? extends Fragment> fragmentClass, int fragmentContainerId, Object... args) {

        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = manager.findFragmentByTag(fragmentClass.getName());

        List<Fragment> fragments = manager.getFragments();

        //Hidden all fragment
        if (fragments != null) {
            for (Fragment f : fragments) if (f != null) transaction.hide(f);
        }

        if (fragment == null) {
            try {
                Class[] argClasses = null;
                if (args != null) argClasses = new Class[args.length];
                if (args != null)
                    for (int i = 0; i < args.length; i++) argClasses[i] = args[i].getClass();
                fragment = (Fragment) fragmentClass.getMethod(BaseCommonFragment.NEW_INSTANCE_METHOD_NAME, argClasses).invoke(null, args);
            } catch (Exception e) {
                try {
                    fragment = fragmentClass.newInstance();
                } catch (Exception e1) {
                    ALog.e(TAG, e1);
                }
            }
            transaction.add(fragmentContainerId, fragment, fragmentClass.getName());
        }

        transaction.show(fragment);
        transaction.commitAllowingStateLoss();

        return fragment;
    }
}
