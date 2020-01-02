package com.itek.library_core.binding.viewadapter.button;

import androidx.databinding.BindingAdapter;
import android.widget.Button;


public final class ViewAdapter {

    @BindingAdapter(value = {"resid"}, requireAll = false)
    public static void setImageResid(Button button, int resid) {
        button.setBackgroundResource(resid);
    }


}
