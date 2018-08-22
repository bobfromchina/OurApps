package com.jackmar.jframelibray.http.interceptor;


import android.content.Context;

import com.jackmar.jframelibray.utils.PreHelper;
import com.jackmar.jframelibray.utils.PreferenceKey;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
public final class ReceiveCookieInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Context context;

    public ReceiveCookieInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
            HashSet<String> LocalCookie = PreHelper.defaultCenter(context).getStringSet(PreferenceKey.COOKIE);
            List<String> cookies = new ArrayList<>();
            if (LocalCookie != null && LocalCookie.size() > 0) {
                cookies.addAll(LocalCookie);
                if (response.headers("Set-Cookie") != null) {
                    for (int i = 0; i < cookies.size(); i++) {
                        for (String cookie : response.headers("Set-Cookie")) {
                            if (cookies.get(i).indexOf(cookie.split(";")[0].split("=")[0]) != -1) {
                                cookies.set(i, cookie);
                            }
                        }
                    }
                    HashSet<String> newCookies = new HashSet<>();
                    newCookies.addAll(cookies);
                    PreHelper.defaultCenter(context).putStringSet(PreferenceKey.COOKIE, newCookies);
                }
            } else {
                HashSet<String> cookie = new HashSet<>();
                if (response.headers("Set-Cookie") != null) {
                    for (String netCookie : response.headers("Set-Cookie")) {
                        cookie.add(netCookie);
                    }
                    PreHelper.defaultCenter(context).putStringSet(PreferenceKey.COOKIE, cookie);
                }
            }


        } catch (Exception e) {
            throw e;
        }

        return response;
    }
}
