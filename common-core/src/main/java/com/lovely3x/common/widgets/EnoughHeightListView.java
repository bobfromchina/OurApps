package com.lovely3x.common.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 足够高的listview
 * Created by lovely3x on 15-11-20.
 */
public class EnoughHeightListView extends ListView {

    private static final String TAG = "EnoughHeightListView";

    public EnoughHeightListView(Context context) {
        super(context);
    }

    public EnoughHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnoughHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public EnoughHeightListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
