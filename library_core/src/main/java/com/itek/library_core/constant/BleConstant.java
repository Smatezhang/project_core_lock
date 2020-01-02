package com.itek.library_core.constant;

/**
 * Author:：simon
 * Date：2019/7/18:2:27 PM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class BleConstant {



    //  ================  bluetooth config  ==========================
    public static final int DEFAULT_MTU = 250;
    public static final String OADService = "f000ffc0-0451-4000-b000-000000000000";
    public static final String OADImageIdentify = "f000ffc1-0451-4000-b000-000000000000";
    public static final String OADImageBlockCharacteristic = "f000ffc2-0451-4000-b000-000000000000";
    public static final String OADControlPointCharacteristic = "f000ffc5-0451-4000-b000-000000000000";
    public static final String FFF3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static final String FFF0 = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static final String FFF1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String FFF2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String FFF4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static final String FFF5 = "0000fff5-0000-1000-8000-00805f9b34fb";


    ///=====================ble commmanders=================

    public static final byte ble_open_lock = 0x01;
    public static final byte ble_debug_authority = 0x02;
    public static final byte ble_debug_authority_new_version = 0x03;       ////新版的进入调试
    public static final byte ble_debug_authority_jack = 0x05;       ////插座 进入调试
    public static final byte ble_read_imsi_info = 0x07;       ////读取imsi
    public static final byte ble_set_main_psd = 0x20;       ////设置密码
    public static final byte ble_demarcate = 0x31;       //通知井盖启动标定
    public static final byte ble_cover_measure_angle = 0x32;   //通知井盖执行一次角度测量 可通过fff2 读取值
    public static final byte ble_cover_close_lock = 0x41;      //通知井盖执行一次锁头出  （锁井盖）
    public static final byte ble_cover_open_lock = 0x42;       //通知井盖执行一次锁头回  （开井盖）
    public static final byte ble_cover_notify_read_current_state = 0x66;       //通知终端读取实时值
    public static final byte ble_restart_Nb_command = 0x01;       //重启NB
    public static final byte ble_restart_Nb_command_cover_lock = (byte) 0x98;       //重启NB  井盖锁
    public static final byte cover_set_sensitivity = 0x68;  //nb 开
    public static final byte ble_upload_photo_start = 0x61;  //nb 开
    public static final byte ble_upload_photo_updating = 0x62;  //nb 开
    public static final byte Image_Block_Write_Char_Response = 0x12;
    public static final byte Image_Block_Write_success = 0x0E;
    public static final byte Enable_OAD_Image_Command = 0x04;
    public static final byte Enable_OAD_Image_Response = 0x04;
    public static final byte Get_OAD_Block_Size_Command = 0x01;
    public static final byte Start_OAD_Process_Command = 0x03;
    public static final byte OAD_SUCCESS = 0x00;
    public final static byte touch_apply_openlock = 0x71;
    public final static byte lockstate_change_action = 0x41;
    public final static byte controller_poweron_action = 0x51;
    public final static byte lockstate_lockopen_dooropen = (byte) 0xF1;
    public final static byte lockstate_lockclose_dooropen = (byte) 0xE1;
    public final static byte lockstate_lockclose_doorclose = (byte) 0xE0;
    public final static byte lockstate_lockopen_doorclose = (byte) 0xF0;
    public static final byte ble_set_heartbeat = 0x05;       ////读取imsi
    public static final byte ble_set_deviceid = 0x04;       ////读取imsi
    public static final byte ble_set_secret = (byte) 0xeb;       ////读取imsi
    public static final byte ble_set_port = 0x03;       ////读取imsi
    public static final byte ble_set_Ip = 0x02;       ////读取imsi
    public static final byte ble_read_controller_info = 0x06;       ////读取imsi
    public static final byte ble_read_hard_version = 0x08;       ////读取硬件版本号
    public static final byte ble_set_transpot_mode = 0x09;       ////设置传输模式
    public static final byte ble_get_module = 0x10;       ////获取模组软件信息
    public static final byte ble_switch_transpot_module = 0x11;       ////切换模组运营商
    public static final byte ble_cover_demarcate = 0x31;       //通知井盖启动标定

    public static final byte ble_set_userid = 0x50;       //通知井盖启动标定


    // public static final byte ble_cover_close_lock = 0x41;      //通知井盖执行一次锁头出  （锁井盖）
    // public static final byte ble_cover_open_lock = 0x42;       //通知井盖执行一次锁头回  （开井盖）

//    public final static String operate_type_read_07 = "operate_type_read_07";
//    public final static String operate_type_read_07_write = "operate_type_read_07_write";
//    public final static String operate_type_read_fff2 = "operate_type_read_fff2";
//    public final static String operate_type_read_fff2_write = "operate_type_read_fff2_write";
//    public final static String operate_type_read_Imei = "operate_type_read_Imei";
//    public final static String operate_type_read_Imei_write = "operate_type_read_Imei_write";
//    public final static String operate_type_read_devicemsg = "operate_type_read_devicemsg";
//    public final static String operate_type_read_devicemsg_write = "operate_type_read_devicemsg_write";
//    public final static String operate_type_read_mozumsg_write = "operate_type_read_mozumsg_write";
//    public final static String operate_type_read_mozumsg = "operate_type_read_mozumsg";
//    public final static String operate_type_read_hardware = "operate_type_read_hardware";
//    public final static String operate_type_read_hardware_write = "operate_type_read_hardware_write";
//    public final static String operate_type_getdebuge_author = "operate_type_getdebuge_author";
//    public final static String operate_type_getdebuge_author_jack = "operate_type_getdebuge_author_jack";
//    public final static String operate_type_getdebuge_author_write = "operate_type_getdebuge_author_write";
//    public final static String operate_type_change_equid_04_write = "operate_type_change_equid_write";
//    public final static String operate_type_change_port_03_write = "operate_type_change_port_03_write";
//    public final static String operate_type_change_ip_02_write = "operate_type_change_ip_02_write";
//    public final static String operate_type_test_openlock = "operate_type_test_openlock";
//    public final static String operate_type_test_openlock_read = "operate_type_test_openlock_read";
//    public final static String operate_type_reportperiod_write = "operate_type_reportperiod_write";
//    public final static String operate_type_transpot_model_write = "operate_type_transpot_model_write";
//    public final static String operate_type_write_secret_bytes = "operate_type_write_secret_bytes";
//    public final static String operate_type_write_secret_bytes_read = "operate_type_write_secret_bytes_read";
//    public final static String operate_type_restart_nb = "operate_type_restart_nb";
//    public final static String operate_type_write_sencitive = "operate_type_write_sencitive";
//
//    public final static int COMMAND_TYPE_UPDATE_HARDWARE = 1001 ;
//    public final static int COMMAND_TYPE_RECONNECT = 1002 ;
//    public final static int COMMAND_TYPE_SETNETCONFIG = 1003 ;
//    public final static int COMMAND_TYPE_SETLICENSE = 1004 ;
//    public final static int COMMAND_TYPE_CONNECTBLEKEY = 1005 ;

}
