package com.lovely3x.jsonparser.conversation.rule;

/**
 * Created by lovely3x on 15-7-2.
 * json value 规则实现类
 * 由于一般的人都不会对value 进行处理,所以直接继承这个类是实现对key的规则变化就行
 */
public abstract class JSONKeyGenerateRule implements JSONGenerateRule {

    @Override
    public Object valueRule(Object value) {
        return value;
    }
}
