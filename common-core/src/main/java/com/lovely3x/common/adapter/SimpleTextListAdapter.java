package com.lovely3x.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovely3x.common.R;
import com.lovely3x.common.utils.Displayable;

import java.util.List;

/**
 * simple text list adapter
 * Created by lovely3x on 15-12-1.
 */
public class SimpleTextListAdapter<T extends Displayable> extends ListAdapter<T> {

    public SimpleTextListAdapter(List<T> datas, Context context) {
        super(datas, context);
    }

    @NonNull
    @Override
    public BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.view_simple_text_list_item, parent, false));
    }

    @Override
    public void handleData(int position, @NonNull BaseViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mTextView.setText(getDatas().get(position).display());
    }

    /**
     * holder hold the views
     */
    public static class ViewHolder extends BaseViewHolder {
        public TextView mTextView;

        /**
         * please don't modify the constructor
         *
         * @param rootView the root view
         */
        public ViewHolder(View rootView) {
            super(rootView);
            mTextView = (TextView) rootView;
        }
    }
}
