package com.itek.library_core.binding.viewadapter.text;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public final class ViewAdapter {

    @BindingAdapter(value = {"colorresid"}, requireAll = false)
    public static void setTextColor(TextView textView, int resid) {
           textView.setTextColor(resid);
    }

}
