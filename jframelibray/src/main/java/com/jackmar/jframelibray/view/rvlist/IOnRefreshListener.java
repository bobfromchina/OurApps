package com.jackmar.jframelibray.view.rvlist;


public abstract interface IOnRefreshListener {

    /**
     * 刷新回掉
     *
     */
    public void onRefresh();

    /**
     * 加载回掉
     */
    public void onLoad();

}
