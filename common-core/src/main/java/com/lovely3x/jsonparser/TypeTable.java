package com.lovely3x.jsonparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lovely3x on 15-7-1.
 */
public class TypeTable {
    private static final HashMap<String, Integer> typeTable = new HashMap<String, Integer>();

    static {
        typeTable.put("byte", JSONType.JSON_TYPE_INT);
        typeTable.put("short", JSONType.JSON_TYPE_INT);
        typeTable.put("char", JSONType.JSON_TYPE_INT);
        typeTable.put("int", JSONType.JSON_TYPE_INT);
        typeTable.put("long", JSONType.JSON_TYPE_LONG);
        typeTable.put("float", JSONType.JSON_TYPE_FLOAT);
        typeTable.put("double", JSONType.JSON_TYPE_DOUBLE);
        typeTable.put("boolean", JSONType.JSON_TYPE_BOOLEAN);

        typeTable.put(String.class.getName(), JSONType.JSON_TYPE_STRING);
        typeTable.put(List.class.getName(), JSONType.JSON_TYPE_ARRAY);
        typeTable.put(LinkedList.class.getName(), JSONType.JSON_TYPE_ARRAY);
        typeTable.put(ArrayList.class.getName(), JSONType.JSON_TYPE_ARRAY);
    }

    /**
     * 更具java的类型 获取对应的json类型
     *
     * @param type 需要获取的java类型
     * @return 对应的json类型
     */
    public static int getJSONTypeByJavaType(String type) {
        Integer jsonType = typeTable.get(type);
        return jsonType == null ? JSONType.JSON_TYPE_INVALIDATE : jsonType;
    }


}
