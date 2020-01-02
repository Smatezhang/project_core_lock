package com.itek.library_core.contract;

import java.util.Map;

/**
 * Author:：simon
 * Date：2019/7/24:7:08 PM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class _BleLogEvent {




    private Map<String, String> map;

    private String log;

    public _BleLogEvent(Map<String, String> map, String log) {
        this.map = map;
        this.log = log;
    }

    public String getLog() {
        return log;
    }

    public Map<String, String> getMap() {
        return map;
    }
}
