package com.lovely3x.core.log;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lovely3x on 17/1/6.
 */

public class Logger {

    private static ConcurrentHashMap<WeakReference<Wrapper>, ILogger> mLoggerMap = new ConcurrentHashMap<>();

    public static final byte DEBUG = 5;
    public static final byte INFO = 4;
    public static final byte WARNING = 3;
    public static final byte ERROR = 2;

    public static byte level = DEBUG;

    private static Class<? extends ILogger> mImpl;

    public static void setLoggerImpl(Class<? extends ILogger> mImpl) {
        Logger.mImpl = mImpl;
    }

    public static Class<? extends ILogger> getLoggerImpl() {
        return mImpl;
    }

    private Logger() {
    }

    public static ILogger getLogger(@NonNull String tag) {
        if (mImpl == null) {
            mImpl = InnerLogger.class;
            return getLogger(tag);
        }

        try {
            for (WeakReference<Wrapper> next : mLoggerMap.keySet()) {
                final Wrapper wrapper = next.get();
                if (wrapper != null && wrapper.mThread.get() == Thread.currentThread() && tag.equals(wrapper.mTag)) {
                    ILogger logger = mLoggerMap.get(next);
                    if (logger != null) {
                        return logger;
                    }
                }
            }
            Constructor<? extends ILogger> constructor = mImpl.getConstructor(String.class);
            ILogger logger = constructor.newInstance(tag);
            mLoggerMap.put(new WeakReference<>(new Wrapper(Thread.currentThread(), tag)), logger);
            return logger;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ILogger getLogger(@NonNull Class clazz) {
        return getLogger(clazz.getName());
    }

    public static ILogger getLogger(Object object) {
        return getLogger((object == null || object.getClass() == null) ? "NULL" : object.getClass().getName());
    }

    private static class InnerLogger extends ILogger {

        public InnerLogger(String tag) {
            super(tag);
        }

        public void e(String msg) {
            e(msg, new Object[0]);
        }

        public void e(String formatMsg, Object... args) {
            if (level >= ERROR) {
                Log.e(mTag, String.format(Locale.US, formatMsg, args));
            }
        }

        public void e(Throwable throwable) {
            if (level >= ERROR) {
                Log.e(mTag, "Error", throwable);
            }
        }

        @Override
        public void w(Throwable throwable) {
            if (level >= WARNING) {
                Log.w(mTag, throwable);
            }
        }

        @Override
        public void w(String msg) {
            w(msg, new Object[0]);
        }

        @Override
        public void w(String formatMsg, Object... args) {
            if (level >= WARNING) {
                Log.w(mTag, String.format(Locale.US, formatMsg, args));
            }
        }

        public void i(String msg) {
            i(msg, new Object[0]);
        }

        public void i(String formatMsg, Object... args) {
            if (level >= INFO) {
                Log.i(mTag, String.format(Locale.US, formatMsg, args));
            }
        }

        public void d(String msg) {
            d(msg, new Object[0]);
        }

        public void d(String formatMsg, Object... args) {
            if (level >= DEBUG) {
                Log.d(mTag, String.format(Locale.US, formatMsg, args));
            }
        }

    }

    private static class Wrapper {
        private WeakReference<Thread> mThread;
        private String mTag;

        private Wrapper(Thread thread, String tag) {
            mThread = new WeakReference<>(thread);
            mTag = tag;
        }
    }
}
