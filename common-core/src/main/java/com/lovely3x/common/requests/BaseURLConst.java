package com.lovely3x.common.requests;

import android.text.TextUtils;


import java.util.Hashtable;
import java.util.Map;

/**
 * 基础地址访问类
 * Created by lovely3x on 15-11-16.
 */
public abstract class BaseURLConst {

    private static BaseURLConst INSTANCE;

    /**
     * 地址表
     * 设计这个的目的是为了让更好的时项目模块话
     * 因为有些东西组合进行的,所以很有可能不存在
     */
    private static final Hashtable<String, String> urlActionTable = new Hashtable<>();

    static {
        //可以在这里初始化默认的地址action
    }

    /**
     * 添加地址到地址表中
     *
     * @param key    获取地址的key
     * @param action 该key对应的地址
     */
    public static void putAction(String key, String action) {
        urlActionTable.put(key, action);
    }

    /**
     * 获取指定key的地址
     *
     * @param key 需要获取地址的key
     * @return 获取到的地址
     */
    public static String getAction(String key) {
        return urlActionTable.get(key);
    }

    /**
     * 初始化
     *
     * @param constImpl 地址实现类
     */
    public static void init(BaseURLConst constImpl) {
        INSTANCE = constImpl;
    }

    public static BaseURLConst getInstance() {
        initCheck();
        return INSTANCE;
    }

    /**
     * 获取服务端的项目名
     *
     * @return 项目名 例如 test
     */
    public abstract String getApplicationName();

    /**
     * 获取服务端的地址
     *
     * @return 获取地址, 可以是ip也可以是域名
     */
    public abstract String getDomain();

    /**
     * 获取端口号 例如 80
     *
     * @return 端口号
     */
    public abstract int getPort();

    /**
     * 获取基础访问地址
     * 例如 http://192.168.1.111:8080/appName/
     * 例如 http://www.baidu.com/imgs/
     *
     * @return 地址
     */
    public String getBaseURL() {
        //http://192.168.0.111:8080
        //http://192.168.0.111/appname/
        //http://192.168.0.111:8080/appname/
        String prefix = getProtocol();
        String domain = getDomain();
        String appName = getApplicationName();
//        int port = getPort();
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(domain);
//        if (port > 0) {
//            sb.append(':').append(port);
//        }
        if (!TextUtils.isEmpty(appName)) {
            sb.append('/').append(appName).append('/');
        }
        return sb.toString();
    }

    /**
     * 初始化检查
     * 检查是否调用了init方法，进行初始化
     */
    protected static void initCheck() {
        if (INSTANCE == null) throw new IllegalStateException("请先调用#init方法进行初始化.");
    }

    /**
     * 连接一个请求动作地址到基础地址上,
     * 一般情况下这个地址就是用于访问服务器了
     *
     * @param action 需要连接的地址
     * @return 完整的访问地址
     */
    public abstract String concatAction(String action);

    public String appendParameter(String action, Map<String, String> parameters) {
        StringBuilder url = new StringBuilder(concatAction(action));
        if (url.charAt(url.length() - 1) != '?') url.append('?');

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            url.append(entry.getKey()).append('=').append(entry.getValue());
            url.append('&');
        }
        if (url.charAt(url.length() - 1) == '&') {
            url.deleteCharAt(url.length() - 1);
        }
        return url.toString();
    }

    /**
     * 获取图片上传地址
     *
     * @return 图片上传地址
     */
    public abstract String getImageUploadUrl();

    /**
     * 获取语音文件上传地址
     */
    public abstract String getAudioUploadUrl();

    public String getProtocol() {
//        return "h" + 't' + 't' + 'p' + ':' + '/' + '/';
        return "h" + 't' + 't' + 'p' + 's' + ':' + '/' + '/';
    }
}
