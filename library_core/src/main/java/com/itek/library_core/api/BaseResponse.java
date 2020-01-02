package com.itek.library_core.api;

import java.io.Serializable;

/**
 * Author:：simon
 * Date：2019-12-12:17:47
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public class BaseResponse <T> implements Serializable {

    private T data;

    private String result;
    private String infos;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return result;
    }

    public void setErrorCode(String errorCode) {
        this.result = errorCode;
    }

    public String getErrorMsg() {
        return infos;
    }

    public void setErrorMsg(String errorMsg) {
        this.infos = errorMsg;
    }
}
