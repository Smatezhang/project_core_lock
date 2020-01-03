package com.itek.library_core.di.component;

import com.google.gson.Gson;
import com.itek.library_core.BaseApplication;
import com.itek.library_core.di.module.AppModule;
import com.itek.library_core.di.module.CacheModule;
import com.itek.library_core.di.module.GlobalConfigModule;
import com.itek.library_core.di.module.HttpClientModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import okhttp3.OkHttpClient;

/**
 * Author:：simon
 * Date：2019-12-14:12:55
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        HttpClientModule.class,
        GlobalConfigModule.class,
//        ServiceModule.class,
//        CacheModule.class,
})
public interface BaseAppComponent {


    Gson gson();

//    ServiceManager serviceManager();


    OkHttpClient okHttpClient();

    BaseApplication baseApplication();

    void inject(BaseApplication application);

//    CacheDatabase cacheDatabase();


}
