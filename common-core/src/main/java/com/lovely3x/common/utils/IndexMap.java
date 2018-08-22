package com.lovely3x.common.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 使用array list 编写的可索引map
 * Created by lovely3x on 16-5-10.
 */
public class IndexMap<K, V> implements Map<K, V> {
    private final ArrayList<K> keys = new ArrayList<>();
    private final ArrayList<V> values = new ArrayList<>();

    @Override
    public void clear() {
        keys.clear();
        values.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values.contains(value);
    }

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(Object key) {
        return values.get(keys.indexOf(key));
    }

    @Override
    public boolean isEmpty() {
        return keys.isEmpty();
    }

    @NonNull
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V put(K key, V value) {
        keys.add(key);
        values.add(value);
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (!(map instanceof IndexMap)) throw new UnsupportedOperationException();
        keys.addAll(((IndexMap) map).keys);
        values.addAll(((IndexMap) map).values);
    }

    @Override
    public V remove(Object key) {
        int index = keys.indexOf(key);
        if (index != -1) {
            keys.remove(index);
            return values.remove(index);
        }
        return null;
    }

    @Override
    public int size() {
        return keys.size();
    }

    @NonNull
    @Override
    public Collection<V> values() {
        return values;
    }

    public ArrayList<K> keys() {
        return keys;
    }

    public int indexOfKey(K k) {
        return keys.indexOf(k);
    }

    public K keyAt(int index) {
        return keys.get(index);
    }

    public V valueAt(int index) {
        return values.get(index);
    }
}
