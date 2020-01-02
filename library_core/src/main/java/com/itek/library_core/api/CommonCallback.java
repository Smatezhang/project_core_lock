package com.itek.library_core.api;

/**
 * Author:：simon
 * Date：2019-12-12:17:48
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public abstract class CommonCallback<T> {

    public CommonCallback() {}


    public void onComplete(BaseResponse<T> baseResponse) {}
    public void onComplete() {}
    public abstract void onComplete(T Result);

    public abstract void onBusinessError(BaseResponse baseResponse);

    public abstract void onNetworkError(ApiResponse apiResponse);
}
