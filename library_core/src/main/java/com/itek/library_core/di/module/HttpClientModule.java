package com.itek.library_core.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.itek.library_core.api.RequestInterceptor;
import com.itek.library_core.api.converter.GsonConverterFactory;
import com.itek.library_core.util.cookie.CookieJarImpl;
import com.itek.library_core.util.cookie.PersistentCookieStore;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Author:：simon
 * Date：2019-12-14:13:01
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
@Module
public class HttpClientModule {
    private static final int TIME_OUT_SECONDS = 20;
    private Context context;

    public HttpClientModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl) {
        return builder
                .baseUrl(httpUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder okHttpClient, Interceptor intercept
            , List<Interceptor> interceptors, CookieJar cookieJar) {
        OkHttpClient.Builder builder = okHttpClient
                .connectTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .addNetworkInterceptor(intercept);

        if (interceptors != null && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }


    @Singleton
    @Provides
    CookieJar providerCookieJar() {
        return new CookieJarImpl(new PersistentCookieStore(context));
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    Interceptor provideIntercept(RequestInterceptor interceptor) {
        return interceptor;
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }




}
