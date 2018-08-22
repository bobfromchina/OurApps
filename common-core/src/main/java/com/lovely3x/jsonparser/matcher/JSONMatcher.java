package com.lovely3x.jsonparser.matcher;

import com.lovely3x.jsonparser.model.JSONObject;
import com.lovely3x.jsonparser.model.ObjectCreatorConfig;

/**
 * Created by lovely3x on 15-6-29.
 * json转换为规则匹配器
 */
public interface JSONMatcher {
    /**
     * 通过这个方法 程序找到匹配规则
     * 你可以制定程序传递进来的这配置文件中的isEqual是否为true ,如果指定为true,
     * 你还需要指定 json 的 value 对应的类型 他将会和 类中的类型对应
     * 这里的配置文件你可以在,对象创建器里面拿到,所以,一切都是自由的
     *
     * @return 配置文件
     */
    ObjectCreatorConfig match(ObjectCreatorConfig newConfig);

    /**
     * 通过这个方法给这个配置中的字段通过 实例赋值
     *
     * @param instance   实例
     * @param jsonObject jsonObject 对象
     * @param config     配置文件
     */
    void putValue(Object instance, JSONObject jsonObject, ObjectCreatorConfig config) throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    /**
     * value 转换原则
     *
     * @param value 需要转换的对象
     * @return 转换后的对象
     */
    Object valueRule(Object value);


}
