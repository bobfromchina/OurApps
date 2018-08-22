package com.lovely3x.common.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.lovely3x.common.R;

/**
 * 自己绘制的一个对勾
 * 主要应用在图片选择菜单中的勾选按钮
 * 因为这个图片选择应该是一个通用的，他的颜色应该是多变了
 * 但是不应该每次都换图片呀，所以就谢了一个这个类
 * Created by lovely3x on 15-12-8.
 */
public class CheckPointView extends View implements Checkable {
    private Paint mPaint;
    private RectF mBackgroundRect;
    private Path path;
    /**
     * 视图的大小
     */
    private float size;
    /**
     * 形状的对勾色
     */
    private int shapeColor;
    /**
     * 背景色
     */
    private int backgroundColor;
    private int mCheckBackgroundColor;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };
    private boolean mChecked;

    public CheckPointView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CheckPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CheckPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckPointView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        if (attrs != null) {
            TypedArray them = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckPointView, defStyleAttr, 0);
            shapeColor = them.getColor(R.styleable.CheckPointView_shapeColor, Color.WHITE);
            this.backgroundColor = them.getColor(R.styleable.CheckPointView_roundBackgroundColor, Color.LTGRAY);
            mChecked = them.getBoolean(R.styleable.CheckPointView_isChecked, false);
            mCheckBackgroundColor = them.getColor(R.styleable.CheckPointView_checkedRoundBackgroundColor, getResources().getColor(R.color.gold_yellow));
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height;
        int width;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            default:
                width = (int) (size + 0.5);
                break;
        }
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            default:
                height = (int) (size + 0.5);
                break;
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mBackgroundRect = new RectF(0, 0, w, h);
        path = new Path();
        path.moveTo(w * (1.0f / 6.18f), h * (1.0f / 2) + h * (1.0f / 8));
        path.lineTo(w * 0.5f, h * (3.0f / 4));
        path.lineTo(w * (3.0f / 4), h * (1.0f / 4));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //绘制背景
        mPaint.setColor(mChecked ? mCheckBackgroundColor : backgroundColor);
        if (mBackgroundRect != null) {
            canvas.drawOval(mBackgroundRect, mPaint);
        }

        //绘制对勾
        if (path != null) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setColor(shapeColor);
            mPaint.setStrokeWidth(getWidth() / 13.0f);
            canvas.save();
            canvas.rotate(15, getWidth() / 2, getHeight() / 2);
            canvas.drawPath(path, mPaint);
            canvas.restore();
        }
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        invalidate();

    }

    public boolean isChecked() {
        return mChecked;
    }


    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
        refreshDrawableState();
        invalidate();
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
        requestLayout();
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int shapeColor) {
        this.shapeColor = shapeColor;
        invalidate();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        invalidate();
    }

    public int getCheckBackgroundColor() {
        return mCheckBackgroundColor;
    }

    public void setCheckBackgroundColor(int mCheckBackgroundColor) {
        this.mCheckBackgroundColor = mCheckBackgroundColor;
        invalidate();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

}
