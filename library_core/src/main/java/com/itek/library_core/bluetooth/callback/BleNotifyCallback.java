package com.itek.library_core.bluetooth.callback;


import com.itek.library_core.bluetooth.exception.BleException;

public abstract class BleNotifyCallback extends BleBaseCallback {

    public abstract void onNotifySuccess(String mac);

    public abstract void onNotifyFailure(String mac, BleException exception);

    public abstract void onCharacteristicChanged(String mac, byte[] data);

}
