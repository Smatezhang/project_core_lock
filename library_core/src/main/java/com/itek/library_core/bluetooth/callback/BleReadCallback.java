package com.itek.library_core.bluetooth.callback;


import com.itek.library_core.bluetooth.exception.BleException;

public abstract class BleReadCallback extends BleBaseCallback {

    public abstract void onReadSuccess(String mac, byte[] data);

    public abstract void onReadFailure(String mac, BleException exception);

}
