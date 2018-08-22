package com.example.wangbo.ourapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wangbo on 2018/8/2.
 */

public class FontTextView extends TextView {

    Context context;

    public FontTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/text_font.OTF");
        this.setTypeface(typeface);
    }
}
