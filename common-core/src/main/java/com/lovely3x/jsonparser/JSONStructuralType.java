package com.lovely3x.jsonparser;

/**
 * Created by lovely3x on 15-6-29.
 * json 结构符号
 */
public class JSONStructuralType {

    /**
     * 无效的结构控制符
     */
    public static final char STRUCTURAL_INVALIDATE = '\0';

    /**
     * 数组的左边起始符
     */
    public static final char STRUCTURAL_LEFT_SQUARE_BRACKET = '[';

    /**
     * 数组容器的右边结束符
     */
    public static final char STRUCTURAL_RIGHT_SQUARE_BRACKET = ']';

    /**
     * 左边的对象起始符号
     */
    public static final char STRUCTURAL_LEFT_CURLY_BRACKET = '{';

    /**
     * 右边的对象结束符号
     */
    public static final char STRUCTURAL_RIGHT_CURLY_BRACKET = '}';

    /**
     * 键值对分隔符
     */
    public static final char STRUCTURAL_COLON = ':';

    /**
     * 对象分割符
     */
    public static final char STRUCTURAL_COMMA = ',';

    /**
     * 引号 不是结构符号
     */
    public static final char QUOTE = '"';

    /**
     * 转义符号
     */
    public static final char ESCAPE = '\\';

}
