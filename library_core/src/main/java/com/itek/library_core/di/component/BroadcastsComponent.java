package com.itek.library_core.di.component;

import android.content.BroadcastReceiver;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * Author:：simon
 * Date：2019-12-14:12:57
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

@Subcomponent(modules = {
        AndroidInjectionModule.class,
})
public interface BroadcastsComponent extends AndroidInjector<BroadcastReceiver> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BroadcastReceiver> {
    }


}