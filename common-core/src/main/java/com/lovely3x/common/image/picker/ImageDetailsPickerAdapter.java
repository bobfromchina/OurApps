package com.lovely3x.common.image.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lovely3x.common.CommonApplication;
import com.lovely3x.common.R;
import com.lovely3x.common.adapter.BaseViewHolder;
import com.lovely3x.common.adapter.ListAdapter;
import com.lovely3x.common.utils.ViewUtils;
import com.lovely3x.common.widgets.CheckPointView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 图片选择
 * Created by lovely3x on 15-9-1.
 */
public class ImageDetailsPickerAdapter extends ListAdapter<ImageItem> {

    private final ImageLoader mLoader;

    private final DisplayImageOptions mDisplayOptions;

    /**
     * 是否是多选模式
     */
    private boolean isMultiCheckModel = true;

    /**
     * 单选模式下是否显示选择标识
     */
    private boolean checkerShowInSingleCheckModel;

    /**
     * 用户选择容器
     */
    private SparseBooleanArray checkArray = new SparseBooleanArray();

    /**
     * 多选监听器
     */
    private MultiOnCheckedListener multiOnCheckedListener;

    /**
     * 单选监听器
     */
    private SingleOnCheckedListener singleOnCheckedListener;

    public ImageDetailsPickerAdapter(List<ImageItem> datas, Context context, ImageLoader loader) {
        super(datas, context);
        this.mLoader = loader;
        Bitmap loading = BitmapFactory.decodeResource(context.getResources(), CommonApplication.getInstance().getLoadingImgResId());
        Bitmap loadFailure = BitmapFactory.decodeResource(context.getResources(), CommonApplication.getInstance().getLoadingFailureResId());
        mDisplayOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageOnLoading(new BitmapDrawable(mContext.getResources(), loading))
                .showImageOnFail(new BitmapDrawable(mContext.getResources(), loadFailure))
                .showImageForEmptyUri(new BitmapDrawable(mContext.getResources(), loadFailure))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    public void setMultiOnCheckedListener(MultiOnCheckedListener multiOnCheckedListener) {
        this.multiOnCheckedListener = multiOnCheckedListener;
    }

    public void setSingleOnCheckedListener(SingleOnCheckedListener singleOnCheckedListener) {
        this.singleOnCheckedListener = singleOnCheckedListener;
    }

    /**
     * 获取选中的表
     *
     * @return
     */
    public SparseBooleanArray getCheckArray() {
        return checkArray;
    }

    /**
     * 设置选中的条目
     *
     * @param position  选中的位置
     * @param isChecked 是否选中
     */
    public void setItemChecked(int position, boolean isChecked) {
        if (position < 0 || position >= datas.size()) {
            throw new IndexOutOfBoundsException();
        }
        checkArray.put(position, isChecked);
        notifyDataSetChanged();
    }

    /**
     * 设置选中的条目
     *
     * @param array 需要选中的条目
     */
    public void setItemChecked(SparseBooleanArray array) {
        int len = array.size();
        int dataSize = datas.size();
        for (int i = 0; i < len; i++) {
            int position = array.keyAt(i);
            if (position < 0 || position >= dataSize) {
                throw new IndexOutOfBoundsException();
            }
            checkArray.put(position, array.get(position));
        }
        notifyDataSetChanged();
    }

    /**
     * 设置选择模式
     *
     * @param multiCheck 是否为多选模式
     */
    public void setCheckModel(boolean multiCheck) {
        this.isMultiCheckModel = multiCheck;
    }

    /**
     * 设置单选模式是否显示选择器标识
     *
     * @param checkerShowInSingleCheckModel true or false
     */
    public void setCheckerShowInSingleCheckModel(boolean checkerShowInSingleCheckModel) {
        this.checkerShowInSingleCheckModel = checkerShowInSingleCheckModel;
    }

    @NonNull
    @Override
    public BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        View convertView = getLayoutInflater().inflate(R.layout.grid_item_common_image_picker, parent, false);
        return new ViewHolder(convertView);
    }

    /**
     * 设置选中的条目
     *
     * @param checkedList 需要选中的条目
     */
    public void setItemChecked(List<Integer> checkedList) {
        int len = checkedList.size();
        for (int i = 0; i < len; i++) {
            Integer position = checkedList.get(i);
            if (position < 0 || position >= datas.size()) {
                throw new IndexOutOfBoundsException();
            }
            this.checkArray.put(position, true);
        }
        notifyDataSetChanged();
    }

    @Override
    public void handleData(int position, @NonNull BaseViewHolder holder) {
        ImageItem data = datas.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        //设置点击监听器,用来监听器这个,选择变化
        holder.mRootView.setOnClickListener(new DefaultClickListener(position));

        if (isMultiCheckModel) {//多选模式
            if (viewHolder.mCtv.getVisibility() != View.VISIBLE) {
                viewHolder.mCtv.setVisibility(View.VISIBLE);
            }
        } else {//单选模式
            if (checkerShowInSingleCheckModel) {//如果设置的单选模式也显示选择器标识
                if (viewHolder.mCtv.getVisibility() != View.VISIBLE) {
                    viewHolder.mCtv.setVisibility(View.VISIBLE);
                }
            } else {
                if (viewHolder.mCtv.getVisibility() != View.GONE) {
                    viewHolder.mCtv.setVisibility(View.GONE);
                }
            }
        }
        boolean isChecked = checkArray.get(position);
        viewHolder.mCtv.setChecked(isChecked);
        mLoader.displayImage("file://" + data.getImagePath(), new ImageViewAware(viewHolder.mItemImage) {
            @Override
            public int getWidth() {
                return ViewUtils.dp2pxF(100);
            }

            @Override
            public int getHeight() {
                return getWidth();
            }
        }, mDisplayOptions);
    }

    /**
     * 单选监听器
     */
    public interface SingleOnCheckedListener {
        /**
         * @param position  当前操作的这个位置
         * @param isChecked 当前操作的这个位置是否为选中
         */
        void onSingleChecked(int position, boolean isChecked);
    }

    /**
     * 多选监听器
     */
    public interface MultiOnCheckedListener {
        /**
         * @param checkedArray 当前选择的所有条目
         * @param position     当前操作的这个条目的位置
         * @param isChecked    当前操作的这个条目是否选中
         */
        void onMultiChecked(List<Integer> checkedArray, int position, boolean isChecked);
    }

    public static class ViewHolder extends BaseViewHolder {
        protected final ImageView mItemImage;
        protected final CheckPointView mCtv;

        /**
         * please don't modify the constructor
         *
         * @param rootView the root view
         */
        public ViewHolder(View rootView) {
            super(rootView);
            mItemImage = ButterKnife.findById(rootView, R.id.grid_item_common_image_picker_image);
            mCtv = ButterKnife.findById(rootView, R.id.ctv_grid_item_common_image_picker_checker);
        }
    }

    /**
     * 条目点击监听器
     */
    private class DefaultClickListener implements View.OnClickListener {
        private final int mPosition;

        public DefaultClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            if (!isMultiCheckModel) {//单选模式
                //清除掉原来选择的
                checkArray.clear();
            }
            boolean origin = checkArray.get(mPosition);
            checkArray.put(mPosition, !origin);
            notifyDataSetChanged();
            if (isMultiCheckModel) {//多选模式
                if (multiOnCheckedListener != null) {
                    ArrayList<Integer> checkList = new ArrayList<Integer>();
                    int len = checkArray.size();
                    for (int i = 0; i < len; i++) {
                        int position = checkArray.keyAt(i);
                        boolean isChecked = checkArray.get(position);
                        if (isChecked) checkList.add(position);
                    }
                    //回调监听器
                    multiOnCheckedListener.onMultiChecked(checkList, mPosition, !origin);
                }
            } else {//单选模式
                if (singleOnCheckedListener != null) {
                    //回调监听器
                    singleOnCheckedListener.onSingleChecked(mPosition, !origin);
                }
            }
        }
    }
}
