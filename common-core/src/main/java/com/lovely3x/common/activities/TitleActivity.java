package com.lovely3x.common.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.utils.ViewUtils;

/**
 * 标题栏的activity
 *
 * @author lovely3x
 * @version 1.0
 *
 *   本类为标题的基类，我们可以修改标题栏或者重写标题栏的一些方法。
 */
public abstract class TitleActivity extends BaseCommonActivity {

    /**
     * 标题
     */
    protected TextView tvTitle;
    /**
     * 返回
     */
    protected ImageView ivBack;
    /**
     * 分割线
     */
    protected View viewDivider;
    /**
     * 整个标题视图
     */
    protected ViewGroup mTitleLayout;
    /**
     * 标题容器
     */
    protected FrameLayout flTitleContainer;
    /**
     * 内容容器
     */
    protected FrameLayout flContent;

    /**
     *  返回的文字按钮
     */
    protected  TextView tvBack;

    //  返回键
    public ImageView getIvBack() {
        return ivBack;
    }

    public FrameLayout getTitleContent() {
        return flContent;
    }

    public TextView getTvback(){
        return tvBack;
    }


    @Override
    public void setContentView(int layoutResID) {
        if (mTitleLayout == null) {
            initTitleView();
            addTitleHeader(flTitleContainer);
            getLayoutInflater().inflate(layoutResID, flContent, true);
        }

        setContentView(mTitleLayout);
    }


    public void setTitleColor(int color) {
        if (tvTitle != null) {
            tvTitle.setTextColor(color);
        }
    }

    public View getViewDivider() {
        return viewDivider;
    }

    @Override
    public void setContentView(View view) {
        if (mTitleLayout == null) {
            initTitleView();
            addTitleHeader(flTitleContainer);
            flContent.addView(view);
        }
        setContentView(mTitleLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        if (mTitleLayout == null) {
            initTitleView();
            addTitleHeader(flTitleContainer);
            flContent.addView(view, params);
        }
        super.setContentView(mTitleLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    /**
     * 获取标题容器
     */
    public FrameLayout getTitleContainer() {
        return flTitleContainer;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    /**
     * 隐藏标题视图
     */
    public void hiddenTitle() {
        if (flTitleContainer != null) {
            flTitleContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 平滑的隐藏标题
     */
    public void smoothHiddenTitle() {
        final ObjectAnimator animator =
                ObjectAnimator.ofFloat(flTitleContainer, "translationY", 0, -flTitleContainer.getMeasuredHeight());
        animator.setDuration(50);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                flTitleContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flTitleContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                flTitleContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /***
     * 平滑的显示标题
     */
    public void smoothShowTitle() {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(flTitleContainer, "translationY", -flTitleContainer.getMeasuredHeight(), 0);
        animator.setDuration(50);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                flTitleContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                flTitleContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 显示标题视图
     */
    public void showTitle() {
        if (flTitleContainer != null) {
            flTitleContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (tvTitle != null) {
            tvTitle.setText(titleId);
        }
    }

    /**
     * 设置默认的标题栏视图 使用者可以重写这个方法将需要添加的视图添加到这里面去
     *
     * @return
     */
    protected void addTitleHeader(ViewGroup titleContainer) {
        titleContainer.removeAllViews();
        getLayoutInflater().inflate(R.layout.view_default_title, flTitleContainer, true);
        tvTitle = (TextView) flTitleContainer.findViewById(R.id.tv_view_default_title);
        ivBack = (ImageView) flTitleContainer.findViewById(R.id.iv_view_default_title_back);
        viewDivider = flTitleContainer.findViewById(R.id.view_view_default_title_divider);
        tvBack = (TextView) flTitleContainer.findViewById(R.id.tv_view_default_title_back);
        TitleClickedListener titleClickedListener = new TitleClickedListener();
        tvTitle.setOnClickListener(titleClickedListener);
        ivBack.setOnClickListener(titleClickedListener);
        tvBack.setOnClickListener(titleClickedListener);
    }

    /**
     * 获取标题视图
     */
    protected void initTitleView() {
        mTitleLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_title, null);
        flTitleContainer = (FrameLayout) mTitleLayout.findViewById(R.id.fl_activity_title);
        flContent = (FrameLayout) mTitleLayout.findViewById(R.id.fl_activity_content);
    }

    /**
     * 当返回按钮被点击了
     *
     * @param v
     */
    protected void onBackClicked(View v) {
        onBackPressed();
    }

    /**
     * 添加一个视图 到title容器中
     *
     * @param view
     * @param lp
     */
    protected void addViewToTitleContainer(View view, FrameLayout.LayoutParams lp) {
        if (flTitleContainer != null) {
            flTitleContainer.addView(view, lp);
        }
    }

    /**
     * 在标题栏右边添加 按钮
     *
     * @param id   添加的这个视图的id
     * @param text 添加的这个视图显示的内容
     */
    protected TextView addRightBtn(String text, int id) {
        int padding = ViewUtils.dp2pxF(10);
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(getColor(R.color.gold_yellow, true));
        tv.setId(id);
        tv.setTextSize(15);
        tv.setPadding(padding, padding, padding, padding);
        FrameLayout.LayoutParams tvLP = new FrameLayout.LayoutParams(-2, -2);
        tvLP.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        addViewToTitleContainer(tv, tvLP);
        return tv;
    }

    /**
     * 添加右边的icon
     * @param resourceId
     * @param id
     * @return
     */
    public ImageView addRightIcon(@DrawableRes int resourceId, int id) {
        ImageView iv = new ImageView(this);
        iv.setImageResource(resourceId);
        iv.setId(id);
        addRightView(iv);
        return iv;
    }

    /**
     * 添加左边的按钮
     *
     * @param text 按钮的文本
     * @param id   id
     */
    public TextView addLeftButton(String text, int id) {
        int padding = ViewUtils.dp2pxF(10);
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(getColor(R.color.gold_yellow, true));
        tv.setId(id);
        tv.setTextSize(15);
        tv.setPadding(padding, padding, padding, padding);
        FrameLayout.LayoutParams tvLP = new FrameLayout.LayoutParams(-2, -2);
        tvLP.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
        addViewToTitleContainer(tv, tvLP);
        return tv;
    }

    /**
     * @param textRes 按钮的文本
     * @param id      id
     *                添加左边的按钮
     */
    public TextView addLeftButton(int textRes, int id) {
        return addLeftButton(getResources().getString(textRes), id);
    }

    /**
     * 在标题栏右边添加 按钮
     *
     * @param id        添加的这个视图的id
     * @param textResId 添加的这个视图显示的内容
     */
    protected TextView addRightBtn(@StringRes int textResId, int id) {
        return addRightBtn(getString(textResId), id);
    }

    /**
     * 在标题栏右边添加一个视图
     * 默认会添加10dp的padding
     *
     * @param view 需要添加的视图
     * @return 被添加的视图对象
     */
    protected View addRightView(View view) {
        int padding = ViewUtils.dp2pxF(10);
        view.setPadding(padding, padding, padding, padding);
        FrameLayout.LayoutParams tvLP = new FrameLayout.LayoutParams(-2, -2);
        tvLP.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        addViewToTitleContainer(view, tvLP);
        return view;
    }

    /**
     * 获取左边的返回按钮
     *
     * @return
     */
    protected ImageView getLeftImageView() {
        return this.ivBack;
    }

    /**
     * 当标题被点击后执行
     */
    protected void onTitleClicked() {
        // do something
    }

    /**
     * 隐藏标题栏
     *
     * @param hidden 是否隐藏
     */
    protected void hiddenTitleBar(boolean hidden) {
        if (flTitleContainer != null) {
            flTitleContainer.setVisibility(hidden ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 获取content容器视图
     *
     * @return 容器视图
     */
    public ViewGroup getRootView() {
        return (ViewGroup) mTitleLayout.getParent();
    }

    /**
     * 头部视图点击监听器
     *
     * @author lovely3x
     * @version 1.0
     * @time 2015-4-24 下午3:48:40
     */
    public class TitleClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_view_default_title_back) {
                onBackClicked(v);
            } else if (v.getId() == R.id.tv_view_default_title) {
                onTitleClicked();
            }else if (v.getId() == R.id.tv_view_default_title_back){
                onBackClicked(v);
            }
        }
    }
}
