package com.lovely3x.common.cacher.container;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.enums.AssignType;
import com.lovely3x.common.cacher.Cache;
import com.lovely3x.common.cacher.ICacheMonitor;
import com.lovely3x.common.cacher.Monitor;
import com.lovely3x.common.requests.Config;
import com.lovely3x.common.utils.ALog;
import com.lovely3x.common.utils.fileutils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * FIFO 磁盘缓存实现者
 * Created by lovely3x on 15-12-3.
 */
public class FIFODiskCacheContainer implements ICacheable<String, Cache> {

    private final InternalFiFOCacheImpl mInternalFiFOCacheImpl;

    public FIFODiskCacheContainer(String cacheFilesDir, Context context, long maxSize) {
        this.mInternalFiFOCacheImpl = new InternalFiFOCacheImpl(cacheFilesDir, context, maxSize);
    }

    @Override
    public boolean put(String s, Cache cache) {
        return mInternalFiFOCacheImpl.put(s, cache);
    }


    @Override
    public Cache get(String s) {
        return mInternalFiFOCacheImpl.get(s);
    }

    @Override
    public boolean clearCache() {
        return mInternalFiFOCacheImpl.clear();
    }

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Cache remove(String s) {
        Cache cache = mInternalFiFOCacheImpl.get(s);
        if (cache != null) {
            if (mInternalFiFOCacheImpl.remove(s)) {
                return cache;
            }
        }
        return null;

    }

    @Override
    public long maxSize() {
        return mInternalFiFOCacheImpl.mMaxSize;
    }

    @Override
    public long currentSize() {
        return mInternalFiFOCacheImpl.mCacheFilesSize;
    }

    @Override
    public void registerCacheMonitor(ICacheMonitor monitor) {
        mInternalFiFOCacheImpl.registerCacheMonitor(monitor);
    }

    @Override
    public void unregisterCacheMonitor(ICacheMonitor monitor) {
        mInternalFiFOCacheImpl.unregisterCacheMonitor(monitor);
    }

    /**
     * 更新缓存的内容部实现类
     */
    private class InternalFiFOCacheImpl implements Monitor {

        private static final java.lang.String TAG = "InternalFiFOCacheImpl";

        /**
         * 缓存文件的文件夹
         */
        private final String mCacheFilesDir;
        /**
         * 数据库操作助手
         */
        private final LiteOrm mLiteOrm;

        /**
         * 缓存描述集
         */
        private final List<CacheIndexDesc> mCacheIndexDesc = new ArrayList<>();

        /**
         * 监视器
         */
        private final List<ICacheMonitor> monitors = new ArrayList<>();


        /**
         * 最大的
         */
        private final long mMaxSize;

        /**
         * 缓存的文件的总大小
         */
        private long mCacheFilesSize;

        public InternalFiFOCacheImpl(String cacheFilesDir, Context context, long maxSize) {
            this.mCacheFilesDir = cacheFilesDir;
            this.mMaxSize = maxSize;
            File dir = new File(mCacheFilesDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.exists()) {
                throw new RuntimeException("can't create dir on " + cacheFilesDir);
            }
            mLiteOrm = LiteOrm.newSingleInstance(context, "disk_caches_index_table");
            updateCacheIndexDesc();
        }

        /**
         * 更新缓存索引表
         */
        public synchronized void updateCacheIndexDesc() {
            ArrayList<CacheIndexDesc> caches = mLiteOrm.query(CacheIndexDesc.class);
            if (caches != null) {
                mCacheIndexDesc.clear();
                mCacheIndexDesc.addAll(caches);
            }
            long count = 0;
            for (CacheIndexDesc cid : mCacheIndexDesc) {
                if (cid != null) {
                    count += cid.size;
                }
            }
            this.mCacheFilesSize = count;
        }

        /**
         * 放置数据
         *
         * @param k 数据的名字
         * @param v 缓存对象
         * @return 是否成功
         */
        protected synchronized boolean put(String k, Cache v) {
            if (k != null && v != null) {
                float factor;
                if ((factor = mCacheFilesSize / mMaxSize) > 0.7) {
                    for (ICacheMonitor monitor : monitors)
                        monitor.onLowCacheSpace(FIFODiskCacheContainer.this, factor);
                }

                if (mCacheFilesSize + v.getSize() > mMaxSize) {
                    trimElement((mCacheFilesSize + v.getSize()) - mMaxSize);
                }
                CacheIndexDesc oldObj = null;
                for (CacheIndexDesc cid : mCacheIndexDesc) {
                    if (cid != null) {
                        if (cid.name.equals(v.getName())) {
                            oldObj = cid;
                            break;
                        }
                    }
                }
                if (oldObj == null) oldObj = new CacheIndexDesc();
                updateDescFromCache(oldObj, v);

//                if (mLiteOrm.queryCount(QueryBuilder.create(CacheIndexDesc.class).where(" _name = ? ", new String[]{oldObj.name})) > 0) {
//                    mLiteOrm.update(oldObj);
//                } else {
//                    mLiteOrm.insert(oldObj);
//                }

                mLiteOrm.save(oldObj);

                //notify monitors
                for (ICacheMonitor monitor : monitors)
                    monitor.onCached(FIFODiskCacheContainer.this, v, maxSize(), mCacheFilesSize);
            }
            return false;
        }

        /***
         * 从cache对象中更新 描述对象
         *
         * @param cacheIndexDesc 缓存描述对象
         * @param cache          缓存对象
         */
        protected synchronized boolean updateDescFromCache(CacheIndexDesc cacheIndexDesc, Cache cache) {
            try {
                File path = new File(mCacheFilesDir, cache.getName());
                StreamUtils.writeByteToStream(new FileOutputStream(path), cache.getData());
                cacheIndexDesc.cacheTime = cache.getTime();
                cacheIndexDesc.size = cache.getSize();
                cacheIndexDesc.name = cache.getName();
                cacheIndexDesc.filePath = path.getAbsolutePath();
                return true;
            } catch (Exception e) {
                if (Config.DEBUG) {
                    e.printStackTrace();
                    ALog.e(TAG, e);
                }
            }
            return false;
        }

        /**
         * 释放元素
         *
         * @param makeSpace 需要释放的空间
         */
        protected synchronized void trimElement(long makeSpace) {
            int alreadyTrimSpace = 0;
            Collections.sort(mCacheIndexDesc, new Comparator<CacheIndexDesc>() {
                @Override
                public int compare(CacheIndexDesc lhs, CacheIndexDesc rhs) {
                    if (lhs.cacheTime < rhs.cacheTime) {
                        return -1;
                    } else if (lhs.cacheTime > rhs.cacheTime) {
                        return 1;
                    }
                    return 0;
                }
            });
            List<CacheIndexDesc> removedElements = new ArrayList<>();
            do {
                CacheIndexDesc removed = mCacheIndexDesc.remove(mCacheIndexDesc.size() - 1);
                alreadyTrimSpace += removed.size;
                mCacheFilesSize -= removed.size;
                removedElements.add(removed);

                if (!monitors.isEmpty()) {
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        FileInputStream fis = new FileInputStream(removed.filePath);
                        StreamUtils.copy(fis, baos, 1024 * 8, true);
                        final byte[] bytes = baos.toByteArray();
                        //notify monitors
                        for (ICacheMonitor monitor : monitors)
                            monitor.onTrimElement(FIFODiskCacheContainer.this,
                                    new Cache(removed.size, removed.name, removed.cacheTime, bytes), maxSize(), mCacheFilesSize);
                    } catch (Exception e) {
                        if (Config.DEBUG) {
                            ALog.e(TAG, "notify trim failure", e);
                        }
                    }
                }

                if (Config.DEBUG) {
                    ALog.d(TAG, String.format("trim elements %s makeSpace count %s  already trim space %s ", removed.toString(), makeSpace, alreadyTrimSpace));
                }
            } while (alreadyTrimSpace < makeSpace);

            if (!removedElements.isEmpty()) {
                //从数据库中删除
                int deleteResult = mLiteOrm.delete(removedElements);
                if (Config.DEBUG) {
                    ALog.d(TAG, "delete elements from databases . delete result " + deleteResult);
                }
            }
        }

        /**
         * 获取文件中获取cache对象
         *
         * @param name 获取文件的key（文件名）
         * @return null或 Cache对象
         */
        protected synchronized Cache get(String name) {
            if (name == null) {
                throw new IllegalArgumentException("Key must not null.");
            }
            ArrayList<CacheIndexDesc> result = mLiteOrm.query(QueryBuilder.create(CacheIndexDesc.class).where("_name = ?", new Object[]{name}));
            if (result != null && !result.isEmpty()) {
                CacheIndexDesc desc = result.get(0);
                File path = new File(desc.filePath);
                if (path.exists()) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(path);
                        return new Cache(desc.size, desc.name, desc.cacheTime, StreamUtils.readToByte(fis));
                    } catch (FileNotFoundException e) {
                        if (Config.DEBUG) {
                            ALog.e(TAG, e);
                        }
                    } finally {
                        StreamUtils.close(fis);
                    }

                }
            }
            return null;
        }

        /**
         * 请出磁盘缓存
         *
         * @return 是否清除成功
         */
        public synchronized boolean clear() {
            try {
                updateCacheIndexDesc();
                Iterator<CacheIndexDesc> it = mCacheIndexDesc.iterator();
                while (it.hasNext()) {
                    CacheIndexDesc next = it.next();
                    if (next != null) {
                        if (!new File(next.filePath).delete()) {
                            return false;
                        } else {
                            it.remove();
                            mLiteOrm.delete(CacheIndexDesc.class);
                            for (ICacheMonitor m : monitors) {
                                if (m != null)
                                    m.onRemovedElement(FIFODiskCacheContainer.this, new Cache(next.size, next.name, next.cacheTime, null), mMaxSize, currentSize());
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                if (Config.DEBUG) {
                    ALog.e(TAG, e);
                }
            }
            return false;
        }

        /**
         * 移除指定的缓存对象
         *
         * @param cache 需要移除的缓存对象
         * @return true or false
         */
        public boolean remove(Cache cache) {
            return remove(cache.getName());
        }

        /**
         * 移除指定的缓存对象对应的key
         *
         * @param name 需要移除的缓存对应的key
         * @return true or false
         */
        public boolean remove(String name) {
            for (CacheIndexDesc next : mCacheIndexDesc) {
                if (next != null && next.name.equals(name)) {
                    mLiteOrm.delete(WhereBuilder.create(CacheIndexDesc.class).where("_name = ? ", next.name));
                    File file = new File(next.filePath);
                    if (file.exists()) {
                        //notify
                        if (!monitors.isEmpty()) {
                            byte[] bytes = null;
                            try {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                FileInputStream fis = new FileInputStream(next.filePath);
                                StreamUtils.copy(fis, baos, 1024 * 8, true);
                                bytes = baos.toByteArray();
                            } catch (FileNotFoundException e) {
                                if (Config.DEBUG) {
                                    ALog.e(TAG, "notify remove failure", e);
                                }
                            }

                            for (ICacheMonitor monitor : monitors)
                                monitor.onRemovedElement(FIFODiskCacheContainer.this, new Cache(next.size, next.name, next.cacheTime, bytes), maxSize(), mCacheFilesSize);
                        }

                        if (file.delete()) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return false;
        }

        @Override
        public synchronized void registerCacheMonitor(ICacheMonitor monitor) {
            monitors.add(monitor);
        }

        @Override
        public synchronized void unregisterCacheMonitor(ICacheMonitor monitor) {
            monitors.remove(monitor);
        }
    }

    /**
     * 缓存表
     */
    public static class CacheIndexDesc {

        @Unique
        @Column("_id")
        @PrimaryKey(AssignType.BY_MYSELF)
        private long id;

        @Column("_name")
        private String name;

        @Column("file_path")
        private String filePath;

        @Column("cache_time")
        private long cacheTime;

        @Column("size")
        private long size;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheIndexDesc that = (CacheIndexDesc) o;

            return !(name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "CacheIndexDesc{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", cacheTime=" + cacheTime +
                    ", size=" + size +
                    '}';
        }
    }
}
