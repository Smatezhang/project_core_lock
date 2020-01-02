package com.itek.library_core.api;


import com.itek.library_core.vo.Resource;

/**
 * Author:：simon
 * Date：2019-12-12:18:01
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public abstract class NetBoundCallback<T> {

    public NetBoundCallback() {
    }

    /*
     * 缓存读取成功
     * */
    public void onCacheComplete(T Result) {
    }

    public void onComplete(Resource<T> resource) {
    }

    public void onComplete() {
    }

    public abstract void onBusinessError(Resource<T> resource);

    public abstract void onNetworkError(Resource<T> resource);
}
