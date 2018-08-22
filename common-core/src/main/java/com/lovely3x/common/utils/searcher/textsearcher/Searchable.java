package com.lovely3x.common.utils.searcher.textsearcher;

/**
 * 实现这个接口则表示可以被搜索
 */
public interface Searchable {

    /**
     * 能够提供的搜索条件列表
     *
     * @return 能够提供的搜索条件列表
     */
    String[] getCondition();
}
