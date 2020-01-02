package com.itek.library_core.base;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import android.util.Log;
import android.view.View;

import com.itek.library_core.binding.command.BindingAction;
import com.itek.library_core.binding.command.BindingCommand;


/**
 * Author:：simon
 * Date：2019/6/24:11:12 AM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class ToolbarViewModel extends BaseViewModel {
    //标题文字
    public ObservableField<String> titleText = new ObservableField<>("");
    //右边文字
    public ObservableField<String> rightText = new ObservableField<>("更多");
    //右边文字的观察者
    public ObservableInt rightTextVisibleObservable = new ObservableInt(View.GONE);
    public ObservableInt leftRowVisibleObservable = new ObservableInt(View.GONE);
    //右边图标的观察者
    public ObservableInt rightIconVisibleObservable = new ObservableInt(View.GONE);
    //兼容databinding，去泛型化
    public ToolbarViewModel toolbarViewModel;

    public ToolbarViewModel() {

        toolbarViewModel = this;
    }

    /**
     * 设置标题
     *
     * @param text 标题文字
     */
    public void setTitleText(String text) {
        titleText.set(text);
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    public void setRightText(String text) {
        rightText.set(text);
    }

    /**
     * 设置右边文字的显示和隐藏
     *
     * @param visibility
     */
    public void setRightTextVisible(int visibility) {
        rightTextVisibleObservable.set(visibility);
    }

    /**
     * 设置右边图标的显示和隐藏
     *
     * @param visibility
     */
    public void setRightIconVisible(int visibility) {
        rightIconVisibleObservable.set(visibility);
    }


  /**
     * 设置右边图标的显示和隐藏
     *
     * @param visibility
     */
    public void setleftRowVisible(int visibility) {
        leftRowVisibleObservable.set(visibility);
    }

    /**
     * 返回按钮的点击事件
     */
    public final BindingCommand backOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Log.e("finish","finish");

            finish();
        }
    });

    public BindingCommand rightTextOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            rightTextOnClick();
        }
    });
    public BindingCommand rightIconOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            rightIconOnClick();
        }
    });

    /**
     * 右边文字的点击事件，子类可重写
     */
    protected void rightTextOnClick() {
    }

    /**
     * 右边图标的点击事件，子类可重写
     */
    protected void rightIconOnClick() {
    }


}
