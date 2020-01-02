package com.itek.library_core.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({"MissingPermission"})
public class TelephoneUtil {
    public TelephoneUtil() {
    }

    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();

        return IMSI;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();

        return IMEI;
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();

        return phoneNumber;
    }

    public static String printTelephoneInfo(Context context) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______ 手机信息  ").append(time).append(" ______________");
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = tm.getSubscriberId();
        String providerName = null;
        if(IMSI != null) {
            if(!IMSI.startsWith("46000") && !IMSI.startsWith("46002")) {
                if(IMSI.startsWith("46001")) {
                    providerName = "中国联通";
                } else if(IMSI.startsWith("46003")) {
                    providerName = "中国电信";
                }
            } else {
                providerName = "中国移动";
            }
        }

        sb.append(providerName).append("  手机号：").append(tm.getLine1Number()).append(" IMSI是：").append(IMSI);
        sb.append("\nDeviceID(IMEI)       :").append(tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion:").append(tm.getDeviceSoftwareVersion());
        sb.append("\ngetLine1Number       :").append(tm.getLine1Number());
        sb.append("\nNetworkCountryIso    :").append(tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator      :").append(tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName  :").append(tm.getNetworkOperatorName());
        sb.append("\nNetworkType          :").append(tm.getNetworkType());
        sb.append("\nPhoneType            :").append(tm.getPhoneType());
        sb.append("\nSimCountryIso        :").append(tm.getSimCountryIso());
        sb.append("\nSimOperator          :").append(tm.getSimOperator());
        sb.append("\nSimOperatorName      :").append(tm.getSimOperatorName());
        sb.append("\nSimSerialNumber      :").append(tm.getSimSerialNumber());
        sb.append("\ngetSimState          :").append(tm.getSimState());
        sb.append("\nSubscriberId         :").append(tm.getSubscriberId());
        sb.append("\nVoiceMailNumber      :").append(tm.getVoiceMailNumber());

        return sb.toString();
    }

    public static TelephoneUtil.TeleInfo getMtkTeleInfo(Context context) {
        TelephoneUtil.TeleInfo teleInfo = new TelephoneUtil.TeleInfo();

        try {
            Class<?> phone = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = phone.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            int simId_1 = ((Integer)fields1.get(null)).intValue();
            Field fields2 = phone.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            int simId_2 = ((Integer)fields2.get(null)).intValue();
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getSubscriberIdGemini = TelephonyManager.class.getDeclaredMethod("getSubscriberIdGemini", Integer.TYPE);
            String imsi_1 = (String)getSubscriberIdGemini.invoke(tm, new Object[]{Integer.valueOf(simId_1)});
            String imsi_2 = (String)getSubscriberIdGemini.invoke(tm, new Object[]{Integer.valueOf(simId_2)});
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imsi_2 = imsi_2;
            Method getDeviceIdGemini = TelephonyManager.class.getDeclaredMethod("getDeviceIdGemini", Integer.TYPE);
            String imei_1 = (String)getDeviceIdGemini.invoke(tm, new Object[]{Integer.valueOf(simId_1)});
            String imei_2 = (String)getDeviceIdGemini.invoke(tm, new Object[]{Integer.valueOf(simId_2)});
            teleInfo.imei_1 = imei_1;
            teleInfo.imei_2 = imei_2;
            Method getPhoneTypeGemini = TelephonyManager.class.getDeclaredMethod("getPhoneTypeGemini", Integer.TYPE);
            int phoneType_1 = ((Integer)getPhoneTypeGemini.invoke(tm, new Object[]{Integer.valueOf(simId_1)})).intValue();
            int phoneType_2 = ((Integer)getPhoneTypeGemini.invoke(tm, new Object[]{Integer.valueOf(simId_2)})).intValue();
            teleInfo.phoneType_1 = phoneType_1;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        return teleInfo;
    }

    public static TelephoneUtil.TeleInfo getMtkTeleInfo2(Context context) {
        TelephoneUtil.TeleInfo teleInfo = new TelephoneUtil.TeleInfo();

        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> phone = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = phone.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            int simId_1 = ((Integer)fields1.get(null)).intValue();
            Field fields2 = phone.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            int simId_2 = ((Integer)fields2.get(null)).intValue();
            Method getDefault = TelephonyManager.class.getMethod("getDefault", Integer.TYPE);
            TelephonyManager tm1 = (TelephonyManager)getDefault.invoke(tm, new Object[]{Integer.valueOf(simId_1)});
            TelephonyManager tm2 = (TelephonyManager)getDefault.invoke(tm, new Object[]{Integer.valueOf(simId_2)});
            String imsi_1 = tm1.getSubscriberId();
            String imsi_2 = tm2.getSubscriberId();
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imsi_2 = imsi_2;
            String imei_1 = tm1.getDeviceId();
            String imei_2 = tm2.getDeviceId();
            teleInfo.imei_1 = imei_1;
            teleInfo.imei_2 = imei_2;
            int phoneType_1 = tm1.getPhoneType();
            int phoneType_2 = tm2.getPhoneType();
            teleInfo.phoneType_1 = phoneType_1;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        return teleInfo;
    }

    @SuppressLint({"WrongConstant"})
    public static TelephoneUtil.TeleInfo getQualcommTeleInfo(Context context) {
        TelephoneUtil.TeleInfo teleInfo = new TelephoneUtil.TeleInfo();

        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
            Class<?> simTMclass = Class.forName("android.telephony.MSimTelephonyManager");
            Object sim = context.getSystemService("phone_msim");
            int simId_1 = 0;
            int simId_2 = 1;
            Method getSubscriberId = simTMclass.getMethod("getSubscriberId", Integer.TYPE);
            String imsi_1 = (String)getSubscriberId.invoke(sim, new Object[]{Integer.valueOf(simId_1)});
            String imsi_2 = (String)getSubscriberId.invoke(sim, new Object[]{Integer.valueOf(simId_2)});
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imsi_2 = imsi_2;
            Method getDeviceId = simTMclass.getMethod("getDeviceId", Integer.TYPE);
            String imei_1 = (String)getDeviceId.invoke(sim, new Object[]{Integer.valueOf(simId_1)});
            String imei_2 = (String)getDeviceId.invoke(sim, new Object[]{Integer.valueOf(simId_2)});
            teleInfo.imei_1 = imei_1;
            teleInfo.imei_2 = imei_2;
            Method getDataState = simTMclass.getMethod("getDataState");
            int phoneType_1 = tm.getDataState();
            int phoneType_2 = ((Integer)getDataState.invoke(sim, new Object[0])).intValue();
            teleInfo.phoneType_1 = phoneType_1;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        return teleInfo;
    }

    public static TelephoneUtil.TeleInfo getSpreadtrumTeleInfo(Context context) {
        TelephoneUtil.TeleInfo teleInfo = new TelephoneUtil.TeleInfo();

        try {
            TelephonyManager tm1 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi_1 = tm1.getSubscriberId();
            String imei_1 = tm1.getDeviceId();
            int phoneType_1 = tm1.getPhoneType();
            teleInfo.imsi_1 = imsi_1;
            teleInfo.imei_1 = imei_1;
            teleInfo.phoneType_1 = phoneType_1;
            Class<?> phoneFactory = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method getServiceName = phoneFactory.getMethod("getServiceName", String.class, Integer.TYPE);
            getServiceName.setAccessible(true);
            String spreadTmService = (String)getServiceName.invoke(phoneFactory, new Object[]{"phone", Integer.valueOf(1)});
            TelephonyManager tm2 = (TelephonyManager)context.getSystemService(spreadTmService);
            String imsi_2 = tm2.getSubscriberId();
            String imei_2 = tm2.getDeviceId();
            int phoneType_2 = tm2.getPhoneType();
            teleInfo.imsi_2 = imsi_2;
            teleInfo.imei_2 = imei_2;
            teleInfo.phoneType_2 = phoneType_2;
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        return teleInfo;
    }

    public static class TeleInfo {
        public String imsi_1;
        public String imsi_2;
        public String imei_1;
        public String imei_2;
        public int phoneType_1;
        public int phoneType_2;

        public TeleInfo() {
        }

        public String toString() {
            return "TeleInfo{imsi_1='" + this.imsi_1 + '\'' + ", imsi_2='" + this.imsi_2 + '\'' + ", imei_1='" + this.imei_1 + '\'' + ", imei_2='" + this.imei_2 + '\'' + ", phoneType_1=" + this.phoneType_1 + ", phoneType_2=" + this.phoneType_2 + '}';
        }
    }
}
