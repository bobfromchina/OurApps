package com.lovely3x.jsonparser.model;

import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.JavaType;

import java.lang.reflect.Field;

/**
 * Created by lovely3x on 15-7-1.
 * 对象创建器的配置文件
 */
public class ObjectCreatorConfig {

    /**
     * json对 的 key 他将和 类 的 field 对应(如果指定isEqual为true)
     */
    public JSONKey jsonKey;
    /**
     * 类的字段 他将和 json对 的key对应(如果指定isEqual为true)
     */
    public Field field;

    /**
     * 这个json的key对应的值的 类型
     */
    public int jsonValueType;

    /**
     * 这个类 字段对应的值的类型
     */
    public String fieldValueType;

    /**
     * 容器子字段类型
     * 比如 创建一个json Array 这个字段就是这个list的泛型的类型
     */
    public String subFieldValueType;


    /**
     * 这个json key 是否和 fieldName 匹配
     */
    public boolean isEqual;

    /**
     * 将这些值设置为默认
     */
    public void reset() {
        field = null;
        jsonKey = null;
        jsonValueType = JSONType.JSON_TYPE_INVALIDATE;
        fieldValueType = JavaType.JAVA_TYPE_NULL;
        isEqual = false;
    }

    @Override
    public String toString() {
        return "ObjectCreatorConfig{" +
                "jsonKey=" + jsonKey +
                ", field=" + field +
                ", jsonValueType=" + jsonValueType +
                ", fieldValueType='" + fieldValueType + '\'' +
                ", subFieldValueType='" + subFieldValueType + '\'' +
                ", isEqual=" + isEqual +
                '}';
    }
}
