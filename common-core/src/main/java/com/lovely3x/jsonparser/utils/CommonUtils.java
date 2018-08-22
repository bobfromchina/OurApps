package com.lovely3x.jsonparser.utils;

import com.lovely3x.jsonparser.JSONType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by lovely3x on 15-8-8.
 * 放一些工具方法进来
 */
public class CommonUtils {
    /**
     * 匹配 关键字 下划线
     */
    public static final char UNDERLINE = '_';

    /**
     * 猜测一个字符串的类型
     *
     * @param value 需要猜测的字符串
     * @return 猜测的类型 {@link JSONType}
     */
    public static int guessType(String value) {

        int type = JSONType.JSON_TYPE_INVALIDATE;
        //无效的值
        if (value == null || value.trim().length() == 0) {
            return type;
        }
        value = value.trim();
        if (value.startsWith("[") && value.endsWith("]")) {//json array
            type = JSONType.JSON_TYPE_ARRAY;
        } else if (value.startsWith("{") && value.endsWith("}")) {//json object
            type = JSONType.JSON_TYPE_OBJECT;
        } else if (value.matches("^[\\+|\\-]?\\d+\\.\\d+([e|E][\\+|\\-]?\\d+)?$")) {//浮点数匹配
            if (Double.parseDouble(value) > Float.MAX_VALUE / 2) {
                type = JSONType.JSON_TYPE_DOUBLE;//double float
            } else {
                type = JSONType.JSON_TYPE_FLOAT;//single float
            }
        } else if (value.matches("^[\\+|\\-]?\\d+$")) {//整形匹配
            if (Long.parseLong(value) > Integer.MAX_VALUE) {
                type = JSONType.JSON_TYPE_LONG;//long int
            } else {
                type = JSONType.JSON_TYPE_INT;// int
            }
        } else if (value.matches("^(true|false)$")) {
            type = JSONType.JSON_TYPE_BOOLEAN;
        } else if (value.matches("^null$")) {
            type = JSONType.JSON_TYPE_NULL;//null
        } else {//string
            type = JSONType.JSON_TYPE_STRING;
        }
        return type;
    }


    /**
     * 将tab键替换为 \t
     *
     * @param string 需要替换的字符串
     * @return 替换完成的字符串
     */
    public static String replaceTab(String string) {
        return string.replaceAll("\\t", "\\\\t");
    }

    /**
     * 将回车键替换为 \r
     *
     * @param string 需要替换的字符串
     * @return 替换完成的字符串
     */
    public static String replaceEnter(String string) {
        return string.replaceAll("\\r", "\\\\r");
    }

    /**
     * 将换行键替换为 \n
     *
     * @param string 需要替换的字符串
     * @return 替换完成的字符串
     */
    public static String replaceNewLine(String string) {
        return string.replaceAll("\\n", "\\\\n");
    }

    /**
     * 替换特殊的空白字符为可见字符
     *
     * @param string 需要替换的字符串
     * @return 替换完成的字符串
     */
    public static String replaceSpaceCharToVisibleChar(String string) {
        return replaceNewLine(replaceEnter(replaceTab(string)));
    }

    /**
     * 替换可见的特殊字符标记为不可见的空白字符
     *
     * @param string 需要替换的字符
     * @return 替换完成的字符
     */
    public static String replaceVisibleChatToInvisibleSpaceChar(String string) {
        return string.replaceAll("\\\\t", "\t").replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenericType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取字段的泛型
     *
     * @param field 需要获取的子类
     * @param index 泛型的下标
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getFieldGenericType(Field field, int index) {
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
            return (Class) arguments[index];
        }
        return Object.class;
    }


    /**
     * 判断一个字段是否能够修改
     *
     * @param field 需要判断的字段
     * @return
     */
    public static boolean fieldCanBeModify(Field field) {
        return field != null && ((field.getModifiers() & Modifier.FINAL) == 0);
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param fieldName
     * @param another
     * @return
     */
    public static boolean isEqual(String fieldName, String another) {
        //如果完全匹配,则直接返回 true
        if (fieldName.equals(another)) return true;

        final int count = another.length();
        StringBuilder sb = new StringBuilder();

        char preChar = '\0';
        for (int i = 0; i < count; i++) {
            char currentJsonKeyChar = another.charAt(i);
            switch (currentJsonKeyChar) {
                case UNDERLINE:
                    //pass
                    break;
                default:
                    switch (preChar) {
                        case UNDERLINE:
                            sb.append((char) (currentJsonKeyChar - 32));
                            break;
                        default:
                            sb.append(currentJsonKeyChar);
                            break;
                    }
                    break;
            }
            preChar = currentJsonKeyChar;
        }
        return sb.toString().equals(fieldName);
    }

    /**
     * 生成下划线名字
     *
     * @param name 名字
     * @return
     */
    public static String generateUnderlineName(String name) {
        StringBuilder sb = new StringBuilder();
        final int count = name.length();
        for (int i = 0; i < count; i++) {
            char currentChar = name.charAt(i);
            if (currentChar >= 'A' && currentChar <= 'Z') {
                sb.append(UNDERLINE).append((char) (currentChar + 32));
            } else {
                sb.append(currentChar);
            }
        }
        return sb.toString();
    }

}
