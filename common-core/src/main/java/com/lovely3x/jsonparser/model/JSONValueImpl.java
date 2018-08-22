package com.lovely3x.jsonparser.model;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.source.JSONSourceImpl;
import com.lovely3x.jsonparser.utils.CommonUtils;

/**
 * json对 的值
 * Created by lovely3x on 15-6-29.
 */
public class JSONValueImpl implements JSONValue {


    private final Config mConfig;
    /**
     * 保存value
     */
    private String value;


    /**
     * 通过指定的值创建JSONValue对象
     *
     * @param value
     */
    public JSONValueImpl(Config config, String value) {
        this.value = filter(value);
        this.mConfig = config;
    }

    /**
     * 获取value
     *
     * @return
     */
    public String getValue() {
        return value.trim();
    }

    /**
     * 设置value
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 简单的处理key 因为原始的key是包含 '"' 和 '\' 符号的
     *
     * @return
     */
    protected String processString(String str) {
        str = filter(str);
        StringBuilder sb = new StringBuilder(str);
        if (str.startsWith("\"") && str.endsWith("\"")) {
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
        }
        //修护了对转义字符 \r\n\t的支持
        str = sb.toString();
        str = CommonUtils.replaceVisibleChatToInvisibleSpaceChar(str);
        str = str.replaceAll("\\\\\"", "\"");
        return str;
    }

    @Override
    public JSONArray getJSONArray() {
        return new JSONArray(new JSONSourceImpl(value), mConfig);
    }

    @Override
    public JSONObject getJSONObject() {
        return new JSONObject(new JSONSourceImpl(value), mConfig);
    }

    @Override
    public String getString() {
        return processString(value);
    }

    /**
     * 简单的过滤
     *
     * @param originalString
     * @return
     */
    protected String filter(String originalString) {
        if (originalString == null) return null;
        return originalString.trim();
    }


    @Override
    public int getInt() {
        if (isNull()) {
            return 0;
        }
        return Integer.parseInt(getString());
    }

    @Override
    public int getCastInt() {
        int result = -1;
        int type = guessType();
        switch (type) {
            case JSONType.JSON_TYPE_LONG:
                long lValue = getLong();
                result = (int) lValue;
                break;
            case JSONType.JSON_TYPE_INT:
                result = getInt();
                break;
            case JSONType.JSON_TYPE_DOUBLE:
            case JSONType.JSON_TYPE_FLOAT:
                double dValue = getDouble();
                result = (int) dValue;
                break;
            case JSONType.JSON_TYPE_STRING:
                result = Integer.parseInt(getString());
                break;
        }
        return result;
    }

    @Override
    public double getDouble() {
        if (isNull()) {
            return 0.0D;
        }
        return Double.parseDouble(getString());
    }

    @Override
    public float getFloat() {
        if (isNull()) {
            return 0.0F;
        }
        return Float.parseFloat(getString());

    }

    @Override
    public boolean getBoolean() {
        if (isNull()) {
            return false;
        }
        return Boolean.parseBoolean(getString());
    }

    @Override
    public long getLong() {
        if (isNull()) {
            return 0;
        }
        return Long.parseLong(getString());
    }

    /**
     * 判断指定的这个字符串是否为null
     *
     * @return
     */
    private boolean isNull() {
        return value == null || "null".equals(value) || "".equals(value);
    }


    @Override
    public int guessType() {
        return CommonUtils.guessType(this.value);
    }

    @Override
    public String toString() {
        return value;
    }

}
