package com.example.wangbo.ourapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.example.wangbo.ourapp.utils.ArrayUtils;
import com.example.wangbo.ourapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 简化BaseAdapter
 *
 * @param <T>
 */
public abstract class ListAdapter<T> extends BaseAdapter {

    public static final String TAG = "ListAdapter";

    /**
     * 条目是否可以被点击
     */
    private boolean itemClickable;

    /**
     * 上下文资源访问对象
     */
    protected Context mContext;

    /**
     * 数据集合
     */
    protected List<T> datas;


    protected LayoutInflater mLayoutInflater;

    /**
     * 选择模式 支持的选择的模式有
     * {@link AbsListView#CHOICE_MODE_MULTIPLE} 多选
     * {@link AbsListView#CHOICE_MODE_SINGLE} 单选
     * {@link AbsListView#CHOICE_MODE_NONE} 无选择模式
     */
    private int checkMode = AbsListView.CHOICE_MODE_NONE;

    /**
     * 选择表
     * 用于保存用户的id和是否选中关系的容器
     */
    protected final Map<String, Boolean> checkTable = new HashMap<>();

    protected final List mTempList = new ArrayList<>();

    /**
     * 条目点击监听器
     */
    protected OnItemClickedListener<T> mOnItemClickedListener;

    /**
     * 视图点击监听器
     */
    protected ViewClickedListener mViewClickedListener;

    private OnCheckedItemChangedListener mOnCheckedItemChangedListener;

    /**
     * 通过一个list集合来创建适配器
     *
     * @param datas   数据集合
     * @param context 上下文对象
     */
    public ListAdapter(List<T> datas, Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        setData(datas);
    }

    /**
     * 判断指定的id是否选中
     *
     * @param id 需要查询状态的id
     * @return
     */
    public boolean isChecked(String id) {
        Boolean isChecked = checkTable.get(id);
        return isChecked != null && isChecked;
    }

    /**
     * 设置指定id的状态是否为选中
     *
     * @param id        需要选中或非选中的视图的id
     * @param isChecked 设置这个id是否为tre false
     */
    public void check(String id, Boolean isChecked) {
        switch (checkMode) {
            case AbsListView.CHOICE_MODE_NONE:
                //Ignored the event.
                return;//
            case AbsListView.CHOICE_MODE_SINGLE:
                //单选模式下,在选择之前我们先清除掉之前的数据
                checkTable.clear();
                break;
            case AbsListView.CHOICE_MODE_MULTIPLE:
                //Ignored
                break;
            default:
                throw new IllegalArgumentException();
        }

        checkTable.put(id, isChecked);

        if (mOnCheckedItemChangedListener != null) {
            mOnCheckedItemChangedListener.onCheckedItemChanged(this, getCheckItems().size(), id, isChecked);
        }
    }


    /**
     * 获取选中的条目
     *
     * @return
     */
    public List<String> getCheckItems() {
        final List<String> checkedItems = mTempList;
        checkedItems.clear();

        Set<Map.Entry<String, Boolean>> entrySet = checkTable.entrySet();
        for (Map.Entry<String, Boolean> entry : entrySet) {
            if (entry.getValue() != null && entry.getValue())
                checkedItems.add(entry.getKey());
        }

        return checkedItems;
    }

    public Map<String, Boolean> getCheckTable() {
        return checkTable;
    }

    /**
     * 从选中表中 寻找一个已经选中的条目
     * 不支持多选模式
     *
     * @return null 或 选中项
     */
    public String findCheckedItem() {
        if (checkMode == AbsListView.CHOICE_MODE_SINGLE) {
            if (checkTable != null && !checkTable.isEmpty()) {
                for (Map.Entry<String, Boolean> next : checkTable.entrySet()) {
                    String key = next.getKey();
                    Boolean value = next.getValue();
                    if (value != null && value) {
                        return key;
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException("Find checked item only use on single check mode.");
        }

        return null;
    }


    /**
     * 设置选择模式
     *
     * @param mode
     */
    public void setCheckMode(int mode) {
        if (checkMode != mode) checkTable.clear();
        switch (mode) {
            case AbsListView.CHOICE_MODE_MULTIPLE:
                break;
            case AbsListView.CHOICE_MODE_SINGLE:
                checkTable.clear();
                break;
            case AbsListView.CHOICE_MODE_NONE:

                return;
            default:
                throw new IllegalArgumentException();
        }
        this.checkMode = mode;
    }

    public int getCheckMode() {
        return checkMode;
    }

    /**
     * 切换指定的id的选中状态
     *
     * @param id 需要切换的id
     */
    public void toggle(String id) {
        check(id, !isChecked(id));
    }


    /**
     * 通过一个数组来创建adapter,使用默认的图片包装器
     *
     * @param datas   需要显示的数据集合
     * @param context 上下文对象
     */
    public ListAdapter(final T[] datas, Context context) {
        this(ArrayUtils.ArrayToList(datas), context);
    }

    /**
     * 将数据添加数据集合的头部
     *
     * @param data 需要添加的数据
     */
    public void addDataToFirst(List<T> data) {
        if (data == null)
            return;
        if (this.datas == null) {
            setData(data);
        } else {
            this.datas.addAll(0, data);
        }
        notifyDataSetChanged();
    }

    /**
     * 从适配器中删除一一个元素
     *
     * @param t 需要删除的元素
     * @return 是否删除成功
     */
    public boolean delete(T t) {
        if (t == null || datas == null)
            return false;
        boolean result = datas.remove(t);
        notifyDataSetChanged();
        return result;
    }

    /**
     * @return 返回当前条目是否可以被点击
     */
    public boolean isItemClickable() {
        return itemClickable;
    }

    /**
     * 设置条目是否可被点击
     * 如果设置不可以点击,那么将不会设置监听器到条目视图上面
     *
     * @param itemClickable 是否可以点击
     */
    public void setItemClickable(boolean itemClickable) {
        this.itemClickable = itemClickable;
    }

    /**
     * 将数据添加到末尾
     *
     * @param data 需要添加的数据
     */
    public void addDataToLast(List<T> data) {
        if (data == null)
            return;
        if (this.datas == null) {
            setData(data);
        } else {
            this.datas.addAll(data);
        }
        notifyDataSetChanged();
    }


    /**
     * 将本次的数据添加到原有数据的末尾
     *
     * @param data 需要添加的数据
     */
    public void addDataToLast(T data) {
        if (data != null) {
            this.datas.add(data);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置数据,原本的数据将会被清空
     *
     * @param datas 需要设置的数据
     */
    public void setData(final List<T> datas) {
        this.datas = (datas == null ? new ArrayList<T>() : datas);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        ViewUtils.mainThreadChecker();
        super.notifyDataSetChanged();
    }

    /**
     * 清除掉当前适配器中的数据
     */
    public void clear() {
        if (datas != null) {
            datas.clear();
            notifyDataSetChanged();
        }
    }

    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获取上下文对象
     */
    public Context getContext() {
        return this.mContext;
    }

    /**
     * @return 获取数据集合
     */
    public List<T> getDatas() {
        return datas;
    }

    /**
     * 设置条目点击监听器
     *
     * @param t 需要设置的监听器
     */
    public void setOnItemClickedListener(OnItemClickedListener<T> t) {
        this.mOnItemClickedListener = t;
        setItemClickable(true);
    }

    /**
     * @return 获取视图充气机
     */
    protected LayoutInflater getLayoutInflater() {
        if (this.mLayoutInflater == null) {
            this.mLayoutInflater = LayoutInflater.from(mContext);
        }
        return mLayoutInflater;
    }

    /**
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null || ((BaseViewHolder) convertView.getTag()).getType() != getItemViewType(position)) {
            holder = createViewHolder(position, parent);
            holder.mRootView.setTag(holder);
            holder.setType(getItemViewType(position));
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        if (itemClickable) bindItemClickListener(holder.mRootView, position);
        handleData(position, holder);
        return holder.mRootView;
    }

    /**
     * 为条目设置监听器
     *
     * @param itemView 需要绑定监听器的根视图
     * @param position 需呀绑定监听器的位置
     */
    private void bindItemClickListener(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickedListener != null)
                    mOnItemClickedListener.onClicked(position, datas.get(position));
            }
        });
    }

    /**
     * 回调这个方法创建一个viewHolder
     *
     * @param position 当前需要创建视图的位置
     * @param parent   parent 咯
     * @return 创建的viewHolder
     */
    public abstract
    @NonNull
    BaseViewHolder createViewHolder(int position, ViewGroup parent);

    /**
     * 通过这个方法用户将数据绑定到holder上面去
     *
     * @param position 当前的位置
     * @param holder   holder
     */
    public abstract void handleData(int position, @NonNull BaseViewHolder holder);


    public void setOnViewClickedListener(ViewClickedListener listener) {
        this.mViewClickedListener = listener;
    }

    /**
     * 视图点击监听器
     */
    public interface ViewClickedListener {

        /**
         * 当视图被点击后执行
         *
         * @param position 点击的视图所属的位置
         * @param view     被点击的视图对象
         */
        void onViewClicked(int position, View view);
    }


    /**
     * list 条目点击监听器
     */
    public interface OnItemClickedListener<T> {

        /**
         * 当条目被点击后执行
         *
         * @param t        被点击的条目的数据
         * @param position 被点击的条目的位置
         */
        void onClicked(int position, T t);
    }


    /**
     * 选中条目变化监听器
     */
    public interface OnCheckedItemChangedListener {

        /**
         * 当选中条目发生变化后执行
         *
         * @param tListAdapter
         * @param checkedItemCount 当前选中的条目数量
         * @param changedItemId    当前变化的条目的id
         * @param isChecked        变化的条目的状态是否为选中
         */
        void onCheckedItemChanged(ListAdapter tListAdapter, int checkedItemCount, String changedItemId, boolean isChecked);

    }
}
