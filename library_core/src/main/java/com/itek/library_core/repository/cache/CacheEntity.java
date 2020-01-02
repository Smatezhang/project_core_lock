package com.itek.library_core.repository.cache;


import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Author:：simon
 * Date：2019-12-12:18:16
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */

@Entity
public class CacheEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long cid;

    @ColumnInfo(name = "cachekey")
    private String key;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }


}
