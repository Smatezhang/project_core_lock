package com.itek.library_core.util.interceptor;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ChangeBaseUrlInterceptor implements Interceptor {

    private Context context;
    private Map<String, String> baseUrl;
    private String heardkey;

    public ChangeBaseUrlInterceptor(Context context, @NonNull Map<String, String> baseUrl, @NonNull String heardkey) {
        this.context = context;
        this.baseUrl = baseUrl;
        this.heardkey = heardkey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        //获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //从request中获取headers，通过给定的键url_name
        List<String> headerValues = request.headers(heardkey);
        if (headerValues != null && headerValues.size() > 0) {
            //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
            builder.removeHeader(heardkey);
            //匹配获得新的BaseUrl
            String headerValue = headerValues.get(0);
            HttpUrl newBaseUrl = null;
            //从request中获取原有的HttpUrl实例oldHttpUrl
            HttpUrl oldHttpUrl = request.url();
            newBaseUrl = oldHttpUrl;
            if(baseUrl != null){
                String mUrl = baseUrl.get(headerValue);
                if (!TextUtils.isEmpty(mUrl)){
                    newBaseUrl = HttpUrl.parse(mUrl);
                }
            }

            //重建新的HttpUrl，修改需要修改的url部分
            HttpUrl newFullUrl = oldHttpUrl
                    .newBuilder()
                    .scheme(newBaseUrl.scheme())
                    .host(newBaseUrl.host())
                    .port(newBaseUrl.port())
                    .build();

            //重建这个request，通过builder.url(newFullUrl).build()；
            //然后返回一个response至此结束修改
            return chain.proceed(builder.url(newFullUrl).build());
        } else {
            return chain.proceed(request);
        }

    }
}
