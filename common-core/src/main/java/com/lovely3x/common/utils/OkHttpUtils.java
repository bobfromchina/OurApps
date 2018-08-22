package com.lovely3x.common.utils;

import okhttp3.FormBody.Builder;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Ok Http 工具
 * Created by lovely3x on 16-3-22.
 */
public class OkHttpUtils {

    /**
     * 获取表单构建数据
     */
    public static okhttp3.FormBody.Builder getFormEncodingBuilder(String[] keys, String[] values) {
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String value = values[i];
            formEncodingBuilder.add(String.valueOf(key), String.valueOf(value));
        }
        return formEncodingBuilder;
    }

    /**
     * 包装请求体用于上传文件的回调
     *
     * @param requestBody             请求体RequestBody
     * @param progressRequestListener 进度回调接口
     * @return 包装后的进度回调请求体
     */
    public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressRequestListener progressRequestListener) {
        //包装请求体
        return new ProgressRequestBody(requestBody, progressRequestListener);
    }

    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param client           待包装的OkHttpClient
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient，使用clone方法返回
     */
    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ProgressResponseListener progressListener) {
        //克隆
        OkHttpClient clone = client.newBuilder().build();
        //增加拦截器
        clone.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                //拦截
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        return clone;
    }

    /**
     * 响应体进度回调接口，比如用于文件下载中
     */
    public interface ProgressResponseListener {
        void onResponseProgress(long bytesRead, long contentLength, boolean done);
    }

    /**
     * 请求体进度回调接口，比如用于文件上传中
     */
    public interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

    /**
     * 包装的请求体，处理进度
     */
    public static class ProgressRequestBody extends RequestBody {
        //实际的待包装请求体
        private final RequestBody requestBody;
        //进度回调接口
        private final ProgressRequestListener progressListener;
        //包装完成的BufferedSink
        private BufferedSink bufferedSink;

        /**
         * 构造函数，赋值
         *
         * @param requestBody      待包装的请求体
         * @param progressListener 回调接口
         */
        public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressListener) {
            this.requestBody = requestBody;
            this.progressListener = progressListener;
        }

        /**
         * 重写调用实际的响应体的contentType
         *
         * @return MediaType
         */
        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        /**
         * 重写调用实际的响应体的contentLength
         *
         * @return contentLength
         * @throws IOException 异常
         */
        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        /**
         * 重写进行写入
         *
         * @param sink BufferedSink
         * @throws IOException 异常
         */
        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            if (bufferedSink == null) {
                //包装
                bufferedSink = Okio.buffer(sink(sink));
            }
            //写入
            requestBody.writeTo(bufferedSink);
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();

        }

        /**
         * 写入，回调进度接口
         *
         * @param sink Sink
         * @return Sink
         */
        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long bytesWritten = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    if (contentLength == 0) {
                        //获得contentLength的值，后续不再调用
                        contentLength = contentLength();
                    }
                    //增加当前写入的字节数
                    bytesWritten += byteCount;
                    //回调
                    progressListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            };
        }
    }

    /**
     * 包装的响体，处理进度
     */
    public static class ProgressResponseBody extends ResponseBody {
        //实际的待包装响应体
        private final ResponseBody responseBody;
        //进度回调接口
        private final ProgressResponseListener progressListener;
        //包装完成的BufferedSource
        private BufferedSource bufferedSource;

        /**
         * 构造函数，赋值
         *
         * @param responseBody     待包装的响应体
         * @param progressListener 回调接口
         */
        public ProgressResponseBody(ResponseBody responseBody, ProgressResponseListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }


        /**
         * 重写调用实际的响应体的contentType
         *
         * @return MediaType
         */
        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        /**
         * 重写调用实际的响应体的contentLength
         *
         * @return contentLength
         * @throws IOException 异常
         */
        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        /**
         * 重写进行包装source
         *
         * @return BufferedSource
         * @throws IOException 异常
         */
        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                //包装
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        /**
         * 读取，回调进度接口
         *
         * @param source Source
         * @return Source
         */
        private Source source(Source source) {

            return new ForwardingSource(source) {
                //当前读取字节数
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    //回调，如果contentLength()不知道长度，会返回-1
                    progressListener.onResponseProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

}
