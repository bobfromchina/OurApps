package com.lovely3x.common.utils.searcher.textsearcher;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.lovely3x.common.utils.searcher.textsearcher.searcherimpls.ContainSearcher;
import com.lovely3x.common.utils.searcher.textsearcher.searcherimpls.EndsSearcher;
import com.lovely3x.common.utils.searcher.textsearcher.searcherimpls.ReSearcher;
import com.lovely3x.common.utils.searcher.textsearcher.searcherimpls.StartsSearcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 文本搜索工具类
 * Created by lovely3x on 16/6/8.
 */
public class TextSearchEngine {

    /**
     * 默认的搜索工厂实现类
     */
    private final SearchFactory defaultSearchFactory = new SearchFactory() {
        StartsSearcher startsSearcher = new StartsSearcher();
        EndsSearcher endsSearcher = new EndsSearcher();
        ContainSearcher containSearcher = new ContainSearcher();
        ReSearcher reSearcher = new ReSearcher();

        @Override
        public Searcher obtainSearcher(int searchMode) {
            switch (searchMode) {
                case SearchMode.RE:
                    return reSearcher;
                case SearchMode.CONTAIN:
                    return containSearcher;
                case SearchMode.ENDS:
                    return endsSearcher;
                case SearchMode.STARTS:
                    return startsSearcher;
            }
            return null;
        }
    };

    /**
     * 自定义的搜索工厂
     */
    private SearchFactory mCustomFactory;

    /**
     * 设置的搜索数据源
     */
    private List<? extends Searchable> mSource;

    /**
     * 异步线程队列
     */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    /**
     * 主线程Looper Handler
     */
    private final Handler MainHandler = new Handler(Looper.myLooper());

    /**
     * 任务关系表
     */
    private final SparseArray<Future> tasksTable = new SparseArray<>();

    /**
     * 任务id生成数
     */
    private int futureIndex;

    /**
     * 搜索监听器
     */
    private SearchListener mSearchListener;

    /**
     * @param searchFactory
     */
    public void setSearchFactory(SearchFactory searchFactory) {
        this.mCustomFactory = searchFactory;
    }

    /**
     * 设置需要搜索的数据源
     *
     * @param source 设置数据源
     */
    public void setDatSource(List<? extends Searchable> source) {
        this.mSource = source;
    }

    /**
     * 之前多少
     *
     * @param searchListener
     */
    public void setSearchResultListener(SearchListener searchListener) {
        this.mSearchListener = searchListener;
    }

    /**
     * 搜索
     *
     * @param keyword    关键字
     * @param searchMode 匹配模式
     * @param async      是否是异步操作
     */
    public int search(final String keyword, final int searchMode, boolean async) {
        final IndexRunnable run = new IndexRunnable() {
            @Override
            public void run() {
                final ArrayList<Searchable> result = new ArrayList<>();
                try {
                    Searcher searcher = (mCustomFactory == null ? defaultSearchFactory.obtainSearcher(searchMode) : mCustomFactory.obtainSearcher(searchMode));
                    if (searcher != null && mSource != null) {
                        for (Searchable searchable : mSource) {
                            searcher.prepare(keyword, searchable.getCondition());
                            if (searcher.match()) {
                                result.add(searchable);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cancelAndRemoveSearchTask(mIndex);
                    if (mSearchListener != null) {
                        final SearchListener listener = mSearchListener;
                        MainHandler.post(new IndexRunnable() {
                            @Override
                            public void run() {
                                listener.onSearched(result);
                            }
                        });
                    }
                }
            }
        };

        if (async) {
            return addTask(run);
        } else {
            run.run();
        }
        return -1;
    }

    /**
     * 移除任务
     *
     * @param taskID 任务id
     */
    public void cancelAndRemoveSearchTask(int taskID) {
        synchronized (tasksTable) {
            Future task = this.tasksTable.get(taskID);
            if (task != null) {
                if (!task.isCancelled()) {
                    task.cancel(true);
                    tasksTable.remove(taskID);
                } else {
                    tasksTable.remove(taskID);
                }
            }
        }
    }

    /**
     * 添加任务
     *
     * @param run 需要添加的任务
     * @return
     */
    private int addTask(Runnable run) {
        synchronized (tasksTable) {
            Future<?> future = executor.submit(run);
            tasksTable.put(futureIndex++, future);
            return futureIndex;
        }
    }

    /**
     * 搜索监听器
     */
    public interface SearchListener {
        /**
         * 当搜索完成后执行
         *
         * @param results 搜索到的结果列表
         */
        void onSearched(List<Searchable> results);
    }

    /**
     * 可设置id的runnable
     */
    public static abstract class IndexRunnable implements Runnable {
        protected int mIndex;

        public void setIndex(int index) {
            this.mIndex = index;
        }
    }


}