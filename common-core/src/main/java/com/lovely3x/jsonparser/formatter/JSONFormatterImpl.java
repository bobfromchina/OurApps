package com.lovely3x.jsonparser.formatter;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.model.JSONArray;
import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONObject;
import com.lovely3x.jsonparser.model.JSONValue;
import com.lovely3x.jsonparser.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by lovely3x on 15-7-2.
 * json格式化实现类
 */
public class JSONFormatterImpl implements JSONFormatter {

    private final Config mConfig;
    private boolean mInvisibleSpaceChar;
    /**
     * 缩进符号
     */
    private String block;
    /**
     * 换行符符号
     */
    private String newLine;

    /**
     * 使用默认的换行符和指定的缩进符号进行创建格式化对象
     *
     * @param block 缩进符号
     */
    public JSONFormatterImpl(Config config, String block) {
        this(config, block, "\n");
    }

    /**
     * 使用指定的的换行符和指定的缩进符号进行创建格式化对象
     *
     * @param block   缩进符号
     * @param newLine 换行符号
     */
    public JSONFormatterImpl(Config config, String block, String newLine) {
        this(config, block, newLine, false);
    }

    /**
     * 使用指定的的换行符和指定的缩进符号进行创建格式化对象
     *
     * @param block              缩进符号
     * @param newLine            换行符号
     * @param invisibleSpaceChar 是否让可见空白字符变为不可见例如 \t 会变为 看不见的留白,默认关闭
     */
    public JSONFormatterImpl(Config config, String block, String newLine, boolean invisibleSpaceChar) {
        this.block = block;
        this.newLine = newLine;
        this.mInvisibleSpaceChar = invisibleSpaceChar;
        this.mConfig = config;
    }

    /**
     * 使用默认的四个空格键作为分隔符号和默认的换行符号进行创建格式化对象
     *
     * @param invisibleSpaceChar 是否让可见空白字符变为不可见例如 \t 会变为 看不见的留白,默认开启
     */
    public JSONFormatterImpl(Config config, boolean invisibleSpaceChar) {
        this(config, "    ", "\n", invisibleSpaceChar);
    }

    /**
     * 使用默认的四个空格键作为分隔符号和默认的换行符号
     */
    public JSONFormatterImpl(Config config) {
        this(config, "    ");
    }

    @Override
    public CharSequence formatJSONObject(JSONObject object) {
        return new StringBuilder().append("{").append(formatJSONObject(object, 0)).append("\n}");
    }

    /**
     * 格式化jsonObject
     *
     * @param object 需要格式化的jsonObject
     * @param level  本次格式化的内容为第几级别
     * @return 本级别格式化好的字符流
     */
    public CharSequence formatJSONObject(JSONObject object, int level) {
        StringBuilder sb = new StringBuilder();
        ArrayList<JSONKey> keys = new ArrayList<>(object.keySet());
        final int count = keys.size();
        for (int i = 0; i < count; i++) {
            JSONKey key = keys.get(i);
            JSONValue value = object.getValue(key);
            switch (value.guessType()) {
                case JSONType.JSON_TYPE_ARRAY://array
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('"').append(key.getKey()).append('"').append(' ').append(':').append(' ').append('[');
                    sb.append(formatJSONArray(object.getJSONArray(key), level + 1));
                    sb.append(newLine).append(addSpace(level + 1)).append(']').append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_OBJECT://object
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('"').append(key.getKey()).append('"').append(' ').append(':').append(' ').append('{');
                    sb.append(formatJSONObject(object.getJSONObject(key), level + 1));
                    sb.append(newLine).append(addSpace(level + 1)).append('}').append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_BOOLEAN://boolean
                case JSONType.JSON_TYPE_INT://int
                case JSONType.JSON_TYPE_LONG://long
                case JSONType.JSON_TYPE_FLOAT://float
                case JSONType.JSON_TYPE_DOUBLE://double
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('"').append(key.getKey()).append('"').append(' ').append(':').append(' ');
                    sb.append(value.getString().trim()).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_STRING://
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('"').append(key.getKey()).append('"').append(' ').append(':').append(' ').append('"').append(processString(value)).append('"').append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_NULL://null
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('"').append(key.getKey()).append('"').append(' ').append(':').append(' ').append("null").append(i + 1 == count ? "" : ",");
                    break;
            }
        }
        return sb;
    }

    /**
     * 添加空白格子
     *
     * @param level 第几级
     * @return 添加的空白格子
     */
    private StringBuilder addSpace(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(block);
        }
        return sb;
    }

    @Override
    public CharSequence formatJSONArray(JSONArray array) {
        return new StringBuilder().append("[" + formatJSONArray(array, 0)).append("\n]");
    }

    /**
     * 格式化jsonArray
     *
     * @param array 需要格式化的jsonArray
     * @param level 本次格式化的内容为第几级别
     * @return 本级别格式化好的字符流
     */
    public CharSequence formatJSONArray(JSONArray array, int level) {
        StringBuilder sb = new StringBuilder();
        final int count = array.length();
        for (int i = 0; i < count; i++) {
            JSONValue value = array.getValue(i);
            //System.out.println("value == " +value);
            switch (value.guessType()) {
                case JSONType.JSON_TYPE_ARRAY://array
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('[').append(formatJSONArray(value.getJSONArray(), level + 1)).append(newLine).append(addSpace(level + 1)).append(']')
                            .append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_OBJECT://object
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('{').append(formatJSONObject(value.getJSONObject(), level + 1)).append(newLine)
                            .append(addSpace(level + 1)).append('}').append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_BOOLEAN://boolean
                case JSONType.JSON_TYPE_INT://int
                case JSONType.JSON_TYPE_LONG://long
                case JSONType.JSON_TYPE_FLOAT://float
                case JSONType.JSON_TYPE_DOUBLE://double
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append(value.getString()).append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_STRING://
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append('"').append(processString(value)).append('"').append(i + 1 == count ? "" : ",");
                    break;
                case JSONType.JSON_TYPE_NULL://null
                    sb.append(newLine);
                    sb.append(addSpace(level)).append(block);
                    sb.append("null").append(i + 1 == count ? "" : ",");
                    break;
            }
        }
        return sb;
    }

    /**
     * 这个就是处理是否开启空白字符转换开的作用实现方法
     * JSON字符串不能存在不可见换行符
     * <p/>
     * 这个起始是考虑GUI界面处理的问题
     * GUI界面应该是是不能删除 \" 中的 \ 的,但是,在代码中获取时需要删除的
     * GUI为什么不能删除?因为在GUI界面中,用户可以随时改变字符串的的内容,也就是我们在格式化时,其实取的就是文本输入框中的内容
     * 但是假设我们删除了的话,那么这个内容就和输入时不一样了(不仅仅是变得漂流了,而是格式变化了,可以说不是一个规范的JSON串了)
     * 所以我们在GUI中就不能打开这个选项
     *
     * @param value 需要处理的值
     * @return 处理完的字符串
     */
    private String processString(JSONValue value) {
        if (true) return value.getString();
        String str = value.getValue();
        StringBuilder sb = new StringBuilder(str);
        if (str.startsWith("\"") && str.endsWith("\"")) {
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
        }
        str = sb.toString();
        if (mInvisibleSpaceChar) {
            str = sb.toString();
            str = CommonUtils.replaceVisibleChatToInvisibleSpaceChar(str);
            str = str.replaceAll("\\\\\"", "\"");
        } else {
            //JSON字符串不能存在不可见换行符
            str = str.replaceAll("\\r", "\\\r").replaceAll("\\n", "\\\n").replaceAll("\\t", "\\\t");
        }
        return str;
    }


}
