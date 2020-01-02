package com.itek.library_core.bluetooth.callback;


import com.itek.library_core.bluetooth.exception.BleException;

public abstract class BleRssiCallback extends BleBaseCallback{

    public abstract void onRssiFailure(BleException exception);

    public abstract void onRssiSuccess(int rssi);

}