package com.itek.library_core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author:：simon
 * Date：2019/7/12:2:57 PM
 * Mail：simon@itekiot.com
 * Copyright (c) 2019 itek. All rights reserved.
 * Description：
 */
public class EncryptUtils {


    // 加密秘钥 ，16个字节也就是128 bit
    private static final byte[] AES_KEY = { 0x5e,0x74,0x78,0x73,0x4f,0x76,0x3d,0x55,0x49,0x3d,0x32,0x36,0x3a,0x4c,0x36,0x14};

    private static final byte[] TIME_KEY = {0x06,0x10,0x04,0x0D,0x05,0x0A};
    // 加密方法
    private static byte[] encrypt(byte[] key, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    // 解密方法
    private static byte[] decrypt(byte[] key, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static byte[] getpdfasswfdsoerd(String imei) {

        if (imei.length() != 15){
            ToastUtils.showShort("imei 位数不对");
            return new byte[1];
        }
        byte[] SOURCE_BUF = imei.getBytes();

        byte[] mBytes = new  byte[imei.length()+1];
        byte[] terminals = new byte[]{0x00};

        System.arraycopy(terminals, 0, mBytes, 0, terminals.length);
        System.arraycopy(SOURCE_BUF, 0, mBytes, terminals.length, SOURCE_BUF.length);

       /* for (int i = 0; i < newImei.length(); i++) {
            String mSubstring = newImei.substring(i, i + 1);
            byte mB = Integer.valueOf(mSubstring).byteValue();
            SOURCE_BUF[i] = mB;
        }*/
        byte[] mBytes_time = getNewTimestampe();

        byte[] encryBuf = new byte[16];

        for (int i = 0; i < 6; i++) {
            Integer in =  mBytes[i]+mBytes_time[i];
            byte mB = in.byteValue();
            mBytes[i] = mB;

        }
        try {
            encryBuf = encrypt(mBytes, AES_KEY);

        } catch (Exception e) {
            e.printStackTrace();
        }


        byte[] mB = new byte[mBytes_time.length + encryBuf.length ];

        System.arraycopy(mBytes_time, 0, mB, 0, mBytes_time.length);
        System.arraycopy(encryBuf, 0, mB, mBytes_time.length , encryBuf.length);
        return mB;
    }

    public static byte[] getNewTimestampe() {
        byte[] mBytes = new byte[6];
        byte[] timestampe = getTimestampe();

        for (int i = 0; i < timestampe.length; i++) {
            mBytes[i] = (byte) (TIME_KEY[i] + timestampe[i]);
        }
        return mBytes;
    }


    public static byte[] getTimestampe() {

        List<String> mList = new ArrayList<>();
        Date mDate = new Date();
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String strD = lsdStrFormat.format(mDate);
        String mSubstring2 = strD.substring(2, 4);
        String mSubstring3 = strD.substring(4, 6);
        String mSubstring4 = strD.substring(6, 8);
        String mSubstring5 = strD.substring(8, 10);
        String mSubstring6 = strD.substring(10, 12);
        String mSubstring1 = strD.substring(12);
        mList.add(mSubstring2);
        mList.add(mSubstring3);
        mList.add(mSubstring4);
        mList.add(mSubstring5);
        mList.add(mSubstring6);
        mList.add(mSubstring1);

        byte[] mBytes = new byte[6];
        for (int i = 0; i < mList.size(); i++) {
            mBytes[i] = Integer.valueOf(mList.get(i)).byteValue();
        }
        return mBytes;
    }

}
