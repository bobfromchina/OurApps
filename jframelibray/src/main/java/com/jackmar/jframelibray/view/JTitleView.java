package com.jackmar.jframelibray.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.utils.StrUtil;
import com.jackmar.jframelibray.utils.ViewUtil;

/**
 * 自定义title
 */

public class JTitleView extends RelativeLayout {
    private TextView mTitleView;
    private TextView mMoreTextView;
    private ImageView mBackImage;
    private ImageView mMoreImage;
    private RelativeLayout mMoreLayout;
    private View mBottomLine;
    private String titleText;//标题
    private String moreText;//右侧文字
    private int backImage;//返回图片
    private int moreImage;//more图片
    private int textColor;//字体颜色
    private int moreTextColor;//右侧文字颜色
    private float textSize;//字体大小
    private float moreTextSize;//右侧字体大小
    private boolean canBack;//可以返回
    private float bottomLineHeight;//底部分割线高度
    private int bottomLineRes;//底部分割线资源
    private Context context;

    public JTitleView(Context context) {
        super(context);
        init(context, null);
    }

    public JTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public JTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_jtitle_view, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JTitleView, 0, 0);
        try {
            titleText = ta.getString(R.styleable.JTitleView_titleText);
            moreText = ta.getString(R.styleable.JTitleView_moreText);
            backImage = ta.getResourceId(R.styleable.JTitleView_backImage, 0);
            moreImage = ta.getResourceId(R.styleable.JTitleView_moreImg, 0);
            textColor = ta.getResourceId(R.styleable.JTitleView_textcolor, 0);
            moreTextColor = ta.getResourceId(R.styleable.JTitleView_moreTextcolor, 0);
            textSize = ta.getDimension(R.styleable.JTitleView_textSize, 16);
            moreTextSize = ta.getDimension(R.styleable.JTitleView_moreTextSize, 13);
            canBack = ta.getBoolean(R.styleable.JTitleView_canBack, false);
            bottomLineHeight = ta.getDimension(R.styleable.JTitleView_bottomlineheight, 1);
            bottomLineRes = ta.getResourceId(R.styleable.JTitleView_bottomres, 0);
            initView();
        } finally {
            ta.recycle();
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //中间文字
        mTitleView = (TextView) findViewById(R.id.tv_title);
        //右侧文字
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        //返回
        mBackImage = (ImageView) findViewById(R.id.iv_back);
        //右侧图片
        mMoreImage = (ImageView) findViewById(R.id.iv_more);
        //更多总布局
        mMoreLayout = (RelativeLayout) findViewById(R.id.rl_more);
        //底部分割线
        mBottomLine = findViewById(R.id.v_bottom_line);
        invalidate();
    }

    public void invalidate() {
        //设置返回按钮
        mBackImage.setVisibility(canBack ? VISIBLE : INVISIBLE);

        //设置title
        if (mTitleView != null) {
            if (!StrUtil.isEmpty(titleText)) {
                mTitleView.setText(titleText);
                mTitleView.setTextSize(px2dip(context, textSize));
            }
            if (textColor != 0) {
                mTitleView.setTextColor(getResources().getColor(textColor));
            }
        }
        //设置右侧文字
        if (mMoreTextView != null) {
            if (!StrUtil.isEmpty(moreText)) {
                mMoreTextView.setText(moreText);
                mMoreTextView.setVisibility(VISIBLE);
                if (textColor != 0) {
                    mMoreTextView.setTextColor(getResources().getColor(textColor));
                }
                if (moreTextColor != 0) {
                    mMoreTextView.setTextColor(getResources().getColor(moreTextColor));
                }
            }
        }
        //设置右侧右侧图片
        if (mMoreImage != null) {
            if (moreImage != 0) {
                mMoreImage.setImageResource(moreImage);
                mMoreImage.setVisibility(VISIBLE);
                mMoreTextView.setVisibility(INVISIBLE);
            } else {
                mMoreImage.setVisibility(INVISIBLE);
            }
        }
        //设置返回图标
        if (backImage != 0) {
            mBackImage.setImageResource(backImage);
        }

        if (canBack) {
            mBackImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) getContext()).finish();
                }
            });
        }
        if (bottomLineRes != 0) {
            mBottomLine.setBackgroundResource(bottomLineRes);
        }
        if (bottomLineHeight != 0) {
            ViewUtil.setViewSizeh(mBottomLine, px2dip(context, bottomLineHeight));
        }

    }

    /**
     * 设置标题
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
        mTitleView = (TextView) findViewById(R.id.tv_title);
        if (!StrUtil.isEmpty(titleText)) {
            mTitleView.setText(titleText);
        }
    }

    /**
     * 设置右侧文字
     *
     * @param moreText
     */
    public void setMoreText(String moreText) {
        this.moreText = moreText;
        //右侧文字
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        mMoreImage = (ImageView) findViewById(R.id.iv_more);
        mMoreTextView.setVisibility(VISIBLE);
        mMoreImage.setVisibility(INVISIBLE);
        if (!StrUtil.isEmpty(moreText)) {
            mMoreTextView.setText(moreText);
        }
    }

    /**
     * 设置右侧文字
     *
     */
    public TextView geTitleText( ) {
        mTitleView = (TextView) findViewById(R.id.tv_title);
        if (!StrUtil.isEmpty(titleText)) {
            mTitleView.setText(titleText);
        }
        return mTitleView;
    }

    /**
     * 设置右侧文字
     *
     */
    public TextView getMoreText( ) {
        this.moreText = moreText;
        //右侧文字
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        mMoreImage = (ImageView) findViewById(R.id.iv_more);
        mMoreTextView.setVisibility(VISIBLE);
        mMoreImage.setVisibility(INVISIBLE);
        if (!StrUtil.isEmpty(moreText)) {
            mMoreTextView.setText(moreText);
        }
        return mMoreTextView;
    }

    /**
     * 设置返回图标
     *
     * @param backImage
     */
    public void setBackImage(int backImage) {
        mBackImage = (ImageView) findViewById(R.id.iv_back);
        //设置返回图标
        if (backImage != 0) {
            mBackImage.setImageResource(backImage);
        }
    }

    /**
     * 设置右侧图标
     *
     * @param moreImage
     */
    public void setMoreImage(int moreImage) {
        //右侧图片
        mMoreImage = (ImageView) findViewById(R.id.iv_more);
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        //设置右侧右侧图片
        if (moreImage != 0) {
            mMoreImage.setImageResource(moreImage);
            mMoreImage.setVisibility(VISIBLE);
            mMoreTextView.setVisibility(INVISIBLE);
        } else {
            mMoreImage.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置整体文字颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        //右侧文字
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        //设置title
        if (mTitleView != null) {
            if (textColor != 0) {
                mTitleView.setTextColor(getResources().getColor(textColor));
                mMoreTextView.setTextColor(getResources().getColor(textColor));
            }
        }
    }

    /**
     * 设置右侧文字颜色
     *
     * @param moreTextColor
     */
    public void setMoreTextColor(int moreTextColor) {
        this.moreTextColor = moreTextColor;
        //右侧文字
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        //设置右侧文字
        if (moreTextColor != 0) {
            mMoreTextView.setTextColor(getResources().getColor(moreTextColor));
        }
    }

    /**
     * 设置整体文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        //右侧文字
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        //设置title
        if (textSize != 0) {
            mTitleView.setTextSize(px2dip(context, getResources().getDimension(textSize)));
        }

    }

    /**
     * 设置右侧文字大小
     *
     * @param moreTextSize
     */
    public void setMoreTextSize(int moreTextSize) {
        mMoreTextView = (TextView) findViewById(R.id.tv_more);
        //设置title
        if (mMoreTextView != null) {
            if (moreTextSize != 0) {
                mMoreTextView.setTextSize(px2dip(context, getResources().getDimension(R.dimen.right_text)));
            }
        }
    }

    /**
     * 设置是否可以返回
     *
     * @param canBack
     */
    public void setCanBack(boolean canBack) {
        mBackImage = (ImageView) findViewById(R.id.iv_back);
        mBackImage.setVisibility(canBack ? VISIBLE : INVISIBLE);
        //设置返回按钮
        if (canBack) {
            mBackImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity) getContext()).finish();
                }
            });
        }
    }

    /**
     * 设置左侧按钮的点击事件
     *
     * @param backAction
     */
    public void setBackAction(OnClickListener backAction) {
        mBackImage = (ImageView) findViewById(R.id.iv_back);
        mBackImage.setOnClickListener(backAction);
    }


    /**
     * 设置右侧按钮点击事件
     *
     * @param moreAction
     */
    public void setMoreAction(OnClickListener moreAction) {
        mMoreLayout = (RelativeLayout) findViewById(R.id.rl_more);
        mMoreLayout.setOnClickListener(moreAction);
    }

    /**
     * 设置标题文字点击事件
     *
     * @param titleTextAction
     */
    public void setTitleTextAction(OnClickListener titleTextAction) {
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setOnClickListener(titleTextAction);
    }


    /**
     * 设置底部分割线的高度
     *
     * @param bottomLineHeight
     */
    public void setBottomLineHeight(int bottomLineHeight) {
        //底部分割线
        mBottomLine = findViewById(R.id.v_bottom_line);
        ViewUtil.setViewSizeh(mBottomLine, (int) getResources().getDimension(bottomLineHeight));
    }

    /**
     * 设置底部分割线的颜色
     *
     * @param bottomLineRes
     */
    public void setBottomLineRes(int bottomLineRes) {
        //底部分割线
        mBottomLine = findViewById(R.id.v_bottom_line);
        mBottomLine.setBackgroundResource(bottomLineRes);
    }


    /**
     * 像素转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置标题文字的右侧drawable
     *
     * @param res
     */
    public void setTitleIconRight(int res) {
        Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩
        mTitleView.setCompoundDrawables(null, null, drawable, null);
    }
}
