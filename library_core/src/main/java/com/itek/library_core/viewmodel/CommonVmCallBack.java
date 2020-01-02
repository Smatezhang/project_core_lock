package com.itek.library_core.viewmodel;

import com.itek.library_core.vo.Resource;

/**
 * Author:：simon
 * Date：2019-12-14:13:29
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public abstract class CommonVmCallBack<T> {
    public CommonVmCallBack(Resource<T> resource){
        DispatchResource(resource);
    }
    public void DispatchResource(Resource<T> resource) {
        switch (resource.status) {
            case LOADING:
                this.onLoading(resource);
                break;
            case SUCCESS:
                this.onComplete(resource);
                break;
        }
    }

    public abstract void onLoading(Resource<T> resource);

    public abstract void onComplete(Resource<T> resource);
}

