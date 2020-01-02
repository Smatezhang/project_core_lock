package com.itek.library_core.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return (char) (s.charAt(0) - 32) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return (char) (s.charAt(0) + 32) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(final String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }


    public static String[] decodeControllerQrcode(String controllerQrcode) {

        String[] result = new String[2];
        String mScancode = "";
        String mLockno = "";
        if (controllerQrcode.contains("equcode=") && controllerQrcode.contains("lockno=")) {
            String[] mSplit = controllerQrcode.split("equcode=");
             mScancode = mSplit[1].substring(0, 8);
            String[] mlockno_pre = controllerQrcode.split("lockno=");
             mLockno = mlockno_pre[1].substring(0, 1);
        } else if (controllerQrcode.contains("equcode=")) {
            String[] mSplit = controllerQrcode.split("equcode=");
             mScancode = mSplit[1].substring(0, 8);
        } else if (controllerQrcode.length() == 8) {
            mScancode = controllerQrcode;
        }
        result[0] = mScancode;
        result[1] = mLockno;

       return result;
    }


    public static boolean isNumeric(String str) {

        if (str == null || TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static  byte[] iDToBytes(String data) {//设备ID为64开头
        byte[] byte_src = new byte[4];
        try {
            String mSubstring1 = data.substring(0, 2);
            String mSubstring2 = data.substring(2, 4);
            String mSubstring3 = data.substring(4, 6);
            String mSubstring4 = data.substring(6);
            Integer mI1 = Integer.parseInt(mSubstring1, 16);
            byte b1 = mI1.byteValue();
            Integer mI2 = Integer.parseInt(mSubstring2, 16);
            byte b2 = mI2.byteValue();
            Integer mI3 = Integer.parseInt(mSubstring3, 16);
            byte b3 = mI3.byteValue();
            Integer mI4 = Integer.parseInt(mSubstring4, 16);
            byte b4 = mI4.byteValue();

            byte_src[0] = b1;
            byte_src[1] = b2;
            byte_src[2] = b3;
            byte_src[3] = b4;
            String mS = HexUtil.encodeHexStr(byte_src);
        }catch (Exception e){

        }
        return byte_src;
    }

    public static  byte[] secretToBytes(String data) {//设备ID为64开头

        byte[] byte_src = new byte[5];
        try {
            String mSubstring1 = data.substring(0, 2);
            String mSubstring2 = data.substring(2, 4);
            String mSubstring3 = data.substring(4, 6);
            String mSubstring4 = data.substring(6,8);
            String mSubstring5 = data.substring(8);
            Integer mI1 = Integer.parseInt(mSubstring1, 16);
            byte b1 = mI1.byteValue();
            Integer mI2 = Integer.parseInt(mSubstring2, 16);
            byte b2 = mI2.byteValue();
            Integer mI3 = Integer.parseInt(mSubstring3, 16);
            byte b3 = mI3.byteValue();
            Integer mI4 = Integer.parseInt(mSubstring4, 16);
            byte b4 = mI4.byteValue();
            Integer mI5 = Integer.parseInt(mSubstring5, 16);
            byte b5 = mI5.byteValue();

            byte_src[0] = b1;
            byte_src[1] = b2;
            byte_src[2] = b3;
            byte_src[3] = b4;
            byte_src[4] = b4;
            String mS = HexUtil.encodeHexStr(byte_src);
        }catch (Exception e){

        }
        return byte_src;
    }


    public static  byte[] timeToBytes(String time) {//设备ID为64开头
        byte[] byte_src = new byte[6];
        try {
            String mSubstring1 = time.substring(2, 4);
            String mSubstring2 = time.substring(4, 6);
            String mSubstring3 = time.substring(6, 8);
            String mSubstring4 = time.substring(8,10);
            String mSubstring5 = time.substring(10,12);
            String mSubstring6 = time.substring(12,14);
            Integer mI1 = Integer.parseInt(mSubstring1, 16);
            byte b1 = mI1.byteValue();
            Integer mI2 = Integer.parseInt(mSubstring2, 16);
            byte b2 = mI2.byteValue();
            Integer mI3 = Integer.parseInt(mSubstring3, 16);
            byte b3 = mI3.byteValue();
            Integer mI4 = Integer.parseInt(mSubstring4, 16);
            byte b4 = mI4.byteValue();

            Integer mI5 = Integer.parseInt(mSubstring5, 16);
            byte b5 = mI5.byteValue();

            Integer mI6 = Integer.parseInt(mSubstring6, 16);
            byte b6 = mI6.byteValue();

            byte_src[0] = b1;
            byte_src[1] = b2;
            byte_src[2] = b3;
            byte_src[3] = b4;
            byte_src[4] = b5;
            byte_src[5] = b6;
            String mS = HexUtil.encodeHexStr(byte_src);
        }catch (Exception e){

        }
        return byte_src;
    }



}
