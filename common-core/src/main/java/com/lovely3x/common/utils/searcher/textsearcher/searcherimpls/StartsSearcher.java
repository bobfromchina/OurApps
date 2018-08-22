package com.lovely3x.common.utils.searcher.textsearcher.searcherimpls;

import com.lovely3x.common.utils.searcher.textsearcher.MatchResult;
import com.lovely3x.common.utils.searcher.textsearcher.Searcher;

/**
 * 开始字符串搜索器
 */
public class StartsSearcher implements Searcher {

    private String[] mConditions;
    private String mKeyWord;

    @Override
    public void prepare(String keyWord, String[] conditions) {
        this.mKeyWord = keyWord;
        this.mConditions = conditions;
    }

    @Override
    public boolean match() {
        if (mConditions == null || mConditions.length == 0) return false;
        for (String condition : mConditions) {
            if (condition != null && condition.startsWith(mKeyWord)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MatchResult matchResult() {
        if (mConditions == null || mConditions.length == 0) return null;
        for (String condition : mConditions) {
            if (condition != null && condition.startsWith(mKeyWord)) {
                MatchResult result = new MatchResult();
                result.condition = condition;
                result.start = 0;
                result.end = mKeyWord.length();
            }
        }
        return null;
    }
}