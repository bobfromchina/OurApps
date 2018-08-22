package com.lovely3x.common.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * 图片压缩器
 * Created by lovely3x on 16-1-8.
 */
public class ImageCompresser {

    /**
     * 缩放图片
     *
     * @param file         需要缩放的我文件
     * @param targetWidth  目标的宽度
     * @param targetHeight 目标高度
     * @return 解析的图片对象
     */
    public static Bitmap scale(File file, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = computeSampleSize(options, -1, targetWidth * targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * 缩放图片
     *
     * @param drawableRes  需要缩放的我文件
     * @param targetWidth  目标的宽度
     * @param targetHeight 目标高度
     * @return 解析的图片对象
     */
    public static Bitmap scale(Resources resources, int drawableRes, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, drawableRes, options);
        options.inSampleSize = computeSampleSize(options, -1, targetWidth * targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, drawableRes, options);
    }

    /**
     * 缩放图片
     *
     * @param bytes        需要缩放的bitmap
     * @param targetWidth  目标的宽度
     * @param targetHeight 目标高度
     * @return 解析的图片对象
     */
    public static Bitmap scale(byte[] bytes, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = computeSampleSize(options, -1, targetWidth * targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
