package com.lovely3x.common.utils;

import com.lovely3x.jsonparser.model.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lovely3x on 17/3/10.
 */

public class PageableData implements Serializable{

    /**
     * total : 0
     * pages : 0
     * list : [{"headimg":"","phone":"18223960536","nickname":"j5iq5ti87r"}]
     * nowpage : 0
     */
    private int total;

    private int pages;

    private int nowpage;

    private String list;

    private List mList;

    private Class mParedClazz;

    private JSONArray mJSONArrayList;

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }


    public int getTotal() {
        return total;
    }

    public int getPages() {
        return pages;
    }

    public int getNowpage() {
        return nowpage;
    }

    public <T> List<T> getList(Class<T> clazz) {
        createJSONArrayIfNeeded();
        if (clazz != mParedClazz) {
            mParedClazz = clazz;
            try {
                mList = mJSONArrayList.createObjects(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    private void createJSONArrayIfNeeded() {
        if (mJSONArrayList == null) mJSONArrayList = new JSONArray(list);
    }

    public JSONArray getJSONArrayList() {
        createJSONArrayIfNeeded();
        return mJSONArrayList;
    }
}
