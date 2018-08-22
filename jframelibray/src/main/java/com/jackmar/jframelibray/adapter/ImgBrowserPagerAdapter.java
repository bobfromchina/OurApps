package com.jackmar.jframelibray.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.jackmar.jframelibray.R;
import com.jackmar.jframelibray.bean.Img;
import com.jackmar.jframelibray.imageUtils.ImageSource;
import com.jackmar.jframelibray.imageUtils.SubsamplingScaleImageView;
import com.lzy.imagepicker.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览器的适配器
 * Created by lovely3x on 15-9-22.
 */
public class ImgBrowserPagerAdapter extends PagerAdapter {

    /**
     * 图片资源类型 bitmap
     */
    public static final int IMG_SOURCE_TYPE_BITMAP = 0x1;

    /**
     * 图片资源类型 网络地址
     */
    public static final int IMG_SOURCE_TYPE_URL = 0x2;
    /**
     * 图片资源类型 文件地址
     */
    public static final int IMG_SOURCE_TYPE_FILE = 0x3;

    /**
     * 图片资源类型 assert文件资源
     */
    public static final int IMG_SOURCE_TYPE_ASSERT = 0x4;

    private final Context mContext;
    private final ArrayList<Img> images;

    private OnItemClickedListener mOnItemClickedListener;

    public ImgBrowserPagerAdapter(Context context, List<? extends Object> imgs,
                                  int contentType, boolean hasAnim,
                                  Bitmap loading,
                                  Bitmap loadFailure) {
        images = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            Object obj = imgs.get(i);
            if (obj instanceof String || obj instanceof Integer) {
                images.add(new Img(contentType, String.valueOf(obj), null, -1, -1, loadFailure, loading));
            } else if (obj instanceof Bitmap) {
                images.add(new Img(contentType, null, (Bitmap) imgs.get(i), -1, -1, loadFailure, loading));
            } else if (obj instanceof Img) {
                images.add((Img) obj);
            } else {
                throw new RuntimeException("不支持的对象：" + obj);
            }
        }
        this.mContext = context;
    }


    public ImgBrowserPagerAdapter(Context mContext, ArrayList<Img> images) {
        this.images = images;
        this.mContext = mContext;
    }

    public ImgBrowserPagerAdapter(Context context, List<? extends Object> imgs,
                                  int contentType, boolean hasAnim,
                                  int loading,
                                  int loadFailure) {
        images = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            Object obj = imgs.get(i);
            if (obj instanceof String || obj instanceof Integer) {
                images.add(new Img(contentType, String.valueOf(obj), null, loadFailure, loading, null, null));
            } else if (obj instanceof Bitmap) {
                images.add(new Img(contentType, null, (Bitmap) imgs.get(i), loadFailure, loading, null, null));
            } else if (obj instanceof Img) {
                images.add((Img) obj);
            } else {
                throw new RuntimeException("不支持的对象：" + obj);
            }
        }
        this.mContext = context;
    }

    public ArrayList<Img> getImages() {
        return images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Img img = images.get(position);

        SubsamplingScaleImageView ssiv = new SubsamplingScaleImageView(mContext);
        //ssiv.setBackgroundColor(Color.RED);
        ssiv.setSoundEffectsEnabled(false);
        ssiv.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(ssiv, lp);

        switch (img.getImgType()) {
            case IMG_SOURCE_TYPE_ASSERT: {
                ssiv.recycle();
                ImageSource preview = null;
                if (img.getImgBitmap() != null) {
                    preview = ImageSource.bitmap(img.getLoadingImgBitmap());
                } else {
                    if (img.getLoadingImgRes() > 0) {
                        preview = ImageSource.resource(img.getLoadingImgRes());
                    }
                }
                ssiv.setImage(ImageSource.asset(img.getImg()), preview);
            }
            break;
            case IMG_SOURCE_TYPE_URL:
                ssiv.recycle();
                if (img.getLoadingImgBitmap() != null && !img.getLoadingImgBitmap().isRecycled()) {
                    ssiv.setImage(ImageSource.bitmap(img.getFailureImgBitmap()));
                } else if (img.getLoadingImgRes() > 0) {
                    ssiv.setImage(ImageSource.resource(img.getLoadingImgRes()));
                }

               /* ImageLoader.getInstance().displayImage(img.getImg(), new ViewAware(ssiv) {
                    @Override
                    protected void setImageDrawableInto(Drawable drawable, View view) {

                    }

                    @Override
                    protected void setImageBitmapInto(Bitmap bitmap, View view) {
                        if (view != null && bitmap != null && !bitmap.isRecycled()) {
                            ((SubsamplingScaleImageView) view).recycle();
                            ((SubsamplingScaleImageView) view).setImage(ImageSource.bitmap(bitmap));
                        }
                    }
                }, getOptions(img), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, final View view, FailReason failReason) {
                        if (view != null && imageUri != null && imageUri.equals(img.getImg())) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    ((SubsamplingScaleImageView) view).recycle();
                                    if (img.getFailureImgBitmap() != null) {
                                        ((SubsamplingScaleImageView) view).setImage(ImageSource.bitmap(img.getFailureImgBitmap()));
                                    } else if (img.getFailureImgRes() > 0) {
                                        ((SubsamplingScaleImageView) view).setImage(ImageSource.resource(img.getFailureImgRes()));
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, final View view, final Bitmap loadedImage) {
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, final View view) {
                        if (view != null && imageUri != null && imageUri.equals(img.getImg())) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (img.getFailureImgBitmap() != null && !img.getFailureImgBitmap().isRecycled()) {
                                        ((SubsamplingScaleImageView) view).setImage(ImageSource.bitmap(img.getFailureImgBitmap()));
                                    } else if (img.getFailureImgRes() > 0) {
                                        ((SubsamplingScaleImageView) view).setImage(ImageSource.resource(img.getFailureImgRes()));
                                    }
                                }
                            });
                        }
                    }
                });*/
                break;
            case IMG_SOURCE_TYPE_FILE: {
                ssiv.recycle();
                ImageSource preview = null;
                if (img.getImgBitmap() != null) {
                    preview = ImageSource.bitmap(img.getLoadingImgBitmap());
                } else {
                    if (img.getLoadingImgRes() > 0) {
                        preview = ImageSource.resource(img.getLoadingImgRes());
                    }
                }
                ssiv.setImage(ImageSource.uri(img.getImg()).tiling(true));
            }
            break;
            case IMG_SOURCE_TYPE_BITMAP:
                ssiv.recycle();
                ssiv.setImage(ImageSource.bitmap(img.getImgBitmap()));
                break;
        }
        ssiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickedListener != null) mOnItemClickedListener.onItemClicked(position);
            }
        });
        return ssiv;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        SubsamplingScaleImageView ssiv = (SubsamplingScaleImageView) object;
        container.removeView(ssiv);
    }

//    /**
//     * 获取图片加载显示器选项
//     *
//     * @param img 需要获取显示选项的的对象
//     * @return 图片加载显示器选项
//     */
//    private DisplayImageOptions getOptions(Img img) {
//        if (img.getLoadingImgBitmap() != null && img.getFailureImgBitmap() != null) {
//            return new DisplayImageOptions.Builder()
//                    .cacheInMemory(true).cacheOnDisk(true)
//                    .showImageOnLoading(new BitmapDrawable(mContext.getResources(), img.getLoadingImgBitmap()))
//                    .showImageOnFail(new BitmapDrawable(mContext.getResources(), img.getFailureImgBitmap()))
//                    .showImageForEmptyUri(new BitmapDrawable(mContext.getResources(), img.getFailureImgBitmap()))
//                    .build();
//        } else if (img.getFailureImgRes() > 0 && img.getLoadingImgRes() > 0) {
//            return new DisplayImageOptions.Builder()
//                    .cacheInMemory(true).cacheOnDisk(true)
//                    .showImageOnLoading(img.getLoadingImgRes())
//                    .showImageForEmptyUri(img.getFailureImgRes())
//                    .showImageOnFail(img.getFailureImgRes())
//                    .build();
//        }
//        return new DisplayImageOptions.Builder()
//                .showImageOnFail(R.drawable.icon_loading_failure)
//                .showImageForEmptyUri(R.drawable.icon_loading_failure)
//                .showImageOnLoading(R.drawable.icon_loading)
//                .cacheInMemory(true).cacheOnDisk(true).build();
//    }


    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setOnItemClicked(OnItemClickedListener listener) {
        this.mOnItemClickedListener = listener;
    }

    public interface OnItemClickedListener {

        void onItemClicked(int position);
    }

}
