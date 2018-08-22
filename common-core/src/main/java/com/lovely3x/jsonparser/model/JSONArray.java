package com.lovely3x.jsonparser.model;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.conversation.rule.JSONKeyGenerateRule;
import com.lovely3x.jsonparser.conversation.rule.UnderlineJSONGenerateKeyRule;
import com.lovely3x.jsonparser.formatter.JSONFormatter;
import com.lovely3x.jsonparser.source.JSONSource;
import com.lovely3x.jsonparser.source.JSONSourceImpl;
import com.lovely3x.jsonparser.source.ListJSONSource;

import java.util.ArrayList;
import java.util.List;

/**
 * json数组
 * Created by lovely3x on 15-6-29.
 */
public class JSONArray {

    /**
     * 数据源
     */
    private final JSONSource mSource;

    private final Config mConfig;


    /**
     * 解析的结果
     */
    private List<JSONValue> values;


    /**
     * 通过指定的参数创建一个JSONArray对象
     *
     * @param source 数据源
     */
    public JSONArray(JSONSource source, Config config) {
        this.mSource = source;
        this.mConfig = config;
        parse();
    }

    public JSONArray(List<?> list, JSONKeyGenerateRule rule) {
        this(new ListJSONSource(list, rule));
    }

    public JSONArray(List<?> list) {
        this(new ListJSONSource(list, new UnderlineJSONGenerateKeyRule()));
    }

    public JSONArray(List<?> list, JSONKeyGenerateRule rule, Config config) {
        this(new ListJSONSource(list, rule), config);
    }

    public JSONArray(List<?> list, Config config) {
        this(new ListJSONSource(list, new UnderlineJSONGenerateKeyRule()), config);
    }


    public JSONArray(JSONSource mSource) {
        this(mSource, Config.createDefault());
    }

    public JSONArray(String mSource) {
        this(new JSONSourceImpl(mSource), Config.createDefault());
    }

    public JSONArray(String mSource, Config config) {
        this(new JSONSourceImpl(mSource), config);
    }


    /**
     * 通过解析器解析
     */
    private void parse() {
        if (this.mConfig == null || mConfig.parser == null)
            throw new RuntimeException("jsonParser can be not null.");
        this.values = mConfig.parser.parseJSONArray(mSource);
        this.values = values == null ? new ArrayList<JSONValue>() : values;
    }

    public JSONObject getJSONOObject(int index) {
        return values.get(index).getJSONObject();
    }

    /**
     * 获取JsonArray型的value
     *
     * @param index
     * @return
     */
    public JSONArray getJSONArray(int index) {
        return values.get(index).getJSONArray();
    }

    /**
     * 获取string类型的value
     *
     * @param index
     * @return
     */
    public String getString(int index) {
        return values.get(index).getString();
    }

    /**
     * 获取当前的这个JSONArray的长度,可通过遍历下标获取这个数组中的元素
     *
     * @return
     */
    public int length() {
        return values.size();
    }

    /**
     * 获取jsonValues
     *
     * @return
     */
    public List<JSONValue> getValues() {
        return values;
    }

    /**
     * 获取jsonValue
     *
     * @return
     */
    public JSONValue getValue(int index) {
        return values.get(index);
    }


    /**
     * 猜测类型
     *
     * @param index
     * @return
     */
    public int guessType(int index) {
        return values.get(index).guessType();
    }

    /**
     * 获取int 类型
     *
     * @param index 获取的下标
     * @return
     */
    public int getInt(int index) {
        return values.get(index).getInt();
    }

    /**
     * 获取long型的描述
     *
     * @param index 下标
     * @return
     */
    public long getLong(int index) {
        return values.get(index).getLong();
    }

    /**
     * 获取float型的描述
     *
     * @return
     */
    public float getFloat(int index) {
        return values.get(index).getFloat();
    }

    /**
     * 获取double型的描述
     *
     * @param index
     * @return
     */
    public double getDouble(int index) {
        return values.get(index).getDouble();
    }

    /**
     * 获取布尔型的描述
     *
     * @param index
     * @return
     */
    public boolean getBoolean(int index) {
        return values.get(index).getBoolean();
    }

    /**
     * 获取强制类型转换 int
     *
     * @param index
     * @return
     */
    public int getCastInt(int index) {
        return values.get(index).getCastInt();
    }

    /**
     * <p/>
     *
     * @param clazz 需要创建的对象的 类对象
     * @param <T>   创建的结果集合
     */
    public <T> List<T> createObjects(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return createObjects(ArrayList.class, clazz);
    }

    /**
     * @param container 对象的容器
     * @param clazz     需要创建的对象的 类对象
     * @param <T>       创建的结果集合
     */
    public <T> List<T> createObjects(Class<? extends List> container, Class clazz) throws IllegalAccessException, InstantiationException {
        List<Object> list = container.newInstance();
        for (JSONValue value : values) {
            switch (value.guessType()) {
                case JSONType.JSON_TYPE_ARRAY:
                    break;
                case JSONType.JSON_TYPE_OBJECT:
                    T obj = (T) value.getJSONObject().createObject(clazz);
                    //注意,我在这里判断了null的这种状况,也就是说,不支持创建一个null对象容器中去
                    //因为在我日常的开发中,是基本没有用到 需要 null 这种情况的
                    //反而是因为 list 中出现 null经常头疼
                    if (obj != null) list.add(obj);
                    break;
                case JSONType.JSON_TYPE_BOOLEAN:
                    list.add(value.getBoolean());
                    break;
                case JSONType.JSON_TYPE_INT:
                    list.add(value.getInt());
                    break;
                case JSONType.JSON_TYPE_LONG:
                    list.add(value.getLong());
                    break;
                case JSONType.JSON_TYPE_FLOAT:
                    list.add(value.getFloat());
                    break;
                case JSONType.JSON_TYPE_DOUBLE:
                    list.add(value.getDouble());
                    break;
                case JSONType.JSON_TYPE_STRING:
                    list.add(value.getString());
                    break;
                case JSONType.JSON_TYPE_NULL:
                    break;
            }
        }
        return (List<T>) list;
    }

    @Override
    public String toString() {
        return mSource.input();
    }

    /**
     * 格式化这个JSONArray对象
     *
     * @return 格式化好的字符串
     */
    public String format() {
        return format(mConfig.mFormatter);
    }

    /**
     * 使用指定的格式类,记性格式化
     *
     * @param formatter 格式化工具
     * @return 格式化好的字符串
     */
    public String format(JSONFormatter formatter) {
        return formatter.formatJSONArray(this).toString();
    }
}
