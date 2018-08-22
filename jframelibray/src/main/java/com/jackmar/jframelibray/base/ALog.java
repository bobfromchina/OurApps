package com.jackmar.jframelibray.base;

///Users/wangbo/AndroidStudioProjects/Colorful

import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lovely3x on 15-8-16.
 * 日志工具,封装了下系统的日志工具,便于管理
 */
public class ALog {

    public static final boolean DEBUG = true;//BuildConfig.DEBUG;

    /**
     * 日志等级 verbose级别
     */
    public final static int DEBUG_VERBOSE = 0;

    /**
     * 日志等级 debug级别
     */
    public final static int DEBUG_DEBUG = 1;
    /**
     * 日志等级 Info 级别
     */
    public final static int DEBUG_INFO = 2;
    /**
     * 日志等级 警告级别
     */
    public final static int DEBUG_WARNING = 3;
    /**
     * 日志等级 错误级别
     */
    public final static int DEBUG_ERROR = 4;

    /**
     * R
     * 写入日志我文件所需日志等级
     */
    private static int writeLevel = DEBUG_ERROR;

    /**
     * 写入文件
     */
    private static final boolean WRITE_TO_FILE = false;

    private static final File LOGFILE = new File(Environment.getExternalStorageDirectory(), new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(System.currentTimeMillis()));

    private static final String DEFAULT_MSG = "ALog";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.US);

    private static RandomAccessFile WRITER;


    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }

        if (WRITE_TO_FILE) {
            writeToFile(tag, null, msg, DEBUG_ERROR);
        }

    }

    public static void e(String tag, String msg, Exception e) {
        if (DEBUG) {
            if (e == null) {
                e(tag, msg);
            } else {
                Log.e(tag == null ? "null" : tag, msg == null ? "null" : msg, e);
            }
        }

        if (WRITE_TO_FILE) {
            writeToFile(tag, e, msg, DEBUG_ERROR);
        }

    }

    public static void e(String tag, Exception e) {
        if (DEBUG) {
            if (e == null) {
                e(tag, DEFAULT_MSG);
            } else {
                Log.e(tag, DEFAULT_MSG, e);
            }
        }

        if (WRITE_TO_FILE) {
            writeToFile(tag, e, null, DEBUG_ERROR);
        }

    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag == null ? "null" : tag, msg == null ? "null" : msg);
        }

        if (WRITE_TO_FILE) {
            writeToFile(tag, null, msg, DEBUG_WARNING);
        }

    }

    public static void w(String tag, String msg, Exception e) {
        if (DEBUG) {
            if (e == null) {
                w(tag, msg);
            } else {
                Log.w(tag == null ? "null" : tag, msg == null ? "null" : msg, e);
            }
        }
        if (WRITE_TO_FILE) {
            writeToFile(tag, e, msg, DEBUG_WARNING);
        }

    }

    public static void w(String tag, Exception e) {
        if (DEBUG) {
            if (e == null) {
                w(tag, DEFAULT_MSG);
            } else {
                Log.w(tag == null ? "null" : tag, DEFAULT_MSG, e);
            }

        }
        if (WRITE_TO_FILE) {
            writeToFile(tag, e, null, DEBUG_WARNING);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg == null ? "null" : msg);
        }
        if (WRITE_TO_FILE) {
            writeToFile(tag, null, msg, DEBUG_INFO);
        }
    }

    public static void d(String tag, Object msg) {
        if (DEBUG) {
            Log.i(tag, msg == null ? "null" : String.valueOf(msg));
        }
        if (WRITE_TO_FILE) {
            writeToFile(tag, null, String.valueOf(msg), DEBUG_DEBUG);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag == null ? "null" : tag, msg == null ? "null" : msg);
        }
        if (WRITE_TO_FILE) {
            writeToFile(tag, null, msg, DEBUG_VERBOSE);
        }
    }

    public static void e(String tag, Object... params) {
        if (DEBUG || WRITE_TO_FILE) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                Object s = params[i];
                sb.append(s == null ? "null" : s.toString());
                if (i + 1 != params.length) sb.append(",");
            }
            if (DEBUG) {

                Log.e(tag, sb.toString());
            }

            if (WRITE_TO_FILE) {
                writeToFile(tag, null, sb.toString(), DEBUG_ERROR);
            }
        }
    }

    public static void writeToFile(String tag, Exception e, String msg, int level) {
        if (level < writeLevel) return;

        synchronized (ALog.class) {

            tag = tag == null ? "NULL" : tag;
            msg = msg == null ? "NULL" : msg;

            try {
                WRITER = new RandomAccessFile(LOGFILE, "rw");
                WRITER.seek(WRITER.length());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                WRITER.write("\n".getBytes("UTF-8"));
                WRITER.write(SDF.format(System.currentTimeMillis()).getBytes("UTF-8"));
                WRITER.write("\t".getBytes("UTF-8"));
                WRITER.write(getReadableDebugLevel(level).getBytes("UTF-8"));
                WRITER.write("\t".getBytes("UTF-8"));
                WRITER.write(tag.getBytes("UTF-8"));
                WRITER.write(":".getBytes("UTF-8"));
                WRITER.write(msg.getBytes("UTF-8"));
                if (e != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintWriter pw = new PrintWriter(baos);
                    e.printStackTrace(pw);
                    WRITER.write(baos.toByteArray());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    WRITER.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getReadableDebugLevel(int level) {
        switch (level) {
            case DEBUG_VERBOSE:
                return "Verbose";
            case DEBUG_DEBUG:
                return "Debug";
            case DEBUG_INFO:
                return "Info";
            case DEBUG_WARNING:
                return "Warning";
            case DEBUG_ERROR:
                return "Error";
            default:
                return "Unknown";
        }
    }
}
