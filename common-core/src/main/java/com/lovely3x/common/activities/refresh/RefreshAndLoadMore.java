package com.lovely3x.common.activities.refresh;

import android.support.annotation.NonNull;

import com.lovely3x.common.adapter.ListAdapter;
import com.lovely3x.common.utils.Response;

/**
 * 下拉刷新和加载更多
 * 它默认是不会开启下拉刷新和上拉加载更多的
 * 你如果实现了 {@link com.lovely3x.common.activities.refresh.CanRefresh} 那么就可以下拉刷新了
 * 你如果实现了 {@link com.lovely3x.common.activities.refresh.CanLoadMore} 那么就可以上拉加载更多了
 * 当然你是可以同时实现和一个都不实现
 * Created by lovely3x on 16-1-21.
 */
public interface RefreshAndLoadMore {

    /**
     * 默认的传递数据的页码
     * 使用 {@link #DEFAULT_RETURN_FIRST_PAGE }替换
     */
    @Deprecated
    public static final int DEFAULT_FIRST_PAGE = 0;

    /**
     * 默认的获取数据返回的第一页的页码
     */
    public static final int DEFAULT_RETURN_FIRST_PAGE = 1;

    /**
     * 默认的请求数据使用的第一页的页码
     */
    public static final int DEFAULT_POST_FIRST_PAGE = DEFAULT_FIRST_PAGE;

    /**
     * 当开始刷新后执行
     */
    void onBeginRefresh();

    /**
     * 当开始加载更多后执行
     */
    void onBeginLoadMore();

    /**
     * 下拉刷新完成
     */
    void refreshComplete();

    /**
     * 加载更多完成
     *
     * @param emptyResult 本次加载的数据是否为null
     * @param hasMore     是否有更多的数据(还可以再次加载更多)
     */
    void loadMoreFinish(boolean emptyResult, boolean hasMore);

    /**
     * 加载更多时发生了错误
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     */
    void loadMoreError(int errorCode, String errorMessage);

    /**
     * 处理刷新状态
     * 在事件处理完成后应该调用
     * 调用了这个方法，方法内部会根据当前的状态及响应值来处理当前的界面显示
     *
     * @param response 响应对象
     */
    void handleRefreshState(@NonNull Response response);

    /**
     * 处理刷新状态
     * 在事件处理完成后应该调用
     * 调用了这个方法，方法内部会根据当前的状态及响应值来处理当前的界面显示
     *
     * @param response    响应对象
     * @param listAdapter 很多情况下 都会用到adapter。所我们还可以根据响应对象及其adapter对象来处理当前的界面显示
     */
    void handleRefreshState(Response response, ListAdapter listAdapter);

    /**
     * 自动刷新
     */
    void autoRefresh();

}
