package com.lovely3x.common.widgets.sidebar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.utils.ViewUtils;

/**
 * Created by lovely3x on 16-5-16.
 */
public class XSideBar extends View implements ISideBar {

    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int choose = -1;// 选中
    private Paint paint = new Paint();
    private float textSize;

    /**
     * 是否正在触摸中
     */
    private boolean istouching;

    private TextView mTextDialog;

    private Canvas indicatorCanvas;
    private Bitmap mIndicatorBm;
    private Bitmap mIndicator;
    private int indicatorXOffset;
    private int indicatorYOffset;
    private int textColor;

    @Override
    public void setOnTouchingLetterChangedListener(com.lovely3x.common.widgets.sidebar.OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public void setTextView(TextView mTextDialog) {
//        this.mTextDialog = mTextDialog;
    }

    public XSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        textSize = ViewUtils.dp2pxF(13);
        mIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.icon_letter_indicator);
        mIndicatorBm = Bitmap.createBitmap(mIndicator.getWidth(), mIndicator.getHeight(), Bitmap.Config.ARGB_8888);
        indicatorXOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        indicatorYOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        indicatorCanvas = new Canvas(mIndicatorBm);
        paint.setAntiAlias(true);
        paint.setDither(true);
        textColor = getResources().getColor(R.color.font_light_gray);
    }

    public XSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XSideBar(Context context) {
        super(context);
        init();
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / b.length;// 获取每一个字母的高度
        height = height - singleHeight / 2;
        singleHeight = height / b.length;

        for (int i = 0; i < b.length; i++) {
            paint.setColor(textColor);

            paint.setTextSize(textSize);


            float textWidth = paint.measureText(b[i]);
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - textWidth / 2;
            float yPos = singleHeight * i + singleHeight;

            // 选中的状态
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.colorAccent));

                final int circleRadius = (int) Math.max(textWidth, textSize);
                Paint.FontMetrics metrics = paint.getFontMetrics();

                float center = (metrics.top + metrics.bottom) / 2;

                canvas.drawCircle(getWidth() / 2, yPos + center, circleRadius / 2, paint);

                paint.setColor(Color.WHITE);
            }

            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

        if (istouching) {
            int choicePositionY = singleHeight * choose + singleHeight;
            choicePositionY -= singleHeight / 2;
            String text = b[choose];
            paint.setColor(Color.WHITE);
            paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 33, getResources().getDisplayMetrics()));
            paint.setAntiAlias(true);

            float textWidth = paint.measureText(text);

            indicatorCanvas.drawARGB(0x00, 0x00, 0x00, 0x00);

            //绘制背景
            indicatorCanvas.drawBitmap(mIndicator, 0, 0, paint);

            Paint.FontMetrics metrics = paint.getFontMetrics();
            int bmHeight = mIndicatorBm.getHeight();

            int centerLine = (int) ((bmHeight - metrics.bottom - metrics.top) / 2);

            indicatorCanvas.drawText(text, mIndicatorBm.getWidth() / 2 - textWidth / 2, centerLine, paint);

            int minY = indicatorYOffset;
            int maxY = getHeight() - mIndicatorBm.getHeight() - indicatorYOffset;

            //矫正位置
            int y = choicePositionY - mIndicatorBm.getHeight() / 2;
            if (y < minY) {
                y = minY;
            }

            if (y > maxY) {
                y = maxY;
            }

            canvas.drawBitmap(mIndicatorBm, -(mIndicatorBm.getWidth() + indicatorXOffset), y, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                istouching = false;
                //   setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                istouching = true;
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                    }
                }

                break;
        }
        postInvalidate();
        ((View) getParent()).postInvalidate();
        return true;
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        paint.setTextSize(textSize);
        float singlewidth = paint.measureText("S");
        int width = MeasureSpec.makeMeasureSpec((int) (singlewidth * 4), MeasureSpec.EXACTLY);
        setMeasuredDimension(width, heightMeasureSpec);
    }
}
