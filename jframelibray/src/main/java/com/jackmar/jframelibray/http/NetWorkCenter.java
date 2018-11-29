package com.jackmar.jframelibray.http;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;


import com.jackmar.jframelibray.JFrameConfig;
import com.jackmar.jframelibray.base.ALog;
import com.jackmar.jframelibray.http.interceptor.ResponseInterceptor;
import com.jackmar.jframelibray.http.util.FastJsonConvertFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by JackMar on 2017/2/27.
 * 邮箱：1261404794@qq.com
 */

public class NetWorkCenter {
    //请求超时时间
    private static final int DEFAULT_TIMEOUT = 10;
    private static boolean isInit;
    private static OkHttpClient.Builder builder;
    private static FastJsonConvertFactory fastJsonConvertFactory = new FastJsonConvertFactory();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static JFrameConfig mFrameConfig;
    private static Properties Propertiesprop;

    /**
     * 获取网络请求的接口
     *
     * @param t
     * @param context
     * @param headers
     * @param <T>
     * @return
     */
    public static <T> T getApi(Class<T> t, Context context, @Nullable final HashMap<String, String> headers) {
        builder = new OkHttpClient.Builder();
        mFrameConfig = JFrameConfig.getInstance(context);
        initInterceptor(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mFrameConfig.getHostUrl()).client(genericClient(headers))
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(fastJsonConvertFactory)
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(rxJavaCallAdapterFactory).build();
        return retrofit.create(t);
    }

    /**
     * 获取网络请求的接口
     *
     * @param t
     * @param context
     * @param headers
     * @param <T>
     * @return
     */
    public static <T> T getApiB(Class<T> t, Context context, @Nullable final HashMap<String, String> headers) {
        builder = new OkHttpClient.Builder();
        mFrameConfig = JFrameConfig.getInstance(context);
        initInterceptor(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://admin.colorfulflorist.com/test/").client(genericClient(headers))
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(fastJsonConvertFactory)
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(rxJavaCallAdapterFactory).build();
        return retrofit.create(t);
    }

    /**
     * 添加请求的Header
     * 此处既可以添加header,可以不添加Header
     * <p>
     * 如果不需要传header，直接传参数null
     */
    static OkHttpClient genericClient(@Nullable final HashMap<String, String> headers) {
        OkHttpClient client = builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                if (headers != null && headers.size() > 0) {
                    for (String key : headers.keySet()) {
                        builder.addHeader(key, headers.get(key));
                    }
                }
                Request request = builder.build();
                return chain.proceed(request);
            }

        }).build();
        return client;
    }

    /**
     * 初始化设置拦截器
     * 第一个
     * 这里的拦截器主要是使用OkHttp提供的HttpLoggingInterceptor
     * 在测试阶段方便打印请求的返回数据
     * <p>
     * 第二个
     * 添加返回数据的自定义拦截器，这个拦截器是为了处理返回的数据
     * 判断请求是否成功，返回数据是否正确
     */
    private static void initInterceptor(Context context) {
        if (JFrameConfig.getInstance(context).isDebug()) {
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i("JackMr_RxJava", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new ResponseInterceptor());
        //处理cookie
//        builder.addInterceptor(new ReceiveCookieInterceptor(context));
//        builder.addInterceptor(new AddCookieInterceptor(context));
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        isInit = true;
    }
}
