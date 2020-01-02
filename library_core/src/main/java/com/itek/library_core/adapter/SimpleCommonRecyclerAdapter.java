package com.itek.library_core.adapter;

import android.view.View;

import androidx.databinding.ViewDataBinding;

/**
 * Author:：simon
 * Date：2019-12-12:17:39
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public class SimpleCommonRecyclerAdapter<T> extends CommonRecyclerAdapter<T, CommonRecyclerAdapter.ViewHolder<ViewDataBinding>> {

    private OnItemClickListener onItemClickListener;

    public SimpleCommonRecyclerAdapter(int layoutId, int brId) {
        super(layoutId, brId);
    }

    /**
     * 设置监听事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder<ViewDataBinding> getHolder(View view) {
        return new ViewHolder<ViewDataBinding>(view, onItemClickListener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
