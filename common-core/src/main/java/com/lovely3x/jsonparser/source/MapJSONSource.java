package com.lovely3x.jsonparser.source;

import com.lovely3x.jsonparser.conversation.rule.JSONGenerateRule;
import org.json2.JSONObject;

import java.util.Map;

/**
 * Created by lovely3x on 15-7-2.
 * map json源
 */
public class MapJSONSource implements JSONSource {

    private final Map mObjectsMap;
    private final JSONGenerateRule rule;
    private String mSource;

    public MapJSONSource(Map map, JSONGenerateRule rule) {
        this.mObjectsMap = map;
        this.rule = rule;
    }

    @Override
    public String input() {
        try {
            mSource = new JSONObject(mObjectsMap).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSource;
    }


}
