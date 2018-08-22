package com.lovely3x.jsonparser;

/**
 * Created by lovely3x on 15-6-29.
 */
public class JSONType {
    /**
     * json 类型 无效
     */
    public static final int JSON_TYPE_INVALIDATE = ~0x0;
    /**
     * json类型 数组
     */
    public static final int JSON_TYPE_ARRAY = 0x0;
    /**
     * json 类型 对象
     */
    public static final int JSON_TYPE_OBJECT = 0x1;
    /**
     * json的类型 int型,代表元素并不指代json格式
     */
    public static final int JSON_TYPE_INT = 0x2;
    /**
     * json的类型 强制int型,代表元素并不指代json格式
     */
    public static final int JSON_TYPE_CAST_INT = 0x3;
    /**
     * json的类型 单精度浮点型型,代表元素并不指代json格式
     */
    public static final int JSON_TYPE_FLOAT = 0x4;
    /**
     * json的类型 双精度浮点型型,代表元素并不指代json格式
     */
    public static final int JSON_TYPE_DOUBLE = 0x5;
    /**
     * json的类型 布尔型,代表元素并不指代json格式
     */
    public static final int JSON_TYPE_BOOLEAN = 0x6;
    /**
     * String的类型 代表元素并不指代json格式
     */
    public static final int JSON_TYPE_STRING = 0x7;

    /**
     * long 类型
     */
    public static final int JSON_TYPE_LONG = 0x8;

    /**
     * null 类型
     */
    public static final int JSON_TYPE_NULL = 0x9;


}
