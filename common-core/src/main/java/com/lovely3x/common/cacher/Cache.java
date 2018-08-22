package com.lovely3x.common.cacher;

public class Cache {

    /**
     * 缓存描述的id
     */
    private long id;

    /**
     * 缓存描述的大小
     */
    private long size;

    /**
     * 缓存描述的名字
     */
    private String name;

    /**
     * 缓存描述的名字
     */
    private long time;

    /**
     * 缓存描述的数据
     */
    private byte[] data;

    public Cache() {
    }

    public Cache(long size, String name, long time, byte[] data) {
        this.size = size;
        this.name = name;
        this.time = time;
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "\nid=" + id +
                "\nsize=" + size +
                "\nname='" + name + '\'' +
                "\ntime=" + time +
                ",\n data size =" + (data == null ? 0 : data.length) +
                '}';
    }
}
