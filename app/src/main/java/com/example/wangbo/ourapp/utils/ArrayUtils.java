package com.example.wangbo.ourapp.utils;

import java.util.ArrayList;

/**
 * Created by lovely3x on 15-7-16.
 * 数组工具
 */
public class ArrayUtils {

    /**
     * Integer的数组转换为int数组
     *
     * @param array
     * @return
     */
    public static int[] IntegerArrayToIntArray(Integer[] array) {
        if (array == null) return null;
        int[] intArray = new int[array.length];
        final int count = array.length;
        for (int i = 0; i < count; i++) {
            Integer value = array[i];
            if (value != null) {
                intArray[i] = value;
            }
        }
        return intArray;
    }

    public static <T> ArrayList<T> ArrayToList(T[] datas) {
        ArrayList<T> array = new ArrayList<>(datas.length);
        for (T t : datas) {
            array.add(t);
        }
        return array;
    }

}
