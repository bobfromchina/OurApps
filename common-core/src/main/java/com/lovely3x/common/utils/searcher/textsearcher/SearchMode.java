package com.lovely3x.common.utils.searcher.textsearcher;

/**
 * 搜索模式
 */
public class SearchMode {
    /**
     * 以指定的搜索的字符串开始
     */
    public static final int STARTS = 1;
    /**
     * 以指定的搜索的字符串结束
     */
    public static final int ENDS = 2;

    /**
     * 包含指定的字符串
     */
    public static final int CONTAIN = 3;

    /**
     * 正则表达式匹配
     */
    public static final int RE = 4;
}
