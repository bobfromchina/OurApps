package com.lovely3x.common.utils.storage;

import android.support.annotation.NonNull;

import com.lovely3x.common.utils.fileutils.StreamUtils;
import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.conversation.rule.UnderlineJSONGenerateKeyRule;
import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONObject;
import com.lovely3x.jsonparser.model.JSONValue;
import com.lovely3x.jsonparser.source.JSONSourceImpl;
import com.lovely3x.jsonparser.source.MapJSONSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 映射文件
 * Created by lovely3x on 16-1-13.
 */
public class JSONFile implements Map<JSONKey, JSONValue> {

    private final File mFile;
    private JSONObject jo;

    public JSONFile(File file) {
        if (file.exists()) try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.mFile = file;
        readMapFromFile();
    }

    /**
     * 从文件中读取内容
     */
    private void readMapFromFile() {
        try {
            JSONSourceImpl content = new JSONSourceImpl(StreamUtils.readToString(new FileInputStream(mFile)));
            jo = new JSONObject(content, Config.createDefault());
        } catch (Exception e) {
        }
    }

    @Override
    public void clear() {
        jo.getPair().clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return jo.getPair().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return jo.getPair().containsValue(value);
    }

    @NonNull
    @Override
    public Set<Entry<JSONKey, JSONValue>> entrySet() {
        return jo.getPair().entrySet();
    }

    @Override
    public JSONValue get(Object key) {
        return jo.getPair().get(key);
    }

    @Override
    public boolean isEmpty() {
        return jo.getPair().isEmpty();
    }

    @NonNull
    @Override
    public Set<JSONKey> keySet() {
        return jo.keySet();
    }

    @Override
    public JSONValue put(JSONKey key, JSONValue value) {
        return jo.getPair().put(key, value);
    }

    public void put(String key, String value) {
        jo.getPair().put(jo.getConfig().pairFactory.getJSONKey(key), jo.getConfig().pairFactory.getJSONValue(value));
    }

    public String get(String key) {
        return jo.getPair().get(jo.getConfig().pairFactory.getJSONKey(key)).getString();
    }

    @Override
    public void putAll(Map<? extends JSONKey, ? extends JSONValue> map) {
        jo.getPair().putAll(map);
    }

    @Override
    public JSONValue remove(Object key) {
        return jo.getPair().remove(key);
    }

    @Override
    public int size() {
        return jo.getPair().size();
    }

    @NonNull
    @Override
    public Collection<JSONValue> values() {
        return jo.getPair().values();
    }

    /**
     * 提交记录
     */
    public boolean commit() {
        synchronized (this) {
            try {
                Map<JSONKey, JSONValue> pair = jo.getPair();
                HashMap<Object, Object> map = new HashMap<>();
                Set<JSONKey> keys = pair.keySet();
                for (JSONKey key : keys) {
                    String stringKey = key.getKey();
                    String stringValue = pair.get(key).getValue();
                    map.put(stringKey, stringValue);
                }

                MapJSONSource source = new MapJSONSource(map, new UnderlineJSONGenerateKeyRule());
                Config config = Config.createDefault();
                jo = new JSONObject(source, config);

                try {
                    return StreamUtils.writeStringToStream(new FileOutputStream(mFile), jo.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
