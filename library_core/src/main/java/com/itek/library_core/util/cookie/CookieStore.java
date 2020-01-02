package com.itek.library_core.util.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Author:：simon
 * Date：2019-12-14:13:21
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public interface CookieStore {


    /**  添加cookie */
    void add(HttpUrl httpUrl, Cookie cookie);

    /** 添加指定httpurl cookie集合 */
    void add(HttpUrl httpUrl, List<Cookie> cookies);

    /** 根据HttpUrl从缓存中读取cookie集合 */
    List<Cookie> get(HttpUrl httpUrl);

    /** 获取全部缓存cookie */
    List<Cookie> getCookies();

    /**  移除指定httpurl cookie集合 */
    boolean remove(HttpUrl httpUrl, Cookie cookie);

    /** 移除所有cookie */
    boolean removeAll();


}
