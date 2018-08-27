package com.example.wangbo.ourapp.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.wangbo.ourapp.R;
import com.example.wangbo.ourapp.utils.WebViewHelper;
import com.jackmar.jframelibray.base.JBaseAct;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangbo on 2018/8/27.
 * <p>
 * 原生 h5交互测试类
 */

public class NativeTestAct extends JBaseAct {

    @BindView(R.id.wb)
    WebView webView;

    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLyContent(R.layout.activity_wv, true, "native交互");
        mJTitleView.setMoreText("dialog");
        mJTitleView.getMoreText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                webView.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {

                        // Android版本变量
                        final int version = Build.VERSION.SDK_INT;
                        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                        if (version < 18) {
                            webView.loadUrl("javascript:callJS()");
                        } else {
                            webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    //此处为 js 返回的结果
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    public void initView() {

        rlContainer.setVisibility(View.GONE);

        WebSettings wbSet = webView.getSettings();

        wbSet.setJavaScriptEnabled(true);

        wbSet.setJavaScriptCanOpenWindowsAutomatically(true);

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                /*
                 *  通过拦截可以判断你想要的操作
                 */
                Uri uri = Uri.parse(url);

                if (uri.getScheme().equals("js")) {

                    if (uri.getAuthority().equals("webview")) {
                        Map<String, String> map = WebViewHelper.URLRequest(url);
                        String arg1 = map.containsKey("arg1") ? map.get("arg1") : "";
                        String arg2 = map.containsKey("arg2") ? map.get("arg2") : "";
                        showAlert("Native拦截H5地址 并取值", "value1=" + arg1 + "value2=" + arg2, "确定", "取消", null, null).show();
                    }

                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        //AndroidtoJS类对象映射到js的test对象
        webView.addJavascriptInterface(new AndroidToJs(), "android");

        webView.loadUrl("file:///android_asset/NativeH5.html");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * Android 传递数据给H5
     */
    class AndroidToJs {
        // 定义JS需要调用的方法
        @JavascriptInterface
        public String loadNative(String msg) {
            /*
             *   我们在这里公开一个方法给JS，
             *   可以将需要的值拼接成一个字符串，
             *   然后让H5那边去截断取值
             */
            return "H5调用了Native的方法,并获取传递传输";
        }
    }
}
