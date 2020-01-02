package com.itek.library_core.binding.command;
/**
 * Author:：simon
 * Date：2019/6/24:11:12 AM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public interface BindingConsumer<T> {
    void call(T t);
}
