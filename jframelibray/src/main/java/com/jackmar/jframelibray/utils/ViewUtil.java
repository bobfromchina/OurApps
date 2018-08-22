package com.jackmar.jframelibray.utils;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * 视图工具类
 */
public class ViewUtil {
    /**
     * 描述：dip转换为px
     *
     * @param context
     * @param dipValue
     * @return
     * @throws
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 描述：字符串部分设置颜色
     *
     * @throws
     */
    public static void SpannableString(TextView textView, int start, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(color);
        builder.setSpan(yellowSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * 描述：px转换为dip
     *
     * @param context
     * @param pxValue
     * @return
     * @throws
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据文本长度 计算实际所得宽度px
     *
     * @param text     文本类容
     * @param textSize 文本大小     例如：textview.getPaint().getTextSize()
     * @return 计算实际所得宽度px
     */
    public static int getTxtWidthPx(String text, float textSize) {
        Paint paint = new Paint();
        Rect rect = new Rect();
        paint.setTextSize(textSize);
        // 返回包围整个字符串的最小的一个Rect区域
        paint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();
        return width;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /***
     * 设置视图大小
     *
     * @param view 视图 extends View即可
     * @param w    设置成的宽度
     * @param h    设置成的高度
     */
    public static void setViewSize(View view, int w, int h) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) params = new ViewGroup.LayoutParams(w, h);
        else {
            params.width = w;
            params.height = h;
        }
        view.setLayoutParams(params);
    }

    /***
     * 设置视图大小
     *
     * @param view 视图 extends View即可
     * @param w    设置成的宽度
     */
    public static void setViewSize(View view, int w) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = w;
        view.setLayoutParams(params);
    }

    /***
     * 设置视图大小
     *
     * @param view 视图 extends View即可
     * @param h    设置成的高度
     */
    public static void setViewSizeh(View view, int h) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = h;
        view.setLayoutParams(params);
    }

    /**
     * 测量这个view，最后通过getMeasuredWidth()获取宽度和高度.
     *
     * @param v 要测量的view
     */
    public static void measureView(View v) {
        if (v == null) {
            return;
        }
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
    }


    /***
     * 设置输入框 drawable left
     *
     * @param textView   文本框
     * @param drawableId 资源图片id
     */
    public static void setTextViewDrawLeft(TextView textView, int drawableId) {
        if (textView == null) return;
        Drawable drawable = textView.getContext().getResources().getDrawable(drawableId);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);

    }

    /**
     * Get center child in X Axes
     */
    public static View getCenterXChild(RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                if (isChildInCenterX(recyclerView, child)) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Get position of center child in X Axes
     */
    public static int getCenterXChildPosition(RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                if (isChildInCenterX(recyclerView, child)) {
                    return recyclerView.getChildAdapterPosition(child);
                }
            }
        }
        return childCount;
    }

    /**
     * Get center child in Y Axes
     */
    public static View getCenterYChild(RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                if (isChildInCenterY(recyclerView, child)) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Get position of center child in Y Axes
     */
    public static int getCenterYChildPosition(RecyclerView recyclerView) {
        int childCount = recyclerView.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = recyclerView.getChildAt(i);
                if (isChildInCenterY(recyclerView, child)) {
                    return recyclerView.getChildAdapterPosition(child);
                }
            }
        }
        return childCount;
    }

    public static boolean isChildInCenterX(RecyclerView recyclerView, View view) {
        int childCount = recyclerView.getChildCount();
        int[] lvLocationOnScreen = new int[2];
        int[] vLocationOnScreen = new int[2];
        recyclerView.getLocationOnScreen(lvLocationOnScreen);
        int middleX = lvLocationOnScreen[0] + recyclerView.getWidth() / 2;
        if (childCount > 0) {
            view.getLocationOnScreen(vLocationOnScreen);
            if (vLocationOnScreen[0] <= middleX && vLocationOnScreen[0] + view.getWidth() >= middleX) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChildInCenterY(RecyclerView recyclerView, View view) {
        int childCount = recyclerView.getChildCount();
        int[] lvLocationOnScreen = new int[2];
        int[] vLocationOnScreen = new int[2];
        recyclerView.getLocationOnScreen(lvLocationOnScreen);
        int middleY = lvLocationOnScreen[1] + recyclerView.getHeight() / 2;
        if (childCount > 0) {
            view.getLocationOnScreen(vLocationOnScreen);
            if (vLocationOnScreen[1] <= middleY && vLocationOnScreen[1] + view.getHeight() >= middleY) {
                return true;
            }
        }
        return false;
    }
}
