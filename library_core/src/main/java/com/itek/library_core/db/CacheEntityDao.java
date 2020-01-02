package com.itek.library_core.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.itek.library_core.repository.cache.CacheEntity;


/**
 * Author:：simon
 * Date：2019-12-12:18:15
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
@Dao
public interface CacheEntityDao {

    @Query("SELECT * FROM CacheEntity WHERE cachekey = :key")
    LiveData<CacheEntity> LoadCache(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertCache(CacheEntity... cacheEntities);

}

