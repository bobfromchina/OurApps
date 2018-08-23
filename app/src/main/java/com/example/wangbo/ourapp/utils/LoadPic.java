package com.example.wangbo.ourapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSONObject;
import com.example.wangbo.ourapp.OurApplication;
import com.example.wangbo.ourapp.manager.UserManager;
import com.jackmar.jframelibray.base.ALog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by wangbo on 2018/8/23.
 * <p>
 * 图片上传测试类
 */

public class LoadPic {


    private static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    private OurApplication mContext;

    private static final String UPLOAD_FILE_KE = "files";

    public static final String TAG = "LoadPic";

    private static OkHttpClient mOkHttpClient;

    /**
     * 初始化 okHttp
     */
    protected static OkHttpClient initOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(Config.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Config.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Config.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS).build();
    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) mOkHttpClient = initOkHttpClient();
        return mOkHttpClient;
    }

    /**
     * 上传图片
     *
     * @param files 图片地址
     * @param token token
     */
    public static String upLoadPic(final List<String> files, final String token) {


        final String path = "未知错误";

        if (files.isEmpty()) {
            return "请上传图片";
        }


        if (token == null || token.equals("")) {
            return "token不能为空";
        }

        threadPool.execute(new Runnable() {

            @Override
            public void run() {
                String[] bufArray = new String[files.size()];
                bufArray = files.toArray(bufArray);
                Response response = new Response();
                response.parameters = bufArray;
                Call call = null;
                try {
                    //构建表单数据
                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                    bodyBuilder.addFormDataPart("token", token);

                    for (int i = 0; i < files.size(); i++) {
                        String fPath = files.get(i);
                        if (ImageDownloader.Scheme.FILE.belongsTo(fPath))
                            fPath = ImageDownloader.Scheme.FILE.crop(fPath);
                        File f = new File(fPath);
                        if (!f.exists()) {
                            throw new IllegalStateException("指定的文件必须存在" + f.getAbsolutePath());
                        }

                        //  将图片设置分辨率
                        Bitmap bitmap = scale(f, 1000, 1000);
                        File tmpFile = getTmpFile();
                        FileOutputStream outputStream = new FileOutputStream(tmpFile);

                        //  对图片进行压缩
                        bitmap.compress(Bitmap.CompressFormat.JPEG, (int) (0.9f * 100), outputStream);
                        StreamUtils.close(outputStream);
                        bodyBuilder.addFormDataPart(UPLOAD_FILE_KE, f.getAbsolutePath(),
                                RequestBody.create(MediaType.parse("image/jpeg"), tmpFile));
                    }

                    // TODO: 2018/8/23 重新设置的一个请求类
                    //构建请求体
                    Request request = new Request.Builder().url(OurApplication.hostUrl + "/app/controller/uploadImage")
                            .post(bodyBuilder.build()).addHeader("connection", "close").build();
                    OkHttpClient okHttpClient = getOkHttpClient();

                    try {
                        call = okHttpClient.newCall(request);
                        okhttp3.Response res = call.execute();
                        String str = res.body().string();
                        ArrayList<String> array = new ArrayList<>();

                        JSONObject jsonObject = JSONObject.parseObject(str);

                        ALog.i(TAG, "图片请求打印------------->" + String.valueOf(jsonObject));
//                        response.errorCode = jo.getInt(CODE_KEY);
//                        response.isSuccessful = response.errorCode == mBaseCodeTable.getSuccessfulCode();
                        if (response.isSuccessful) {
                            response.obj = array;
                            // response.obj = jo.getJSONObject(DATA_KEY).createObject(HeaderBean.class);
                        }
                        if (Config.DEBUG) {
                            ALog.e(TAG, "response : " + str);
                        }
                    } catch (IOException e) {
                        if (Config.DEBUG) {
                            ALog.e("uploadMultiBytes", e);
                        }
                        response.isSuccessful = false;
                        response.errorCode = -1;
                    }
                } catch (Exception e) {
                    if (Config.DEBUG) {
                        ALog.e(TAG, e);
                    }
                    response.isSuccessful = false;
//                    if (e instanceof ConnectException) {
//                        response.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
//                        response.errorMsg = mBaseCodeTable.getCodeDescription(response.errorCode);
//                    } else {
//                        if (String.valueOf(e.getMessage()).contains("Canceled")) {
//                            response.errorCode = mBaseCodeTable.getCanceledExceptionCode();
//                        } else {
//                            response.errorCode = mBaseCodeTable.getUnknownErrorCode();
//                        }
//                    }
                }
//                finally {
//                    removeCall(call, what);
//                    if (mHandler != null) {
//                        mHandler.obtainMessage(what, response).sendToTarget();
//                    }
//                }
            }
        });

        return path;
    }

    /**
     * 缩放图片
     *
     * @param file         需要缩放的我文件
     * @param targetWidth  目标的宽度
     * @param targetHeight 目标高度
     * @return 解析的图片对象
     */
    private static Bitmap scale(File file, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = computeSampleSize(options, -1, targetWidth * targetHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
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

    /**
     * 获取临时文件
     *
     * @return 临时文件对象
     */
    public static File getTmpFile() throws IOException {
        return File.createTempFile("tmp", ".jpg");
    }
}
