package com.lovely3x.jsonparser.objectcreator;

import com.lovely3x.jsonparser.model.JSONObject;

/**
 * Created by lovely3x on 15-6-29.
 * 通过这个类可以将json创建为类对象
 */
public interface ObjectCreator<T> {
    /**
     * 通过这个方法你可以讲配置文件转换为一个指定的对象
     *
     * @param jsonObject 需要转换的json对象
     * @param t          需要转换的目标对象类
     * @return 创建的类对象
     */
    T create(JSONObject jsonObject, Class<T> t);


}
