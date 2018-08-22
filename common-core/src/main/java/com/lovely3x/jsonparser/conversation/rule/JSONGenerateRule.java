package com.lovely3x.jsonparser.conversation.rule;

/**
 * Created by lovely3x on 15-7-2.
 * 由对象 转换为 json 使用的规则
 */
public interface JSONGenerateRule {

    /**
     * 转换规则 你可以通过这个方法指定字段名到json字段名的规则
     *
     * @param fieldName 字段名
     * @return json名
     */
    String keyRule(String fieldName);

    /**
     * value转换原则
     *
     * @param value
     * @return
     */
    Object valueRule(Object value);

}
