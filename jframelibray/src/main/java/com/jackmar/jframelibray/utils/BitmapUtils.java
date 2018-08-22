package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */

public class BitmapUtils {

    /**
     * @param filePath
     * @param targetOutPath
     * @param quality
     * @return
     */
    public static String compressImage(Context context, String filePath, String targetOutPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        File outputFile = new File(JFileHelper.getDiskCacheDir(context), targetOutPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    /**
     * 描述：获取src中的图片资源.
     *
     * @param src 图片的src路径，如（“image/arrow.png”）
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFormSrc(String src) {
        Bitmap bit = null;
        try {
            bit = BitmapFactory.decodeStream(BitmapUtils.class
                    .getResourceAsStream(src));
        } catch (Exception e) {

        }
        return bit;
    }

    /**
     * 根据指定的高度和宽度缩放图片
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     */
    public static Bitmap getSDCardBitmapWH(String filePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap imageBitmap = null;
        BitmapFactory.decodeFile(filePath, options);
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }
        int realWidth = options.outWidth;
        int realheight = options.outHeight;
        if (reqWidth > reqHeight)
            options.inSampleSize = Math.round((float) realheight / (float) reqHeight);
        else
            options.inSampleSize = Math.round((float) realWidth / (float) reqWidth);

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//		options.inPreferredConfig = Bitmap.Config.RGB_565; // 与ARGM_8888相比少小号2倍的内存,默认为ARGM_8888
        imageBitmap = BitmapFactory.decodeFile(filePath, options);
        return imageBitmap;
    }

    /**
     * 保存bitmap
     *
     * @param bitmap
     * @param savePath
     * @param saveFileName
     * @return
     */
    public static String saveBitmap(Bitmap bitmap, String savePath, String saveFileName) {
        // 图片文件名称
        if (TextUtils.isEmpty(saveFileName)) {
            saveFileName = (System.currentTimeMillis() + ".jpg");
        }
        File file = new File(savePath, saveFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 复制图片到制定路径
     *
     * @param bitmapPath
     * @param savePath
     * @param saveFileName
     * @return
     */
    public static File saveBitmapByPath(String bitmapPath, String savePath, String saveFileName) {
        // 图片文件名称
        if (TextUtils.isEmpty(saveFileName)) {
            saveFileName = (System.currentTimeMillis() + ".jpg");
        }
        File file = new File(savePath, saveFileName);
        if (file.exists()) {
            file.delete();
        }
        File bitmapFile = new File(bitmapPath);
        if (bitmapFile.exists()) {
            try {
                FileInputStream in = new FileInputStream(bitmapFile);
                Bitmap tbitmap = BitmapFactory.decodeStream(in);
                FileOutputStream out = new FileOutputStream(file);
                tbitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                tbitmap.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 压缩bitmap质量存储
     *
     * @param context
     * @param bitmap
     * @param targetPath
     * @param quality
     * @return
     */
    public static String bitmapToFile(Context context, Bitmap bitmap, String targetPath, int quality) {

        File outputFile = new File(JFileHelper.getDiskCacheDir(context), targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    /**
     * view 转bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

}
