package com.lovely3x.common.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

/**
 * Created by lovely3x on 15-9-11.
 * bitmap utils
 */
public class BitmapUtils {


    /**
     * 获取缩略图
     *
     * @param bm   原图对象
     * @param size 图片的尺寸
     * @return
     */
    public static final Bitmap getThumbnail(Bitmap bm, int size) {
        return Bitmap.createScaledBitmap(bm, size, size, false);
    }

    /**
     * 获取缩略图
     *
     * @param bm     原图对象
     * @param width  缩略图的宽度
     * @param height 缩略图的高度
     * @return
     */
    public static final Bitmap getThumbnail(Bitmap bm, int width, int height) {
        return Bitmap.createScaledBitmap(bm, width, height, false);
    }

    /**
     * 获取缩略图
     *
     * @param filePath 缩略图的文件地址
     * @param width    缩略图的宽度
     * @param height   缩略图的高度
     * @return
     */
    public static Bitmap getThumbnail(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);

        //int realWidth = options.outWidth >= width ? width : options.outWidth;
        //int realHeight = options.outHeight >= height ? height : options.outHeight;

        float scaleX = options.outWidth >= width ? 1.0f * options.outWidth / width : 1;
        float scaleY = options.outHeight >= height ? 1.0f * options.outHeight / height : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) Math.min(scaleX, scaleY);
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int getInSampleSizeFromScale(float scale, boolean keepPowOfTwo) {
        int insample = 1;
        float scaleThreshold = 1.f;

        while (scaleThreshold >= scale) {
            if (keepPowOfTwo) {
                insample *= 2;
            } else {
                insample += 1;
            }

            scaleThreshold = scaleThreshold / insample;
        }

        if (keepPowOfTwo) {
            return insample / 2;
        } else {
            return insample - 1;
        }
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //计算缩放比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }



    public static Bitmap screenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        return view.getDrawingCache();
    }

}
