package com.itek.library_core.db;


import com.itek.library_core.repository.cache.CacheEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;


/**
 * Author:：simon
 * Date：2019-12-12:18:15
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

@Database(entities = {CacheEntity.class}, version = 1)
public abstract class CacheDatabase extends RoomDatabase {

    public abstract CacheEntityDao cacheEntityDao();
}

