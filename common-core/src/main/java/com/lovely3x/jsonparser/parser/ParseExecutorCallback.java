package com.lovely3x.jsonparser.parser;

/**
 * Created by lovely3x on 15-6-30.
 * 解析回调
 */
public interface ParseExecutorCallback {
    /**
     * 当解析出一条后执行
     *
     * @param currentStatement
     */
    void onParsed(String currentStatement);
}
