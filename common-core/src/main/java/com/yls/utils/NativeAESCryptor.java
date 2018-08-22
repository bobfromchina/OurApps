package com.yls.utils;


import android.support.v4.util.LruCache;

import com.lovely3x.common.utils.ALog;

import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * AES 编码器
 */
public class NativeAESCryptor {

    private static final String TAG = "NativeAESCryptor";

    private NativeAESCryptor() {
        throw new IllegalStateException("Can't instantiate a singleton class");
    }

    /**
     * 加密缓存
     */
    private static final LruCache<String, String> mEncryptedCache = new StringLruCache(1024 * 1024 * 10);

    /**
     * 解密缓存
     */
    private static final LruCache<String, String> mDecryptedCache = new StringLruCache(1024 * 1024 * 10);

    static {
        try {
            System.loadLibrary("util_jni");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 模式：编码
     */
    private static final int ENCRYPT = 0;

    /**
     * 模式：解码
     */
    private static final int DECRYPT = 1;

    /**
     * 编解码
     *
     * @param data 需要编码或解码的数据
     * @param time 时间戳
     * @param mode 模式：编码或解码
     * @return 编码或解码的结果
     */
    private native static byte[] crypt(byte[] data, long time, int mode);

    private native static byte[] read(String path, long time);

    /**
     * 编码数据
     *
     * @param data 需要编码的数据
     * @return 编码完成的数据
     */
    public static byte[] encryptData(byte[] data) {
        //编码数据
        byte[] crypt = null;
        try {
            crypt = crypt(data, System.currentTimeMillis(), ENCRYPT);
        } catch (Exception e) {
            if (ALog.DEBUG) ALog.e(TAG, e);
        }
        return crypt;
    }

    /**
     * 解码数据
     *
     * @param data 需要解码的数据
     * @return 解码完成的数据
     */
    public static byte[] decryptData(byte[] data) {
        //解码操作
        byte[] crypt = null;
        try {
            crypt = crypt(data, System.currentTimeMillis(), DECRYPT);
        } catch (Exception e) {
            if (ALog.DEBUG) ALog.e(TAG, e);
        }
        return crypt;
    }

    /**
     * 将16进制转换为二进制(服务端)
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStr2Bytes(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换为16进制
     *
     * @param data
     * @return
     */
    public static String bytes2HexStr(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte aData : data) {
            if (((int) aData & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
                buf.append("0");
            }
            buf.append(Long.toHexString((int) aData & 0xff)); /* 转换16进制,下方法同 */
        }
        return buf.toString();
    }

    /**
     * 编码数据
     *
     * @param data 需要编码的数据
     * @return 编码之后的数据或null
     */
    public static String encryptToAESHexString(String data) {
        if (data == null) return null;

        //从缓存中获取
        String cache = mEncryptedCache.get(data);
        if (cache != null) {
            if (ALog.DEBUG) {
                ALog.i(TAG, String.format(Locale.US, "EncryptToAESHexString key [%s] 命中缓存 [%s]", data, cache));
            }
            return cache;
        }

        try {
            byte[] encryptData = encryptData(data.getBytes("UTF-8"));

            if (encryptData != null) {
                String result = bytes2HexStr(encryptData);
                mEncryptedCache.put(data, result);
                return result;
            }
        } catch (UnsupportedEncodingException e) {
            //Ignored
        }
        return null;
    }

    /**
     * 解码数据
     *
     * @param data 需要解码的数据
     * @return 解码之后的数据或null
     */
    public static String decryptFromAESHexString(String data) {
        String cache = null;

        //尝试从缓存中获取
        if ((cache = mDecryptedCache.get(data)) != null) {
            if (ALog.DEBUG) {
                ALog.i(TAG, String.format(Locale.US, "DecryptFromAESHexString key [%s] 命中缓存 [%s]", data, cache));
            }
            return cache;
        }

        //先把十六进制的AES字符串转换为字节数组
        byte[] buf = hexStr2Bytes(data);

        //再解密这个数组
        byte[] decryptData = decryptData(buf);

        //如果解密成功
        if (decryptData != null) {

            String result = null;

            try {
                //创建一个解密结果字符串
                result = new String(decryptData, "UTF-8");

                //将结果放入缓存
                mDecryptedCache.put(data, result);

                return result;
            } catch (UnsupportedEncodingException e) {
                //Ignored
            }
        }
        return null;
    }
}