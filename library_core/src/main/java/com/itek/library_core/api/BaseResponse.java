package com.itek.library_core.api;

import java.io.Serializable;

/**
 * Author:：simon
 * Date：2019-12-12:17:47
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */



public class BaseResponse<T> {

    private String infos;
    private String result;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T result) {
        this.data = result;
    }

    public boolean isOk() {
        return "1".equals(result);
    }
    public String getMessage() {
        return infos;
    }

    public void setMessage(String message) {
        this.infos = message;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

}
