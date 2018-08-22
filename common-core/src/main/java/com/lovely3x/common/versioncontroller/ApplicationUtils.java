package com.lovely3x.common.versioncontroller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * application工具
 * Created by lovely3x on 15-8-26.
 */
public class ApplicationUtils {

    private static final String TAG = "ApplicationUtils";

    /**
     * 获取指定的安装包的信息
     *
     * @param context 上下文
     * @param path    需要查看的安装的绝对路径
     * @return null或 {@link Version}
     */
    public static Version getArchivePackageVersion(Context context, String path) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            Version version = new Version();
            version.setVersionCode(packageInfo.versionCode);
            version.setVersionName(packageInfo.versionName);
            version.setPackageName(packageInfo.packageName);
            version.setUrl(Uri.fromFile(new File(path)).toString());

            return version;
        }
        return null;
    }
// TODO: 2017/7/6  
    /**
     * 获取安装包的版本信息
     *
     * @param context 上下文对象
     */
    public static Version getCurrentPackageVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                Version version = new Version();
                version.setVersionCode(packageInfo.versionCode);
                version.setVersionName(packageInfo.versionName);
                version.setPackageName(packageInfo.packageName);
                return version;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从xml文件中解析版本对象
     *
     * @param stream 需要解析的输入流
     * @return null或者是Version对象
     */
    public static Version parseVersion(InputStream stream) {
        final Version v = new Version();
        try {
            final SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
            sp.parse(stream, new DefaultHandler() {
                String currentName = null;

                public void startDocument() throws SAXException {
                }

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    currentName = qName;
                }

                public void characters(char[] ch, int start, int length) throws SAXException {
                    super.characters(ch, start, length);
                    String text = new String(ch, start, length);
                    if ("versionName".equalsIgnoreCase(currentName)) {
                        v.setVersionName(text);
                    } else if ("versionCode".equalsIgnoreCase(currentName)) {
                        v.setVersionCode(Integer.parseInt(text));
                    } else if ("content".equalsIgnoreCase(currentName)) {
                        v.setDescription(text);
                    } else if ("date".equalsIgnoreCase(currentName)) {
                        try {
                            v.setPublishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(text).getTime());
                        } catch (Exception e) {
                            v.setPublishTime(-1);
                        }
                    } else if ("force".equalsIgnoreCase(currentName)) {
                        v.setForceUpdate(Boolean.parseBoolean(text));
                    } else if ("address".equalsIgnoreCase(currentName)) {
                        v.setUrl(text);
                    } else if ("package".equalsIgnoreCase(currentName)) {
                        v.setPackageName(text);
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    currentName = null; // when the element end ,set the
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    /**
     * 判断指定的loc版本是否是最新的
     *
     * @param remoteVersion 服务端的版本对象
     * @param locVersion    本地版本对象
     * @return true or false
     */
    public static boolean isLatestVersion(Version remoteVersion, Version locVersion) {
        return !(remoteVersion != null && locVersion != null) || !locVersion.getPackageName().equals(remoteVersion.getPackageName()) || locVersion.getVersionCode() >= remoteVersion.getVersionCode();
    }

    /**
     * 获取新版本下载保存文件地址
     *
     * @param context       上下文
     * @param remoteVersion 版本对象，根据这个对象来生成文件名
     * @return 获取到的新版本文件保存地址
     */
    public static File getNewVersionDownloadFile(Context context, Version remoteVersion) {
        return new File(context.getExternalCacheDir(), String.format("%s_%d_v%s.apk", remoteVersion.getPackageName(), remoteVersion.getVersionCode(), remoteVersion.getVersionName()));
    }
}
