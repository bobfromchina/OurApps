package com.lovely3x.jsonparser.model;

import com.lovely3x.jsonparser.Config;

/**
 * json key 实现类
 * Created by lovely3x on 15-6-29.
 */
public class JSONKeyImpl implements JSONKey {
    private final Config mConfig;
    private String key;

    /**
     * 通过指定的key创建一个jsonKey对象
     *
     * @param key
     */
    public JSONKeyImpl(Config config, String key) {
        this.key = processKey(key);
        this.mConfig = config;
    }

    /**
     * 处理key 因为原始的key是包含 '"' 和 '\' 符号的
     *
     * @return
     */
    private String processKey(String key) {
        StringBuilder sb = new StringBuilder(key);
        if (key.startsWith("\"") && key.endsWith("\"")) {
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public String getKey() {
        return key.trim();
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JSONKeyImpl jsonKey = (JSONKeyImpl) o;

        return !(key != null ? !key.equals(jsonKey.key) : jsonKey.key != null);

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "JSONKeyImpl{" + "key='" + key + '\'' + '}';
    }
}
