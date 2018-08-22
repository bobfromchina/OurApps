package com.jackmar.jframelibray.fileutils;


import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 流工具
 * Created by lovely3x on 15-11-14.
 */
public class StreamUtils {

    /**
     * 关闭closeable对象,流对象都是closeable的
     *
     * @param closeable 需要关闭的对象
     * @return true or false 表示失败或成功
     */
    public static boolean close(Closeable closeable) {
        boolean result = true;
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 关闭一组可以关闭的对象
     *
     * @param failureContinue 关闭一个失败后是否继续
     * @param closeables      可关闭的一组对象
     */
    public static void close(boolean failureContinue, Closeable... closeables) {
        for (Closeable c : closeables) {
            if (!close(c)) {
                if (!failureContinue) {
                    break;
                }
            }
        }
    }

    /**
     * 关闭一组可以关闭的对象,在关闭其中一个失败后会继续关闭其他的
     *
     * @param closeables 可关闭的一组对象
     */
    public static void close(Closeable... closeables) {
        close(true, closeables);
    }


    /**
     * 从输入流中复制数据到输出流中
     *
     * @param is         输入流
     * @param os         输出流
     * @param bufferSize 缓冲区大小
     * @param needClose  赋值完成 是否需要关闭流
     * @return 成功或失败
     */
    public static boolean copy(InputStream is, OutputStream os, int bufferSize, boolean needClose) {

        boolean result = true;

        byte[] buffer = new byte[bufferSize <= 0 ? (1024 * 4) : bufferSize];
        int len = -1;

        try {
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        } finally {
            if (needClose) {
                StreamUtils.close(is, os);
            }
        }

        return result;
    }

    /**
     * 读取字符串
     *
     * @param fis      输入流
     * @param encoding 编码
     * @return 读取的字符串
     */
    public static String readToString(InputStream fis, String encoding) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(fis, baos, 1024 * 8, true);
        try {
            return new String(baos.toByteArray(), 0, baos.size(), encoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("readToString", String.valueOf(e));
        }
        return "";
    }

    /**
     * 读取字符串
     *
     * @param fis 输入流
     * @return 读取的字符串
     */
    public static String readToString(InputStream fis) {
        return readToString(fis, "UTF-8");
    }

    /**
     * 读取为字节数组
     *
     * @param fis 输入流
     * @return 读取的结果数组
     */
    public static byte[] readToByte(InputStream fis) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(fis, baos, 4 * 1024, true);
        return baos.toByteArray();
    }


    public static boolean writeByteToStream(OutputStream os, byte[] bys) {
        return copy(new ByteArrayInputStream(bys), os, 4 * 1024, true);
    }

    /**
     * 写入字符串到指定的输出流中
     *
     * @param os      输出流
     * @param content 需要写入的内容
     * @return 是否写入成功
     */
    public static boolean writeStringToStream(OutputStream os, String content) {
        return copy(new ByteArrayInputStream(content.getBytes()), os, 4 * 1024, true);
    }

    /**
     * 写入字符串到指定的输出流中
     *
     * @param os       输出流
     * @param encoding 编码
     * @param content  需要写入的内容
     * @return 是否写入成功
     */
    public static boolean writeStringToStream(OutputStream os, String content, String encoding) {
        try {
            return copy(new ByteArrayInputStream(content.getBytes(encoding)), os, 4 * 1024, true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
