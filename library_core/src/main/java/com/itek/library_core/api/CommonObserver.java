package com.itek.library_core.api;


import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

/**
 * Author:：simon
 * Date：2019-12-12:17:48
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public  class CommonObserver<T> implements Observer<ApiResponse<BaseResponse<T>>> {

    private CommonCallback<T> commonCallback;

    public CommonObserver(CommonCallback<T> commonCallback) {
        this.commonCallback = commonCallback;
    }
    public void onChanged(@Nullable ApiResponse<BaseResponse<T>> apiResponse) {
        this.commonCallback.onComplete();
        if(apiResponse.code != 200) {
            this.commonCallback.onNetworkError(apiResponse);
        } else if(apiResponse.body == null||"-1".equals(apiResponse.body.getErrorCode()) ) {
            this.commonCallback.onBusinessError(apiResponse.body);
        } else {
            this.commonCallback.onComplete(apiResponse.body);
            this.commonCallback.onComplete((T)((BaseResponse)apiResponse.body).getData());
        }
    }
}

