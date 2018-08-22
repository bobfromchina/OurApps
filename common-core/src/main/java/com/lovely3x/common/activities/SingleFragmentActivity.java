package com.lovely3x.common.activities;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.lovely3x.common.R;

import butterknife.ButterKnife;

/**
 * 单Fragment界面
 * Created by lovely3x on 16/7/31.
 */
public abstract class SingleFragmentActivity<T extends Fragment> extends TitleActivity {

    protected Fragment mFragment;

    @Override
    @CallSuper
    protected int getContentView() {
        return R.layout.activity_single_fragment;
    }

    @Override
    @CallSuper
    protected void onViewInitialized() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Class<? extends Fragment> clazz = getFragmentClass();
        if (clazz != null) {
            Fragment fragment = manager.findFragmentByTag(clazz.getName());

            if (fragment == null) {
                transaction.add(R.id.fl_activity_single_fragment_fragment_container, fragment = createFragmentInstance(clazz), fragment.getClass().getCanonicalName());
            }

            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
            this.mFragment = fragment;
        }
    }

    @Override
    @CallSuper
    protected void initViews() {
        ButterKnife.bind(this);
    }

    /**
     * 创建Fragment实例
     *
     * @return
     */
    public Fragment createFragmentInstance(Class<? extends Fragment> fClazz) {
        Class<? extends Fragment> clazz = getFragmentClass();
        if (clazz == null)
            throw new IllegalArgumentException("Fragment class can bt not null.If your fragment not empty constructor ,You may need to override #createFragmentInstance method.");
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取需要创建的fragment的类对象
     * 如果你重载了{@link #createFragmentInstance(Class)} 你可能就不需要实现这个方法了,因为这个方法是在默认实现中使用的
     * 如果你不需要使用默认实现,那么,同样你可以不使用这个方法
     *
     * @return
     */
    public abstract Class<T> getFragmentClass();

    @Override
    public void restoreInstanceOnCreateBefore(@NonNull Bundle savedInstance) {
        //Nothing to do
    }

    @Override
    public void restoreInstanceOnCreateAfter(@NonNull Bundle savedInstance) {
        //Nothing to do
    }


    public T getFragment() {
        return (T) mFragment;
    }


    /**
     * 自定义导航栏的背景色
     */
    @Override
    public Tint getTint() {
        Tint tint = super.getTint();
        tint.mStatusBarColor = getResources().getColor(R.color.color245);
        return tint;
    }
}
