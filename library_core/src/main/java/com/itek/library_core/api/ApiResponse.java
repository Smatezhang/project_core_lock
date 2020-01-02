package com.itek.library_core.api;

import android.util.Log;

import java.io.IOException;
import java.io.Serializable;

import androidx.annotation.Nullable;
import retrofit2.Response;

/**
 * Author:：simon
 * Date：2019-12-12:17:42
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class ApiResponse<T> implements Serializable {

    public static String TAG = "ApiResponse";

    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;


    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String TAG) {
        ApiResponse.TAG = TAG;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    public ApiResponse(T t) {
        code = 200;
        body = t;
        errorMessage = null;
    }

    public ApiResponse(Response<T> response) {
//       String contentType =  header.get("Content-Type");
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Log.e(TAG, "error while parsing response" + ignored.getMessage());
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }
}

