package com.itek.library_core.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.itek.library_core.viewmodel.HivescmViewModelFactory;

import dagger.Binds;
import dagger.Module;

/**
 * Author:：simon
 * Date：2019-12-14:12:59
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
@Module
public abstract class BaseViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(HivescmViewModelFactory factory);
}
