package com.lovely3x.common.utils.blur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.lovely3x.common.BuildConfig;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.BitmapUtils;
import com.lovely3x.common.utils.CommonUtils;
import com.lovely3x.common.utils.Md5Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Blur utils
 * Created by lovely3x on 17/2/6.
 */
public class Blur {

    private static final java.lang.String TAG = "Blur";

    private final static ExecutorService mThreadPoolExecutor = Executors.newFixedThreadPool(3);
    private final static ExecutorService mCacheExecutor = Executors.newSingleThreadExecutor();

    public static final int DEFAULT_SCALE_DOWN = 16;
    public static final int DEFAULT_RADIUS = 10;
    private static final int FADE_IN_MS = 200;
    public static final String CACHE_KEY_SUFFIX = "cache_key_";

    private int mResource;
    private Bitmap mBitmap;

    private boolean mSync = false;
    private boolean mFadeIn = false;
    private boolean recycleSrcBitmap = true;

    private float mScaleUp;
    private float mScaleDown;

    private int mRadius = DEFAULT_RADIUS;

    private boolean mAvoidOOM = true;

    private static DiskLruCache mDiskLruCache;
    private final static LruCache<String, Bitmap> mMemoryCache = new LruCache<>(10);

    private String mIdentifier;

    private boolean mSkipCache;
    private int mFadeInDurations = FADE_IN_MS;

    private Blur(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    private Blur(int resource) {
        this.mResource = resource;
    }

    public static Blur from(Bitmap bitmap) {
        return new Blur(bitmap);
    }

    public static Blur from(int resource) {
        return new Blur(resource);
    }

    public Blur sync() {
        mSync = true;
        return this;
    }

    public Blur aSync() {
        mSync = false;
        return this;
    }

    public Blur fadeIn(boolean fadeIn) {
        mFadeIn = fadeIn;
        return this;
    }

    public Blur duration(int duration) {
        this.mFadeInDurations = duration;
        return this;
    }

    public Blur scaleUp(float scale) {
        this.mScaleUp = scale;
        return this;
    }

    public Blur scaleDown(float scaleDown) {
        this.mScaleDown = scaleDown;
        return this;
    }

    public Blur radius(int radius) {
        this.mRadius = radius;
        return this;
    }

    public Blur recycleSrcBitmap(boolean recycleSrcBitmap) {
        this.recycleSrcBitmap = recycleSrcBitmap;
        return this;
    }

    public Blur avoidOOM() {
        this.mAvoidOOM = true;
        return this;
    }

    public Blur skipCache(boolean skipCache) {
        this.mSkipCache = skipCache;
        return this;
    }

    public Future<?> load(@NonNull final Context context, final CallBack callBack) {
        final Bitmap cachedBm = findCache(context);

        final Runnable task = new Runnable() {
            @Override
            public void run() {

                final long startTime = SystemClock.elapsedRealtime();

                Bitmap dst = null;

                try {
                    Bitmap src = null;
                    if (cachedBm == null) {
                        src = getBitmap();
                        if (mScaleUp != mScaleDown) {
                            if (mScaleUp > mScaleDown) {
                                float diff = mScaleUp - mScaleDown;
                                dst = Bitmap.createScaledBitmap(src,
                                        (int) (src.getWidth() * diff),
                                        (int) (src.getHeight() * diff), false);
                            } else {
                                float diff = mScaleDown - mScaleUp;
                                dst = Bitmap.createScaledBitmap(src,
                                        (int) (src.getWidth() / diff),
                                        (int) (src.getHeight() / diff), false);
                            }
                        } else {
                            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                            int width = metrics.widthPixels;
                            int height = metrics.heightPixels;

                            BitmapFactory.Options ops = new BitmapFactory.Options();
                            ops.outHeight = height;
                            ops.outWidth = width;

                            int scale = BitmapUtils.calculateInSampleSize(ops, width, height);
                            dst = Bitmap.createScaledBitmap(src, src.getWidth() * scale, src.getHeight() * scale, false);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && CommonUtils.supportArm()) {
                            dst = RenderScriptBlur.blurBitmap(context, dst, mRadius);
                        } else {
                            dst = JavaStackBlur.blur(dst, mRadius)/*FastBlur.doBlur(dst, mRadius, true)*/;
                        }
                        mayCacheOnWorkerThread(context, dst);
                    } else {
                        dst = cachedBm;
                    }
                    callBack.result(new BlurResult(BlurResult.CODE_SUCCESS, src, dst, cachedBm != null, (SystemClock.elapsedRealtime() - startTime)));
                    if (recycleSrcBitmap && src != null) src.recycle();
                } catch (Throwable e) {
                    callBack.result(new BlurResult(BlurResult.CODE_FAIL, null, dst));
                }
            }

            private Bitmap getBitmap() {
                if (mBitmap != null) return mBitmap;
                Resources resources = context.getResources();

                if (mAvoidOOM) {
                    DisplayMetrics display = resources.getDisplayMetrics();

                    final int width = display.widthPixels >> 1;
                    final int height = display.heightPixels >> 1;

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(resources, mResource, options);

                    if (options.outHeight > height || options.outWidth > width) {
                        return BitmapUtils.decodeSampledBitmapFromResource(resources, mResource, width, height);
                    } else {
                        return BitmapFactory.decodeResource(resources, mResource, options);
                    }

                } else {
                    return BitmapFactory.decodeResource(resources, mResource);
                }
            }
        };

        if (mSync || cachedBm != null) {
            task.run();
        } else {
            return mThreadPoolExecutor.submit(task);
        }

        return null;
    }

    @Nullable
    private Bitmap findCache(@NonNull Context context) {
        Bitmap cachedBm = null;
        try {
            cachedBm = null;
            String identifier = getIdentifier();
            if (identifier != null && (cachedBm = mMemoryCache.get(identifier)) == null) {
                createDiskLurCacheIfNeed(context);
                if (mDiskLruCache != null) {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(identifier);
                    if (snapshot != null) {
                        InputStream inputStream = snapshot.getInputStream(0);
                        if (inputStream != null) {
                            Bitmap bm = BitmapFactory.decodeStream(inputStream);
                            if (bm != null) {
                                cachedBm = bm;
                                mMemoryCache.put(identifier, cachedBm);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            ALog.e(TAG, e);
        }
        return cachedBm;
    }

    public Future<?> into(@NonNull final View imageView) {
        return load(imageView.getContext(), new CallBack() {
            @Override
            public void onResult(BlurResult blurResult) {
                ALog.i(TAG, "Consuming time (ms) -> " + " isSuccess " + blurResult.isSuccess() + "  " + blurResult.mTimeConsuming);
                if (blurResult.isSuccess() && blurResult.getBitmap() != null && !blurResult.getBitmap().isRecycled()) {
                    if (mFadeIn || !blurResult.isFromCache()) {
                        Drawable placeholder = getViewOriginalDrawable(imageView);

                        final TransitionDrawable transition = new TransitionDrawable(new Drawable[]{
                                placeholder, new BitmapDrawable(imageView.getResources(), blurResult.getBitmap())
                        });

                        setDrawableToView(imageView, transition);
                        transition.startTransition(mFadeInDurations);
                    } else {
                        setDrawableToView(imageView, new BitmapDrawable(blurResult.getBitmap()));
                    }
                } else {
                    if (blurResult.decodedSrcBitmap != null && !blurResult.decodedSrcBitmap.isRecycled()) {
                        setDrawableToView(imageView, new BitmapDrawable(blurResult.decodedSrcBitmap));
                    }
                }
            }
        });
    }

    public static void cleanupMemoryCache() {
        mMemoryCache.trimToSize(0);
    }

    public static void cleanupDiskCache(Context context) {
        createDiskLurCacheIfNeed(context);
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.setMaxSize(0);
                mDiskLruCache = null;
                createDiskLurCacheIfNeed(context);
            } catch (Exception e) {
                ALog.e(TAG, e);
            }
        }
    }

    public static void cleanupCache(Context context) {
        cleanupMemoryCache();
        cleanupDiskCache(context);
    }


    private void mayCacheOnWorkerThread(final Context context, final Bitmap dst) {
        if (!mSkipCache && dst != null && !dst.isRecycled()) {
            mCacheExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    if (!dst.isRecycled()) {
                        cacheBlurBitmap(context, getIdentifier(), dst);
                    }
                }
            });
        }
    }

    private String getIdentifier() {
        if (TextUtils.isEmpty(mIdentifier)) {
            if (mResource > 0) {
                return CACHE_KEY_SUFFIX + mResource + mRadius + mScaleUp + mScaleDown;
            } else {
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(mBitmap.getRowBytes());
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    try {
                        return Md5Utils.getMD5Str(Md5Utils.getMD5Str(baos.toByteArray()) + (mRadius + mScaleUp + mScaleDown));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return mIdentifier;
        }
        return null;
    }

    private Drawable getViewOriginalDrawable(View view) {
        Drawable placeholder;
        if (view instanceof ImageView) {
            if (((ImageView) view).getDrawable() != null) {
                placeholder = ((ImageView) view).getDrawable();
            } else {
                placeholder = new ColorDrawable(Color.parseColor("#00FFFFFF"));
            }
        } else {
            if (view.getBackground() != null) {
                placeholder = view.getBackground();
            } else {
                placeholder = new ColorDrawable(Color.parseColor("#00FFFFFF"));
            }
        }
        return placeholder;
    }

    private void setDrawableToView(View view, Drawable drawable) {
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
            }
        }
    }

    /**
     * 这个资源的标识
     * 主要用于缓存，可提高速度
     * 不设置则会使用Bitmap的MD5或者ResourceId
     *
     * @param identifier 标识
     */
    public Blur identifier(String identifier) {
        mIdentifier = identifier;
        return this;
    }

    private synchronized void cacheBlurBitmap(
            Context context,
            String key, Bitmap bitmap) {

        if (TextUtils.isEmpty(key) || bitmap == null || bitmap.isRecycled()) return;

        mMemoryCache.put(key, bitmap);

//        createDiskLurCacheIfNeed(context);
//
//        if (mDiskLruCache != null) {
//            OutputStream out = null;
//            try {
//                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
//                out = editor.newOutputStream(0);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                editor.commit();
//            } catch (Throwable e) {
//                ALog.e(TAG, e);
//            }
//        }
    }

    private static void createDiskLurCacheIfNeed(Context context) {
        try {
            if (mDiskLruCache == null) {
                mDiskLruCache = DiskLruCache.open(context.getCacheDir(), BuildConfig.VERSION_CODE, 1, 1024 * 1024 * 50);
            }
        } catch (Exception e) {
            ALog.e(TAG, e);
        }
    }

    public static abstract class CallBack {
        final static Handler mainHandler = new Handler(Looper.getMainLooper());

        final void result(final BlurResult blurResult) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                onResult(blurResult);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResult(blurResult);
                    }
                });
            }
        }

        public abstract void onResult(BlurResult blurResult);


    }

    public class BlurResult {
        public static final int CODE_SUCCESS = 1;
        public static final int CODE_FAIL = -1;
        public static final int CODE_DECODE_BITMAP_FAIL = -2;

        private Bitmap decodedSrcBitmap;
        private int errCode;
        private Bitmap mBitmap;
        private boolean mFromCache;
        private long mTimeConsuming;

        public BlurResult(int errCode, Bitmap decodedSrcBitmap, Bitmap bitmap) {
            this.decodedSrcBitmap = decodedSrcBitmap;
            this.errCode = errCode;
            mBitmap = bitmap;
        }

        public BlurResult(int errCode, Bitmap decodedSrcBitmap, Bitmap bitmap, boolean fromCache, long timeConsuming) {
            this.decodedSrcBitmap = decodedSrcBitmap;
            this.errCode = errCode;
            mBitmap = bitmap;
            this.mFromCache = fromCache;
            this.mTimeConsuming = timeConsuming;
        }

        public boolean isFromCache() {
            return mFromCache;
        }

        public boolean isSuccess() {
            return errCode == CODE_SUCCESS;
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }
    }

}
