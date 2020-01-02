package com.itek.library_core.ble;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import com.itek.library_core.bluetooth.BleManager;
import com.itek.library_core.bluetooth.callback.BleGattCallback;
import com.itek.library_core.bluetooth.callback.BleMtuChangedCallback;
import com.itek.library_core.bluetooth.callback.BleNotifyCallback;
import com.itek.library_core.bluetooth.callback.BleReadCallback;
import com.itek.library_core.bluetooth.callback.BleWriteCallback;
import com.itek.library_core.bluetooth.data.BleDevice;
import com.itek.library_core.bluetooth.exception.BleException;
import com.itek.library_core.bus.RxBus;
import com.itek.library_core.bus.event.BlekeyEvent;
import com.itek.library_core.bus.event.DeviceConnectEvent;
import com.itek.library_core.constant.BleConstant;
import com.itek.library_core.constant.BleKeyConstant;
import com.itek.library_core.contract._BleEvent;
import com.itek.library_core.contract._BleLogEvent;
import com.itek.library_core.utils.EncryptUtils;
import com.itek.library_core.utils.HexUtil;
import com.itek.library_core.utils.KLog;
import com.itek.library_core.utils.StringUtils;
import com.itek.library_core.utils.ThreadPoolUtils;
import com.itek.library_core.utils.ToastUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import androidx.annotation.Nullable;

import static com.itek.library_core.constant.BleConstant.OADControlPointCharacteristic;
import static com.itek.library_core.constant.BleConstant.OADImageIdentify;


/**
 * author:：simon
 * date：2018/12/6:10:31 PM
 * mail：simon@itekiot.com
 * description：
 */
public class BleKeyHelper {

    private Context mContext;
    private BleDevice mBleDevice;
    private int mLockNum = 0;
    private int mCurrentOperateType = 0;
    private int commandType;
    private String mMac = "";
    private String mImei = "";
    private String mImsi = "";
    private String mEquiname = "";
    private String mBluetoothId = "";
    private String mSfversion = "";
    private String mHardversion = "";
    private String mModule = "";
    private String mEquId = "";
    private String mModuleDetail = "";
    private String psd = "";
    private String mClientIp = "";
    private String mClientPort = "";
    private String mHeartbeat = "";
    private String mWorkstate = "";
    private String mCoap = "0";
    private String error = "0";
    private String mNbNetCollect = "";
    private String mCharge = "";
    private String mNbSignalStrength = "";
    private String mTemperature = "";
    private String mHumidity = "";
    private String mActiveHole = "";
    private String mType = "";
    private String mDeviceId = "";
    private String mDeviceName = "";
    private byte mOperateCode = 0;
    private byte sensitive = 0;
    private byte[] mOperateCodes;
    private String applyUser = "";  //当蓝牙长连接时，虽然开锁时有指定ApplyUser，但是当开关状态有变化时，其实有时并不知道是哪个打开的。
    private boolean stopConnect;
    private String _ip;
    private String _port;
    private String _reportperiod;
    private String _transpot_model;
    private String _equid;
    private String _alias;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Map<String, String> mData;
    private String mFtId;
    private String mPosition;
    private Map<Integer, Byte[]> mMap = new HashMap<>();
    private byte mSubpachagesize;
    private byte[] previewByte = new byte[4];
    private byte[] mBytes;
    private int mImageSize;
    private int times;
    private int process ;
    private Timer mTimer;
    private byte[] mImageLength;
    private byte[] keyUserId = new byte[]{0x04,0x03,0x02,0x01};
    private byte[] keyConnectId = new byte[]{0x11,0x22,0x33,0x44};
    private Timer mTimerForDisconnect;
    private boolean isFtControl = false;
    private TimerTask mTask;

    public ThreadPoolUtils mThreadPoolUtils;

    private static BleKeyHelper sInstance;

    public static BleKeyHelper getInstance() {
        return sInstance;
    }

    public BleKeyHelper(Context context) {
        this.mContext = context;
        mThreadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread,20);
        initTimer();
    }

    public static synchronized void init(Application context) {
        if (sInstance == null) {
            synchronized (BleKeyHelper.class) {
                if (sInstance == null) {
                    sInstance = new BleKeyHelper(context);
                    BleManager.getInstance().init(context);
                }
            }
        }
    }


    public boolean isSupportBle() {
        return BleManager.getInstance().isSupportBle();
    }

    public void openBlueTooth() {

        if (!BleManager.getInstance().isBlueEnable()) {
            BleManager.getInstance().enableBluetooth();
        }
        configBLE();
    }

    public void configBLE() {
        BleManager.getInstance()
                .enableLog(true)
                .setSplitWriteNum(BleConstant.DEFAULT_MTU);
    }

    public void connectToEqu11(String mac, String name) {
        process = 0;
        mDeviceName = name;
        mFtId = name.substring(2, 4);
        mPosition = name.substring(4, 8);  // 加上架号   00 01 02 03 （00--前缀, 01--架号,02--层号,03--列号）
        mMac = mac;
        KLog.e("connectToEqu11 " + mac);
        commandType = BleFunctionType.COMMAND_TYPE_UPDATE_HARDWARE;
        mCurrentOperateType = BleOperateType.GET_DEBUG_AUTH;
        isFtControl = true;
        BleManager.getInstance().connect(mac, mBleGattCallback);

        //ToastUtil.showProgress("连接中...");
    }

    public void connectToBLEKey(BleDevice bleDevice) {

        isFtControl = false;
        commandType = BleFunctionType.COMMAND_TYPE_CONNECTBLEKEY;
        mCurrentOperateType = BleOperateType.GET_DEBUG_AUTH_BLEKEY;
        BleManager.getInstance().connect(bleDevice, mBleGattCallback);
        //ToastUtil.showProgress("连接中...");
    }

    public void connectToBLEKey(String mac) {
        mMac = mac;
        isFtControl = false;
        commandType = BleFunctionType.COMMAND_TYPE_CONNECTBLEKEY;
        mCurrentOperateType = BleOperateType.GET_DEBUG_AUTH_BLEKEY;
        BleManager.getInstance().connect(mac, mBleGattCallback);
        //ToastUtil.showProgress("连接中...");
    }

    public void connectTojack(String mac) {
        isFtControl = false;
        mCurrentOperateType = BleOperateType.GET_DEBUG_AUTH_JACK;
        BleManager.getInstance().connect(mac, mBleGattCallback);
        // ToastUtil.showProgress("连接中...");
    }

    public void connectToEqu(String mac) {
        isFtControl = true;

        KLog.e("connectToEqu " + mac);

        commandType = BleFunctionType.COMMAND_TYPE_RECONNECT;
        mCurrentOperateType = BleOperateType.GET_DEBUG_AUTH;
//        mImei = imei;
        BleManager.getInstance().connect(mac, mBleGattCallback);
        // ToastUtil.showProgress("连接中...");
    }

    public void connectToEqu(String mac, String imei) {
        isFtControl = false;
        mCurrentOperateType = BleOperateType.GET_DEBUG_AUTH;
        mImei = imei;
        BleManager.getInstance().connect(mac, mBleGattCallback);
        // ToastUtil.showProgress("连接中...");
    }

    public void openlock(String imei, int lockNum) {
        //测试蓝牙开锁
        byte[] mGetpdfasswfdsoerd = EncryptUtils.getpdfasswfdsoerd(imei);
        byte[] mBytes = concatenat2Bytes(mGetpdfasswfdsoerd, BleConstant.ble_open_lock);
        writeTerminal(BleOperateType.TEST_OPENLOCK, BleConstant.FFF0, BleConstant.FFF1, concatenat2Bytes_new(mBytes, new byte[]{(byte) lockNum}));
    }

    public void restartNb() {

        writeTerminal(BleOperateType.RESTART_NB, BleConstant.FFF0, BleConstant.FFF1, new byte[]{BleConstant.ble_restart_Nb_command});
    }

    public void writeSencitive(byte decode) {
        writeTerminal(BleOperateType.WRITE_SENCITIVE, BleConstant.FFF0, BleConstant.FFF3, new byte[]{BleConstant.cover_set_sensitivity, decode});
    }

    public void getConfig() {
        readTerminal(BleOperateType.READ_DEVICE_STATUS, BleConstant.FFF0, BleConstant.FFF2);
    }


    public void writeSecrets(byte[] decode) {

//            BleManager.getInstance().disconnect(mBleDevice);
//            /// 烧写完成
//            RxBus.getDefault().post(new _BleEvent("3", getInfos()));

        commandType = BleFunctionType.COMMAND_TYPE_SETLICENSE;
        String mS = HexUtil.encodeHexStr(decode);
        byte[] mBytes = concatenat2Bytes(decode, BleConstant.ble_set_secret);
        writeTerminal(BleOperateType.WRITE_SECRET_BYTES, BleConstant.FFF0, BleConstant.FFF1, mBytes);

    }


    /**
     * 配置参数
     *
     * @param equid
     * @param port
     * @param ip
     * @param reportperiod
     * @param transpot_model
     */
    public void config(String alias, String equid, String port, String ip, String reportperiod, String transpot_model) {
        KLog.e("config ");
        commandType = BleFunctionType.COMMAND_TYPE_SETNETCONFIG;
        _ip = ip;
        _port = port;
        _equid = equid;
        _reportperiod = reportperiod;
        _transpot_model = transpot_model;
        _alias = alias;
        byte[] mBytes = concatenat2Bytes(StringUtils.iDToBytes(equid), BleConstant.ble_set_deviceid);
        writeTerminal(BleOperateType.CHANGE_DEVICEID, BleConstant.FFF0, BleConstant.FFF3, mBytes);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                byte[] mBytes = concatenat2Bytes(ipToBytes(_ip), BleConstant.ble_set_Ip);
                writeTerminal(BleOperateType.CHANGE_DEVICE_IP, BleConstant.FFF0, BleConstant.FFF3, mBytes);
            }
        }, 2000);

    }

    public void getDeviceInfos() {

        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_read_infos;
            mOperateCodes = getPackage(concatenat2Bytes_new(concatenat2Bytes(keyConnectId, mOperateCode), keyUserId));
            writeTerminal(BleOperateType.BLEKEY_READ_DEVICE_INFOS, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

    public void setDeviceID(String deviceId) {

        byte[] mBytes = StringUtils.iDToBytes(deviceId);
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_set_deviceid;
            mOperateCodes = getPackage(concatenat2Bytes(mBytes, mOperateCode));
            writeTerminal(BleOperateType.BLEKEY_SET_DEVICEID, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

    public void setDeviceTime() {

        byte[] mTimestampe = EncryptUtils.getTimestampe();
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_set_devicetime;
            mOperateCodes = getPackage(concatenat2Bytes(mTimestampe, mOperateCode));
            writeTerminal(BleOperateType.BLEKEY_SET_DEVICETIME, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }


    public void getLockID() {
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_read_lockid;
            mOperateCodes = getPackage(new byte[]{mOperateCode});
            writeTerminal(BleOperateType.BLEKEY_GET_LOCKID, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

    public void clearAuth() {
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_clear_auth;
            mOperateCodes = getPackage(new byte[]{mOperateCode});
            writeTerminal(BleOperateType.BLEKEY_CLEAR_AUTH, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

  public void getOffAuth() {
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_get_off_line_auth;
            mOperateCodes = getPackage(new byte[]{mOperateCode});
            writeTerminal(BleOperateType.BLEKEY_GET_OFF_LINE_AUTH, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

  public void setCommunicateSecret(String oldcommunicateSecret , String newcommunicateSecret) {

      byte[] mBytes_oldcommunicateSecret = StringUtils.iDToBytes(oldcommunicateSecret);
      byte[] mBytes_newcommunicateSecret = StringUtils.iDToBytes(newcommunicateSecret);

      if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_set_communicate_secret;
          mOperateCodes = getPackage(concatenat2Bytes_new(concatenat2Bytes(mBytes_oldcommunicateSecret,mOperateCode),mBytes_newcommunicateSecret));
          writeTerminal(BleOperateType.BLEKEY_SET_COMMUNICATE_SECRET, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
      }

    }

  public void openLockOnLine(String lockid , String lockSecret) {

      byte[] mBytes_lockid = StringUtils.iDToBytes(lockid);
      byte[] mBytes_lockSecret = StringUtils.iDToBytes(lockSecret);

      if (BleManager.getInstance().isConnected(mBleDevice)) {
          mOperateCode = BleKeyConstant.ble_key_openlock_online;
          mOperateCodes = getPackage(concatenat2Bytes_new(concatenat2Bytes(mBytes_lockid,mOperateCode),mBytes_lockSecret));
          writeTerminal(BleOperateType.BLEKEY_SET_COMMUNICATE_SECRET, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
      }

    }

    public void getKeyLockStatus() {

        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_get_key_lock_status;
            mOperateCodes = getPackage(new byte[]{mOperateCode});
            writeTerminal(BleOperateType.BLEKEY_GET_LOCK_KEY_STATUS, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

    public void setLockId(String lockid, String lockSecret) {

        byte[] mBytes_lockid = StringUtils.iDToBytes(lockid);
        byte[] mBytes_lockSecret = StringUtils.iDToBytes(lockSecret);
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_set_lockid;
            mOperateCodes = getPackage(concatenat2Bytes_new(concatenat2Bytes(mBytes_lockid,mOperateCode),mBytes_lockSecret));
            writeTerminal(BleOperateType.BLEKEY_SET_LOCKID, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }


    public void setOffAuth(String lockid, String lockSecret, String offlinetime) {
        byte[] mBytes_lockid = StringUtils.iDToBytes(lockid);
        byte[] mBytes_lockSecret = StringUtils.iDToBytes(lockSecret);
        byte[] mBytes_offlinetime = StringUtils.timeToBytes(offlinetime);
        if (BleManager.getInstance().isConnected(mBleDevice)) {
            mOperateCode = BleKeyConstant.ble_key_set_off_line_auth;
            mOperateCodes = getPackage(concatenat2Bytes_new(concatenat2Bytes_new(concatenat2Bytes(mBytes_lockid,mOperateCode),mBytes_lockSecret),mBytes_offlinetime));
            writeTerminal(BleOperateType.BLEKEY_SET_OFF_LINE_AUTH, BleKeyConstant.FFE0, BleKeyConstant.FFE1, mOperateCodes, false);
        }
    }

    private BleGattCallback mBleGattCallback = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            RxBus.getDefault().post(new _BleEvent("99", getprogress()));
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            // ToastUtil.shortShow(mContext.getString(R.string.openlock_failure));
            BleManager.getInstance().connect(bleDevice.getMac(), mBleGattCallback);
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            if (isFtControl){
                initTimerForDisconnect();
                startTimerForDisconnect(15000);
            }
//            ToastUtils.showShort("连接成功");
            addLog("连接成功",true);
            mBleDevice = bleDevice;
            changeMtu(250);
            RxBus.getDefault().post(new DeviceConnectEvent(bleDevice.getMac(),true));

        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

//            if (!isActiveDisConnected && device != null) {
//                BleManager.getInstance().connect(device, mBleGattCallback);
//            }

            RxBus.getDefault().post(new DeviceConnectEvent(device.getMac(),false));
            addLog("连接断开",true);
            ToastUtils.showShort("连接断开");
        }
    };

    private void changeMtu(int mtu) {
        if (stopConnect) {
            return;
        }
        BleManager.getInstance().setMtu(mBleDevice, mtu, mBleMtuChangedCallback);
    }

    private BleMtuChangedCallback mBleMtuChangedCallback = new BleMtuChangedCallback() {
        @Override
        public void onSetMTUFailure(String mac, BleException exception) {
        }

        @Override
        public void onMtuChanged(String mac, int mtu) {
            switchOperateType();
        }
    };

    private void switchOperateType() {
        RxBus.getDefault().post(new _BleEvent("99", getprogress()));
        if (stopConnect) {
            return;
        }

        switch (mCurrentOperateType) {
            case BleOperateType.GET_DEBUG_AUTH:
                ToastUtils.showShort("连接成功");
//            byte[] mGetpdfasswfdsoerd = Utils.getpdfasswfdsoerd(mImei);
//            byte[] mBytes = concatenat2Bytes(mGetpdfasswfdsoerd, (byte) 0x02);
                /// AES 有点问题
                writeTerminal(BleOperateType.WRITE_FOR_DEBUG_AUTH, BleConstant.FFF0, BleConstant.FFF1, new byte[]{BleConstant.ble_debug_authority_new_version});
                break;
            case BleOperateType.GET_DEBUG_AUTH_BLEKEY:
                ToastUtils.showShort("连接成功");

                RxBus.getDefault().post(new BlekeyEvent("",BleKeyConstant.blekey_connect_success,mBleDevice.getName()));

                startNotify(BleKeyConstant.FFE0, BleKeyConstant.FFE2);

                startTimer(2000L, BleOperateType.BLEKEY_GET_DEVICE_INFOS);

                break;

            case BleOperateType.GET_DEBUG_AUTH_JACK:
                ToastUtils.showShort("连接成功");
//            byte[] mGetpdfasswfdsoerd = Utils.getpdfasswfdsoerd(mImei);
//            byte[] mBytes = concatenat2Bytes(mGetpdfasswfdsoerd, (byte) 0x02);
                /// AES 有点问题
                writeTerminal(BleOperateType.WRITE_FOR_DEBUG_AUTH, BleConstant.FFF0, BleConstant.FFF1, new byte[]{BleConstant.ble_debug_authority_jack});

                break;
            default:
                writeTerminal(BleOperateType.WRITE_FOR_DEBUG_AUTH, BleConstant.FFF0, BleConstant.FFF1, new byte[]{BleConstant.ble_debug_authority_new_version});
                break;
        }
    }

    private void initTimer() {
        this.times = 0;
        if (this.mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }


    private void initTimerForDisconnect() {

        if (this.mTimerForDisconnect != null) {
            this.mTimerForDisconnect.cancel();
            this.mTimerForDisconnect = null;
        }

        if (mTask != null){
            mTask.cancel();
        }
    }

    private void startTimerForDisconnect(long delay) {
        if (this.mTimerForDisconnect != null){
            initTimerForDisconnect();
        }
        if (mTask != null){
            mTask.cancel();
        }
        this.mTimerForDisconnect = new Timer();
        // BleManager.getInstance().disconnect(mBleDevice);
        mTask = new TimerTask() {
            public void run() {
                 BleManager.getInstance().disconnect(mBleDevice);
            }
        };
        this.mTimerForDisconnect.schedule(mTask, delay);
    }



    private void startTimer(long delay, int workstatus) {
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    initTimer();
                    switch (workstatus) {
                        case BleOperateType.BLEKEY_GET_DEVICE_INFOS:
                            getDeviceInfos();
                          break;
                    }
                } catch (Exception var14) {
                    var14.printStackTrace();
                }
            }
        }, delay, 2000L);
    }

    private byte[] getPackage(byte[] bytes){
        short mLength = (short) bytes.length;
        byte[] ml = shortToBytesHL( mLength);
        byte[] preB = new byte[]{BleKeyConstant.ble_key_head,ml[0],ml[1]};
        byte[] tailB = new byte[]{BleKeyConstant.ble_key_tail,BleKeyConstant.ble_key_tail};
        byte[] crc = new byte[]{0x00,0x00};
        byte[] mBytes = concatenat2Bytes_new(concatenat2Bytes_new(concatenat2Bytes_new(preB, bytes), crc), tailB);
        return mBytes;
    }

    private void stopTimer(int _workResult, String _message) {
        try {
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer = null;
            }
            Thread.sleep(500L);
        } catch (Exception var26) {
            var26.printStackTrace();
        }
    }
   private  byte[]  dataBuffer;
   private  int  datalenth;
    private synchronized void decodeNotifyData(byte[] data) {

        if ( data == null || data.length == 0) {
            return;
        }
        addLog("decodeNotifyData: "+"count:" +data.length+ " "+ HexUtil.formatHexString(data,true) ,true);

        switch (commandType){
            case BleFunctionType.COMMAND_TYPE_CONNECTBLEKEY:

                RxBus.getDefault().post(new _BleLogEvent(new HashMap<>(),"decodeNotifyData:"+"count:" +data.length+ " "+ HexUtil.formatHexString(data,true)));

                if (data.length >= 3 && data[0] == BleKeyConstant.ble_key_head) {

                    datalenth = 0;
                    int mI = HexUtil.byteArrayToInt(new byte[]{data[2],data[1],0x00,0x00 });
                      dataBuffer = new byte[mI+1];

                    if (data[data.length-1] == BleKeyConstant.ble_key_tail && data[data.length-2] == BleKeyConstant.ble_key_tail){
                        // TODO: 2019/7/24  crc 校验
                        for (int mI1 = 0; mI1 < data.length - 7; mI1++) {
                            dataBuffer[mI1] = data[mI1+3];
                            datalenth ++;
                        }
                        decodeKeyNotifyData(dataBuffer);
                        /// 解析

                    }else {
                        for (int mI1 = 0; mI1 < data.length - 3 ; mI1++) {
                            dataBuffer[mI1] = data[mI1+3];
                            datalenth++;
                        }
                    }
                }else {
                    RxBus.getDefault().post(new BlekeyEvent(HexUtil.formatHexString(data,false),BleKeyConstant.blekey_openlock_apply,mBleDevice.getName()));
                }

//                else if (data.length > 3 && data[data.length-1] == BleKeyConstant.ble_key_tail && data[data.length-2] == BleKeyConstant.ble_key_tail ){
//
//                    int lenth = datalenth;
//                    for (int mI1 = 0; mI1 < data.length - 4 ; mI1++) {
//                        dataBuffer[lenth+mI1] = data[mI1];
//                        datalenth++;
//                    }
//                    /// 解析
//
//                }else if (data.length <= 3){
//                    int lenth = datalenth;
//
//                    if (data.length ==2 && data[0] == BleKeyConstant.ble_key_tail&&data[1] == BleKeyConstant.ble_key_tail){
////               dataBuffer[dataBuffer.length-1] =   crc;
//
//                    }else if (data.length == 1 && data[0] == BleKeyConstant.ble_key_tail){
//                        dataBuffer[dataBuffer.length-1] = BleKeyConstant.ble_key_tail;
//                    }
//                }else if (data.length >= 18){
//                    int lenth = datalenth;
//
//                    for (int mI1 = 0; mI1 < data.length ; mI1++) {
//                        dataBuffer[lenth+mI1] = data[mI1];
//                        datalenth ++;
//                    }
//                }else {
//                    //decodeKeyNotifyData(data);
//                }

               // decodeKeyNotifyData(data);

                break;
            case BleFunctionType.COMMAND_TYPE_UPDATE_HARDWARE:

                int mLength = data.length;
                if (mLength == 3) {
                    mSubpachagesize = data[1];
                    // send Image Identify Payload
                    sendImageIdnetifyPayload();
                } else if (mLength == 1) {
                    if (data[0] == BleConstant.OAD_SUCCESS) {
                        //send  start OAD Process Command"
                        sendStartOADProcessCommand();
                    }else {

                        error = String.valueOf(data[1]);
                        RxBus.getDefault().post(new _BleEvent("00", getInfos()));
                    }
                } else if (mLength == 6) {
                    //Byte 0: Command ID (0x12)
                    if (data[0] == BleConstant.Image_Block_Write_Char_Response) {
                        //Byte 1: Status of the previous block received
                        //Byte 2-5: Block number
                        if (data[1] == BleConstant.OAD_SUCCESS) {
                            addLog("Command ID (0x12) OAD_SUCCESS",true);
                            previewByte[0] = data[2];
                            previewByte[1] = data[3];
                            previewByte[2] = data[4];
                            previewByte[3] = data[5];
                            sendingImageBlock(previewByte);
                        } else if (data[1] == BleConstant.Image_Block_Write_success) {

                            addLog("固件升级成功",true);
                            mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    writeTerminal(BleOperateType.OAD_Command, BleConstant.OADService, OADControlPointCharacteristic, new byte[]{BleConstant.Enable_OAD_Image_Command});
//                                commandType = "2";
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            addLog("准备重新连接设备",true);
                                            connectToEqu(mMac);
                                        }
                                    }, 15 * 1000);
                                }
                            }, 30);
                        }else {
                            error = String.valueOf(data[1]);
                            RxBus.getDefault().post(new _BleEvent("00", getInfos()));
                        }

                    } else {
                        error = String.valueOf(data[0]);
                        RxBus.getDefault().post(new _BleEvent("00", getInfos()));
                        addLog("写入失败",true);
                        ToastUtils.showShort("写入失败");
                    }
                } else if (mLength == 2) {
                    if (data[0] == BleConstant.Enable_OAD_Image_Response) {
                        if (data[1] == BleConstant.OAD_SUCCESS) {
                            addLog("固件升级成功",true);

                            ToastUtils.showShort("成功");
                            ///  连接设备 写入相关参数；
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    addLog("准备重新连接设备",true);
                                    connectToEqu(mMac);
                                }
                            }, 5 * 1000);
                        }
                    }
                }


                break;
            default:

                /// 入库调试相关

                switch (data[0]) {
                    case BleConstant.touch_apply_openlock:
                        addLog("触摸事件");
                        break;
                    case BleConstant.controller_poweron_action:
                        addLog("上电/心跳 ");
                        break;
                    case BleConstant.lockstate_change_action:
                        if (data.length >= 6) {
                            switch (data[5]) {
                                case BleConstant.lockstate_lockopen_dooropen:
                                    addLog("门锁状态改变事件：锁开门开");
                                    break;
                                case BleConstant.lockstate_lockclose_dooropen:
                                    addLog("门锁状态改变事件：锁关门开");
                                    break;
                                case BleConstant.lockstate_lockclose_doorclose:
                                    addLog("门锁状态改变事件：锁关门关");
                                    break;
                                case BleConstant.lockstate_lockopen_doorclose:
                                    addLog("门锁状态改变事件：锁开门关");
                                    break;
                            }
                        }
                        break;
                    case BleConstant.ble_upload_photo_start:
                        addLog("开始拍照:" + HexUtil.encodeHexStr(data).substring(0, 2));
                        break;
                    case BleConstant.ble_upload_photo_updating:
                        addLog("图片上传中:" + HexUtil.encodeHexStr(data).substring(0, 2));
                        break;
                    default:
                        addLog("nb:  " + HexUtil.encodeHexStr(data));
                        break;
                }
                break;

        }

    }



    private void decodeKeyNotifyData(byte[] data) {

            switch (data[0]){
                case BleKeyConstant.ble_key_read_infos:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success  && data.length >= 9) {
                            int ver_hard = data[2];
                            int ver_soft = data[3];
                            int ver_year = data[4];
                            int ver_month = data[5];
                            int ver_day = data[6];
                            int ver_hour = data[7];
                            int ver_minute = data[8];
                            int ver_second = data[9];
                            Map<String, String> mMap = new HashMap<>();
                            mMap.put("ver_hard",ver_hard+"");
                            mMap.put("ver_soft",ver_soft+"");
                            mMap.put("ver_year",ver_year+"");
                            mMap.put("ver_month",ver_month+"");
                            mMap.put("ver_day",ver_day+"");
                            mMap.put("ver_hour",ver_hour+"");
                            mMap.put("ver_minute",ver_minute+"");
                            mMap.put("ver_second",ver_second+"");
                            RxBus.getDefault().post(new _BleLogEvent(mMap,""));

                    }else {
                         ToastUtils.showShort("失败");
                    }
                    break;

                case BleKeyConstant.ble_key_set_deviceid:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){
                        ToastUtils.showShort("成功");
                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;

                case BleKeyConstant.ble_key_set_devicetime:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){
                        ToastUtils.showShort("成功");
                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;

                case BleKeyConstant.ble_key_read_lockid:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){
                        ToastUtils.showShort("成功");
                        String mLockId = HexUtil.encodeHexStr(new byte[]{data[2], data[3], data[4], data[5]});
                        int lock_ver_hard = data[6];
                        int lock_ver_soft = data[7];
                        int lock_ver_protocol = data[8];
                        int lock_type = data[9];
                        Map<String, String> mMap = new HashMap<>();
                        mMap.put("lockid",mLockId);
                        mMap.put("lock_ver_hard",lock_ver_hard+"");
                        mMap.put("lock_ver_soft",lock_ver_soft+"");
                        mMap.put("lock_ver_protocol",lock_ver_protocol+"");
                        mMap.put("lock_type",lock_type+"");
                        RxBus.getDefault().post(new _BleLogEvent(mMap,""));

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;

                case BleKeyConstant.ble_key_set_lockid:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("成功");

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;
                case BleKeyConstant.ble_key_get_key_lock_status:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("连接");

                    }else {
                        ToastUtils.showShort("断开");
                    }

                    break;

                case BleKeyConstant.ble_key_set_off_line_auth:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("成功");

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;


                 case BleKeyConstant.ble_key_clear_auth:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("成功");

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;

                    case BleKeyConstant.ble_key_get_off_line_auth:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("成功");

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;


                case BleKeyConstant.ble_key_read_openlock_log:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("成功");

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;

                case BleKeyConstant.ble_key_set_communicate_secret:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("成功");

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;

                case BleKeyConstant.ble_key_openlock_online:

                    if (data[1] ==  BleKeyConstant.ble_key_return_success){

                        ToastUtils.showShort("开锁成功");
                        RxBus.getDefault().post(new BlekeyEvent(HexUtil.formatHexString(data,false),BleKeyConstant.blekey_openlock_success,mBleDevice.getName()));

                    }else {
                        ToastUtils.showShort("失败");
                    }

                    break;
                default:
                    break;
            }

//            ToastUtils.showShort("失败");

    }


    private synchronized void decodeJustWrite(byte[] justWrite) {

        addLog("decodeJustWrite"+HexUtil.encodeHexStr(justWrite));


        if (justWrite.length > 0) {
            Integer mI;
            byte[] mBytes;
            switch (mCurrentOperateType) {

                case BleOperateType.WRITE_FOR_DEBUG_AUTH:
                    //   startNotify(Constants.ble_service_uuid,Constants.ble_characteristic_uuid_f3);
                    readTerminal(BleOperateType.READ_DEVICE_STATUS, BleConstant.FFF0, BleConstant.FFF2);
                    break;
                case BleOperateType.CHANGE_DEVICEID:
                    mBytes = concatenat2Bytes(ipToBytes(_ip), BleConstant.ble_set_Ip);
                    writeTerminal(BleOperateType.CHANGE_DEVICE_IP, BleConstant.FFF0, BleConstant.FFF3, mBytes);
                    break;
                case BleOperateType.CHANGE_DEVICE_IP:
                    if (!TextUtils.isEmpty(mSfversion) && StringUtils.isNumeric(mSfversion)) {
                        int mI1 = Integer.parseInt(mSfversion);
                        if (mI1 >= 18) {
                            mBytes = concatenat2Bytes(portToBytes_new(_port), BleConstant.ble_set_port);
                        } else {
                            mBytes = concatenat2Bytes(portToBytes(_port), BleConstant.ble_set_port);
                        }
                    } else {
                        mBytes = concatenat2Bytes(portToBytes(_port), BleConstant.ble_set_port);
                    }

                    writeTerminal(BleOperateType.CHANGE_DEVICE_PORT, BleConstant.FFF0, BleConstant.FFF3, mBytes);

                    break;

                case BleOperateType.CHANGE_DEVICE_PORT:

                    //（每4小时为单位）例：0x0501 就是4小时上报1次  0x0502 就是8小时上报1次
                    mI = Integer.parseInt(_reportperiod);
                    mBytes = concatenat2Bytes(new byte[]{mI.byteValue()}, BleConstant.ble_set_heartbeat);
                    writeTerminal(BleOperateType.WRITE_REPORTPERIOD, BleConstant.FFF0, BleConstant.FFF3, mBytes);

                    break;

                case BleOperateType.WRITE_REPORTPERIOD:

                    //（每4小时为单位）例：0x0501 就是4小时上报1次  0x0502 就是8小时上报1次
                    if ("UDP".equals(_transpot_model)) {
                        mI = 2;
                    } else if ("COAP".equals(_transpot_model)) {
                        mI = 1;
                    } else {
                        mI = Integer.parseInt(_transpot_model);
                    }

                    mBytes = concatenat2Bytes(new byte[]{mI.byteValue()}, BleConstant.ble_set_transpot_mode);

                    writeTerminal(BleOperateType.WRITE_TRANSPOT_MODEL, BleConstant.FFF0, BleConstant.FFF3, mBytes);

                    break;


                case BleOperateType.WRITE_TRANSPOT_MODEL:

                    writeTerminal(BleOperateType.WRITE_FOR_READ_HARDWARE_INFOS, BleConstant.FFF0, BleConstant.FFF3, new byte[]{BleConstant.ble_read_hard_version});

//                    writeTerminal(operate_type_read_07_write, FFF0, FFF3, new byte[]{0x07});

                    break;

                case BleOperateType.WRITE_FOR_READ_IMSI:

                    readTerminal(BleOperateType.READ_IMSI, BleConstant.FFF0, BleConstant.FFF3);

                    break;
                case BleOperateType.WRITE_FOR_READ_HARDWARE_INFOS:

                    readTerminal(BleOperateType.READ_HARDWARE_INFOS, BleConstant.FFF0, BleConstant.FFF3);

                    break;

                case BleOperateType.WRITE_FOR_READ_DEVICE_INFOS:

                    readTerminal(BleOperateType.READ_DEVICE_INFOS, BleConstant.FFF0, BleConstant.FFF3);
                    break;
                case BleOperateType.WRITE_FOR_READ_MODULE_INFOS:

                    // 获取模组具体软件信息
                    readTerminal(BleOperateType.READ_MODULE_INFOS, BleConstant.FFF0, BleConstant.FFF3);
                    break;

                case BleOperateType.WRITE_FOR_READ_DEVICE_STATUS:

                    readTerminal(BleOperateType.READ_DEVICE_STATUS, BleConstant.FFF0, BleConstant.FFF3);
                    break;

                case BleOperateType.WRITE_SECRET_BYTES:


                    readTerminal(BleOperateType.READ_RESULT_WRITE_SECRET_BYTES, BleConstant.FFF0, BleConstant.FFF3);
                    //设置灵敏度
                    // writeSencitive((byte) 0x09);
                    break;
                case BleOperateType.RESTART_NB:
                    if (justWrite[0] == BleConstant.ble_restart_Nb_command) {
                        ToastUtils.showShort("重启NB");
                    }
                    break;
                case BleOperateType.TEST_OPENLOCK:
                    if (justWrite[0] == BleConstant.ble_open_lock) {
                        readTerminal(BleOperateType.READ_RESULT_TEST_OPENLOCK, BleConstant.FFF0, BleConstant.FFF3);
                        //ToastUtil.shortShow("开锁成功");
                    }
                    break;
                case BleOperateType.WRITE_SENCITIVE:
                    if (justWrite[0] == BleConstant.cover_set_sensitivity) {
                        ToastUtils.showShort("设置成功");
                    }
                    break;
            }
        }
    }

    private Map<String, String> getprogress() {
        process ++;
        Map<String, String> mMap = new HashMap<>();

        mMap.put("deviceName", mDeviceName);
        mMap.put("progress", process+"");
        return mMap;
    }


    private void sendingImageBlock(final byte[] previewByte) {


        int mI = HexUtil.byteArrayToInt(previewByte);
        addLog(" sending Image Block number：" + mI + "",true);
        Byte[] mBytes = mMap.get(mI);

        if (mBytes != null && mBytes.length != 0) {
            int msize = HexUtil.byteToInt(mSubpachagesize);

            final byte[] terminals;
            if (mI == mMap.size() - 1) {

                terminals = new byte[(mImageSize % (msize - 4)) + 4];
            } else {
                terminals = new byte[msize];
            }

            for (int i = 0; i < previewByte.length; i++) {
                terminals[i] = previewByte[i];
            }

            for (int i = 0; i < mBytes.length; i++) {

                Byte mByte = mBytes[i];
                if (mByte != null) {
                    terminals[4 + i] = mByte.byteValue();
                }

            }
            Log.e("sendingImageBlock", "OADImageBlockCharacteristic");
                           Log.e("sendingImageBlock", HexUtil.byte2HexStr2(terminals));
//                            addLog("OADImageBlockCharacteristic："+HexUtil.encodeHexStr(terminals));
            //  mNotifys.add("OADImageBlockCharacteristic："+Utils.BytetohexString(terminals));
            writeTerminal_oad(BleOperateType.SENDING_IMAGE_BLOCK, BleConstant.OADService, BleConstant.OADImageBlockCharacteristic, terminals);
        }
    }

    private void sendImageIdnetifyPayload() {

        if (mCurrentOperateType == BleOperateType.OAD_SEND_IMAGE_IDENTIFY_PAYLOAD) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                byte[] mImageIdentifyPayload = readProfile();
                    // 利用本地bin文件
//                    byte[] mImageIdentifyPayload = readProfilefromAssets();
                    subPackageSize(mSubpachagesize);
                    //send Image Identify Payload
                    addLog("send Image Identify Payload", true);


                    writeTerminal(BleOperateType.SEND_START_OAD_PROCESS_COMMAND, BleConstant.OADService, OADImageIdentify, mImageIdentifyPayload);
//                addLog("mImageIdentifyPayload："+HexUtil.encodeHexStr(mImageIdentifyPayload));
                }
            }, 1000);
        }
    }

    private void sendStartOADProcessCommand() {


        if (mCurrentOperateType == BleOperateType.SEND_START_OAD_PROCESS_COMMAND) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    addLog("send Start OAD Process Command", true);

                    writeTerminal(BleOperateType.SENDING_IMAGE_BLOCK, BleConstant.OADService, OADControlPointCharacteristic, new byte[]{BleConstant.Start_OAD_Process_Command});
                }
            }, 1000);
        }
    }

    private synchronized void subPackageSize(final byte subpachagesize) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = HexUtil.byteToInt(subpachagesize) - 4;
//                Log.e(TAG, "subPackageSize: " + size);
                addLog("subPackageSize：" + size,true);
                int j = 0;
                for (int i = 0; i < mBytes.length; i += size) {
                    Byte[] mvalue;
                    if (j == mImageSize / size) {
                        mvalue = new Byte[mImageSize % size];
                    } else {
                        mvalue = new Byte[size];
                    }
                    for (int k = 0; k < mvalue.length; k++) {
                        if (k + i < mImageSize) {
                            mvalue[k] = mBytes[k + i];
                        }
                    }
                    mMap.put(j, mvalue);
                    j++;
                }
            }
        }).start();

    }

    public void addLog(String s) {
        KLog.e(Thread.currentThread().getName() + ":" + mDeviceName + ":" + s);
    }

    public void addLog(String s, boolean isSave) {

        if (isSave) {
            mThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    final String saveStr = s;
//                    StringBuffer mAppend = new StringBuffer(DateUtil.getCurrentDateSSS()).append(":").append(saveStr).append(MyConstant.SplitKey);
//                    FileUtils.saveFile2(Utils.getContext(), mAppend.toString(), mBleDevice.getMac(), true);
                }
            });
        }
        KLog.e(Thread.currentThread().getName() + ":" + mDeviceName + ":" + s);
    }

    private synchronized void decodeReadData(byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }
//        Log.e("decodeReadData1", HexUtil.formatHexString(data));
//        Log.e("decodeReadData2", new String(data));
//        String mHexStr = HexUtil.encodeHexStr(data);

        String mParameter = new String(data);
        switch (mCurrentOperateType) {
            case BleOperateType.READ_IMEI:

//                if (isNumeric(mParameter) && mParameter.length() == 15) {
//                    mImei = mParameter;
//                    ToastUtil.shortShow("进入调试模式");
//                    byte[] mGetpdfasswfdsoerd = Utils.getpdfasswfdsoerd(mImei);
//
//                    byte[] mBytes = concatenat2Bytes(mGetpdfasswfdsoerd, (byte) 0x02);
//
//
//                    writeTerminal(operate_type_getdebuge_author_write, FFF0, FFF1, new byte[]{0x03});
//
//                } else {
//                    ToastUtil.shortShow("未获取到设备相关信息");
//                }


                writeTerminal(BleOperateType.WRITE_FOR_DEBUG_AUTH, BleConstant.FFF0, BleConstant.FFF1, new byte[]{BleConstant.ble_debug_authority_new_version});


                break;

            case BleOperateType.READ_IMSI:


                mImsi = mParameter;

                // 获取硬件版本号

                writeTerminal(BleOperateType.WRITE_FOR_READ_HARDWARE_INFOS, BleConstant.FFF0, BleConstant.FFF3, new byte[]{BleConstant.ble_read_hard_version});

                break;
            case BleOperateType.READ_HARDWARE_INFOS:


                showHardVersion(mParameter);
                // 获取蓝牙锁的信息

                writeTerminal(BleOperateType.WRITE_FOR_READ_DEVICE_INFOS, BleConstant.FFF0, BleConstant.FFF3, new byte[]{BleConstant.ble_read_controller_info});

                break;

            case BleOperateType.READ_DEVICE_INFOS:

                showParameter(mParameter);
                // 获取模组具体软件信息

                writeTerminal(BleOperateType.WRITE_FOR_READ_MODULE_INFOS, BleConstant.FFF0, BleConstant.FFF3, new byte[]{BleConstant.ble_get_module});

                break;
            case BleOperateType.READ_MODULE_INFOS:

                mModuleDetail = mParameter;
                mCurrentOperateType = 0;

                if (BleFunctionType.COMMAND_TYPE_UPDATE_HARDWARE == commandType) {
                    mCurrentOperateType = BleOperateType.OAD_Command;
                    addLog("请求固件包",true);
                    /// 获取了固件的所有信息

                } else if (BleFunctionType.COMMAND_TYPE_RECONNECT == commandType) {

                    /// 设置IP地址等；
                    String mIp_ = mData.get("ip_");
                    String port_ = mData.get("port_");
                    String equid = mData.get("equid");
                    String mid = Long.toHexString(Long.parseLong(equid));
                    String coap = mData.get("coap");

//                    config("safd", mid, port_, mIp_, "24", coap);

                    commandType = BleFunctionType.COMMAND_TYPE_SETNETCONFIG;
                    _ip = mIp_;
                    _port = port_;
                    _equid = mid;
                    _reportperiod = "24";
                    _transpot_model = coap;
                    _alias = "safd";
                    byte[] mBytes = concatenat2Bytes(StringUtils.iDToBytes(mid), BleConstant.ble_set_deviceid);
                    writeTerminal(BleOperateType.CHANGE_DEVICEID, BleConstant.FFF0, BleConstant.FFF3, mBytes);
                    addLog("配置基本信息",true);

                } else if (BleFunctionType.COMMAND_TYPE_SETNETCONFIG == commandType) {
                    String mLicense = mData.get("license");
                    byte[] mDecode = Base64.decode(mLicense, Base64.DEFAULT);
                    /// 设置lisence
                    writeSecrets(mDecode);
                    addLog("配置licence",true);

                }

                break;
            case BleOperateType.READ_DEVICE_STATUS:

                showcharacter(data);
                writeTerminal(BleOperateType.WRITE_FOR_READ_HARDWARE_INFOS, BleConstant.FFF0, BleConstant.FFF3, new byte[]{BleConstant.ble_read_hard_version});
                //writeTerminal(operate_type_read_07_write, FFF0, FFF3, new byte[]{0x07});

                break;

            case BleOperateType.READ_RESULT_TEST_OPENLOCK:

                //ToastUtils.showShort(new String(data));

                break;

            case BleOperateType.READ_RESULT_WRITE_SECRET_BYTES:

                // ToastUtils.showShort(new String(data));

                // ToastUtil.shortShow(new String(data));
                // writeSencitive((byte) 0x09);
                if (BleFunctionType.COMMAND_TYPE_SETLICENSE == commandType) {
                    BleManager.getInstance().disconnect(mBleDevice);
                    /// 烧写完成
                }

                break;

        }
    }

    private Map getInfos() {

        Map<String, String> mMap = new HashMap<>();
        mMac = mBleDevice.getMac();

        mMap.put("ftId", mFtId);
        mMap.put("deviceName", mDeviceName);
        mMap.put("position", mPosition);
        mMap.put("imei", mImei);
        mMap.put("imsi", mImsi);
        mMap.put("charge", mCharge);
        mMap.put("clientip", mClientIp);
        mMap.put("clientport", mClientPort);

        mMap.put("activehole", mActiveHole);
        mMap.put("deviceid", mDeviceId);
        mMap.put("equid", mEquId);
        mMap.put("equname", mEquiname);
        mMap.put("hardversion", mHardversion);

        mMap.put("heartbeat", mHeartbeat);
        mMap.put("humidity", mHumidity);
        mMap.put("module", mModule);
        mMap.put("moduledetail", mModuleDetail);
        mMap.put("bluetoothid", mBluetoothId);

        mMap.put("nbnetcollect", mNbNetCollect);
        mMap.put("nbsignalstrength", mNbSignalStrength);
        mMap.put("temperature", mTemperature);
        mMap.put("sfversion", mSfversion);
        mMap.put("workstate", mWorkstate);
        mMap.put("type", mType);
        mMap.put("mac", mBleDevice.getMac());
        mMap.put("coap", mCoap);
        mMap.put("error", error);

        return mMap;

    }


    private void showcharacter(byte[] data) {

        if (data != null) {
            byte mconnectstate = data[0];//NB网络连接状态
            byte msignalstrength = data[1];//NB信号强度
            byte mpressure_integer = data[2];//电池电压（整数部分）
            byte mpressure_decimals = data[3];//电池电压（小数部分）
            byte mtemperature_integer = data[4];//温度整数位
            byte mtemperature_decimals = data[5];//温度小数位
            if (mconnectstate == 1) {
                mNbNetCollect = "连接";
            } else if (mconnectstate == 0) {
                mNbNetCollect = "断开";
            }
            StringBuffer charge = new StringBuffer();
            StringBuffer temperature = new StringBuffer();
            charge.append(mpressure_integer).append(".").append(mpressure_decimals);
            temperature.append(mtemperature_integer).append(".").append(mtemperature_decimals);
            mCharge = charge.toString();
            mNbSignalStrength = String.valueOf(msignalstrength);
            mTemperature = temperature + "°c";


            if (data.length >= 7) {
                byte mhumidity = data[6];//相对湿度
                mHumidity = HexUtil.encodeHexStr(new byte[]{mhumidity});

            }

            if (data.length >= 8) {

                byte mactive_hole = data[7];//相对湿度
                mActiveHole = HexUtil.encodeHexStr(new byte[]{mactive_hole});
                byte mtype = data[8];//相对湿度
                mType = HexUtil.encodeHexStr(new byte[]{mtype});
                byte mdevice_id = data[9];//相对湿度
                mDeviceId = HexUtil.encodeHexStr(new byte[]{mdevice_id});
            }

            if (data.length >= 9) {

                byte mtype = data[8];//相对湿度
                mType = HexUtil.encodeHexStr(new byte[]{mtype});
                byte mdevice_id = data[9];//相对湿度
                mDeviceId = HexUtil.encodeHexStr(new byte[]{mdevice_id});
            }

            if (data.length >= 10) {

                byte mdevice_id = data[9];//相对湿度
                mDeviceId = HexUtil.encodeHexStr(new byte[]{mdevice_id});
            }

        } else {


        }
    }


    /**
     * 读的状态值
     *
     * @param parameter
     */
    private void showParameter(String parameter) {
        //218.2.234.170-9610-设备ID-上报周期-MIEI-工作状态


        if (parameter.length() > 0) {
            String[] mSplit = parameter.split("\\-");

            if (mSplit.length == 6) {
                mClientIp = mSplit[0];
                mClientPort = mSplit[1];
                mEquId = mSplit[2];
                mHeartbeat = mSplit[3];
                mImei = mSplit[4];
                mWorkstate = mSplit[5];

            } else if (mSplit.length == 7) {
                mClientIp = mSplit[0];
                mClientPort = mSplit[1];
                mEquId = mSplit[2];
                mHeartbeat = mSplit[3];
                mImei = mSplit[4];
                mWorkstate = mSplit[5];
                mCoap = mSplit[6];
            }

        }
    }

    private void showHardVersion(String parameter) {
        KLog.e(parameter);
        if (parameter.length() > 0) {
            String[] mSplit = parameter.split("\\-");
            if (mSplit.length == 4) {
                mEquiname = mSplit[0];
                mSfversion = mSplit[1];
                mHardversion = mSplit[2];
                mModule = mSplit[3];
            } else if (mSplit.length == 3) {
                mEquiname = mSplit[0];
                mSfversion = mSplit[1];
                mHardversion = mSplit[2];
            }
        }
    }

    public byte[] portToBytes(String inputString) {
        if ("8531".equals(inputString)) {
            inputString = "8111";
        }
        String mSubstring1 = inputString.substring(0, 2);
        String mSubstring2 = inputString.substring(2);
        Integer mI = Integer.parseInt(mSubstring1);
        byte b1 = mI.byteValue();
        Integer mI12 = Integer.parseInt(mSubstring2);
        byte b2 = mI12.byteValue();
        byte[] byte_src = new byte[2];
        byte_src[0] = b1;
        byte_src[1] = b2;
        String mS = HexUtil.encodeHexStr(byte_src);
        return byte_src;
    }

    public static byte[] shortToBytesHL(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n & 0xff);
        return b;
    }

    public static byte[] shortToBytesLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    public byte[] portToBytes_new(String inputString) {
        if ("8531".equals(inputString) || "8870".equals(inputString)) {
            inputString = "8111";
        }
        return shortToBytesHL(Short.parseShort(inputString));
    }


    private synchronized void writeTerminal(int operation, String serviceuuid, String charactoruuid, byte[] b, boolean isSplit) {
        try {
            Thread.sleep(300);
            this.mCurrentOperateType = operation;
            if (BleManager.getInstance().isConnected(mBleDevice)) {
                BleManager.getInstance().write(mBleDevice, serviceuuid, charactoruuid, b, isSplit, mBleWriteCallback);
            } else {
                KLog.e("mBleDevice" + mBleDevice.getName());
            }
        } catch (Exception e) {
            KLog.e("writeTerminal" + operation);
        }
    }

    private synchronized void writeTerminal(int operation, String serviceuuid, String charactoruuid, byte[] b) {

        try {
            Thread.sleep(300);
            this.mCurrentOperateType = operation;
            if (BleManager.getInstance().isConnected(mBleDevice)) {
                BleManager.getInstance().write(mBleDevice, serviceuuid, charactoruuid, b, false, mBleWriteCallback);
            } else {
                KLog.e("mBleDevice" + mBleDevice.getName());
            }
        } catch (Exception e) {
            KLog.e("writeTerminal" + operation);
        }
    }

    private synchronized void writeTerminal_oad(int operation, String serviceuuid, String charactoruuid, byte[] b) {
        try {
            this.mCurrentOperateType = operation;
            if (BleManager.getInstance().isConnected(mBleDevice)) {
                BleManager.getInstance().write(mBleDevice, serviceuuid, charactoruuid, b, false, mBleWriteCallback_oad);
            } else {
                KLog.e("mBleDevice" + mBleDevice.getName());
            }
        } catch (Exception e) {
            KLog.e("writeTerminal" + operation);
        }
    }

    private synchronized void readTerminal(int operation, String serviceuuid, String charactoruuid) {
        try {
            Thread.sleep(300);
            this.mCurrentOperateType = operation;
            BleManager.getInstance().read(mBleDevice, serviceuuid, charactoruuid, mBleReadCallback);
        } catch (Exception e) {

        }
    }

    private void startNotify(String serviceUuid, String characteristicUuid) {
        if (stopConnect) {
            return;
        }
        try {
            Thread.sleep(100);
            BleManager.getInstance().notify(mBleDevice, serviceUuid, characteristicUuid, mBleNotifyCallback);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startNotify_OADControlPointCharacteristic(String serviceUuid, String characteristicUuid) {
        if (stopConnect) {
            return;
        }
        try {
            Thread.sleep(100);
            BleManager.getInstance().notify(mBleDevice, serviceUuid, characteristicUuid, mBleNotifyCallback_OADControlPointCharacteristic);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startNotify__OADImageIdentify(String serviceUuid, String characteristicUuid) {

        if (stopConnect) {
            return;
        }
        try {
            Thread.sleep(100);
            BleManager.getInstance().notify(mBleDevice, serviceUuid, characteristicUuid, mBleNotifyCallback_OADImageIdentify);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopNotify(String serviceUuid, String characteristicUuid) {
        if (isFtControl){
            initTimerForDisconnect();
            startTimerForDisconnect(10000);
        }

        try {
            Thread.sleep(100);
            BleManager.getInstance().stopNotify(mBleDevice, serviceUuid, characteristicUuid);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BleWriteCallback mBleWriteCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(String mac, int current, int total, byte[] justWrite) {

            if (isFtControl){
                initTimerForDisconnect();
                startTimerForDisconnect(10000);
            }
            decodeJustWrite(justWrite);
        }

        @Override
        public void onWriteFailure(String mac, BleException exception) {
            KLog.e("写入失败" + exception.toString());
            BleManager.getInstance().disconnect(mBleDevice);
        }
    };
 private BleWriteCallback mBleWriteCallback_oad = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(String mac, int current, int total, byte[] justWrite) {
            if (isFtControl){
                initTimerForDisconnect();
                startTimerForDisconnect(10000);
            }

            decodeJustWrite(justWrite);
        }

        @Override
        public void onWriteFailure(String mac, BleException exception) {
            KLog.e("写入失败" + exception.toString());
            BleManager.getInstance().disconnect(mBleDevice);
        }
    };

    private BleReadCallback mBleReadCallback = new BleReadCallback() {
        @Override
        public void onReadSuccess(String mac, byte[] data) {

            if (isFtControl){
                initTimerForDisconnect();
                startTimerForDisconnect(10000);
            }

            if (data != null) {
                decodeReadData(data);
            }
        }

        @Override
        public void onReadFailure(String mac, BleException exception) {
            BleManager.getInstance().disconnect(mBleDevice);
        }
    };

    private BleNotifyCallback mBleNotifyCallbackE1 = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess(String mac) {
            addLog("NotifySuccess ");
        }

        @Override
        public void onNotifyFailure(String mac, BleException exception) {
            addLog("NotifyFailure ");
            BleManager.getInstance().disconnect(mBleDevice);
        }

        @Override
        public void onCharacteristicChanged(String mac, byte[] data) {
            decodeNotifyData(data);
        }
    };


    private BleNotifyCallback mBleNotifyCallback = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess(String mac) {
            addLog("NotifySuccess ");
        }

        @Override
        public void onNotifyFailure(String mac, BleException exception) {
            addLog("NotifyFailure ");
            BleManager.getInstance().disconnect(mBleDevice);
        }

        @Override
        public void onCharacteristicChanged(String mac, byte[] data) {
            decodeNotifyData(data);
        }
    };

    private BleNotifyCallback mBleNotifyCallback_OADImageIdentify = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess(String mac) {
            addLog("NotifySuccess ");
        }

        @Override
        public void onNotifyFailure(String mac, BleException exception) {
            addLog("NotifyFailure "+exception.toString());
            BleManager.getInstance().disconnect(mBleDevice);
        }

        @Override
        public void onCharacteristicChanged(String mac, byte[] data) {
            decodeNotifyData(data);
        }
    };

    private BleNotifyCallback mBleNotifyCallback_OADControlPointCharacteristic = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess(String mac) {
            addLog("NotifySuccess ");
        }

        @Override
        public void onNotifyFailure(String mac, BleException exception) {
            addLog("NotifyFailure "+exception.toString());
            BleManager.getInstance().disconnect(mBleDevice);
        }

        @Override
        public void onCharacteristicChanged(String mac, byte[] data) {
            decodeNotifyData(data);
        }
    };


    public void disConnected() {
        BleManager.getInstance().disconnectAllDevice();
    }

    public boolean isConnected(@Nullable String name, @Nullable String mac) {
        List<BleDevice> list = BleManager.getInstance().getAllConnectedDevice();
        for (BleDevice bleDevice : list) {
            if (bleDevice != null) {
                if (bleDevice.getMac().equals(mac) || name.equals(bleDevice.getName())) {
                    mBleDevice = bleDevice;
                    return true;
                }
            }
        }
        return false;
    }


    private byte[] concatenat2Bytes(byte[] b, byte code) {
        byte[] mBytes;
        if (b == null) {
            mBytes = new byte[]{code};
        } else {
            mBytes = new byte[b.length + 1];
            byte[] terminals = new byte[]{code};
            System.arraycopy(terminals, 0, mBytes, 0, terminals.length);
            System.arraycopy(b, 0, mBytes, terminals.length, b.length);
        }
        return mBytes;
    }

    private byte[] concatenat2Bytes_new(byte[] pre, byte[] back) {
        byte[] mBytes;
        mBytes = new byte[pre.length + back.length];
        System.arraycopy(pre, 0, mBytes, 0, pre.length);
        System.arraycopy(back, 0, mBytes, pre.length, back.length);
        return mBytes;
    }


    private byte[] ipToBytes(String data) {

        String[] mSplit = data.split("\\.");
        byte[] byte_src = new byte[mSplit.length];
        for (int i = 0; i < mSplit.length; i++) {
            Integer mI1 = Integer.parseInt(mSplit[i]);
            byte b1 = mI1.byteValue();
            byte_src[i] = b1;
        }
        String mS = HexUtil.encodeHexStr(byte_src);

        return byte_src;
    }

    public void configSoft(Map<String, String> data) {


        mData = data;
        String mBase64 = mData.get("soft");
        mBytes = Base64.decode(mBase64, Base64.DEFAULT);
        if (mBytes != null && mBytes.length != 0) {
            mImageSize = mBytes.length;
            readProfile();
//            readProfilefromAssets();
        }
        KLog.e("configSoft");
        if (BleFunctionType.COMMAND_TYPE_UPDATE_HARDWARE == commandType  && mCurrentOperateType == BleOperateType.OAD_Command) {
            mCurrentOperateType = BleOperateType.GET_OAD_BLOCK_SIZE_COMMAND;
            setNotification();
        }
    }

    private void setNotification() {

        if (isFtControl){
            initTimerForDisconnect();
            startTimerForDisconnect(10000);
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                startNotify__OADImageIdentify(BleConstant.OADService, OADImageIdentify);
//                writeDescriptor(OADImageIdentify);
            }
        }, 100);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startNotify_OADControlPointCharacteristic(BleConstant.OADService, OADControlPointCharacteristic);
//                writeDescriptor(OADControlPointCharacteristic);
            }
        }, 1400);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                getOADBlockSize();
            }
        }, 2500);
    }

    private void getOADBlockSize() {

        if (isFtControl){
            initTimerForDisconnect();
            startTimerForDisconnect(10000);
        }

        if (mCurrentOperateType == BleOperateType.GET_OAD_BLOCK_SIZE_COMMAND) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    addLog("Get_OAD_Block_Size_Command");
                    writeTerminal(BleOperateType.OAD_SEND_IMAGE_IDENTIFY_PAYLOAD, BleConstant.OADService, OADControlPointCharacteristic, new byte[]{BleConstant.Get_OAD_Block_Size_Command});
                }
            }, 700);
        }
    }

    private byte[] readProfile() {

        if (isFtControl){
            initTimerForDisconnect();
            startTimerForDisconnect(10000);
        }

        byte[] mBytes_Image_Identify = new byte[22];
        for (int i = 0; i < 8; i++) {
            mBytes_Image_Identify[i] = mBytes[i];
        }
        mBytes_Image_Identify[8] = mBytes[12];
        mBytes_Image_Identify[9] = mBytes[13];

        for (int i = 10; i < 14; i++) {
            mBytes_Image_Identify[i] = mBytes[i + 6];
        }
        mImageLength = new byte[4];
        for (int i = 14; i < 18; i++) {
            //image length
            mImageLength[i - 14] = mBytes[i + 10];
            mBytes_Image_Identify[i] = mBytes[i + 10];
        }
        for (int i = 18; i < 22; i++) {
            mBytes_Image_Identify[i] = mBytes[i + 14];
        }
        return mBytes_Image_Identify;

    }

    /**
     * assets
     * @return
     */

    private byte[] readProfilefromAssets() {

        if (isFtControl){
            initTimerForDisconnect();
            startTimerForDisconnect(10000);
        }

        byte[] mOAD_Image_Identification_Value = new byte[8];

        byte[] mBytes_i = new byte[80];
        byte[] mBytes_Image_Identify = new byte[22];

        AssetManager manager = mContext.getResources().getAssets();
        try {

            InputStream inputStream = manager.open("lock_712(1).bin");
            DataInputStream mDataInputStream = new DataInputStream(inputStream);
            mDataInputStream.read(mBytes_i, 0, mBytes_i.length);
            for (int i = 0; i < 8; i++) {
                mBytes_Image_Identify[i] = mBytes_i[i];
            }
            mBytes_Image_Identify[8] = mBytes_i[12];
            mBytes_Image_Identify[9] = mBytes_i[13];

            for (int i = 10; i < 14; i++) {
                mBytes_Image_Identify[i] = mBytes_i[i + 6];
            }

            mImageLength = new byte[4];
            for (int i = 14; i < 18; i++) {
                //image length
                mImageLength[i - 14] = mBytes_i[i + 10];
                mBytes_Image_Identify[i] = mBytes_i[i + 10];
            }

            for (int i = 18; i < 22; i++) {
                mBytes_Image_Identify[i] = mBytes_i[i + 14];
            }

            mImageSize = HexUtil.byteArrayToInt(mImageLength);
            mBytes = new byte[mImageSize];
            inputStream.close();
            mDataInputStream.close();

            //InputStream inputStream1 = new FileInputStream(path);
            InputStream inputStream1 = manager.open("lock_712(1).bin");
            DataInputStream mDataInputStream1 = new DataInputStream(inputStream1);
            mDataInputStream1.read(mBytes, 0, mBytes.length);
            inputStream1.close();
            mDataInputStream1.close();

            byte[] mBytes_crc = new byte[4];
            for (int i = 0; i < mBytes_crc.length; i++) {
                mBytes_crc[i] = mBytes_i[i + 8];
            }
            return mBytes_Image_Identify;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mOAD_Image_Identification_Value;
    }

    public void clear(){

        if (mTask != null){
         mTask.cancel();
         mTask = null;
        }

        if (mTimerForDisconnect != null){
            mTimerForDisconnect.cancel();
            mTimerForDisconnect =null;

        }

        //mBleDevice = null;
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
            mHandler = null;
        }

        if (mMap != null) {
            mMap.clear();
            mMap = null;
        }
    }




}
