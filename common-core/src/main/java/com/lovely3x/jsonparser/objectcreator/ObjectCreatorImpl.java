package com.lovely3x.jsonparser.objectcreator;

import com.lovely3x.jsonparser.Config;
import com.lovely3x.jsonparser.JSONType;
import com.lovely3x.jsonparser.log.Log;
import com.lovely3x.jsonparser.model.JSONKey;
import com.lovely3x.jsonparser.model.JSONObject;
import com.lovely3x.jsonparser.model.ObjectCreatorConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 对象创建器,默认的实现
 * Created by lovely3x on 15-7-1.
 */
public class ObjectCreatorImpl<T> implements ObjectCreator<T> {

    private static final String TAG = "ObjectCreatorImpl";
    private final Config mConfig;

    public ObjectCreatorImpl(Config config) {
        this.mConfig = config;
    }

    @Override
    public T create(JSONObject jsonObject, Class<T> t) {
        ArrayList<JSONKey> keys = new ArrayList<>(jsonObject.keySet());

        Field[] fields = t.getDeclaredFields();

        ObjectCreatorConfig config = new ObjectCreatorConfig();
        try {
            Object object = Class.forName(t.getName()).newInstance();
            for (JSONKey key : keys) {
                config.reset();
                config.jsonKey = key;
                for (Field field : fields) {
                    field.setAccessible(true);
                    config.field = field;
                    config.isEqual = false;
                    config.jsonValueType = JSONType.JSON_TYPE_INVALIDATE;
                    ObjectCreatorConfig tem = mConfig.matcher.match(config);
                    if (tem != null && tem.isEqual) {
                        try {
                            mConfig.matcher.putValue(object, jsonObject, tem);
                        } catch (Exception e) {
                            Log.e(TAG, e);
                        }
                    }
                }
            }
            return (T) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
