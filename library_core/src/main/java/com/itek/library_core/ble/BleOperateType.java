package com.itek.library_core.ble;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;


@IntDef({

        BleOperateType.READ_IMSI,
        BleOperateType.WRITE_FOR_READ_IMSI,
        BleOperateType.READ_DEVICE_STATUS,
        BleOperateType.WRITE_FOR_READ_DEVICE_STATUS,
        BleOperateType.READ_IMEI,
        BleOperateType.WRITE_FOR_READ_IMEI,
        BleOperateType.READ_DEVICE_INFOS,
        BleOperateType.WRITE_FOR_READ_DEVICE_INFOS,
        BleOperateType.READ_MODULE_INFOS,
        BleOperateType.WRITE_FOR_READ_MODULE_INFOS,
        BleOperateType.READ_HARDWARE_INFOS,
        BleOperateType.WRITE_FOR_READ_HARDWARE_INFOS,
        BleOperateType.GET_DEBUG_AUTH,
        BleOperateType.GET_DEBUG_AUTH_JACK,
        BleOperateType.GET_DEBUG_AUTH_BLEKEY,
        BleOperateType.WRITE_FOR_DEBUG_AUTH,
        BleOperateType.CHANGE_DEVICEID,
        BleOperateType.CHANGE_DEVICE_PORT,
        BleOperateType.CHANGE_DEVICE_IP,
        BleOperateType.WRITE_REPORTPERIOD,
        BleOperateType.TEST_OPENLOCK,
        BleOperateType.WRITE_TRANSPOT_MODEL,
        BleOperateType.WRITE_SECRET_BYTES,
        BleOperateType.RESTART_NB,
        BleOperateType.WRITE_SENCITIVE,
        BleOperateType.READ_RESULT_WRITE_SECRET_BYTES,
        BleOperateType.READ_RESULT_TEST_OPENLOCK,
        BleOperateType.OAD_Command,
        BleOperateType.BLEKEY_GET_DEVICE_INFOS,
        BleOperateType.BLEKEY_SET_DEVICEID,
        BleOperateType.BLEKEY_SET_DEVICETIME,
        BleOperateType.BLEKEY_GET_LOCKID,
        BleOperateType.BLEKEY_SET_LOCKID,
        BleOperateType.BLEKEY_GET_LOCK_KEY_STATUS,
        BleOperateType.BLEKEY_GET_OFF_LINE_AUTH,
        BleOperateType.BLEKEY_SET_OFF_LINE_AUTH,
        BleOperateType.BLEKEY_CLEAR_AUTH,
        BleOperateType.BLEKEY_SET_COMMUNICATE_SECRET,
        BleOperateType.BLEKEY_OPENLOCK_ONLINE,
        BleOperateType.OAD_SEND_IMAGE_IDENTIFY_PAYLOAD,
        BleOperateType.GET_OAD_BLOCK_SIZE_COMMAND,
        BleOperateType.SEND_START_OAD_PROCESS_COMMAND,
        BleOperateType.SENDING_IMAGE_BLOCK,
        BleOperateType.OAD_OAD_image_startNotify,
        BleOperateType.OAD_OAD_control_startNotify,
        BleOperateType.OAD_startNotify,
        BleOperateType.SET_USERID,
        BleOperateType.READ_RESULT_SET_USERID,

})

@Retention(RetentionPolicy.SOURCE)
public @interface BleOperateType {
    int READ_RESULT_SET_USERID = 2049;

    int SET_USERID = 2048;

    int OAD_OAD_image_startNotify = 2047;
    int OAD_startNotify = 2046;
    int OAD_OAD_control_startNotify = 2045;

    int SENDING_IMAGE_BLOCK = 2044;
    int SEND_START_OAD_PROCESS_COMMAND = 2043;
    int GET_OAD_BLOCK_SIZE_COMMAND = 2042;
    int OAD_SEND_IMAGE_IDENTIFY_PAYLOAD = 2041;

    int BLEKEY_OPENLOCK_ONLINE = 2040;
    int BLEKEY_SET_COMMUNICATE_SECRET = 2039;
    int BLEKEY_CLEAR_AUTH = 2038;
    int BLEKEY_SET_OFF_LINE_AUTH = 2037;
    int BLEKEY_GET_OFF_LINE_AUTH = 2036;
    int BLEKEY_GET_LOCK_KEY_STATUS = 2035;
    int BLEKEY_SET_LOCKID = 2034;
    int BLEKEY_GET_LOCKID = 2033;
    int BLEKEY_SET_DEVICETIME = 2032;
    int BLEKEY_SET_DEVICEID = 2031;
    int BLEKEY_READ_DEVICE_INFOS = 2030;
    int BLEKEY_GET_DEVICE_INFOS = 2029;
    int READ_RESULT_WRITE_SECRET_BYTES = 2028;
    int READ_RESULT_TEST_OPENLOCK = 2027;
    int OAD_Command = 2026;
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