package com.jackmar.jframelibray.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jackmar.jframelibray.R;

import java.io.File;


public class GlideImageLoadUtil {

    /**
     * 使用glide加载网络图片
     *
     * @param context
     * @param url            图片链接
     * @param imageView      被加载视图
     * @param animRes        动画资源
     * @param placeHolderRes 加载中状态资源
     * @param errorRes       加载失败状态资源
     */
    public static void loadImage(Context context, String url, ImageView imageView, int animRes, int placeHolderRes, int errorRes) {
        Glide.with(context).load(url).asBitmap().placeholder(placeHolderRes).error(errorRes).animate(animRes).into(imageView);
    }


    /**
     * 常规图片加载
     *
     * @param context
     * @param url       图片链接
     * @param imageView 被加载视图
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().placeholder(R.drawable.header_logo).animate(R.anim.anim_image_load).into(imageView);
    }

    /**
     * 常规图片加载
     *
     * @param context
     * @param url       图片链接
     * @param imageView 被加载视图
     */
    public static void loadImageWh(Context context, String url, ImageView imageView, int width, int height, int res) {
        Glide.with(context).load(url).asBitmap().animate(R.anim.anim_image_load).error(res).placeholder(res).override(width, height).into(imageView);
    }


    /**
     * 常规图片加载
     *
     * @param context
     * @param url       图片链接
     * @param imageView 被加载视图
     */
    public static void loadImageNoImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().into(imageView);
    }

    /**
     * 加载文件中的图片
     *
     * @param context
     * @param file      图片文件
     * @param imageView 被加载视图
     */
    public static void loadFileImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file).asBitmap().animate(R.anim.anim_image_load).into(imageView);
    }


    /**
     * 加载res文件
     *
     * @param context
     * @param imageRes       本地的图片的资源ID
     * @param imageView      被加载视图
     * @param animRes        动画资源
     * @param placeHolderRes 加载中状态
     * @param errorRes       错误资源图
     */
    public static void loadResImage(Context context, int imageRes, ImageView imageView, int animRes, int placeHolderRes, int errorRes) {
        Glide.with(context).load(imageRes).asBitmap().placeholder(placeHolderRes).error(errorRes).animate(animRes).into(imageView);
    }

    /**
     * 加载本地文件的图片
     *
     * @param context
     * @param imageFilePath  图片的地址
     * @param imageView      被加载视图
     * @param animRes        动画资源
     * @param placeHolderRes 加载过程中
     * @param errorRes       加载失败
     */
    public static void loadFileImage(Context context, String imageFilePath, ImageView imageView, int animRes, int placeHolderRes, int errorRes) {
        File file = new File(imageFilePath);
        if (file.exists()) {
            Glide.with(context).load(file).asBitmap().placeholder(placeHolderRes).error(errorRes).animate(animRes).into(imageView);
        }
    }

    /**
     * 加载本地文件的图片
     *
     * @param context
     * @param imageFilePath 图片的地址
     * @param imageView     被加载视图
     */
    public static void loadFileImage(Context context, String imageFilePath, ImageView imageView) {
        File file = new File(imageFilePath);
        if (file.exists()) {
            Glide.with(context).load(file).asBitmap().into(imageView);
        }
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param url       地址
     * @param imageView 控件
     */
    public static void loadRoundImage(final Context context, String url, final ImageView imageView, int width, int height, int defaultImage) {
        Glide.with(context).load(url).asBitmap().
                placeholder(defaultImage).error(defaultImage).
                centerCrop().override(width, height).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param url       地址
     * @param imageView 控件
     */
    public static void loadRoundImage200(final Context context, String url, final ImageView imageView) {
        Glide.with(context).load(url).asBitmap().
                placeholder(R.drawable.mooc).error(R.drawable.mooc).
                centerCrop().override(200, 200).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param url       地址
     * @param imageView 控件
     */
    public static void loadRoundImage5(final Context context, String url, final ImageView imageView) {
        Glide.with(context).load(url).asBitmap().
                placeholder(R.drawable.mooc).error(R.drawable.mooc).
                centerCrop().override(200, 200).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(false);
                circularBitmapDrawable.setCornerRadius(5);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param url       地址
     * @param imageView 控件
     */
    public static void loadRoundImage3(final Context context, String url, final ImageView imageView) {
        Glide.with(context).load(url).asBitmap().
                placeholder(R.drawable.mooc).error(R.drawable.mooc).
                centerCrop().override(200, 200).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(false);
                circularBitmapDrawable.setCornerRadius(3);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param imageFile 图片文件
     * @param imageView 控件
     */
    public static void loadRoundImage(final Context context, String imageFile, final ImageView imageView, int defaultImage) {
        File file = new File(imageFile);
        if (file.exists()) {
            Glide.with(context).load(imageFile).asBitmap().
                    placeholder(defaultImage).error(defaultImage).
                    centerCrop().override(200, 200).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param imageFile 图片文件
     * @param imageView 控件
     */
    public static void loadRoundImage(final Context context, String imageFile, final ImageView imageView) {
        File file = new File(imageFile);
        if (file.exists()) {
            Glide.with(context).load(imageFile).asBitmap().
                    placeholder(R.drawable.mooc).error(R.drawable.mooc).
                    centerCrop().override(200, 200).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }

    /**
     * 加载圆形图片
     *
     * @param context   上下文
     * @param imageFile 图片文件
     * @param imageView 控件
     */
    public static void loadRoundImage(final Context context, int imageFile, final ImageView imageView) {
        Glide.with(context).load(imageFile).asBitmap().
                placeholder(R.drawable.dafault_round).error(R.drawable.dafault_round).
                centerCrop().override(200, 200).animate(R.anim.anim_image_load).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(false);
                circularBitmapDrawable.setCornerRadius(12);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
