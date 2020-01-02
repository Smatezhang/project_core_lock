package com.itek.library_core.di.module;

import com.itek.library_core.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Author:：simon
 * Date：2019-12-14:12:58
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
@Module
public class AppModule {

    private BaseApplication application;

    public AppModule(BaseApplication application){
        this.application = application;
    }

    @Singleton
    @Provides
    public BaseApplication provideApplication() {
        return application;
    }

}