package com.lovely3x.common.utils.searcher.textsearcher.searcherimpls;

import com.lovely3x.common.utils.CommonUtils;

/**
 * 包含搜索
 * Created by lovely3x on 16/6/8.
 */
public class ContainSearcher extends ReSearcher {
    @Override
    public void prepare(String keyWord, String[] conditions) {
        String key = String.format(".*?%s.*?", CommonUtils.escapeExprSpecialWord(keyWord));
        super.prepare(key, conditions);
    }
}
