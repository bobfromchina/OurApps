package com.lovely3x.common.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lovely3x on 15-9-8.
 */
public class DotLine extends View {
    private DashPathEffect pathEffect;
    private Paint paint;
    private Path path;
    private int lineHeight;
    private int mColor;


    public DotLine(Context context) {
        super(context);
        init();
    }

    public DotLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        pathEffect = new DashPathEffect(new float[]{10, 5}, 3);
        paint = new Paint();
        path = new Path();
        lineHeight = (int) (getContext().getResources().getDisplayMetrics().density * 1.0f);
        mColor = 0xFFC0C0C0;
    }

    public DashPathEffect getPathEffect() {
        return pathEffect;
    }

    public void setPathEffect(DashPathEffect pathEffect) {
        this.pathEffect = pathEffect;
        invalidate();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        invalidate();
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
        invalidate();
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.reset();
        path.reset();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineHeight);
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect);
        float halfY = getHeight() / 2.0f;
        path.moveTo(0, halfY);
        path.lineTo(getWidth(), halfY);

        canvas.drawPath(path, paint);
    }
}
