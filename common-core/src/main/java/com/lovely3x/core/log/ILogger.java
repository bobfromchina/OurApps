package com.lovely3x.core.log;

/**
 * Created by lovely3x on 17/1/6.
 */

public abstract class ILogger {

    protected final String mTag;

    public ILogger(String tag) {
        this.mTag = tag;
    }

    public String getTag() {
        return mTag;
    }

    public abstract void e(String msg);

    public abstract void e(String formatMsg, Object... args);

    public abstract void e(Throwable throwable);

    public abstract void w(Throwable throwable);

    public abstract void w(String msg);

    public abstract void w(String formatMsg, Object... args);

    public abstract void i(String msg);

    public abstract void i(String formatMsg, Object... args);

    public abstract void d(String msg);

    public abstract void d(String formatMsg, Object... args);

}
