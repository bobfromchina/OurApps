package com.jackmar.jframelibray.utils.imagepicker;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

/**
 * 图片选择器工具
 * Created by JackMar on 2017/3/7.
 */

public class ImagePickerUtils {
    private static ImagePicker imagePicker;

    public static void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

    }

    /**
     * 设置图片选择数量
     *
     * @param count
     */
    public static void setSelectCount(int count) {
        ImagePicker.getInstance().setSelectLimit(count);
    }

    /**
     * 设置图片选择后是否需要裁剪
     *
     * @param needCrop
     */
    public static void enableCrop(boolean needCrop) {
        imagePicker.setCrop(needCrop);//设置是否需要裁剪
    }

}
