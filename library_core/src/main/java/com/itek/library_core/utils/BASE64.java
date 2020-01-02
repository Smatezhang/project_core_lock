package com.itek.library_core.utils;

import android.util.Base64;

public class BASE64 {

    public static byte[] encode(byte[] plain) {
        return Base64.encode(plain, Base64.DEFAULT);
    }

    public static String encodeToString(byte[] plain) {
        return Base64.encodeToString(plain, Base64.DEFAULT);
    }

    public static byte[] decode(String text) {
        return Base64.decode(text, Base64.DEFAULT);
    }

    public static byte[] decode(byte[] text) {
        return Base64.decode(text, Base64.DEFAULT);
    }
}