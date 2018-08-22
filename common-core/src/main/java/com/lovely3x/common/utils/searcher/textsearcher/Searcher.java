package com.lovely3x.common.utils.searcher.textsearcher;

/**
 * 搜索器
 */
public interface Searcher {

    /**
     * 准备匹配
     *
     * @param keyWord    需要搜索的关键字
     * @param conditions 需要搜索的条件
     */
    void prepare(String keyWord, String[] conditions);

    /**
     * 匹配
     *
     * @return 匹配结果 true or false 表示是否条件和关键字匹配
     */
    boolean match();

    /**
     * 匹配
     *
     * @return 返回匹配的结果或null, 如果不匹配
     */
    MatchResult matchResult();
}

   