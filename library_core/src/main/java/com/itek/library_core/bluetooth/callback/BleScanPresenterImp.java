package com.itek.library_core.bluetooth.callback;


import com.itek.library_core.bluetooth.data.BleDevice;

public interface BleScanPresenterImp {

    void onScanStarted(boolean success);

    void onScanning(BleDevice bleDevice);

}
