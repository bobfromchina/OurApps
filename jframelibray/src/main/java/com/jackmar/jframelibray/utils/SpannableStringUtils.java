package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

/**
 * Created by 12985 on 2017/1/11.
 */
public class SpannableStringUtils {
    private SpannableString span;
    private SpannableStringBuilder ssb;
    private Context context;
    private String content;

    public SpannableStringUtils(Context context, String content) {
        this.context = context;
        this.content = content;
        span = new SpannableString(content);
    }

    /**
     * 设置颜色
     */
    public SpannableStringUtils setColor(int color, int start, int end) {
        span.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置背景颜色
     */
    public SpannableStringUtils setBgColor(int bgcolor, int start, int end) {
        span.setSpan(new BackgroundColorSpan(bgcolor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置连接地址
     */
    public SpannableStringUtils setUrl(String url, int start, int end) {
        span.setSpan(new URLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置电话
     */
    public SpannableStringUtils setTel(String tel, int start, int end) {
        span.setSpan(new URLSpan("tel:" + tel), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置自定义事件
     */
    public SpannableStringUtils setCustomClick(int foreColor, int start, int end, SpannableStringUtilsOnClick spannableStringUtilsOnClick) {
        span.setSpan(new CustomURLSpan("", foreColor, spannableStringUtilsOnClick), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体风格
     */
    public SpannableStringUtils setStyle(int style, int start, int end) {
        span.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 删除线
     */
    public SpannableStringUtils setDelLine(boolean isDel, int start, int end) {
        span.setSpan(isDel ? new StrikethroughSpan() : new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置图片
     */
    public SpannableStringUtils setImage(Bitmap bitmap, int start, int end) {
        ImageSpan imgspan = new VerticalImageSpan(context, bitmap);
        span.setSpan(imgspan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字大小
     */
    public SpannableStringUtils setTextSize(int textSize, int start, int end) {
        span.setSpan(new AbsoluteSizeSpan((int) ScrUtils.getRealWidth(context, textSize)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 取消下划线
     */
    public SpannableStringUtils setCanncelUnderLine(int start, int end) {
        span.setSpan(new CanncelUnderLine(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return this;
    }


    /**
     * 设置部分文字的点击
     */
    public SpannableStringUtils setOnClick(String str, final int foreColor, final int clickId, final SpannableStringUtilsOnClick spannableStringUtilsOnClick) {
        if (ssb == null) {
            ssb = new SpannableStringBuilder(span);
        }
        int start = ssb.length();
        ssb.append(str);
        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                if (spannableStringUtilsOnClick != null) {
                    spannableStringUtilsOnClick.onClick(clickId);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 去掉下划线
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
                // 设置文本颜色
                if (foreColor != 0) {
                    ds.setColor(context.getResources().getColor(foreColor));
                }
            }

        }, start, start + str.length(), 0);
        return this;
    }

    /**
     * 添加空格
     */
    public SpannableStringUtils setSpace(int count) {
        if (ssb != null) {
            ssb.append('\u0008');
        }
        return this;
    }

    /**
     * 返回SpannableString
     */
    public SpannableString getSpannableString() {
        return span;
    }

    /**
     * 返回SpannableStringBuilder
     */
    public SpannableStringBuilder getSpannableStringBuilder() {
        return ssb;
    }

    /**
     * 点击接口
     */
    public interface SpannableStringUtilsOnClick {
        void onClick(int clickId);
    }

    /**
     * 重写下划线
     */
    public class CanncelUnderLine extends UnderlineSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    /**
     * 重写urlSpan点击事件
     */
    public class CustomURLSpan extends URLSpan {
        private int foreColor;
        private SpannableStringUtilsOnClick spannableStringUtilsOnClick;

        public CustomURLSpan(String url, int foreColor, SpannableStringUtilsOnClick spannableStringUtilsOnClick) {
            super(url);
            this.foreColor = foreColor;
            this.spannableStringUtilsOnClick = spannableStringUtilsOnClick;
        }

        @Override
        public void onClick(View widget) {
            if (spannableStringUtilsOnClick != null) {
                spannableStringUtilsOnClick.onClick(0);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            // 去掉下划线
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
            // 设置文本颜色
            if (foreColor != 0) {
                ds.setColor(context.getResources().getColor(foreColor));
            }
        }
    }
}
