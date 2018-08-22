package com.lovely3x.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.lovely3x.common.R;


/**
 * Created by lovely3x on 16-5-16.
 *
 *    重写红色小圆点
 */
public class RedPoint extends View {

    private final float textSize;
    private int color = Color.RED;
    private int textColor = Color.WHITE;
    private Paint mPaint;
    private String num;
    private int MAX_NUM = 99;

    public RedPoint(Context context) {
        this(context, null);
    }

    public RedPoint(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, new int[]{
                android.R.attr.text,
                android.R.attr.textSize,
                android.R.attr.textColor,
        });

        num = array.getString(0);
        num = num == null ? "" : num;
        textSize = array.getDimensionPixelSize(1, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        textColor = array.getColor(2, Color.WHITE);

        array = context.obtainStyledAttributes(attrs, R.styleable.RedPoint);
        color = array.getColor(R.styleable.RedPoint_color, Color.RED);
        array.recycle();

        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (TextUtils.isEmpty(num) || "0".equals(num)) {
            setMeasuredDimension(0, 0);
        } else {
            int padding = Math.max(Math.max(getPaddingLeft(), getPaddingRight()), Math.max(getPaddingTop(), getPaddingBottom()));
            int width = (int) (mPaint.measureText(String.valueOf(MAX_NUM)) + padding);
            setMeasuredDimension(width, width);
        }
    }

    public void setNum(int num) {
        this.num = String.valueOf(Math.min(num, MAX_NUM));
        requestLayout();
        invalidate();
    }

    protected void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(0x00, 0x00, 0x00, 0x00);

        mPaint.setColor(color);

        canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, mPaint);
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        String text = String.valueOf(num);

        final int textWidth = (int) mPaint.measureText(text);

        float centerLineX = (getWidth() - textWidth) / 2;
        float centerY = (getHeight() - (metrics.top + metrics.bottom)) / 2;

        mPaint.setColor(textColor);
        canvas.drawText(text, centerLineX, centerY, mPaint);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }
}
