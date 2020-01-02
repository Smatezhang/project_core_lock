package com.itek.library_core.bus.event;

/**
 * Author:：simon
 * Date：2019-12-11:11:43
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class DeviceConnectEvent {
    private String mac;
    private boolean isConnected;

    public DeviceConnectEvent(String mac, boolean isConnected) {
        this.mac = mac;
        this.isConnected = isConnected;
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
