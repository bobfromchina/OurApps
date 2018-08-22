package com.lovely3x.common.utils.searcher.textsearcher.searcherimpls;

import com.lovely3x.common.utils.CommonUtils;
import com.lovely3x.common.utils.searcher.textsearcher.MatchResult;
import com.lovely3x.common.utils.searcher.textsearcher.Searcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * Created by lovely3x on 16/6/8.
 */
public class ReSearcher implements Searcher {

    private Pattern mPattern;
    private String[] mConditions;

    @Override
    public void prepare(String keyWord, String[] conditions) {
        this.mPattern = Pattern.compile(keyWord);
        this.mConditions = conditions;
    }

    @Override
    public boolean match() {
        if (mConditions == null || mConditions.length == 0) return false;
        for (String condition : mConditions) {
            Matcher matcher = mPattern.matcher(condition);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MatchResult matchResult() {
        if (mConditions == null || mConditions.length == 0) return null;
        for (String condition : mConditions) {
            Matcher matcher = mPattern.matcher(condition);
            if (matcher.find()) {
                MatchResult matchResult = new MatchResult();
                matchResult.condition = condition;
                matchResult.start = matcher.start();
                matchResult.end = matcher.end();
                return matchResult;
            }
        }
        return null;
    }
}
