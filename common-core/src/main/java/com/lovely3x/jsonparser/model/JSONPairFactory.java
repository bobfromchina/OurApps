package com.lovely3x.jsonparser.model;

import com.lovely3x.jsonparser.source.JSONSource;

/**
 * Created by lovely3x on 15-6-29.
 * json对 工厂
 */
public interface JSONPairFactory {

    /**
     * 工厂方法 获取json值
     *
     * @param value
     * @return
     */
    JSONValue getJSONValue(String value);

    /**
     * 工厂方法 获取json key
     *
     * @param key
     * @return
     */
    JSONKey getJSONKey(String key);


    /**
     * 工厂方法 获取json值
     *
     * @param value
     * @return
     */
    JSONValue getJSONValue(JSONSource value);

    /**
     * 工厂方法 获取json key
     *
     * @param key
     * @return
     */
    JSONKey getJSONKey(JSONSource key);


}
