package com.lovely3x.common.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 高度可缩放视图
 * 这个视图的作用就是
 * 让视图的高度根据视图的宽度变化
 * 图片的宽度和高度跟随视图的宽度变化而变化
 * 这样的结果就是，视图会自适应视图
 * Created by lovely3x on 15-12-16.
 */
public class HeightScaleableImageView extends ImageView {

    Matrix matrix = new Matrix();
    private int mImageWidth;
    private int mImageHeight;

    public HeightScaleableImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public HeightScaleableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public HeightScaleableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeightScaleableImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mImageHeight <= 0 || mImageWidth <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            //measure
            int width = 0;
            int height;

            switch (MeasureSpec.getMode(widthMeasureSpec)) {
                case MeasureSpec.EXACTLY:
                    width = MeasureSpec.getSize(widthMeasureSpec);
                    break;
                case MeasureSpec.AT_MOST:
                case MeasureSpec.UNSPECIFIED:
                    width = mImageWidth;
                    break;
            }

            float scaleX = width * 1.0f / mImageWidth;
            if (mImageWidth < width) {//图片宽度小于视图宽度
                height = mImageHeight;
                int translationX = (width - mImageWidth) / 2;
                matrix.reset();
                matrix.setTranslate(translationX, 0);
            } else {
                height = (int) (mImageHeight * scaleX);
                matrix.reset();
                matrix.postScale(scaleX, scaleX);
            }

            setImageMatrix(matrix);
            setMeasuredDimension(width, height);
        }

    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateSize();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        updateSize();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        updateSize();
    }

    protected void updateSize() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            mImageHeight = drawable.getIntrinsicHeight();
            mImageWidth = drawable.getIntrinsicWidth();
            requestLayout();
        }
    }
}
