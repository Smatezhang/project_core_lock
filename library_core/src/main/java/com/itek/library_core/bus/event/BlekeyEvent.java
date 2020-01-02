package com.itek.library_core.bus.event;


import com.itek.library_core.bluetooth.data.BleDevice;

/**
 * Author:：simon
 * Date：2019/11/4:8:20 PM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class BlekeyEvent {

    private String data;

    private BleDevice mBleDevice;

    private String keyname;

    private String type;  // 用于区分开锁申请  和开锁结果


    public BlekeyEvent(String data, String type, String keyname, BleDevice mBleDevice) {
        this.data = data;
        this.type = type;
        this.mBleDevice = mBleDevice;
        this.keyname = keyname;
    }

    public BlekeyEvent(String data, String type, String keyname) {
        this.data = data;
        this.type = type;
        this.keyname = keyname;
    }
    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        mBleDevice = bleDevice;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }


}
