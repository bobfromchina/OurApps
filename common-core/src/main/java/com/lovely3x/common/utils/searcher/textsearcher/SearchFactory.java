package com.lovely3x.common.utils.searcher.textsearcher;

/**
 * 搜索器工厂
 */
public interface SearchFactory {
    /**
     * 根据搜索模式获取一个搜索器
     *
     * @param searchMode
     * @return
     */
    Searcher obtainSearcher(int searchMode);

}