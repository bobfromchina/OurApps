package com.lovely3x.common.requests.impl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lovely3x.common.beans.HeaderBean;
import com.lovely3x.common.cacher.Cache;
import com.lovely3x.common.cacher.CacheManager;
import com.lovely3x.common.cacher.ICacheMonitor;
import com.lovely3x.common.cacher.MD5FlagRecordGenerator;
import com.lovely3x.common.cacher.container.ICacheable;
import com.lovely3x.common.consts.Const;
import com.lovely3x.common.managements.user.TokenManager;
import com.lovely3x.common.requests.CommonRequests;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.requests.HandlerRequest;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.ImageCompresser;
import com.lovely3x.common.utils.NetUtils;
import com.lovely3x.common.utils.Response;
import com.lovely3x.common.utils.fileutils.StreamUtils;
import com.lovely3x.jsonparser.model.JSONArray;
import com.lovely3x.jsonparser.model.JSONObject;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * handler 请求器
 * Created by lovely3x on 15-11-18.
 */
public class HandlerRequestImpl extends HandlerRequest {


    /**
     * 当无法获取到token时使用此token代替
     */
    public static final String INVALID_TOKEN = "65244253378998545531726810823479";
    /**
     * 获取数据组的key
     */
    public static final String DATA_KEY = "data";
    /**
     * 获取服务器返回码的值
     */
    public static final String CODE_KEY = "status";

    /**
     * 打印类
     */
    public static final String TAG = "BaseRequest";
    /**
     * 传递token的key
     */
    public static final String TOKEN_KEY = "tokenId";
    /**
     * 传递时间戳的key
     */
    public static final String TIMESTAMP_KEY = "timestamp";
    /**
     * 获取消息（描述信息）的key
     */
    public static final String MESSAGE_KEY = "msg";
    /**
     * 线程池
     */
    protected static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Const.REQUESTER_MAX_CONCURRENT_THREAD);
    private static final String UPLOAD_FILE_KE = "imageName";
    private static final String UPLOAD_VOICE_KE = "fileupload";

    /**
     * 当前的网络请求调用任务
     */
    private final HashMap<Integer, List<Call>> TASKS = new HashMap<>();

    public HandlerRequestImpl() {
    }

    public HandlerRequestImpl(Handler handler) {
        super(handler);
    }

    /**
     * 添加token对
     *
     * @param formBuilder 表单构建器
     */
    public static void addTokenPair(FormBody.Builder formBuilder) {
        formBuilder.add(TOKEN_KEY, getToken());
    }

    /**
     * 获取token，这个token 可能无效也可能有效
     *
     * @return token
     */
    public static String getToken() {
        String token = TokenManager.getInstance().getTokeValue();
        return token == null ? INVALID_TOKEN : token;
    }

    /**
     * 添加时间戳
     *
     * @param formBuilder 表单构建器
     */
    public static void addTimestamp(FormBody.Builder formBuilder) {
        formBuilder.add(TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
    }


    /**
     * 检查当前的网络状态
     * 当没有网络时,会给handler发送消息
     *
     * @param res  当没有网络时需要发送给handler的响应
     * @param what handler消息标识值
     * @return 是否有网络
     */

    private boolean networkChecker(Response res, int what, Object[] parameters) {
        if (!NetUtils.hasNetWork()) {
            if (mHandler != null) {
                if (res == null) {
                    res = new Response();
                }
                res.parameters = parameters;
                res.errorCode = mBaseCodeTable.getNoNetWorkErrorCode();
                mHandler.obtainMessage(what, res).sendToTarget();
            }
            return false;
        }
        return true;
    }


    /**
     * 增加call
     *
     * @param call 需要增加的call
     * @param what what值
     */
    protected void addCall(Call call, int what) {
        synchronized (CommonRequests.class) {
            List<Call> calls = TASKS.get(what);
            if (calls == null) {
                calls = new ArrayList<>();
                TASKS.put(what, calls);
            }
            calls.add(call);
        }
    }

    /**
     * 移除call
     *
     * @param call 需要移除的call
     * @param what 需要移除的call的what值
     */
    protected void removeCall(Call call, int what) {
        synchronized (CommonRequests.class) {
            List<Call> calls = TASKS.get(what);
            if (calls != null && call != null) {
                calls.remove(call);
            }
        }
    }


    /**
     * 请求处理
     *
     * @param pr     请求回调接口
     * @param action 请求的地址
     * @param what   请求的 what值 如果handler 不为null则使用handler发送消息
     * @param params 请求的参数
     */
    public void process(final ProcessResult pr, String action, final int what, final Object... params) {
        action = mURLConst.concatAction(action);
        if (Config.DEBUG) {
            ALog.e(TAG, "params -> " + action + " -> " + Arrays.toString(params));
        }
        if (networkChecker(null, what, params)) {

            FormBody.Builder formEncodingBuilder = new FormBody.Builder();
            addTimestamp(formEncodingBuilder);
            addTokenPair(formEncodingBuilder);
// TODO: 2017/11/29
            //如果存在参数,并且参数是合法的
            if (params != null && params.length > 0 && params.length % 2 == 0) {
                final int paramsCount = params.length / 2;
                for (int i = 0; i < paramsCount; i++) {
                    String key = params[i * 2].toString();
                    String value = String.valueOf(params[i * 2 + 1]);
                    if (key != null && value != null) {
                        formEncodingBuilder.add(key, value);
                    }
                }
            }

            //获取okHttpClient,用于网络请求
            OkHttpClient client = mContext.getOkHttpClient();
            //构建请求体
            Request requestBody = new Request.Builder().post(formEncodingBuilder.build())
                    .addHeader("connection", "close").url(action).build();

            //添加一个新的call到队列请求队列中
            Call call = client.newCall(requestBody);
            addCall(call, what);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ALog.e(TAG, e);
                    removeCall(call, what);
                    if (mHandler != null) {
                        Response res = new Response();
                        res.parameters = params;
                        res.isSuccessful = false;
                        if (String.valueOf(e.getMessage()).contains("Canceled")) {
                            res.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                        } else {
                            res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                        }
                        mHandler.obtainMessage(what, res).sendToTarget();
                    }
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    removeCall(call, what);
                    Response res = new Response();
                    res.parameters = params;
                    try {
                        if (response.isSuccessful()) {
                            TokenManager.getInstance().updateTokeLastTime();
                            String result = response.body().string();
                            if (Config.DEBUG) {
                                ALog.d(TAG, result);
                            }
                            // TODO: 2017/11/29
                            JSONObject jo = new JSONObject(result);
                            res.errorCode = jo.getInt(CODE_KEY);
                            res.errorMsg = jo.getString(MESSAGE_KEY);
                            res.isSuccessful = (res.errorCode == mBaseCodeTable.getSuccessfulCode());
                            pr.onResult(res, jo);
                        } else {//错误的响应码
                            res.isSuccessful = false;
                            res.errorCode = mBaseCodeTable.getResponseErrorCode(response.code());
                            res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                            ALog.e(TAG, "Http status code : " + response.code() + " ，Msg : " + response.message());
                            ALog.e(TAG, "Error Reason : " + response.body().string());
                        }
                    } catch (Exception e) {
                        ALog.e(TAG, e);
                        res.isSuccessful = false;
                        res.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                        res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                    } finally {
                        if (mHandler != null) {
                            mHandler.obtainMessage(what, res).sendToTarget();
                        }
                    }
                }
            });
        }
    }


    /**
     * 同步执行网络请求
     *
     * @param action 请求的地址
     * @param params 请求的参数
     * @param what   取消时需要用
     * @return 返回的Response对象, 如果{{@link Response#isSuccessful}那么就会携带一个返回的{@link JSONObject}
     */
    public Response processSync(String action, int what, Object... params) {
        action = mURLConst.concatAction(action);
        if (Config.DEBUG) {
            ALog.i(TAG, "params -> " + action + " -> " + Arrays.toString(params));
        }

        Response res = new Response();
        res.parameters = params;
        if (NetUtils.hasNetWork()) {

            FormBody.Builder formEncodingBuilder = new FormBody.Builder();
            addTimestamp(formEncodingBuilder);
            addTokenPair(formEncodingBuilder);

            //如果存在参数,并且参数是合法的
            if (params != null && params.length > 0 && params.length % 2 == 0) {
                final int paramsCount = params.length / 2;
                for (int i = 0; i < paramsCount; i++) {
                    String key = params[i * 2].toString();
                    String value = String.valueOf(params[i * 2 + 1]);
                    if (key != null && value != null) {
                        formEncodingBuilder.add(key, value);
                    }
                }
            }

            //获取okHttpClient,用于网络请求
            OkHttpClient client = mContext.getOkHttpClient();
            //构建请求体
            Request requestBody = new Request.Builder().post(formEncodingBuilder.build()).addHeader("connection", "close").url(action).build();

            Call call = null;
            try {
                call = client.newCall(requestBody);
                addCall(call, what);
                okhttp3.Response response = call.execute();//.enqueue(new Callback() {
                if (response.isSuccessful()) {
                    TokenManager.getInstance().updateTokeLastTime();
                    String result = response.body().string();
                    if (Config.DEBUG) {
                        ALog.d(TAG, result);
                    }
                    JSONObject jo = new JSONObject(result);
                    res.errorCode = jo.getInt(CODE_KEY);
                    res.errorMsg = jo.getString(MESSAGE_KEY);
                    res.isSuccessful = (res.errorCode == mBaseCodeTable.getSuccessfulCode());
                    res.obj = jo;
                } else {//错误的响应码
                    res.isSuccessful = false;
                    res.errorCode = mBaseCodeTable.getResponseErrorCode(response.code());
                    res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                res.isSuccessful = false;
                if (e instanceof ConnectException) {
                    res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                    res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                } else {
                    if (String.valueOf(e.getMessage()).contains("Canceled")) {
                        res.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                    } else {
                        res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                    }
                }
            } finally {
                removeCall(call, what);
            }
        } else {
            res.isSuccessful = false;
            res.errorCode = mBaseCodeTable.getNoNetWorkErrorCode();
        }
        return res;
    }

    /**
     * 请求处理
     *
     * @param pr      请求回调接口
     * @param address 请求的地址
     * @param what    请求的 what值 如果handler 不为null则使用handler发送消息
     * @param params  请求的参数
     */
    public void process(final ProcessResultExtension pr, String address, final int what, Object... params) {
        address = mURLConst.concatAction(address);
        if (Config.DEBUG) {
            ALog.e(TAG, "params -> " + address + " , " + Arrays.toString(params));
        }

        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        addTimestamp(formEncodingBuilder);
        addTokenPair(formEncodingBuilder);

        //如果存在参数,并且参数时合法的
        if (params != null && params.length > 0 && params.length % 2 == 0) {
            final int paramsCount = params.length / 2;
            for (int i = 0; i < paramsCount; i++) {
                String key = params[i * 2].toString();
                Object value = params[i * 2 + 1];
                if (key != null && value != null) {
                    formEncodingBuilder.add(key, String.valueOf(value));
                }
            }
        }

        //获取okHttpClient,用于网络请求
        OkHttpClient client = mContext.getOkHttpClient();
        //构建请求体
        Request requestBody = new Request.Builder().post(formEncodingBuilder.build()).addHeader("connection", "close").url(address).build();

        final Response res = new Response();
        res.parameters = params;

        pr.onRequestBefore(res, client);
        if (networkChecker(res, what, params)) {

            //添加一个新的call到队列请求队列中
            Call call = client.newCall(requestBody);
            addCall(call, what);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (Config.DEBUG) {
                        ALog.e(TAG, e);
                    }
                    removeCall(call, what);
                    if (mHandler != null) {
                        Response res = new Response();
                        res.isSuccessful = false;
                        if (String.valueOf(e.getMessage()).contains("Canceled")) {
                            res.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                        } else {
                            res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                        }
                        mHandler.obtainMessage(what, res).sendToTarget();
                    }
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    removeCall(call, what);
                    try {
                        if (response.isSuccessful()) {
                            //更新token的最后使用时间
                            TokenManager.getInstance().updateTokeLastTime();
                            String result = response.body().string();
                            if (Config.DEBUG) {
                                ALog.d(TAG, result);
                            }
                            JSONObject jo = new JSONObject(result);
                            res.errorCode = jo.getInt(CODE_KEY);
                            res.errorMsg = jo.getString(MESSAGE_KEY);
                            res.isSuccessful = (res.errorCode == mBaseCodeTable.getSuccessfulCode());
                            pr.onResult(res, jo);
                        } else {//错误的响应码
                            if (Config.DEBUG) {
                                ALog.e(TAG, "Http status code : " + response.code() + " ，Msg : " + response.message());
                                ALog.e(TAG, "Error Reason  " + response.body().string());
                            }
                            res.isSuccessful = false;
                            res.errorCode = mBaseCodeTable.getResponseErrorCode(response.code());
                            res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                        }
                    } catch (Exception e) {
                        if (Config.DEBUG) {
                            ALog.e(TAG, e);

                        }
                        res.isSuccessful = false;
                        res.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                        res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                    } finally {
                        if (mHandler != null) {
                            mHandler.obtainMessage(what, res).sendToTarget();
                        }
                    }
                }
            });
        }
    }

    /**
     * 处理可以缓存的的请求
     * 使用这个来进行网络请求可以对指定 数据进行缓存
     *
     * @param processResultExtension 处理结果回调
     * @param action                 地址
     * @param what                   handler what
     * @param initiativeRefresh      是否是用户主动在下拉刷新，如果是用户主动刷新的话就可能会不使用缓存
     * @param params                 参数
     */
    public void processCanCache(final ProcessResultCanCacheExtension processResultExtension,
                                final String action,
                                final int what,
                                final boolean initiativeRefresh,
                                final Object... params) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                Cache cache = processResultExtension.readCache(action, what, initiativeRefresh, params);
                if (cache != null) {
                    if (isDebugMode()) {
                        ALog.d(TAG, "get cache object from caches container " + cache);
                    }
                    MD5FlagRecordGenerator.KeyDesc desc = MD5FlagRecordGenerator.decodeKey(cache.getName());
                    if (isDebugMode()) {
                        ALog.d(TAG, "decode cache message " + desc);
                    }
                    //位置相同才可能从缓存中获取数据
                    if (desc != null && desc.getFlag() == processResultExtension.getFlag()) {
                        //如果是主动刷新，则不管缓存，直接从网络获取,如果没网就用缓存
                        if (initiativeRefresh && NetUtils.hasNetWork()) {
                            //get data from network
                            if (isDebugMode()) {
                                ALog.d(TAG, "user initiative refresh,get data from network.");
                            }
                            if (isDebugMode()) {
                                ALog.d(TAG, "because user initiative refresh,so will clear all caches.");
                            }
                            getDataFromNetWorkAndCacheCache(processResultExtension, action, initiativeRefresh, what, params);
                        } else {
                            // generate object form cache
                            Response res = new Response();
                            res.parameters = params;
                            try {
                                processResultExtension.onProcessCacheBefore(res, null);
                                JSONObject jo = new JSONObject(new String(cache.getData()));
                                res.errorCode = jo.getInt(CODE_KEY);
                                res.errorMsg = jo.getString(MESSAGE_KEY);
                                res.isSuccessful = (res.errorCode == mBaseCodeTable.getSuccessfulCode());
                                processResultExtension.onCacheResult(res, cache, jo);
                            } catch (Exception e) {
                                res.isSuccessful = false;
                                res.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                                res.errorMsg = mBaseCodeTable.getCodeDescription(res);
                            } finally {
                                if (mHandler != null) {
                                    mHandler.obtainMessage(what, res).sendToTarget();
                                }
                            }
                        }
                    } else {
                        if (isDebugMode()) {
                            ALog.d(TAG, "position don't match,get data from network.");
                        }
                        //get data from network
                        getDataFromNetWorkAndCacheCache(processResultExtension, action, initiativeRefresh, what, params);
                    }

                } else {
                    ALog.d(TAG, "can't  from cache container get objects");
                    ALog.d(TAG, "start to get data -> " + action);
                    getDataFromNetWorkAndCacheCache(processResultExtension, action, initiativeRefresh, what, params);
                }
            }
        });
    }

    /**
     * 从网络服务中获取数据并缓存
     *
     * @param processResultExtension
     * @param action
     * @param what
     * @param params
     */
    protected void getDataFromNetWorkAndCacheCache(final ProcessResultCanCacheExtension processResultExtension, final String action, boolean initiativeRefresh, final int what, final Object... params) {
        String address = mURLConst.concatAction(action);
        if (Config.DEBUG) {
            ALog.e(TAG, "params -> " + address + " , " + Arrays.toString(params));
        }
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        addTimestamp(formEncodingBuilder);
        addTokenPair(formEncodingBuilder);

        //如果存在参数,并且参数时合法的
        if (params != null && params.length > 0 && params.length % 2 == 0) {
            final int paramsCount = params.length / 2;
            for (int i = 0; i < paramsCount; i++) {
                if (params[i * 2] == null) {
                    continue;
                }
                String key = params[i * 2].toString();
                Object value = params[i * 2 + 1];
                if (key != null && value != null) {
                    formEncodingBuilder.add(key, String.valueOf(value));
                }
            }
        }

        //获取okHttpClient,用于网络请求
        OkHttpClient client = mContext.getOkHttpClient();
        //构建请求体
        Request requestBody = new Request.Builder().post(formEncodingBuilder.build()).addHeader("connection", "close").addHeader("content-encode", "AES").url(address).build();

        final Response res = new Response();
        res.parameters = params;
        processResultExtension.onRequestBefore(res, client);
        if (networkChecker(res, what, params)) {
            Call call = null;
            try {
                //添加一个新的call到队列请求队列中
                call = client.newCall(requestBody);
                addCall(call, what);
                okhttp3.Response response = call.execute();
                if (response.isSuccessful()) {
                    //更新token的最后使用时间
                    TokenManager.getInstance().updateTokeLastTime();
                    String result = response.body().string();
                    if (Config.DEBUG) {
                        ALog.d(TAG, result);
                    }
                    JSONObject jo = new JSONObject(result);
                    res.errorCode = jo.getInt(CODE_KEY);
                    res.errorMsg = jo.getString(MESSAGE_KEY);
                    res.isSuccessful = (res.errorCode == mBaseCodeTable.getSuccessfulCode());
                    processResultExtension.onResult(res, jo);

                    //////////  Thread.sleep(1000 * 10);
                    if (res.isSuccessful) {
                        if (processResultExtension.needClear(action, what, initiativeRefresh, result, params))
                            processResultExtension.clearCache();
                        //缓存数据到缓存中
                        processResultExtension.saveCache(action, what, initiativeRefresh, result, params);
                    }
                } else {//错误的响应码
                    res.isSuccessful = false;
                    res.errorCode = mBaseCodeTable.getResponseErrorCode(response.code());
                    res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                }
            } catch (Exception e) {
                if (Config.DEBUG) {
                    ALog.e(TAG, e);
                }
                res.isSuccessful = false;
                if (e instanceof ConnectException) {
                    res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                    res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                } else {
                    if (String.valueOf(e.getMessage()).contains("Canceled")) {
                        res.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                    } else {
                        res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                    }

                }
            } finally {
                removeCall(call, what);
                if (mHandler != null) {
                    mHandler.obtainMessage(what, res).sendToTarget();
                }
            }
        }
    }

    /**
     * 打包缓存对象
     *
     * @param name 对象名
     * @param json 缓存的json对象
     * @return Cache对象or Null
     */
    protected Cache packetCacheObject(String name, String json) {
        if (!TextUtils.isEmpty(json)) {
            return new Cache(json.length(), name, System.currentTimeMillis(), json.getBytes());
        }
        return null;
    }

    /**
     * 上传多张图片
     *
     * @param buf  图片组
     * @param type 图片的类型 headimg用户头像   realname 实名真正图片
     * @param what what值
     */
    public void uploadMultiBitmaps(final ArrayList<Bitmap> buf, final String type, final int what) {
        Bitmap[] bufArray = new Bitmap[buf.size()];
        bufArray = buf.toArray(bufArray);
        if (!networkChecker(null, what, bufArray)) return;

        final Bitmap[] finalBufArray = bufArray;
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Response response = new Response();
                response.parameters = finalBufArray;
                Call call = null;
                try {
                    //构建表单数据
                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                    bodyBuilder.addFormDataPart("imgtype", type);
                    String token = TokenManager.getInstance().getTokeValue() == null ? INVALID_TOKEN : TokenManager.getInstance().getTokeValue();
                    bodyBuilder.addFormDataPart(TOKEN_KEY, token);

                    String timestamp = String.valueOf(System.currentTimeMillis());
                    bodyBuilder.addFormDataPart(TIMESTAMP_KEY, timestamp);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    for (int i = 0; i < buf.size(); i++) {
                        baos.reset();
                        buf.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        Bitmap tmpBm = ImageCompresser.scale(bytes, Config.IMAGE_WIDTH, Config.IMAGE_HEIGHT);
                        bytes = null;
                        baos.reset();
                        tmpBm.compress(Bitmap.CompressFormat.JPEG, (int) (Config.scaleFactor * 100), baos);
                        bytes = baos.toByteArray();
                        bodyBuilder.addFormDataPart(UPLOAD_FILE_KE, "upload" + i, RequestBody.create(MediaType.parse("image/jpeg"), bytes));
                    }
                    StreamUtils.close(baos);
                    //构建请求
                    Request request = new Request.Builder().url(mURLConst.concatAction(mURLConst.getImageUploadUrl())).post(bodyBuilder.build()).addHeader("connection", "close").addHeader("content-encode", "AES").build();
                    OkHttpClient okHttpClient = mContext.getOkHttpClient();

                    try {
                        call = okHttpClient.newCall(request);
                        addCall(call, what);
                        okhttp3.Response res = call.execute();
                        String str = res.body().string();
                        ArrayList<String> array = new ArrayList<>();

                        JSONObject jo = new JSONObject(str);
                        response.errorCode = jo.getInt(CODE_KEY);
                        response.isSuccessful = response.errorCode == mBaseCodeTable.getSuccessfulCode();
                        response.obj = array;
                        JSONArray ja = jo.getJSONArray(DATA_KEY);
                        final int count = ja.length();
                        for (int i = 0; i < count; i++) {
                            array.add(ja.getString(i));
                        }
                        if (Config.DEBUG) {
                            ALog.e(TAG, str);
                        }
                    } catch (IOException e) {
                        if (Config.DEBUG) {
                            ALog.e("uploadMultiBytes", e);
                        }
                        response.isSuccessful = false;
                        if (e instanceof ConnectException) {
                            response.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                            response.errorMsg = mBaseCodeTable.getCodeDescription(response.errorCode);
                        } else {
                            if (String.valueOf(e.getMessage()).contains("Canceled")) {
                                response.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                            } else {
                                response.errorCode = mBaseCodeTable.getUnknownErrorCode();
                            }
                        }
                    } finally {
                        removeCall(call, what);
                        try {
                            baos.close();
                        } catch (IOException e) {
                            if (Config.DEBUG) {
                                ALog.w(TAG, e);

                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.isSuccessful = false;
                    response.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                } finally {
                    if (mHandler != null) {
                        mHandler.obtainMessage(what, response).sendToTarget();
                    }
                }
            }
        });
    }

    /**
     * 上传多张图片
     * headimg：头像地址
     * taskimg：任务图片
     * channel：话题图片
     * dynamic：动态图片（包含一次性任务完成）
     *
     * @param files 需要上传的图片集合
     * @param type  图片的类型
     * @param what  handler的what值
     */
    public void uploadMultiFiles(final List<String> files, final String type, final int what) {
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
                    bodyBuilder.addFormDataPart("imgtype", type);
                    String token = TokenManager.getInstance().getTokeValue() == null ? INVALID_TOKEN : TokenManager.getInstance().getTokeValue();
                    bodyBuilder.addFormDataPart(TOKEN_KEY, token);
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    bodyBuilder.addFormDataPart(TIMESTAMP_KEY, timestamp);
                    for (int i = 0; i < files.size(); i++) {
                        String fPath = files.get(i);
                        if (ImageDownloader.Scheme.FILE.belongsTo(fPath))
                            fPath = ImageDownloader.Scheme.FILE.crop(fPath);
                        File f = new File(fPath);
                        if (!f.exists()) {
                            throw new IllegalStateException("指定的文件必须存在" + f.getAbsolutePath());
                        }

                        //  将图片设置分辨率
                        Bitmap bitmap = ImageCompresser.scale(f, Config.IMAGE_WIDTH, Config.IMAGE_HEIGHT);
                        File tmpFile = getTmpFile();
                        FileOutputStream outputStream = new FileOutputStream(tmpFile);

                        // TODO: 2018/3/12  
                        //  对图片进行压缩
                        bitmap.compress(Bitmap.CompressFormat.JPEG, (int) (Config.scaleFactor * 100), outputStream);
                        StreamUtils.close(outputStream);
                        bodyBuilder.addFormDataPart(UPLOAD_FILE_KE, f.getAbsolutePath(),
                                RequestBody.create(MediaType.parse("image/jpeg"), tmpFile));
                    }

                    //  构建请求体后  需要对请求参数进行组装
//                    MultipartBody requestBody = bodyBuilder.build();
                    //构建请求体
                    Request request = new Request.Builder().url(mURLConst.concatAction(mURLConst.getImageUploadUrl()))
                            .post(bodyBuilder.build()).addHeader("connection", "close").build();
                    OkHttpClient okHttpClient = mContext.getOkHttpClient();

                    try {
                        call = okHttpClient.newCall(request);
                        okhttp3.Response res = call.execute();
                        String str = res.body().string();
                        ArrayList<String> array = new ArrayList<>();

                        JSONObject jo = new JSONObject(str);
                        ALog.i(TAG, "图片请求打印------------->" + String.valueOf(jo));

                        response.errorCode = jo.getInt(CODE_KEY);
                        response.isSuccessful = response.errorCode == mBaseCodeTable.getSuccessfulCode();
                        if (response.isSuccessful) {
                            response.obj = array;
                            response.obj = jo.getJSONObject(DATA_KEY).createObject(HeaderBean.class);
//                            JSONArray ja = jo.getJSONArray(DATA_KEY);
//                            final int count = ja.length();
//                            for (int i = 0; i < count; i++) {
//                                array.add(ja.getString(i));
//                            }
                        }
                        if (Config.DEBUG) {
                            ALog.e(TAG, "response : " + str);
                        }
                    } catch (IOException e) {
                        if (Config.DEBUG) {
                            ALog.e("uploadMultiBytes", e);
                        }
                        response.isSuccessful = false;
                        response.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                    }
                } catch (Exception e) {
                    if (Config.DEBUG) {
                        ALog.e(TAG, e);
                    }
                    response.isSuccessful = false;
                    if (e instanceof ConnectException) {
                        response.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                        response.errorMsg = mBaseCodeTable.getCodeDescription(response.errorCode);
                    } else {
                        if (String.valueOf(e.getMessage()).contains("Canceled")) {
                            response.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                        } else {
                            response.errorCode = mBaseCodeTable.getUnknownErrorCode();
                        }
                    }
                } finally {
                    removeCall(call, what);
                    if (mHandler != null) {
                        mHandler.obtainMessage(what, response).sendToTarget();
                    }
                }
            }
        });
    }


    /**
     * 上传音频文件
     *
     * @param path 上传的文件的路径
     * @param what
     */
    public void uploadAudioFile(final String path, final int what) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Response response = new Response();
                response.parameters = new Object[]{path};
                Call call = null;
                try {
                    //构建表单数据
                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
                    String token = TokenManager.getInstance().getTokeValue() == null ? INVALID_TOKEN : TokenManager.getInstance().getTokeValue();
                    bodyBuilder.addFormDataPart(TOKEN_KEY, token);

                    String timestamp = String.valueOf(System.currentTimeMillis());
                    bodyBuilder.addFormDataPart(TIMESTAMP_KEY, timestamp);

                    File f = new File(path);
                    if (!f.exists()) {
                        throw new IllegalStateException("指定的文件必须存在" + f.getAbsolutePath());
                    }

                    bodyBuilder.addFormDataPart(UPLOAD_VOICE_KE, f.getAbsolutePath(), RequestBody.create(MediaType.parse("audio/mpeg"), f));

                    //构建请求
                    Request request = new Request.Builder().url(mURLConst.concatAction(mURLConst.getAudioUploadUrl()))
                            .post(bodyBuilder.build()).addHeader("connection", "close").addHeader("content-encode", "AES").build();
                    OkHttpClient okHttpClient = mContext.getOkHttpClient();

                    try {
                        call = okHttpClient.newCall(request);
                        okhttp3.Response res = call.execute();
                        String str = res.body().string();
                        ArrayList<String> array = new ArrayList<>();

                        JSONObject jo = new JSONObject(str);
                        response.errorCode = jo.getInt(CODE_KEY);
                        response.isSuccessful = response.errorCode == mBaseCodeTable.getSuccessfulCode();
                        response.obj = array;
                        JSONArray ja = jo.getJSONArray(DATA_KEY);
                        final int count = ja.length();
                        for (int i = 0; i < count; i++) {
                            array.add(ja.getString(i));
                        }
                        if (Config.DEBUG) {
                            ALog.e(TAG, "response : " + str);
                        }
                    } catch (IOException e) {
                        if (Config.DEBUG) {
                            ALog.e("uploadMultiBytes", e);
                        }
                        response.isSuccessful = false;
                        response.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                    }
                } catch (Exception e) {
                    if (Config.DEBUG) {
                        ALog.e(TAG, e);
                    }
                    response.isSuccessful = false;
                    if (e instanceof ConnectException) {
                        response.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                        response.errorMsg = mBaseCodeTable.getCodeDescription(response.errorCode);
                    } else {
                        if (String.valueOf(e.getMessage()).contains("Canceled")) {
                            response.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                        } else {
                            response.errorCode = mBaseCodeTable.getUnknownErrorCode();
                        }
                    }
                } finally {
                    removeCall(call, what);
                    if (mHandler != null) {
                        mHandler.obtainMessage(what, response).sendToTarget();
                    }
                }
            }
        });

    }

    /**
     * 获取临时文件
     *
     * @return 临时文件对象
     */
    public File getTmpFile() throws IOException {
        return File.createTempFile("tmp", ".jpg");
    }

    /**
     * 取消指定what的最后一条任务
     *
     * @param what 需要取消的任务的what
     */
    public void cancelTask(int what) {
        synchronized (CommonRequests.class) {
            List<Call> calls = TASKS.get(what);
            if (calls != null && !calls.isEmpty()) {
                try {
                    calls.get(calls.size() - 1).cancel();
                } catch (Exception e) {
                    if (isDebugMode()) ALog.e(TAG, e);
                }
            }
        }
    }

    /**
     * 取消指定的what的所有任务
     *
     * @param what 需要取消的任务的what
     */
    public void cancelTasks(int what) {
        synchronized (CommonRequests.class) {
            List<Call> calls = TASKS.get(what);
            if (calls != null && !calls.isEmpty()) {
                for (Call call : calls) {
                    try {
                        call.cancel();
                    } catch (Exception e) {
                        if (isDebugMode()) ALog.e(TAG, e);
                    }
                }
            }
        }
    }

    /**
     * 取消所有的任务
     */
    public void cancelTasks() {
        synchronized (CommonRequests.class) {
            for (List<Call> calls : TASKS.values()) {
                if (calls != null && !calls.isEmpty()) {
                    for (Call call : calls) {
                        try {
                            call.cancel();
                        } catch (Exception e) {
                            if (isDebugMode()) ALog.e(TAG, e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理结果接口
     */
    public interface ProcessResult {
        /**
         * 当得到结果回调
         *
         * @param result 解析出来的数据 JSONObject结果集合
         * @param res    你可以将处理结果放在这个响应实体中
         */
        void onResult(Response res, JSONObject result) throws Exception;
    }

    /**
     * 处理结果接口
     * 如果你想总是在handler中获取到指定的值,那么你可能需呀使用这个类
     * 使用场景:
     * 如果你要做一个加载更多
     * 那么,你需要传递页码,不管什么时候你在得到本次请求结果时,同样需要知道该请求属于第几页的
     */
    public interface ProcessResultExtension {
        /**
         * 当得到结果回调
         *
         * @param result 解析出来的数据 JSONObject结果集合
         * @param res    你可以将处理结果放在这个响应实体中
         */
        void onResult(Response res, JSONObject result) throws Exception;

        /**
         * 请求之前执行,主要解决可能在某些情况下无法回调 onResult 但是任然需要通过response传递附加数据时使用
         *
         * @param res    Response 对象
         * @param client 网络请求对象
         */
        void onRequestBefore(Response res, OkHttpClient client);
    }

    /**
     * 空处理结果实现
     */
    public static class EmptyProcessResult implements ProcessResult {

        @Override
        public void onResult(Response res, JSONObject result) throws Exception {

        }
    }

    /**
     * 可以处理缓存结果的的回调
     */
    public interface ProcessResultCanCacheExtension extends ProcessResultExtension {

        /**
         * 当缓存结果获取后执行
         *
         * @param response   用来响应结果
         * @param jsonObject 根据缓存对象创建的json对象
         * @param result     获取的缓存实体对象
         */
        void onCacheResult(Response response, Cache result, JSONObject jsonObject) throws Exception;

        /**
         * 处理缓存之前,主要解决可能在某些情况下无法回调 onCacheResult 但是任然需要通过response传递附加数据时使用
         *
         * @param res   Response 对象
         * @param cache 缓存对象
         */
        void onProcessCacheBefore(Response res, Cache cache);

        /**
         * 读取缓存对象
         *
         * @return 获取到的缓存对象
         */
        @Nullable
        Cache readCache(String action,
                        int what,
                        boolean initiativeRefresh,
                        Object... params);

        /**
         * 保存Cache对象文件中
         */
        void saveCache(String action, int what, boolean initiativeRefresh, String result, Object... params);

        /**
         * 获取数据识别标志位
         * 例如：
         * 你在获取列表类的数据市就可以返回 获取的数据的下标
         * 你在获取一个动态的详情时就可以返回 需要获取的数据的下标
         *
         * @return 识别标志位
         */
        int getFlag();

        /**
         * 清楚缓存
         */
        void clearCache();

        /**
         * 获取身份标志
         *
         * @return
         */
        String getIdentity();


        /**
         * 是否需要清楚缓存
         *
         * @return true或false
         */
        boolean needClear(String action, int what, boolean initiativeRefresh, String result, Object... params);
    }

    /**
     * 可以处理缓存结果的的回调
     */
    public abstract class SimpleProcessResultCanCacheExtension implements ProcessResultCanCacheExtension {

        @Override
        public void onCacheResult(Response response, Cache result, JSONObject jsonObject) throws Exception {
            onResult(response, jsonObject);
        }

        @Override
        public void onProcessCacheBefore(Response res, Cache cache) {
            onRequestBefore(res, null);
        }

        @Nullable
        @Override
        public Cache readCache(String action, int what, boolean initiativeRefresh, Object... params) {
            //缓存管理器的初始化，假设没有初始化的话
            CacheManager instance = CacheManager.getInstance();
            if (!instance.isInitialized()) {
                instance.init(mContext);
                //这句话加上只是为了监视缓存的
                //可以不加
                if (Config.DEBUG) {
                    instance.registerCacheMonitor(new ICacheMonitor() {
                        @Override
                        public void onCached(ICacheable cacheable, Cache cache, long maxCacheSize, long currentCachedSize) {
                            ALog.i(TAG, String.format(Locale.US, "%s onCached -> %s maxCacheSize %s currentCacheSize %s factor %.4f", cacheable, cache, maxCacheSize, currentCachedSize, currentCachedSize * 1.0 / maxCacheSize));
                        }

                        @Override
                        public void onTrimElement(ICacheable cacheable, Cache cache, long maxCacheSize, long currentCachedSize) {
                            ALog.i(TAG, String.format(Locale.US, "%s onTrimElement -> %s maxCacheSize %s currentCacheSize %s factor %.4f", cacheable, cache, maxCacheSize, currentCachedSize, currentCachedSize * 1.0 / maxCacheSize));
                        }

                        @Override
                        public void onRemovedElement(ICacheable cacheable, Cache cache, long maxCacheSize, long currentCachedSize) {
                            ALog.i(TAG, String.format(Locale.US, "%s onRemovedElement -> %s maxCacheSize %s currentCacheSize %s factor %.4f", cacheable, cache, maxCacheSize, currentCachedSize, currentCachedSize * 1.0 / maxCacheSize));
                        }

                        @Override
                        public void onLowCacheSpace(ICacheable cacheable, float factor) {
                            ALog.i(TAG, String.format(Locale.US, "%s onLowCacheSpace -> %.2f", cacheable, factor));
                        }
                    });
                }
            }

            String key = MD5FlagRecordGenerator.encodeKey(action, getFlag(), getIdentity());
            return instance.get(key);
        }

        @Override
        public void saveCache(String action, int what, boolean initiativeRefresh, String result, Object... params) {
            String key = MD5FlagRecordGenerator.encodeKey(action, getFlag(), getIdentity());
            Cache cache = packetCacheObject(key, result);
            CacheManager.getInstance().put(key, cache);
        }

        @Override
        public String getIdentity() {
            return getToken();
        }

        @Override
        public void clearCache() {
            CacheManager.getInstance().clear();
        }

        @Override
        public boolean needClear(String action, int what, boolean initiativeRefresh, String result, Object... params) {
            //这里的默认实现其实只是针对下表获取数据的方式来编写的
            //如果你缓存的是id的话，你应该就需要重写这个方法了
            return getFlag() == 0 && initiativeRefresh;
        }
    }

    /**
     * 请求处理
     * <p>
     * 在不调用URLRequest的情况下去调用接口
     *
     * @param pr     请求回调接口
     * @param action 请求的地址
     * @param what   请求的 what值 如果handler 不为null则使用handler发送消息
     * @param params 请求的参数
     */
    public void processNotUrlRequest(final ProcessResult pr, String action, final int what, final Object... params) {
//        action = mURLConst.concatAction(action);
        if (Config.DEBUG) {
            ALog.e(TAG, "params -> " + action + " -> " + Arrays.toString(params));
        }
        if (networkChecker(null, what, params)) {

           /* StringBuffer sb = new StringBuffer();

            //如果存在参数,并且参数是合法的
            if (params != null && params.length > 0 && params.length % 2 == 0) {
                final int paramsCount = params.length / 2;
                for (int i = 0; i < paramsCount; i++) {
                    String key = params[i * 2].toString();
                    String value = String.valueOf(params[i * 2 + 1]);
                    if (key != null && value != null) {
                        if (i != 0) sb.append("&");
                        sb.append(key).append("=").append(value);
                    }
                }
            }*/

            FormBody.Builder formEncodingBuilder = new FormBody.Builder();
            addTimestamp(formEncodingBuilder);
            addTokenPair(formEncodingBuilder);
            //如果存在参数,并且参数是合法的
            if (params != null && params.length > 0 && params.length % 2 == 0) {
                final int paramsCount = params.length / 2;
                for (int i = 0; i < paramsCount; i++) {
                    String key = params[i * 2].toString();
                    String value = String.valueOf(params[i * 2 + 1]);
                    if (key != null && value != null) {
                        formEncodingBuilder.add(key, value);
                    }
                }
            }

            //获取okHttpClient,用于网络请求
            OkHttpClient client = mContext.getOkHttpClient();
            //构建请求体
            Request requestBody = new Request.Builder().post(formEncodingBuilder.build())
                    .addHeader("connection", "close").url(action).build();
//            Request requestBody = new Request.Builder().get().url(action + sb.toString()).build();

            //添加一个新的call到队列请求队列中
            Call call = client.newCall(requestBody);
            addCall(call, what);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ALog.e(TAG, e);
                    removeCall(call, what);
                    if (mHandler != null) {
                        Response res = new Response();
                        res.parameters = params;
                        res.isSuccessful = false;
                        if (String.valueOf(e.getMessage()).contains("Canceled")) {
                            res.errorCode = mBaseCodeTable.getCanceledExceptionCode();
                        } else {
                            res.errorCode = mBaseCodeTable.getNetWorkExceptionCode();
                        }
                        mHandler.obtainMessage(what, res).sendToTarget();
                    }
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    removeCall(call, what);
                    Response res = new Response();
                    res.parameters = params;
                    try {
                        if (response.isSuccessful()) {
                            TokenManager.getInstance().updateTokeLastTime();
                            String result = response.body().string();
                            if (Config.DEBUG) {
                                ALog.d(TAG, result);
                            }
                            JSONObject jo = new JSONObject(result);
                            res.errorMsg = jo.getString("msg");
                            if (jo.getString("status").equals("1")) {
                                res.errorCode = 1;
                            } else {
                                res.errorCode = -1;
                            }
                            res.isSuccessful = (res.errorCode == mBaseCodeTable.getSuccessfulCode());
                            pr.onResult(res, jo);
                        } else {//错误的响应码
                            res.isSuccessful = false;
                            res.errorCode = mBaseCodeTable.getResponseErrorCode(response.code());
                            res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                            ALog.e(TAG, "Http status code : " + response.code() + " ，Msg : " + response.message());
                            ALog.e(TAG, "Error Reason : " + response.body().string());
                        }
                    } catch (Exception e) {
                        ALog.e(TAG, e);
                        res.isSuccessful = false;
                        res.errorCode = mBaseCodeTable.getParseExceptionErrorCode(e);
                        res.errorMsg = mBaseCodeTable.getCodeDescription(res.errorCode);
                    } finally {
                        if (mHandler != null) {
                            mHandler.obtainMessage(what, res).sendToTarget();
                        }
                    }
                }
            });
        }
    }
}
