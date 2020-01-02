package com.itek.library_core.bluetooth.callback;


import com.itek.library_core.bluetooth.exception.BleException;

public abstract class BleWriteCallback extends BleBaseCallback{

    public abstract void onWriteSuccess(String mac, int current, int total, byte[] justWrite);

    public abstract void onWriteFailure(String mac, BleException exception);

}
