package com.lovely3x.jsonparser.model;

/**
 * json对 的值
 * Created by lovely3x on 15-6-29.
 */
public interface JSONValue {
    /**
     * 获取value
     *
     * @return
     */
    String getValue();

    /**
     * 设置value
     *
     * @param str
     */
    void setValue(String str);


    /**
     * 将当前的value以 JSONArray的形式返回
     *
     * @return
     */
    JSONArray getJSONArray();

    /**
     * 将当前的value以 JSONObject
     *
     * @return
     */
    JSONObject getJSONObject();

    /**
     * 将当前的value以 String的形式返回
     *
     * @return
     */
    String getString();

    /**
     * 讲当前的值转换为int型
     *
     * @return
     */
    int getInt();

    /**
     * 获取强制类型转换int,可以将一部分无法转换的类型强制转换为int类型的数值
     *
     * @return
     */
    int getCastInt();


    /**
     * 将当前的值转换为双精度浮点型
     *
     * @return
     */
    double getDouble();

    /**
     * 将那个当前的这个值转换为单精度浮点型
     *
     * @return
     */
    float getFloat();

    /**
     * 就当前的这个值转换为boolean类型
     *
     * @return
     */
    boolean getBoolean();

    /**
     * 获取long型
     *
     * @return
     */
    long getLong();

    /**
     * 尝试猜测这个value是什么类型的
     *
     * @return
     */
    int guessType();


}
