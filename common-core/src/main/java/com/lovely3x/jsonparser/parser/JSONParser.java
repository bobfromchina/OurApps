package com.lovely3x.jsonparser.parser;

import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONValue;
import com.lovely3x.jsonparser.source.JSONSource;

import java.util.List;
import java.util.Map;

/**
 * Created by lovely3x on 15-6-29.
 * json解析器
 */
public interface JSONParser {
    /**
     * 解析json数组
     *
     * @return
     */
    List<JSONValue> parseJSONArray(JSONSource source);

    /**
     * 解析json对象
     *
     * @return
     */
    Map<JSONKey, JSONValue> parseJSONObject(JSONSource source);


}
