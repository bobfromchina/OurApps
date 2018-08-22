package com.example.wangbo.ourapp.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.jackmar.jframelibray.utils.GlideImageLoadUtil;

/**
 * 网络图片Holder
 */
public class NetWorkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        GlideImageLoadUtil.loadImage(context, data, imageView);
    }
}