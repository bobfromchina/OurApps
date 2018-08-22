package com.lovely3x.jsonparser.matcher;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.TypeTable;
import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONObject;
import com.lovely3x.jsonparser.model.ObjectCreatorConfig;
import com.lovely3x.jsonparser.utils.CommonUtils;

import java.lang.reflect.Field;

/**
 * 下划线匹配器,常规数据库命名法则是 单词小写 多个单词间使用 '_' 分开 这个就适用于这种情况,
 * 当然,如果是完全相同也是可以的
 * Created by lovely3x on 15-7-1.
 */
public class UnderlineMatcher implements JSONMatcher {

    private static final String TAG = "UnderlineMatcher";

    private final Config mConfig;

    public UnderlineMatcher(Config config) {
        this.mConfig = config;
    }

    @Override
    public ObjectCreatorConfig match(ObjectCreatorConfig newConfig) {
        final JSONKey jsonKey = newConfig.jsonKey;
        final Field field = newConfig.field;
        newConfig.isEqual = isEqual(field.getName(), jsonKey.getKey());
        int type = newConfig.jsonValueType = TypeTable.getJSONTypeByJavaType(field.getType().getName());
        if (type == -1) {
            newConfig.jsonValueType = JSONType.JSON_TYPE_OBJECT;
        }
        return newConfig;
    }

    /**
     * 判断给定的这个字段名和json的字段名是否相同
     *
     * @param fieldName   字段名
     * @param jsonKyeName json 的key名
     * @return
     */
    private boolean isEqual(String fieldName, String jsonKyeName) {
        return CommonUtils.isEqual(fieldName, jsonKyeName);
    }

    @Override
    public void putValue(Object instance, JSONObject jsonObject,
                         ObjectCreatorConfig config) throws IllegalAccessException, InstantiationException {
        Field field = config.field;
        switch (config.jsonValueType) {
            case JSONType.JSON_TYPE_BOOLEAN:
                field.setBoolean(instance, (Boolean) mConfig.matcher.valueRule(jsonObject.getBoolean(config.jsonKey)));
                break;
            case JSONType.JSON_TYPE_INT:
                field.setInt(instance, (Integer) mConfig.matcher.valueRule(jsonObject.getInt(config.jsonKey)));
                break;
            case JSONType.JSON_TYPE_LONG:
                field.setLong(instance, (Long) mConfig.matcher.valueRule(jsonObject.getLong(config.jsonKey)));
                break;
            case JSONType.JSON_TYPE_FLOAT:
                field.setFloat(instance, (Float) mConfig.matcher.valueRule(jsonObject.getFloat(config.jsonKey)));
                break;
            case JSONType.JSON_TYPE_DOUBLE:
                field.setDouble(instance, (Double) mConfig.matcher.valueRule(jsonObject.getDouble(config.jsonKey)));
                break;
            case JSONType.JSON_TYPE_STRING:
                field.set(instance, mConfig.matcher.valueRule(jsonObject.getString(config.jsonKey)));
                break;
            case JSONType.JSON_TYPE_OBJECT: {
                Class<?> subClass = field.getType();
                if (subClass != null) {
                    field.set(instance, mConfig.matcher.valueRule(
                            jsonObject.getJSONObject(config.jsonKey).
                                    createObject(subClass)));
                }
            }
            break;
            case JSONType.JSON_TYPE_ARRAY: {
                Class subClass = CommonUtils.getFieldGenericType(field, 0);
                field.set(instance, mConfig.matcher.valueRule(
                        jsonObject.getJSONArray(config.jsonKey)
                                .createObjects(mConfig.defaultJSONArrayContainer, subClass)));
            }
            break;
        }
    }

    @Override
    public Object valueRule(Object value) {
        return value;
    }
}
