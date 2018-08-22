package com.lovely3x.common.utils.searcher.textsearcher.searcherimpls;

import com.lovely3x.common.utils.searcher.textsearcher.MatchResult;
import com.lovely3x.common.utils.searcher.textsearcher.Searcher;

/**
 * 结束字符串搜索器
 */
public class EndsSearcher implements Searcher {

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
            if (condition != null && condition.endsWith(mKeyWord)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MatchResult matchResult() {
        if (mConditions == null || mConditions.length == 0) return null;
        for (String condition : mConditions) {
            if (condition != null && condition.endsWith(mKeyWord)) {
                MatchResult result = new MatchResult();
                result.condition = condition;
                result.start = condition.length() - mKeyWord.length();
                result.end = condition.length();
            }
        }
        return null;
    }
}