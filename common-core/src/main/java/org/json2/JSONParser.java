package org.json2;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONValue;
import com.lovely3x.jsonparser.source.JSONSource;

import java.util.*;

/**
 * json解析器
 * 使用org.json2 提供的源代码实现
 * Created by lovely3x on 16-1-26.
 */
public class JSONParser implements com.lovely3x.jsonparser.parser.JSONParser {

    private final Config mConfig;

    public JSONParser(Config config) {
        this.mConfig = config;
    }

    @Override
    public List<JSONValue> parseJSONArray(JSONSource source) {
        List<JSONValue> jsonValues = null;
        try {
            JSONArray ja = new JSONArray(source.input());
            List<Object> values = ja.getValues();
            jsonValues = new ArrayList<>(values.size());
            for (Object object : values) {
                jsonValues.add(mConfig.pairFactory.getJSONValue(JSON.toString(object)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonValues;
    }

    @Override
    public Map<JSONKey, JSONValue> parseJSONObject(JSONSource source) {
        HashMap<JSONKey, JSONValue> map = null;
        try {
            JSONObject jo = new JSONObject(source.input());
            Map<String, Object> nameValuePair = jo.getNameValuePairs();
            map = new HashMap<>(nameValuePair.size());
            Set<String> keySet = nameValuePair.keySet();
            for (String key : keySet) {
                map.put(mConfig.pairFactory.getJSONKey(key),
                        mConfig.pairFactory.getJSONValue(new GJSONSource(nameValuePair.get(key), mConfig)));
            }
        } catch (Exception ignored) {
        }
        return map;
    }

    public class GJSONSource implements JSONSource {
        private final Config mConfig;
        private final Object source;

        public GJSONSource(Object object, Config config) {
            this.mConfig = config;
            this.source = object;
        }

        @Override
        public String input() {
            if (source instanceof Double) {
                return String.valueOf(((Double) source).doubleValue());
            } else if (source instanceof Float) {
                return String.valueOf(((Float) source).floatValue());
            } else if (source instanceof Integer) {
                return String.valueOf(((Integer) source).intValue());
            } else if (source instanceof Boolean) {
                return String.valueOf(((Boolean) source).booleanValue());
            } else if (source == null) {
                return String.valueOf("null");
            } else if (source instanceof JSONObject) {
                return String.valueOf(source);
            } else if (source instanceof JSONArray) {
                return String.valueOf(source);
            }
            return String.valueOf("\"" + source.toString() + "\"");
        }
    }
}
