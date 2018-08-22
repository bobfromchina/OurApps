package com.lovely3x.jsonparser.source;

import com.lovely3x.jsonparser.conversation.rule.JSONGenerateRule;
import com.lovely3x.jsonparser.conversation.utils.JavaObjectTOJSONUtilsPlain;

import java.util.Map;

/**
 * Created by lovely3x on 15-7-2.
 * map json源
 */
public class MapJSONSourcePlain implements JSONSource {

    private final Map mObjectsMap;
    private final int mStackLevel;
    private final JSONGenerateRule rule;
    private String mSource;


    public MapJSONSourcePlain(Map map, JSONGenerateRule rule, int stackLevel) {
        this.mObjectsMap = map;
        this.rule = rule;
        this.mStackLevel = stackLevel;
    }


    @Override
    public String input() {
        try {
            mSource = new JavaObjectTOJSONUtilsPlain(rule, mStackLevel).parseMap(mObjectsMap).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mSource;
    }


}
