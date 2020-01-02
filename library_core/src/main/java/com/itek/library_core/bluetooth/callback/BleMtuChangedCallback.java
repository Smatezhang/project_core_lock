package com.itek.library_core.bluetooth.callback;


import com.itek.library_core.bluetooth.exception.BleException;

public abstract class BleMtuChangedCallback extends BleBaseCallback {

    public abstract void onSetMTUFailure(String mac, BleException exception);

    public abstract void onMtuChanged(String mac, int mtu);

}
