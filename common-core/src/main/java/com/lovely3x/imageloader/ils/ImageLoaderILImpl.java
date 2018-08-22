package com.lovely3x.imageloader.ils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

//import com.ant.liao.GifAction;
//import com.ant.liao.GifDecoder;
import com.lovely3x.imageloader.IL;
import com.lovely3x.imageloader.ImageDisplayOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ViewAware;

import java.io.IOException;
import java.io.InputStream;

/**
 * Universal ImageLoader
 * Created by lovely3x on 16-5-25.
 */
public class ImageLoaderILImpl implements IL {

    @Override
    public void display(View view, String url) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, new ULViewAware(view));
    }

    @Override
    public void display(ImageView iv, String url) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, iv);
    }

    @Override
    public void display(View view, String url, ImageDisplayOptions options) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, new ULViewAware(view), buildULImageOptions(options));
    }

    @Override
    public void display(ImageView iv, String url, ImageDisplayOptions options) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(url, iv, buildULImageOptions(options));
    }

    @Override
    public void init(Context context) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
    }

   /* @Override
    public void displayGif(ImageView iv, String url) {
        ImageLoader.getInstance().displayImage(url, iv, new DisplayImageOptions.Builder().displayer(new BitmapDisplayer() {
            @Override
            public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                imageAware.setImageBitmap(bitmap);
            }
        }).decoder(new ImageDecoder() {
            final Object lock = new Object();

            @Override
            public Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException {
                final InputStream is = imageDecodingInfo.getDownloader().getStream(imageDecodingInfo.getImageUri(), imageDecodingInfo.getExtraForDownloader());

                GifDecoder decoder = new GifDecoder(new GifAction() {
                    @Override
                    public void parseReturn(int iResult) {
                        switch (iResult) {
                            case RETURN_FIRST://第一帖被解析成功
                            {
                                synchronized (lock) {
                                    lock.notifyAll();
                                }
                            }
                            break;
                            case RETURN_ERROR: {//解码错误
                                synchronized (lock) {
                                    lock.notifyAll();
                                }
                            }
                            break;
                        }
                    }

                    @Override
                    public void loopEnd() {
                    }
                });

                decoder.setGifImage(is);
                decoder.start();

                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Bitmap bm = decoder.getFrameImage();
                decoder.destroy();
                return bm;
            }
        }).build());
    }*/

    /**
     * 构建一个图片加载器选项
     */
    private DisplayImageOptions buildULImageOptions(ImageDisplayOptions options) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(options.cacheInMemory())
                .cacheOnDisk(options.cacheInDisk())
                .showImageOnLoading(options.loadingImgResource())
                .showImageForEmptyUri(options.loadFailImgResource())
                .showImageOnFail(options.loadFailImgResource()).build();
    }

    private static class ULViewAware extends ViewAware {

        public ULViewAware(View view) {
            super(view);
        }

        public ULViewAware(View view, boolean checkActualViewSize) {
            super(view, checkActualViewSize);
        }

        @Override
        protected void setImageDrawableInto(Drawable drawable, View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }

        @Override
        protected void setImageBitmapInto(Bitmap bitmap, View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(new BitmapDrawable(bitmap));
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        }
    }
}
