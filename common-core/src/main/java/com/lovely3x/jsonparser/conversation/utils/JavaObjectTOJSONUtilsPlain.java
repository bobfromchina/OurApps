package com.lovely3x.jsonparser.conversation.utils;

import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.TypeTable;
import com.lovely3x.jsonparser.annotations.JSON;
import com.lovely3x.jsonparser.annotations.JSONArray;
import com.lovely3x.jsonparser.annotations.JSONObject;
import com.lovely3x.jsonparser.conversation.rule.JSONGenerateRule;
import com.lovely3x.jsonparser.utils.CommonUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java 对象转换为json的工具类
 * 普通模式 不会对引用再次解析
 * Created by lovely3x on 15-7-2.
 */
public class JavaObjectTOJSONUtilsPlain {

    /**
     * 转换规则
     */
    private final JSONGenerateRule mRule;
    /**
     * 栈深度
     */
    private final int stackLevel;

    private int objectStackLevel;

    private int mapStackLevel;

    private int listStackLevel;


    /**
     * @param rule 转换规则
     */
    public JavaObjectTOJSONUtilsPlain(JSONGenerateRule rule, int stackLevel) {
        this.mRule = rule;
        this.stackLevel = stackLevel;
        objectStackLevel = stackLevel;
        mapStackLevel = stackLevel;
        listStackLevel = stackLevel;
    }

    /**
     * 将 list 数据转换为 json 数据
     */
    public StringBuilder parseList(List objects) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        if (listStackLevel < 0) return sb;
        sb.append('[');
        final int count = objects == null ? 0 : objects.size();
        for (int i = 0; i < count; i++) {
            Object object = objects.get(i);
            if (object instanceof List) {
                listStackLevel--;
                StringBuilder sSb = parseList((List) object);
                sb.append(i == 0 ? "" : ",").append(sSb);
            } else if (object instanceof Map) {
                mapStackLevel--;
                sb.append(i == 0 ? "" : ",").append(parseMap((Map) object));
            } else if (object instanceof Integer) {//int
                sb.append(i == 0 ? "" : ",").append(object);
            } else if (object instanceof Long) {//long
                sb.append(i == 0 ? "" : ",").append(object);
            } else if (object instanceof Float) {//float
                sb.append(i == 0 ? "" : ",").append(object);
            } else if (object instanceof Double) {//double
                sb.append(i == 0 ? "" : ",").append(object);
            } else if (object instanceof Boolean) {//boolean
                sb.append(i == 0 ? "" : ",").append(object);
            } else if (object instanceof String) {//String
                sb.append(i == 0 ? "" : ",").append('"').append(processString(String.valueOf(object))).append('"');
            } else {
                objectStackLevel--;
                sb.append(i == 0 ? "" : ",").append(parseObject(object));
            }
        }
        sb.append(']');
        return sb;
    }

    /**
     * 在字段的注解中查找JSON 的 key
     *
     * @param field 需要查找的字段
     * @return null或查找的注解Value
     */
    public String searchKeyInAnnotations(Field field) {
        JSON jsonAnnotation = field.getAnnotation(JSON.class);
        if (jsonAnnotation != null) return jsonAnnotation.value();
        JSONObject jsonObjectAnnotation = field.getAnnotation(JSONObject.class);
        if (jsonObjectAnnotation != null) return jsonObjectAnnotation.value();
        JSONArray jsonArrayAnnotation = field.getAnnotation(JSONArray.class);
        if (jsonArrayAnnotation != null) return jsonArrayAnnotation.value();
        return null;
    }

    /**
     * 将对象转换为json字符串
     */
    public StringBuilder parseObject(Object object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        if (objectStackLevel < 0) return sb;
        Field[] fields = object == null ? new Field[0] : object.getClass().getDeclaredFields();
        final int count = fields.length;
        if (count > 0) sb.append('{');
        for (int i = 0; i < count; i++) {
            Field field = fields[i];
            String annotationKey = null;
            boolean useKeyRule = false;
            JSON jsonAnnotation = field.getAnnotation(JSON.class);
            if (jsonAnnotation != null) {
                if (jsonAnnotation.useAnnotation()) {
                    annotationKey = jsonAnnotation.value();
                    useKeyRule = jsonAnnotation.useKeyRuleOnUseAnnotation();
                }
            } else {
                JSONObject jsonObjectAnnotation = field.getAnnotation(JSONObject.class);
                if (jsonObjectAnnotation != null) {
                    if (jsonObjectAnnotation.useAnnotation()) {
                        annotationKey = jsonObjectAnnotation.value();
                        useKeyRule = jsonObjectAnnotation.useKeyRuleOnUseAnnotation();
                    }
                } else {
                    JSONArray jsonArrayAnnotation = field.getAnnotation(JSONArray.class);
                    if (jsonArrayAnnotation != null) {
                        if (jsonArrayAnnotation.useAnnotation()) {
                            annotationKey = jsonArrayAnnotation.value();
                            useKeyRule = jsonArrayAnnotation.useKeyRuleOnUseAnnotation();
                        }
                    }
                }
            }

            String key = field.getName();
            if (annotationKey != null) {
                key = annotationKey;
            } else {
                //字段总是使用转换规则
                useKeyRule = true;
            }

            field.setAccessible(true);
            if (!CommonUtils.fieldCanBeModify(field)) continue;
            int type = TypeTable.getJSONTypeByJavaType(field.getType().getName());
            switch (type) {
                case JSONType.JSON_TYPE_BOOLEAN:
                    sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                    sb.append(':').append(field.getBoolean(object)).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_INT:
                    sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                    sb.append(':').append(field.getInt(object)).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_LONG:
                    sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                    sb.append(':').append(field.getLong(object)).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_FLOAT:
                    sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                    sb.append(':').append(field.getFloat(object)).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_DOUBLE:
                    sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                    sb.append(':').append(field.getDouble(object)).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_STRING:
                    sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                    sb.append(':').append('"').append(processString(String.valueOf(field.get(object)))).append('"').append(i + 1 == count ? "" : ",");
                    break;
                default:
                    Object fieldInstance = field.get(object);
                    if (fieldInstance == null) {
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append("null").append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Map) {//map
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        mapStackLevel--;
                        sb.append(':').append(parseMap((Map) fieldInstance)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof List) {//list
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        this.listStackLevel--;
                        sb.append(':').append(parseList((List) fieldInstance)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Boolean) {//boolean
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Byte) {//byte
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Short) {//short
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Character) {//char
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Integer) {//int
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Long) {//long
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Float) {//float
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else if (fieldInstance instanceof Double) {//double
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        sb.append(':').append(field.get(object)).append(i + 1 == count ? "" : ",");
                    } else {
                        sb.append('"').append(useKeyRule ? mRule.keyRule(key/*field.getName()*/) : key).append('"');
                        objectStackLevel--;
                        sb.append(':').append(parseObject(fieldInstance)).append(i + 1 == count ? "" : ",");
                    }
                    break;
            }
        }
        //虽然在数据解析途中判断了,但是也很可能无效,因为解析途中根据的是字段的数量判断的
        //如果发生了字段无效,那么则判断也就无效了

        if (count > 0) {
            if (sb.charAt(sb.length() - 1) == ',') sb.deleteCharAt(sb.length() - 1);
            sb.append('}');
        }
        return sb;
    }


    /**
     * 将 map 转换为json字符串
     * <p/>
     * {key=list,key=object,key=map}
     *
     * @return 转换后的结果
     */
    public StringBuilder parseMap(Map map) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        if (mapStackLevel < 0) return sb;
        ArrayList keys = map == null ? new ArrayList() : new ArrayList(map.keySet());
        final int count = keys.size();
        sb.append('{');
        for (int i = 0; i < count; i++) {
            Object key = keys.get(i);
            Object value = map.get(key);
            if (value instanceof Map) {
                mapStackLevel--;
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(parseMap((HashMap) value));
            } else if (value instanceof List) {
                this.listStackLevel--;
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(parseList((List) value));
            } else if (value instanceof Integer) {//int
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(value);
            } else if (value instanceof Long) {//long
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(value);
            } else if (value instanceof Float) {//float
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(value);
            } else if (value instanceof Double) {//double
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(value);
            } else if (value instanceof Boolean) {//boolean
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(value);
            } else if (value instanceof String) {//String
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append('"').append(processString(String.valueOf(value))).append('"');
            } else {
                objectStackLevel--;
                sb.append(i == 0 ? "" : ",").append('"').append(key).append('"').append(':').append(parseObject(value));
            }
        }
        sb.append('}');
        return sb;
    }

    /**
     * 增加了对转义字符 \r\n\t 的支持
     * 处理转义字符串
     *
     * @param string 需要处理的字符串
     * @return 处理完成的字符串
     */
    private String processString(String string) {
        return CommonUtils.replaceSpaceCharToVisibleChar(string).replaceAll("\"", "\\\\\"");
    }
}
