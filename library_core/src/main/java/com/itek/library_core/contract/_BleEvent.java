package com.itek.library_core.contract;

import java.util.Map;

/**
 * Author:：simon
 * Date：2019/7/12:4:19 PM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class _BleEvent {

    private String type; // 1. 获取固件的总体信息
    private Map map;

    public _BleEvent(String type, Map map) {
        this.type = type;
        this.map = map;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
