package com.lovely3x.jsonparser.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by lovely3x on 15-6-29.
 * 打印工具
 */
public class Log {

    public static final boolean DEBUG = true;

    /**
     * 打印日志 通知级别
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (DEBUG) System.out.println(tag + " : " + msg);
    }

    /**
     * 打印日志 错误级别
     *
     * @param tag
     * @param throwable
     */
    public static void e(String tag, Throwable throwable) {
        if (DEBUG) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            throwable.printStackTrace(ps);
            try {
                System.err.print(tag + " : " + new String(baos.toByteArray(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                ps.close();
            }
        }
    }

    /**
     * 打印日志 错误级别
     *
     * @param tag
     */
    public static void e(String tag, String msg) {
        if (DEBUG) {
            System.err.println(tag + " : " + msg);
        }
    }
}
