package com.itek.library_core.constant;

/**
 * Author:：simon
 * Date：2019/7/18:2:27 PM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class BleKeyConstant {



    //  ================  bluetooth config  ==========================

    public static final String FFE1 = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String FFF0 = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static final String FFE0 = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String FFE2 = "0000ffe2-0000-1000-8000-00805f9b34fb";


    public static final String Default_lockid = "fe190508";
    public static final String Default_secretkey = "39214d49";

    public static final String blekey_openlock_apply = "blekey_openlock_apply";
    public static final String blekey_openlock_success = "blekey_openlock_success";
    public static final String blekey_connect_success = "blekey_connect_success";




    ///=====================ble commmanders=================
    public static final byte ble_key_read_infos = 0x01;
    public static final byte ble_key_set_deviceid = 0x02;
    public static final byte ble_key_set_devicetime = 0x03;
    public static final byte ble_key_set_lockid = 0x04;
    public static final byte ble_key_read_lockid = 0x06;
    public static final byte ble_key_get_key_lock_status = 0x07;
    public static final byte ble_key_set_off_line_auth = 0x08;
    public static final byte ble_key_clear_auth = 0x09;
    public static final byte ble_key_get_off_line_auth = 0x0a;
    public static final byte ble_key_read_openlock_log= 0x0b;
    public static final byte ble_key_set_communicate_secret = 0x0c;
    public static final byte ble_key_openlock_online = 0x0d;
    public static final byte ble_key_head = 0x7B;
    public static final byte ble_key_tail = 0x7D;

    public static final byte ble_key_return_success = 0x00;
    public static final byte ble_key_return_failure= 0x01;


}
