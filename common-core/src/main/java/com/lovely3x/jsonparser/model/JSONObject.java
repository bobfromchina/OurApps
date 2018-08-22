package com.lovely3x.jsonparser.model;

import com.lovely3x.common.utils.ALog;
import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.conversation.rule.JSONGenerateRule;
import com.lovely3x.jsonparser.conversation.rule.JSONKeyGenerateRule;
import com.lovely3x.jsonparser.conversation.rule.UnderlineJSONGenerateKeyRule;
import com.lovely3x.jsonparser.source.JSONSource;
import com.lovely3x.jsonparser.source.JSONSourceImpl;
import com.lovely3x.jsonparser.source.MapJSONSource;
import com.lovely3x.jsonparser.source.ObjectJSONSource;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lovely3x on 15-6-29.
 * json 对象
 */
public class JSONObject {

    private static final String TAG = "JSONObject";
    /**
     * json数据源
     */
    private final JSONSource mSource;
    private final Config mConfig;

    /**
     * 解析好的键值对
     */
    private Map<JSONKey, JSONValue> mPair;


    public JSONObject(JSONSource source, Config config) {
        this.mConfig = config;
        this.mSource = source;
        parse();
    }

    public JSONObject(Map map, JSONGenerateRule rule, Config config) {
        this(new MapJSONSource(map, rule), config);
    }

    public JSONObject(Map map, Config config) {
        this(new MapJSONSource(map, new UnderlineJSONGenerateKeyRule()), config);
    }

    public JSONObject(Map map) {
        mSource = new MapJSONSource(map, new UnderlineJSONGenerateKeyRule());
        this.mConfig = Config.createDefault();
        parse();
    }


    public JSONObject(String source, Config config) {
        this.mConfig = config;
        this.mSource = new JSONSourceImpl(source);
        parse();
    }

    public JSONObject(String source) {
        this.mSource = new JSONSourceImpl(source);
        this.mConfig = Config.createDefault();
        parse();
    }

    public JSONObject(JSONSource source) {
        this(source, Config.createDefault());
    }

    public JSONObject(Object object, JSONKeyGenerateRule rule) {
        this(new ObjectJSONSource(object, rule));
    }


    public JSONObject(Object object, JSONKeyGenerateRule rule, Config config) {
        this(new ObjectJSONSource(object, rule), config);
    }


    public JSONObject(Object object) {
        this(new ObjectJSONSource(object, new UnderlineJSONGenerateKeyRule()));
    }

    public JSONObject(Object object, Config config) {
        this(new ObjectJSONSource(object, new UnderlineJSONGenerateKeyRule()), config);
    }


    /**
     * 解析,如果是在解析情况下会自动调用
     * 但是如果是在将对象创建为json对象,则需要主动调用这个方法解析
     * 因为在大多数情况下,没有人会将一个对象转换为json串之后再自己进行解析
     */
    private void parse() {
        this.mPair = mConfig.parser.parseJSONObject(mSource);
        if (mPair == null) {
            mPair = new HashMap<>();
        }
    }

    /**
     * 通过制定的key获取json对象
     *
     * @param key 指定的key
     */
    public JSONObject getJSONObject(String key) {
        return getJSONObject(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 通过制定的key获取json对象
     *
     * @param key 指定的key
     */
    public JSONObject getJSONObject(JSONKey key) {
        return mPair.get(key).getJSONObject();
    }

    /**
     * 通过指定的key 获取jsonArray
     *
     * @param key 指定的key
     * @return null如果没有找到该key 或者是 JsonArray
     */
    public JSONArray getJSONArray(String key) {
        return getJSONArray(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 通过指定的key 获取jsonArray
     *
     * @param key 指定的key
     * @return null如果没有找到该key 或者是 JsonArray
     */
    public JSONArray getJSONArray(JSONKey key) {
        return mPair.get(key).getJSONArray();
    }

    /**
     * 通过制定的key 获取String
     */
    public String getString(String key) {
        return getString(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 通过制定的key 获取String
     */
    public String getString(JSONSource source) {
        return getString(mConfig.pairFactory.getJSONKey(source));
    }

    /**
     * 通过制定的key 获取String
     */
    public String getString(JSONKey key) {
        JSONValue jsonValue;
        if ((jsonValue = mPair.get(key)) != null) {
            return jsonValue.getString();
        }
        return null;
    }


    /**
     * 获取当前json串的键值对
     */
    public Map<JSONKey, JSONValue> getPair() {
        return mPair;
    }

    /**
     * 猜测指定的key对应的数据类型
     */
    public int guessType(JSONSource key) {
        return mPair.get(mConfig.pairFactory.getJSONKey(key)).guessType();
    }


    /**
     * 获取int 类型
     *
     * @param key 获取的的key
     */
    public int getInt(String key) {
        return getInt(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 获取 int 类型
     */
    public int getInt(JSONSource key) {
        return getInt(mConfig.pairFactory.getJSONKey(key));
    }


    /**
     * 获取 int 类型
     */
    public int getInt(JSONKey key) {
        JSONValue jsonValue;
        if ((jsonValue = mPair.get(key)) != null) {
            try {
                return jsonValue.getInt();
            } catch (Exception e) {
                ALog.w(TAG, String.format("Json Key :  %s Exception %s", key.getKey(), e.getMessage()));
                throw new RuntimeException(e);
            }
        }
        return 0;
    }


    /**
     * 获取long型的描述
     *
     * @param key key
     */
    public long getLong(String key) {
        return getLong(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 获取long型的描述
     *
     * @param source key
     */
    public long getLong(JSONSource source) {
        return getLong(mConfig.pairFactory.getJSONKey(source));
    }

    /**
     * 获取long型的描述
     *
     * @param key key
     */
    public long getLong(JSONKey key) {
        JSONValue jsonValue;
        if ((jsonValue = mPair.get(key)) != null) {
            return jsonValue.getLong();
        }
        return 0;
    }

    /**
     * 获取float型的描述
     *
     * @param key key
     */
    public float getFloat(String key) {
        return getFloat(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 获取float型的描述
     *
     * @param source key
     */
    public float getFloat(JSONSource source) {
        return getFloat(mConfig.pairFactory.getJSONKey(source));
    }

    /**
     * 获取float型的描述
     *
     * @param key key
     */
    public float getFloat(JSONKey key) {
        JSONValue jsonValue;
        if ((jsonValue = mPair.get(key)) != null) {
            return jsonValue.getFloat();
        }
        return 0;
    }

    /**
     * 获取double型的描述
     *
     * @param key key
     */
    public double getDouble(String key) {
        return getDouble(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 获取double的描述
     *
     * @param source key
     */
    public double getDouble(JSONSource source) {
        return getDouble(mConfig.pairFactory.getJSONKey(source));
    }

    /**
     * 获取double的描述
     *
     * @param key key
     */
    public double getDouble(JSONKey key) {
        JSONValue jsonValue;
        if ((jsonValue = mPair.get(key)) != null) {
            return jsonValue.getDouble();
        }
        return 0;
    }

    /**
     * 获取double型的描述
     *
     * @param key key
     */
    public boolean getBoolean(String key) {
        return getBoolean(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 获取double的描述
     *
     * @param source key
     */
    public boolean getBoolean(JSONSource source) {
        return getBoolean(mConfig.pairFactory.getJSONKey(source));
    }

    /**
     * 获取double的描述
     *
     * @param key key
     */
    public boolean getBoolean(JSONKey key) {
        JSONValue jsonValue;
        return (jsonValue = mPair.get(key)) != null && jsonValue.getBoolean();
    }

    /**
     * 获取强制类型转换 int
     *
     * @param key key
     */
    public int getCastInt(String key) {
        return getCastInt(mConfig.pairFactory.getJSONKey(key));
    }

    /**
     * 获取强制类型转换 int
     *
     * @param source key
     */
    public int getCastInt(JSONSource source) {
        return getCastInt(mConfig.pairFactory.getJSONKey(source));
    }

    /**
     * 获取强制类型转换 int
     *
     * @param key key
     */
    public int getCastInt(JSONKey key) {
        JSONValue jsonValue;
        if ((jsonValue = mPair.get(key)) != null) {
            return jsonValue.getCastInt();
        }
        return 0;
    }


    /**
     * 获取JSONValue对象
     *
     * @param key 指定获取JSONValue的key
     * @return 这个key 对应的value
     */
    public JSONValue getValue(String key) {
        return getValue(mConfig.pairFactory.getJSONKey(key));
    }


    /**
     * 通过json数据源 获取JSONValue对象
     *
     * @param source 数据源 应该是key的数据源
     * @return 这个key对应的value
     */
    public JSONValue getValue(JSONSource source) {
        return getValue(mConfig.pairFactory.getJSONKey(source));
    }


    /**
     * 获取JSONValue对象
     *
     * @param key 制定的JSONKey
     * @return 根据JSONKey获取JSONValue对象
     */
    public JSONValue getValue(JSONKey key) {
        return mPair.get(key);
    }

    /**
     * 获取key集合
     *
     * @return 获取当前的这个json对象的所有key集合
     */
    public Set<JSONKey> keySet() {
        return mPair.keySet();
    }

    /**
     * 将当前的json对象转换为java对象
     * <p/>
     * 使用默认的对象创建器
     *
     * @param clazz 需要转换的类
     * @param <T>   需要结果类型
     * @return 创建成功则返回该对象, 否则为null
     */
    public <T> T createObject(Class<T> clazz) {
        return (T) mConfig.creator.create(this, clazz);
    }

    /**
     * 使用默认的格式化实现类进行格式化
     *
     * @return 格式化号的字符串
     */
    public String format() {
        return mConfig.mFormatter.formatJSONObject(this).toString();
    }


    /**
     * 使用指定的creator 创建 class
     *
     * @param className 类名
     * @param os        输出流
     * @return 输出流
     */
    public OutputStream createClass(String className, OutputStream os) {
        return mConfig.classCreator.createObject(this, className, os);
    }

    public Config getConfig() {
        return mConfig;
    }

    public JSONSource geSource() {
        return mSource;
    }

    /**
     * toString 可以显示出当前的数据源,如果你是在进行使用对象生成json串,
     * 那么你可能需要通过这个方法获得转换字符串的
     *
     * @return 当前的数据源
     */
    @Override
    public String toString() {
        return mSource.input();
    }
}
