package com.lovely3x.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.lovely3x.common.R;

/**
 * 带搜索icon的edittext
 *
 * @author lovely3x
 * @version 1.0
 * @time 2015-5-25 下午11:36:50
 */

public class SearchEditTextView extends EditText {

    /**
     * 画笔咯
     */
    private Paint mPaint;
    /**
     * 搜索按钮
     */
    private Bitmap searchIcon;

    /**
     * 搜索按钮和文字之间的内边距
     */
    private float padding;

    /**
     * 图标的高度
     */
    private float iconHeight;

    /**
     * 图标的宽度
     */
    private float iconWidth;

    /**
     * 提示文字的颜色
     */
    private int tipTextColor;

    /**
     * 提示文本的大小
     */
    private float tipTextSize;

    /**
     * 提示文字
     */
    private String tipText;

    /**
     * 是否需要显示提示
     */
    public boolean displayTip = true;

    public SearchEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText, 0, 0);
            tipText = typedArray.getString(R.styleable.SearchEditText_tipText);
            if (TextUtils.isEmpty(tipText))
                tipText = "Search";
            tipTextColor = typedArray.getColor(R.styleable.SearchEditText_tipColor, getHintTextColors().getDefaultColor());
            padding = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_tipPadding, (int) (context.getResources().getDisplayMetrics().density * 3));
            tipTextSize = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_tipTextSize, (int) getTextSize());
            typedArray.recycle();
        } else {
            tipText = "Search";
            tipTextSize = getTextSize();
            tipTextColor = getHintTextColors().getDefaultColor();
        }
        init();
    }

    public SearchEditTextView(Context context) {
        this(context, null);
    }

    public SearchEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEditText, defStyle, 0);
        tipText = typedArray.getString(R.styleable.SearchEditText_tipText);
        if (TextUtils.isEmpty(tipText))
            tipText = "Search";
        tipTextColor = typedArray.getColor(R.styleable.SearchEditText_tipColor, getHintTextColors().getDefaultColor());
        padding = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_tipPadding, (int) (context.getResources().getDisplayMetrics().density * 3));
        tipTextSize = typedArray.getDimensionPixelSize(R.styleable.SearchEditText_tipTextSize, (int) getTextSize());
        typedArray.recycle();
        init();

    }

    private void init() {
        mPaint = new Paint();

        mPaint.setTextSize(tipTextSize);
        mPaint.setColor(tipTextColor);

        searchIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_city_search);
        iconWidth = searchIcon.getWidth();
        iconHeight = searchIcon.getHeight();
        addTextChangedListener(new SearchTextWatcher());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (displayTip) {
            FontMetrics fontMetrics = mPaint.getFontMetrics();

            float textWidth = mPaint.measureText(tipText);
            float textHeight = fontMetrics.top + fontMetrics.bottom;

            float bothWidth = textWidth + padding + iconWidth;
            float bothHeight = Math.max(textHeight, iconHeight);

            float startX = (getWidth() / 2 - bothWidth / 2) - iconWidth / 2;
            float startY = getHeight() / 2 - bothHeight / 2;

            canvas.drawBitmap(searchIcon, startX, startY, null);

            canvas.drawText(tipText, startX + iconWidth + padding, getHeight() / 2 - textHeight / 2, mPaint);
        }
        super.onDraw(canvas);
    }

    /**
     * 文本变化监听器 用来控制是否显示tip
     *
     * @author lovely3x
     * @version 1.0
     * @time 2015-5-26 上午12:13:34
     */
    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            displayTip = s.length() == 0;
            invalidate();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

    }

}
