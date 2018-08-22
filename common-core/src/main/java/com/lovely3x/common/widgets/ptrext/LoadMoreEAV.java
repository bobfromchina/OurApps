package com.lovely3x.common.widgets.ptrext;

import android.content.Context;
import android.view.View;

import in.srain.cube.views.ptr.loadmore.LoadMoreContainerBase;

/**
 * EAV 的加载更多
 * Created by lovely3x on 16/8/9.
 */
public class LoadMoreEAV extends LoadMoreContainerBase {

    public LoadMoreEAV(Context context) {
        super(context);
    }

    @Override
    protected void addFooterView(View view) {

    }

    @Override
    protected void removeFooterView(View view) {
        
    }

    @Override
    protected Object retrieveListView() {
        return null;
    }
}
