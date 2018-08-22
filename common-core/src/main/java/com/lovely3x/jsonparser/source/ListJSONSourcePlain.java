package com.lovely3x.jsonparser.source;

import com.lovely3x.jsonparser.conversation.rule.JSONGenerateRule;
import com.lovely3x.jsonparser.conversation.rule.UnderlineJSONGenerateKeyRule;
import com.lovely3x.jsonparser.conversation.utils.JavaObjectTOJSONUtilsPlain;

import java.util.List;

/**
 * Created by lovely3x on 15-7-2.
 * 列表json数组 数据源
 */
public class ListJSONSourcePlain implements JSONSource {

    /**
     * 需要解析的对象
     */
    private final List mObjects;
    /**
     * 栈深度
     */
    private final int mStackLevel;
    /**
     * 生成规则
     */
    private final JSONGenerateRule mRule;
    /**
     * 解析结果
     */
    private String mSource;

    /**
     * 通过 list 生成 jsonArray
     * 解析是不会产生对象依赖的,就是指如果对象内包含了一个array 这种是不会生成array的
     * 举个例子:
     * List list = new List();
     * List list2 = new List();
     * list.add(person);
     * list2.add(person);
     * list.add(list2);
     * <p/>
     * 这种是可以构建出一个Array中包含一个Object和一个Array的
     * 但是如果是下面这种可能是无法达到预计的期望的
     * List list = new List();
     * Person p = new Person();
     * p.friends = new ArrayList();
     * list.add(p);
     * 你可能会认为 应该是一个Array包含一个对象,而这个对象内包含一个Array,
     * 但是,事实上对象内的Array是不会被创建的
     *
     * @param objects 需要生成的对象
     * @param rule    对象生成规则
     */
    public ListJSONSourcePlain(List objects, JSONGenerateRule rule, int stackLevel) {
        this.mObjects = objects;
        this.mRule = rule;
        this.mStackLevel = stackLevel;
    }

    public ListJSONSourcePlain(List objects, int stackLevel) {
        this(objects, new UnderlineJSONGenerateKeyRule(), stackLevel);
    }

    @Override
    public String input() {
        try {
            this.mSource = new JavaObjectTOJSONUtilsPlain(mRule, mStackLevel).parseList(mObjects).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return mSource;
    }
}
