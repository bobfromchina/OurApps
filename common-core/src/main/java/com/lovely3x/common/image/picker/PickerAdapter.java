package com.lovely3x.common.image.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovely3x.common.CommonApplication;
import com.lovely3x.common.R;
import com.lovely3x.common.adapter.BaseViewHolder;
import com.lovely3x.common.adapter.ListAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 图片选择
 * Created by lovely3x on 15-9-1.
 */
public class PickerAdapter extends ListAdapter<ImageBucket> {

    private final ImageLoader mLoader;
    private final DisplayImageOptions mDisplayOptions;

    public PickerAdapter(List<ImageBucket> datas, Context context, ImageLoader loader) {
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


    @NonNull
    @Override
    public BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        View convertView = getLayoutInflater().inflate(R.layout.list_item_common_image_picker, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void handleData(int position, @NonNull BaseViewHolder holder) {
        ImageBucket data = datas.get(position);
        ViewHolder pickerHolder = (ViewHolder) holder;
        mLoader.displayImage("file://" + data.getBucketList().get(0).getImagePath(), pickerHolder.mItemImage, mDisplayOptions);
        pickerHolder.mItemTitle.setText(data.bucketName + " (" + data.getBucketList().size() + ")");
    }

    static class ViewHolder extends BaseViewHolder {

        private final ImageView mItemImage;
        private final TextView mItemTitle;

        /**
         * please don't modify the constructor
         *
         * @param rootView the root view
         */
        public ViewHolder(View rootView) {
            super(rootView);
            mItemImage = ButterKnife.findById(rootView, R.id.list_item_common_image_picker_thumbnail);
            mItemTitle = ButterKnife.findById(rootView, R.id.list_item_common_image_picker_title);
        }
    }
}
