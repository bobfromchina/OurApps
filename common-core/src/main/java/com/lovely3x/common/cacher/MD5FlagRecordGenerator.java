package com.lovely3x.common.cacher;

import android.util.Base64;

import com.lovely3x.common.utils.Md5Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * MD5key生成
 * 可以用于记录标志
 * Created by lovely3x on 15-12-4.
 */
public class MD5FlagRecordGenerator {

    private static final List<KeyDesc> recycleBins = new ArrayList<>();
    private static MD5FlagRecordGenerator mInstance;

    static {
        for (int i = 0; i < 10; i++) {
            recycleBins.add(new KeyDesc(null, -1, null));
        }
    }

    /**
     * 获取一个keyDesc
     * 内部从对象池中取
     *
     * @return
     */
    public static KeyDesc getKeyDesc() {
        for (KeyDesc desc : recycleBins) {
            if (desc.isRecycled) {
                desc.isRecycled = false;
                return desc;
            }
        }
        KeyDesc newer = new KeyDesc(null, -1, null);
        newer.isRecycled = false;
        recycleBins.add(newer);
        return newer;
    }

    public static String encodeKey(String originalKey, int position, String identity) {
        String key = Md5Utils.getMD5Str(originalKey);
        final String strPosition = String.valueOf(position);
        final String result = String.format("%s,%s,%s", key, strPosition, identity);
        return Base64.encodeToString(result.getBytes(), Base64.URL_SAFE);
    }

    /**
     * 解码key
     *
     * @param encodedKey 加密后的key
     * @return 解码后的key
     */
    public static KeyDesc decodeKey(String encodedKey) {
        String result = new String(Base64.decode(encodedKey.getBytes(), Base64.URL_SAFE));
        String[] resultArr = result.split(",");
        if (resultArr.length == 3) {
            return getKeyDesc().reConstruct(resultArr[0], Integer.parseInt(resultArr[1]), resultArr[2]);
        }
        throw new IllegalStateException("错误的key，正确的key必须由是三部分组成【1】 == 原始key，【2】 == 标志 【3】身份标志");
    }

    /**
     * key描述
     */
    public static class KeyDesc {

        /**
         * 标志
         */
        private int flag = -1;

        /**
         * key
         */
        private String name = null;

        /**
         * 身份标志
         */
        private String identity;


        /**
         * 是否已经回收了
         */
        private boolean isRecycled = true;

        public void recycle() {
            flag = -1;
            name = null;
            isRecycled = true;
        }

        /**
         * 重新构建
         *
         * @param position 位置
         * @param name     名字
         * @return 对象
         */
        public KeyDesc reConstruct(String name, int position, String identity) {
            this.flag = position;
            this.name = name;
            this.identity = identity;
            return this;
        }

        private KeyDesc(String name, int position, String identity) {
            this.flag = position;
            this.name = name;
            this.identity = identity;
        }

        public int getFlag() {
            if (isRecycled) throw new IllegalStateException("该对象已经被回收,请重新获取并重构建");
            return flag;
        }

        public String getName() {
            if (isRecycled) throw new IllegalStateException("该对象已经被回收,请重新获取并重构建");
            return name;
        }

        @Override
        public String toString() {
            return "KeyDesc{" +
                    "flag=" + flag +
                    ", name='" + name + '\'' +
                    ", isRecycled=" + isRecycled +
                    '}';
        }
    }
}
