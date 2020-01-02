package com.itek.library_core.di.component;

import com.itek.library_core.base.BaseFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Author:：simon
 * Date：2019-12-14:12:53
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

@Subcomponent(modules = {
        AndroidInjectionModule.class, AndroidSupportInjectionModule.class,
})
public interface BaseFragmentComponent extends AndroidInjector<BaseFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BaseFragment> {
    }

}
