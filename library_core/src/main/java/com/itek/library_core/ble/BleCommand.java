package com.itek.library_core.ble;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;


@IntDef({
        BleCommand.READ_IMSI,
        BleCommand.WRITE_FOR_READ_IMSI,
        BleCommand.READ_DEVICE_STATUS,
        BleCommand.WRITE_FOR_READ_DEVICE_STATUS,
        BleCommand.READ_IMEI,
        BleCommand.WRITE_FOR_READ_IMEI,
        BleCommand.READ_DEVICE_INFOS,
        BleCommand.WRITE_FOR_READ_DEVICE_INFOS,
        BleCommand.READ_MODULE_INFOS,
        BleCommand.WRITE_FOR_READ_MODULE_INFOS,
        BleCommand.READ_HARDWARE_INFOS,
        BleCommand.WRITE_FOR_READ_HARDWARE_INFOS,
        BleCommand.GET_DEBUG_AUTH,
        BleCommand.GET_DEBUG_AUTH_JACK,
        BleCommand.GET_DEBUG_AUTH_BLEKEY,
        BleCommand.WRITE_FOR_DEBUG_AUTH,
        BleCommand.CHANGE_DEVICEID,
        BleCommand.CHANGE_DEVICE_PORT,
        BleCommand.CHANGE_DEVICE_IP,
        BleCommand.WRITE_REPORTPERIOD,
        BleCommand.TEST_OPENLOCK,
        BleCommand.WRITE_TRANSPOT_MODEL,
        BleCommand.WRITE_SECRET_BYTES,
        BleCommand.RESTART_NB,
        BleCommand.WRITE_SENCITIVE,
})

@Retention(RetentionPolicy.SOURCE)
public @interface BleCommand {

    int WRITE_SENCITIVE = 2025;
    int RESTART_NB = 2024;
    int WRITE_SECRET_BYTES = 2023;
    int WRITE_TRANSPOT_MODEL = 2022;
    int WRITE_REPORTPERIOD = 2021;
    int TEST_OPENLOCK = 2020;
    int CHANGE_DEVICE_IP = 2019;
    int CHANGE_DEVICE_PORT = 2018;
    int CHANGE_DEVICEID = 2017;
    int WRITE_FOR_DEBUG_AUTH = 2016;
    int GET_DEBUG_AUTH_BLEKEY = 2015;
    int GET_DEBUG_AUTH_JACK = 2014;
    int GET_DEBUG_AUTH = 2013;
    int WRITE_FOR_READ_HARDWARE_INFOS = 2012;
    int READ_HARDWARE_INFOS = 2011;
    int WRITE_FOR_READ_MODULE_INFOS = 2010;
    int READ_MODULE_INFOS = 2009;
    int WRITE_FOR_READ_DEVICE_INFOS = 2008;
    int READ_DEVICE_INFOS = 2007;
    int WRITE_FOR_READ_IMEI = 2006;
    int READ_IMEI = 2005;
    int WRITE_FOR_READ_DEVICE_STATUS = 2004;
    int READ_DEVICE_STATUS = 2003;
    int WRITE_FOR_READ_IMSI = 2002;
    int READ_IMSI = 2001;

}