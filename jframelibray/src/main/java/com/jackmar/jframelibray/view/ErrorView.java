package com.jackmar.jframelibray.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.utils.ViewUtil;

import butterknife.ButterKnife;


public class ErrorView extends ViewBase {
    ImageView mIvImage;
    TextView mTvText;
    private Context context;
    private View view;


    public ErrorView(Context context) {
        super(context);
        init(context);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.error_view, this, true);
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mTvText = (TextView) view.findViewById(R.id.tv_text);
        ViewUtil.setViewSize(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
    }

    /**
     * 设置错误
     *
     * @param res
     */
    public void setError(int res, String text) {
        mIvImage.setImageResource(res);
        mTvText.setText(text);
    }


}
