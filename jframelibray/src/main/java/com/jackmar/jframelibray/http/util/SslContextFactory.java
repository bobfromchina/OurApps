package com.jackmar.jframelibray.http.util;

import javax.net.ssl.SSLContext;

/**
 * Created by JackMar on 2017/6/14.
 * 邮箱：1261404794@qq.com
 */

public class SslContextFactory {

    private static final String CLIENT_TRUST_PASSWORD = "changeit";//信任证书密码，该证书默认密码是changeit
    private static final String CLIENT_AGREEMENT = "TLS";//使用协议
    private static final String CLIENT_TRUST_MANAGER = "X509";
    private static final String CLIENT_TRUST_KEYSTORE = "BKS";
    SSLContext sslContext = null;

//    public SSLContext getSslSocket() {
//        try {
////取得SSL的SSLContext实例
//            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
////取得TrustManagerFactory的X509密钥管理器实例
//            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(CLIENT_TRUST_MANAGER);
////取得BKS密库实例
//            KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
//            InputStream is = XApplication.getInstance().getResources().openRawResource(R.raw.suplcerts);
//            try {
//                tks.load(is, CLIENT_TRUST_PASSWORD.toCharArray());
//            } finally {
//                is.close();
//            }
////初始化密钥管理器
//            trustManager.init(tks);
////初始化SSLContext
//            sslContext.init(null, trustManager.getTrustManagers(), null);
//        } catch (Exception e) {
//            Log.e("SslContextFactory", e.getMessage());
//        }
//        return sslContext;
//    }
}
