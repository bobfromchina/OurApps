package com.lovely3x.jsonparser.source;

import com.lovely3x.jsonparser.conversation.rule.JSONGenerateRule;
import com.lovely3x.jsonparser.conversation.rule.UnderlineJSONGenerateKeyRule;
import com.lovely3x.jsonparser.conversation.utils.JavaObjectTOJSONUtilsPlain;

/**
 * Created by lovely3x on 15-7-2.
 * 对象json数据源, 讲对象数据看做json数据源
 */
public class ObjectJSONSourcePlain implements JSONSource {

    /**
     * 需要转换为json对象的java对象
     */
    private final Object mObject;

    private final JSONGenerateRule mRule;
    /**
     * 栈深度
     */
    private final int mStackLevel;

    /**
     * 转换之后的结果
     */
    private String mSource;

    /**
     * 使用默认的json生成规则
     *
     * @param object 需要是生成的对象
     */
    public ObjectJSONSourcePlain(Object object, int stackLevel) {
        this(object, new UnderlineJSONGenerateKeyRule(), stackLevel);
    }

    /**
     * 使用指定的对象和规则文件生成对象
     *
     * @param object 需要生成的对象
     * @param rule   转换规则
     */
    public ObjectJSONSourcePlain(Object object, JSONGenerateRule rule, int stackLevel) {
        this.mRule = rule;
        this.mObject = object;
        this.mStackLevel = stackLevel;
    }

    @Override
    public String input() {
        try {
            mSource = new JavaObjectTOJSONUtilsPlain(mRule, mStackLevel).parseObject(mObject).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSource;
    }
}
