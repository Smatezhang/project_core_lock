package com.itek.library_core.vo;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.itek.library_core.vo.Status.BUSSINESSERROR;
import static com.itek.library_core.vo.Status.ERROR;
import static com.itek.library_core.vo.Status.LOADING;
import static com.itek.library_core.vo.Status.SUCCESS;

/**
 * Author:：simon
 * Date：2019-12-12:18:03
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable String msg,@Nullable T data) {
        return new Resource<>(LOADING, data, msg);
    }

    public static <T> Resource<T> bussinessError(String msg, @Nullable T data) {
        return new Resource<>(BUSSINESSERROR, data, msg);
    }
}
