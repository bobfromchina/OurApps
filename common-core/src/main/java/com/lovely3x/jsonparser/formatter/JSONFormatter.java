package com.lovely3x.jsonparser.formatter;

import com.lovely3x.jsonparser.model.JSONArray;
import com.lovely3x.jsonparser.model.JSONObject;

/**
 * Created by lovely3x on 15-7-2.
 * json格式化
 */
public interface JSONFormatter {

    /**
     * 格式化jsonObject
     *
     * @param object
     * @return 格式化好字符串
     */
    CharSequence formatJSONObject(JSONObject object);

    /**
     * 格式化jsonarray
     *
     * @param array 需要格式化的array
     * @return 格式化好字符串
     */
    CharSequence formatJSONArray(JSONArray array);


}
