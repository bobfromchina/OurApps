package com.jackmar.jframelibray.http.interceptor;


import android.content.Context;

import com.jackmar.jframelibray.utils.PreHelper;
import com.jackmar.jframelibray.utils.PreferenceKey;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Title 拦截器
 * @Date 2016-05-23 17:14
 */

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class AddCookieInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Context context;

    public AddCookieInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        HashSet<String> cookie = PreHelper.defaultCenter(context).getStringSet(PreferenceKey.COOKIE);
        if (cookie != null) {
            for (String name : cookie) {
                builder.addHeader("Cookie", name);
            }
        }
        return chain.proceed(builder.build());
    }
}
