package com.lovely3x.jsonparser.classcreator;

import com.lovely3x.jsonparser.model.JSONObject;

import java.io.OutputStream;

/**
 * 将 JsonObject 转换为Java Class
 * Created by lovely3x on 15-7-2.
 */
public interface ClassCreator {
    /**
     * 讲 JsonObject 转换为Java Class
     *
     * @param className 类的名字
     * @param object
     * @return 写入输出结果的流
     */
    OutputStream createObject(JSONObject object, String className, OutputStream outputStream);


}
