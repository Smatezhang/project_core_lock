package com.itek.library_core.ble;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;


@IntDef({
        BleFunctionType.COMMAND_TYPE_UPDATE_HARDWARE,
        BleFunctionType.COMMAND_TYPE_RECONNECT,
        BleFunctionType.COMMAND_TYPE_SETNETCONFIG,
        BleFunctionType.COMMAND_TYPE_SETLICENSE,
        BleFunctionType.COMMAND_TYPE_CONNECTBLEKEY,

})

@Retention(RetentionPolicy.SOURCE)
public @interface BleFunctionType {

    int COMMAND_TYPE_UPDATE_HARDWARE = 1001 ;
    int COMMAND_TYPE_RECONNECT = 1002 ;
    int COMMAND_TYPE_SETNETCONFIG = 1003 ;
    int COMMAND_TYPE_SETLICENSE = 1004 ;
    int COMMAND_TYPE_CONNECTBLEKEY = 1005 ;

}