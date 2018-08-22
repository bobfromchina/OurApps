package com.lovely3x.common.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lovely3x.common.R;
import com.lovely3x.common.utils.ViewUtils;

/**
 * 引导页
 * Created by lovely3x on 15-11-10.
 */
public abstract class GuideActivity extends CommonActivity {

    /**
     * 第一次使用记录
     */
    public static final String FIRST_ENTRY_RECORD = "first.entry.record";
    /**
     * 保存是否是第一次使用的key
     */
    private static final String IS_FIRST_USE_KEY = "is.first.use.key";

    /**
     * app版本记录key
     */
    private static final String APP_VERSION_KEY = "app.version.key";


    protected ViewPager mGidePager;

    protected int flaggingWidth;
    protected GestureDetector mGestureDetector;
    private LinearLayout mIndicator;

    private int[] indicatorDrawable = null;

    /**
     * 获取指示器drawable文件
     * 数组[0] 未选中
     * 数组[1] 选中
     */
    protected int[] getIndicatorDrawable() {
        return new int[]{R.drawable.guide_indicator_ponit_uncheck, R.drawable.guide_indicator_point_checked};
    }

    /**
     * 获取内容视图
     */
    protected int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isFirstEntry()) {
            onPageScrollDone();
            finish();
            return;
        }
        setContentView(getContentView());
        // 获取分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        flaggingWidth = dm.widthPixels / 3;

        indicatorDrawable = getIndicatorDrawable();

        this.mIndicator = (LinearLayout) findViewById(R.id.ll_activity_guide_indicator);
        mGidePager = (ViewPager) findViewById(R.id.act_guide_pager);
        mGidePager.setAdapter(getAdapter());
        mGestureDetector = new GestureDetector(this, new GuideViewTouch());

        makeIndicator();

        mGidePager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                checkIndicator(mIndicator.getChildAt(position));
            }
        });

        if (mIndicator.getChildCount() > 0) checkIndicator(mIndicator.getChildAt(0));
    }

    /**
     * 创建指示器
     */
    protected void makeIndicator() {
        final int N = getRes() == null ? 0 : getRes().length;
        final int pointSize = getIndicatorSize();

        for (int i = 0; i < N; i++) {
            LinearLayout.LayoutParams marginLeftParameter = new LinearLayout.LayoutParams(pointSize, pointSize);
            if (i != 0) {
                marginLeftParameter.leftMargin = ViewUtils.dp2pxF(16);
                mIndicator.addView(makeIndicatorPoint(i, false), marginLeftParameter);
            } else {
                mIndicator.addView(makeIndicatorPoint(i, false), marginLeftParameter);
            }
        }
    }

    /**
     * 获取指示器的大小
     *
     * @return
     */
    protected int getIndicatorSize() {
        return ViewUtils.dp2pxF(8);
    }

    /**
     * 创建指示器
     *
     * @param position 位置
     * @param checked  默认是否选中
     * @return
     */
    protected View makeIndicatorPoint(int position, boolean checked) {
        View indicator = new View(this);
        checkIndicatorPoint(indicator, checked);
        return indicator;
    }

    /**
     * 选中当前的指示器
     *
     * @param view
     */
    protected void checkIndicator(View view) {
        final int N = mIndicator.getChildCount();
        for (int i = 0; i < N; i++) {
            View curView = mIndicator.getChildAt(i);
            checkIndicatorPoint(curView, curView == view);
        }
    }

    /**
     * 选中指示器或非选中指示器
     *
     * @param view
     * @param checked
     */
    protected void checkIndicatorPoint(View view, boolean checked) {
        view.setBackgroundResource(indicatorDrawable[checked ? 1 : 0]);
    }

    /**
     * 获取引导页的资源图片数据集合
     *
     * @return 引导页的资源图片数据集合
     */
    public abstract int[] getRes();

    /**
     * 当页面滑动完成之后再次滑动,会调用这个方法
     */
    public abstract void onPageScrollDone();

    /**
     * 获取默认的 引导页pager适配器
     * 子类可以重写
     *
     * @return
     */
    protected PagerAdapter getAdapter() {
        return new GuideViewPager(getRes(), this);
    }

    /**
     * 是否能够通过滑动鞥进入app的操作
     *
     * @return true or false
     */
    protected abstract boolean calScrollToEntry();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (calScrollToEntry() && mGestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }

    public static class GuideViewPager extends PagerAdapter {

        protected final Context mContext;
        protected int[] res;

        public GuideViewPager(int[] res, Context context) {
            this.mContext = context;
            this.res = res;
        }

        @Override
        public int getCount() {
            return res == null ? 0 : res.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(mContext);
            iv.setImageResource(res[position]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(iv);
            return iv;
        }
    }

    private class GuideViewTouch extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mGidePager.getCurrentItem() == mGidePager.getAdapter().getCount() - 1) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
                        && (e1.getX() - e2.getX() <= (-flaggingWidth) || e1
                        .getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        onPageScrollDone();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * 是否是第一次进入app
     *
     * @return true or false
     */
    protected boolean isFirstEntry() {
        SharedPreferences sp = getFirstEntryRecord();

        boolean result = sp.getBoolean(IS_FIRST_USE_KEY, true);
        //int currentVersionCode = getCurrentVersionCode();
        //int recordVersion = sp.getInt(APP_VERSION_KEY, currentVersionCode);

        //result = currentVersionCode != recordVersion || sp.getBoolean(IS_FIRST_USE_KEY, true);

        updateFirstEntryUseRecord(result ? false : false);

        return result;
    }

    /**
     * 更新是否是第一次使用
     *
     * @param isFirstUse true or false
     */
    public void updateFirstEntryUseRecord(boolean isFirstUse) {
        SharedPreferences.Editor editor = getFirstEntryRecord().edit();
        int currentVersionCode = getCurrentVersionCode();
        editor.putInt(APP_VERSION_KEY, currentVersionCode);
        editor.putBoolean(IS_FIRST_USE_KEY, isFirstUse).commit();
    }

    protected int getCurrentVersionCode() {
        int currentVersionCode = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                currentVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return currentVersionCode;
    }

    /**
     * 获取是否第一次进入的记录文件
     *
     * @return
     */
    protected SharedPreferences getFirstEntryRecord() {
        return getSharedPreferences(FIRST_ENTRY_RECORD, Context.MODE_PRIVATE);
    }
}
