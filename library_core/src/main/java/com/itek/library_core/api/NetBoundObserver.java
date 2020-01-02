package com.itek.library_core.api;


import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.itek.library_core.vo.Resource;



/**
 * Author:：simon
 * Date：2019-12-12:18:02
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public class NetBoundObserver<T> implements Observer<Resource<T>> {
    private NetBoundCallback<T> netBoundCallback;

    public NetBoundObserver(NetBoundCallback<T> netBoundCallback) {
        this.netBoundCallback = netBoundCallback;
    }

    @Override
    public void onChanged(@Nullable Resource<T> tResource) {

        switch (tResource.status) {
            case LOADING:
                if (tResource.data != null) {
                    netBoundCallback.onCacheComplete(tResource.data);
                }
                break;
            case SUCCESS:
                netBoundCallback.onComplete(tResource);
                break;
            case BUSSINESSERROR:
                netBoundCallback.onComplete();
                netBoundCallback.onBusinessError(tResource);
                break;
            case ERROR:
                netBoundCallback.onComplete();
                netBoundCallback.onNetworkError(tResource);
                break;
        }
    }
}
