package com.lovely3x.common.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovely3x.common.activities.CommonActivity;
import com.lovely3x.common.managements.user.IUser;
import com.lovely3x.common.managements.user.UserLoginStateChangedListener;
import com.lovely3x.common.managements.user.UserManager;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.ConnectivityReceiver;
import com.lovely3x.common.utils.Event;
import com.lovely3x.common.viewcache.ViewPool;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * 通用fragment
 * Created by lovely3x on 15-9-7.
 */
public abstract class CommonFragment extends Fragment implements Event {

    /**
     * 用户状态变化监听器
     */
    private final UserLoginStateChangedListener mStateChangedListener = new UserLoginStateChangedListener.SimpleUserLoginStateChangedListener() {
        @Override
        public void onUserLoginSuccessful(IUser IUser) {
            shouldLoadData();
        }

        @Override
        public void onUserLogoutSuccessful(IUser IUser) {
            shouldLoadData();
        }
    };


    /**
     * 连接变化监听器
     */
    private final ConnectivityReceiver.ConnectivityListener CONNECTIVITY_LISTENER = new ConnectivityReceiver.ConnectivityListener() {
        @Override
        public void onConnectivityChanged(boolean hasNetwork, int type) {
            if (hasNetwork) onNetworkConnected(type);
            else onNetworkDisconnected();
        }

        @Override
        public void onHostAccessibilityChanged(boolean isConnected) {
            CommonFragment.this.onHostAccessibilityChanged(isConnected);
        }
    };

    /**
     * 当主机访问性变化后执行
     *
     * @param isConnected 是否能够访问到主机
     */
    protected void onHostAccessibilityChanged(boolean isConnected) {
        ALog.d(TAG, String.format("HostAccessibilityChanged[%s]", isConnected));
    }

    /**
     * 当连接到网络后执行
     *
     * @param type 连接的网络的类型
     */
    protected void onNetworkConnected(int type) {
        ALog.d(TAG, "Current network is connected ,connectivity type is " + type);
    }

    /**
     * 当网络连接断开后执行
     */
    protected void onNetworkDisconnected() {
        ALog.d(TAG, "Current network is disconnected.");
    }


    protected final String TAG;

    protected CommonActivity mActivity;

    protected View mRootView;

    protected LayoutInflater mInflater;
    protected boolean isFirstVisible = true;

    public CommonFragment() {
        TAG = this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isFirstVisible = true;
        ALog.d(TAG, this + "onAttach -> " + context);
        this.mActivity = (CommonActivity) context;
    }

    /**
     * 恢复记录文件
     */
    public void restoreRecord() {
        SharedPreferences sp = mActivity.getSharedPreferences(CommonActivity.RECORD_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Map<String, ?> all = sp.getAll();
        Set<String> keys = all.keySet();
        for (String key : keys) {
            String[] keyArray = key.split(":");
            if (keyArray.length == 2) {
                //className:identifier
                if (keyArray[0].equals(getClass().getName())) {
                    CommonActivity.Record record = new CommonActivity.Record();
                    record.identifier = keyArray[1];
                    record.object = sp.getString(key, null);
                    onRecordIdentifier(record);
                }
            }
        }
    }

    /**
     * 当activity销毁时，会记录返回的这个对象
     *
     * @return 需要记录的对象
     */
    public CommonActivity.Record getRecordIdentifier() {
        return null;
    }

    /**
     * 当activity创建时会返回这个class名记录的对象
     *
     * @param object 记录的对象
     */
    public void onRecordIdentifier(CommonActivity.Record object) {
    }


    /**
     * 记录标志
     */
    public void recordIdentifier() {
        //记录标志
        CommonActivity.Record record = getRecordIdentifier();
        if (record != null) {
            SharedPreferences sp = mActivity.getSharedPreferences(CommonActivity.RECORD_SHARE_PREFERENCES, Context.MODE_PRIVATE);

            sp.edit().putString(getClass().getName() + ":" + record.identifier, record.object).commit();
        }
    }

    /**
     * 获取视图的id
     *
     * @return 视图id
     */
    protected abstract int getContentView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ALog.d(TAG, this + "onActivityCreated");

    }

    /**
     * 初始化参数，在调用{@link #onViewInitialized()}之前
     *
     * @param bundle 参数存放集
     */
    protected void initArgumentsBeforeInitialized(@NonNull Bundle bundle) {
    }

    /**
     * 初始化参数，在调用 {@link #initViews()}之前
     *
     * @param bundle 参数存放集
     */
    protected void initArgumentsBeforeOnCreateView(Bundle bundle) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.d(TAG, "onCreate " + this);
        restoreRecord();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ALog.d(TAG, this + "onCreateView");
        if (mRootView == null || mRootView.getHeight() == 0) {
            Bundle arguments = getArguments();
            if (arguments != null) initArgumentsBeforeOnCreateView(arguments);
            if (savedInstanceState != null) restoreInstanceOnCreateBefore(savedInstanceState);
            this.mInflater = inflater;
            int cacheID = getCacheViewIdentification();
            ViewPool.ViewWrapper cache;
            if (cacheID != -1 && (cache = ViewPool.findRecycledViewInCachePool(cacheID)) != null) {
                cache.isRecycled = false;
                mRootView = cache.view;
                ALog.d(TAG, "Found cache view.");
                initViews();
            } else {
                int viewId = getContentView();
                if (viewId > 0) {
                    mRootView = inflater.inflate(viewId, container, false);
                    initViews();
                }
            }

            if (savedInstanceState != null) restoreInstanceOnCreateAfter(savedInstanceState);

            if (arguments != null) {
                initArgumentsBeforeInitialized(arguments);
            }

            onViewInitialized();
            UserManager.getInstance().addUserStateChangedListener(mStateChangedListener, true);
            ConnectivityReceiver.addConnectivityListener(CONNECTIVITY_LISTENER);
        }

        return mRootView;
    }


    public <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);
    }

    public <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * 应该加载数据
     * 什么情况会回调?
     * 1,界面初始化完成后会调用一次(在调用{@link #onViewInitialized()} 调用后执行)
     * 2,用户登录状态发生变化后会调用(每次变化都会调用)
     */
    protected void shouldLoadData() {
        //// TODO: 16/6/18
    }


    /**
     * 初始化视图
     */
    protected abstract void initViews();


    /**
     * 当时视图初始化完成后调用
     */
    protected abstract void onViewInitialized();


    /**
     * 恢复实例状态 在执行初始化之前
     *
     * @param savedInstanceState 保存实例状态的bundle对象
     */
    protected abstract void restoreInstanceOnCreateBefore(Bundle savedInstanceState);

    /**
     * 恢复实例后 在视图初始化完成之后调用
     *
     * @param savedInstanceState 保存实例状态的bundle对象
     */
    protected abstract void restoreInstanceOnCreateAfter(Bundle savedInstanceState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ALog.e(TAG, "onSaveInstanceState -> " + outState);
    }

    /**
     * 获取 root View
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }

    /**
     * 获取布局初始化器
     *
     * @return
     */
    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            onUserHintVisibleChanged(true, isFirstVisible);
            isFirstVisible = false;
        } else {
            onUserHintVisibleChanged(false, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面，"MainScreen"为页面名称，可自定义
        if (getUserVisibleHint()) {
            onUserHintVisibleChanged(true, isFirstVisible);
            isFirstVisible = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 当用户可见性会发生变化后调用
     *
     * @param isFirstVisible  是否时第一次可见（可见性会在视图destroy时改变）
     * @param isVisibleToUser 是否会变为可见
     */
    public void onUserHintVisibleChanged(boolean isVisibleToUser, boolean isFirstVisible) {
        ALog.d(TAG, this + "onUserHintVisibleChanged -> " + isVisibleToUser + "   viewIsCreated -> " + isFirstVisible);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        ALog.d(TAG, this + "onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        ALog.d(TAG, this + "onStart");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getUserVisibleHint()) {
            onUserHintVisibleChanged(false, false);
        }
        isFirstVisible = true;
        ALog.d(TAG, this + "onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ALog.d(TAG, "onDestroy " + this);
        recordIdentifier();
        ConnectivityReceiver.removeConnectivityListener(CONNECTIVITY_LISTENER);
        UserManager.getInstance().removeUserStateChangedListener(mStateChangedListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFirstVisible = true;
        ALog.d(TAG, this + "onDetach");
        if (getCacheViewIdentification() != -1) {
            ViewPool.ViewWrapper wrapper = ViewPool.findWrapperByView(mRootView, getCacheViewIdentification());
            if (wrapper != null) {
                wrapper.isRecycled = true;
            }
        }
    }


    @Override
    public void onEvent(int eventWhat, Object objects) {
        //On evnet
        if (Config.DEBUG) {
            ALog.d(TAG, "onEvent");
        }
    }

    /**
     * 获取缓存视图标识
     *
     * @return
     */
    public int getCacheViewIdentification() {
        return -1;
    }
}
