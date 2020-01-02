package com.itek.library_core.base;


import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.itek.library_core.lifecycle.RxFragment;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * Author:：simon
 * Date：2019-12-12:18:07
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

public class BaseInjectFragment extends RxFragment {

    protected void inject() {
        AndroidSupportInjection.inject(this);
//        AndroidInjection.inject(this);
        if (injectRouter())
            ARouter.getInstance().inject(this);
    }

    protected boolean injectRouter() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        inject();
        super.onAttach(activity);
    }
}
