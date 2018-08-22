package com.lovely3x.jsonparser.matcher;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.JavaType;
import com.lovely3x.jsonparser.TypeTable;
import com.lovely3x.jsonparser.annotations.JSON;
import com.lovely3x.jsonparser.annotations.JSONArray;
import com.lovely3x.jsonparser.annotations.JSONObject;
import com.lovely3x.jsonparser.model.ObjectCreatorConfig;
import com.lovely3x.jsonparser.utils.CommonUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 注解匹配器,通过注解的方式来判断一个json的key是否和这个字段名匹配
 * Created by lovely3x on 15-7-11.
 */
public class AnnotationMatcher implements JSONMatcher {

    private static final String TAG = "AnnotationMather";
    private final Config mConfig;

    public AnnotationMatcher(Config config) {
        this.mConfig = config;
    }

    @Override
    public ObjectCreatorConfig match(ObjectCreatorConfig newConfig) {
        JSON jsonField = newConfig.field.getAnnotation(JSON.class);
        if (jsonField != null) {
            return configWithBaseType(jsonField, newConfig);
        }
        JSONArray jsonArrayField = newConfig.field.getAnnotation(JSONArray.class);
        if (jsonArrayField != null) {
            return configWithJSONArrayType(jsonArrayField, newConfig);
        }
        JSONObject jsonObjectField = newConfig.field.getAnnotation(JSONObject.class);
        if (jsonObjectField != null) {
            return configWithJSONObjectType(jsonObjectField, newConfig);
        }
        return newConfig;
    }

    @Override
    public void putValue(Object instance, com.lovely3x.jsonparser.model.JSONObject jsonObject,
                         ObjectCreatorConfig objectConfig) throws IllegalAccessException,
            ClassNotFoundException,
            InstantiationException {


        Field field = objectConfig.field;
        switch (objectConfig.jsonValueType) {
            case JSONType.JSON_TYPE_BOOLEAN:
                field.setBoolean(instance, (Boolean) mConfig.matcher.valueRule(jsonObject.getBoolean(objectConfig.jsonKey)));
                break;
            case JSONType.JSON_TYPE_INT:
                field.setInt(instance, (Integer) mConfig.matcher.valueRule(jsonObject.getInt(objectConfig.jsonKey)));
                break;
            case JSONType.JSON_TYPE_LONG:
                field.setLong(instance, (Long) mConfig.matcher.valueRule(jsonObject.getLong(objectConfig.jsonKey)));
                break;
            case JSONType.JSON_TYPE_FLOAT:
                field.setFloat(instance, (Float) mConfig.matcher.valueRule(jsonObject.getFloat(objectConfig.jsonKey)));
                break;
            case JSONType.JSON_TYPE_DOUBLE:
                field.setDouble(instance, (Double) mConfig.matcher.valueRule(jsonObject.getDouble(objectConfig.jsonKey)));
                break;
            case JSONType.JSON_TYPE_STRING:
                field.set(instance, mConfig.matcher.valueRule(jsonObject.getString(objectConfig.jsonKey)));
                break;
            case JSONType.JSON_TYPE_OBJECT:
                Object subObject = createObject(objectConfig.fieldValueType);
                if (subObject != null) {
                    field.set(instance, mConfig.matcher.valueRule(
                            jsonObject.getJSONObject(objectConfig.jsonKey).
                                    createObject(subObject.getClass())));
                }
                break;
            case JSONType.JSON_TYPE_ARRAY:
                //创建容器对象
                if (!JavaType.JAVA_TYPE_NULL.equals(objectConfig.fieldValueType)) {
                    Object container = createObject(objectConfig.fieldValueType);
                    if (!JavaType.JAVA_TYPE_NULL.equals(objectConfig.subFieldValueType)) {
                        Class subClass = Class.forName(objectConfig.subFieldValueType);
                        if (container != null) {
                            field.set(instance, mConfig.matcher.valueRule(jsonObject.getJSONArray(objectConfig.jsonKey).createObjects((Class<? extends List>) container.getClass(), subClass)));
                        }
                    }
                }
                break;
        }
    }

    /**
     * 根据给定类名创建对象
     *
     * @param name 需要创建对象的类名
     * @return null 或者 创建的对象
     */
    private Object createObject(String name) {
        try {
            return Class.forName(name).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 配置JSON Object属性
     *
     * @param jsonObjectField 指定的json对象注解字段
     * @param newConfig       指定的配置文件
     * @return 配置文件
     */
    private ObjectCreatorConfig configWithJSONObjectType(JSONObject jsonObjectField, ObjectCreatorConfig newConfig) {
        if (jsonObjectField == null) {
            newConfig.isEqual = false;
            return newConfig;
        }
        if (newConfig.jsonKey != null && newConfig.jsonKey.getKey() != null) {
            if (newConfig.jsonKey.getKey().equals(jsonObjectField.value())) {
                newConfig.isEqual = true;
                newConfig.jsonValueType = JSONType.JSON_TYPE_OBJECT;
                newConfig.fieldValueType = jsonObjectField.object().getName();
            }
        } else {
            newConfig.isEqual = false;
        }
        return newConfig;
    }

    /**
     * 配置jsonArray属性
     *
     * @param jsonArrayField 指定的jsonArray注解字段
     * @param newConfig      指定的配置文件
     * @return 配置文件
     */
    private ObjectCreatorConfig configWithJSONArrayType(JSONArray jsonArrayField, ObjectCreatorConfig newConfig) {
        if (jsonArrayField == null) {
            newConfig.isEqual = false;
            return newConfig;
        }
        if (newConfig.jsonKey != null && newConfig.jsonKey.getKey() != null) {
            if (newConfig.jsonKey.getKey().equals(jsonArrayField.value()) && !jsonArrayField.value().equals(JSONArray.NULL)) {
                newConfig.isEqual = true;
                newConfig.jsonValueType = JSONType.JSON_TYPE_ARRAY;
                newConfig.fieldValueType = jsonArrayField.container().getName();
                //没有指定容器的泛型
                if (jsonArrayField.object() == JSONArray.Non.class) {
                    //获取泛型
                    newConfig.subFieldValueType = CommonUtils.getFieldGenericType(newConfig.field, 0).getName();
                } else {
                    newConfig.subFieldValueType = jsonArrayField.object().getName();
                }
            }
        } else {
            newConfig.isEqual = false;
        }
        return newConfig;
    }

    /**
     * 配置基础属性
     *
     * @param jsonField 指定的json注解字段
     * @param newConfig 指定的配置文件
     * @return 配置文件
     */
    private ObjectCreatorConfig configWithBaseType(JSON jsonField, ObjectCreatorConfig newConfig) {
        if (jsonField == null) {
            newConfig.isEqual = false;
            return newConfig;
        }
        if (newConfig.jsonKey != null && newConfig.jsonKey.getKey() != null) {
            if (newConfig.jsonKey.getKey().equals(jsonField.value())) {
                newConfig.isEqual = true;
                newConfig.jsonValueType = TypeTable.getJSONTypeByJavaType(newConfig.field.getType().getName());
            }
        } else {
            newConfig.isEqual = false;
        }
        return newConfig;
    }

    @Override
    public Object valueRule(Object value) {
        return value;
    }
}
