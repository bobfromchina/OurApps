package com.lovely3x.common.utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by lovely3x on 16/8/15.
 */
public class IntentUtils {

    private static final java.lang.String TAG = "IntentUtils";

    public static Intent makeIntent(String key, String value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }

    public static Intent makeIntent(String key, int value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }

    public static Intent makeIntent(String key, long value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }


    public static Intent makeIntent(String key, float value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }


    public static Intent makeIntent(String key, double value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }


    public static Intent makeIntent(String key, Parcelable value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }


    public static Intent makeIntent(String key, Serializable value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }


    public static Intent makeIntent(Object... objects) {
        if (objects == null) return null;

        Bundle bundle = new Bundle();
        if (objects.length % 2 == 0) {
            final int count = objects.length / 2;
            for (int i = 0; i < count; i++) {
                String key = objects[i * 2].toString();
                Object value = objects[i * 2 + 1];
                if (key != null && value != null) {
                    if (value instanceof Byte) {
                        bundle.putByte(key, (Byte) value);
                    } else if (value instanceof Short) {
                        bundle.putShort(key, (Short) value);
                    } else if (value instanceof Integer) {
                        bundle.putInt(key, (Integer) value);
                    } else if (value instanceof Long) {
                        bundle.putLong(key, (Long) value);
                    } else if (value instanceof Float) {
                        bundle.putFloat(key, (Float) value);
                    } else if (value instanceof Double) {
                        bundle.putDouble(key, (Double) value);
                    } else if (value instanceof String) {
                        bundle.putString(key, (String) value);
                    } else if (value instanceof Bundle) {
                        bundle.putBundle(key, (Bundle) value);
                    } else if (value instanceof Parcelable) {
                        bundle.putParcelable(key, (Parcelable) value);
                    } else if (value instanceof ArrayList) {
                        bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) value);
                    } else if (value instanceof Collection) {
                        bundle.putParcelableArrayList(key, new ArrayList<>((Collection<? extends Parcelable>) value));
                    } else if (value instanceof Serializable) {
                        bundle.putSerializable(key, (Serializable) value);
                    } else {
                        ALog.e(TAG, "unsupported object");
                    }
                }
            }

        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        return intent;
    }
}
