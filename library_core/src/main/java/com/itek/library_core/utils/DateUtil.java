package com.itek.library_core.utils;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import static java.lang.System.currentTimeMillis;


public class DateUtil {

    /* 日期格式 */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_PATTERN_STR = "yyyy年MM月dd日";
    /* 时间格式 */
    public static final String TIME_PATTERN = "HH:mm:ss";
    /* 日期时间格式 */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /* 日期时间毫秒格式 */
    public static final String DATETIME_SSS_PATTERN = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String DATE_PATTERN_WITHOUT_UNDERLINE = "yyyyMMddHHmmss";

    /** 准备第一个模板，从字符串中提取出日期数字  */
    private static String pat1 = "yyyy-MM-dd HH:mm:ss" ;
    /** 准备第二个模板，将提取后的日期数字变为指定的格式*/
    private static String pat2 = "yyyy年MM月dd日 HH:mm:ss" ;
    /** 实例化模板对象*/
    private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1) ;
    private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2) ;

    public static Long farmatTime(String string){
        Date date =null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(new Date());
    }


    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDateSSS() {
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_SSS_PATTERN);
        return df.format(new Date());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getTomorrowDate() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return String.valueOf(Integer.valueOf(df.format(new Date())) + 1);
    }


    /**
     * 获取当前日期
     * @return
     */
    public static String getCustomDateWithoutUnderline(Date date) {

        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN_WITHOUT_UNDERLINE);

        return df.format(date);
    }





    /**
     * 获取当前日期字符串
     * @return
     */
    public static String getCurrentDateString() {
        SimpleDateFormat df = new SimpleDateFormat(DATETIME_PATTERN);
        return df.format(new Date());
    }

    /**
     * 获取当前年
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前日
     * @return
     */
    public static int getCurrentDay() {
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     * 切割标准时间
     * @param time
     * @return
     */
    @Nullable
    public static String subStandardTime(String time) {
        int idx = time.indexOf(".");
        if (idx > 0) {
            return time.substring(0, idx).replace("T"," ");
        }
        return null;
    }

    /**
     * 将时间戳转化为字符串
     * @param showTime
     * @return
     */
    public static String formatTime2String(long showTime) {
        return formatTime2String(showTime,false);
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_PATTERN);
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    /**
     *  将日期转化为long
     * @return
     */
    public static Date formatStrDate2Date(String date, String pattern) {

        Date mDate = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            mDate = df.parse(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mDate;
    }


    /**
     * 获取pattern格式的当前日期
     */
    public static final String getCurrentDate(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        return df.format(date);
    }

    /**
     * 将date转化为字符串

     * @return
     */
    public static String formatDate2String(String date, String pattern) {

        long mTime = 0L;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            Date mParse = df.parse(date);
            mTime = mParse.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formatTime2String(mTime,true);
    }

    public static String formatTime2String(long showTime , boolean haveYear) {
        String str = "";
        long  days = 0L;
        long  months = 0L;
        long distance = currentTimeMillis() / 1000 - showTime / 1000;
        if(distance < 300){
            str = "刚刚";
        }else if( distance < 600){
            str = "5分钟前";
        }else if( distance < 1200){
            str = "10分钟前";
        }else if( distance < 1800){
            str = "20分钟前";
        }else if( distance < 3600){
            str = "半小时前";
        }else {
            Date date = new Date(showTime);
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
            str = formatDateTime(sdf.format(date) , haveYear);
        }


        /*

                if( distance < 60 * 60 * 24){

                    days = distance / 3600;

                    str = days + "天前";

                }else if( distance < 60 * 60 * 24 * 30){

                    months = distance / 60 * 60 * 24 * 30;

                    str = months + "月前";

                }
                */



        return str;
    }

    public static String formatDate2String(String time) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_PATTERN);
        if(time == null){
            return "未知";
        }
        try {
            long createTime = format.parse(time).getTime() / 1000;
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - createTime - 24 * 3600 > 0) { //超出一天
                return (currentTime - createTime) / (24 * 3600) + "天前";
            } else {
                return (currentTime - createTime) / 3600 + "小时前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    public static String formatDateTime(String time , boolean haveYear) {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_PATTERN);
        if(time == null){
            return "";
        }
        Date date;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);
        if(current.after(today)){
            return "今天 "+time.split(" ")[1];
        }else if(current.before(today) && current.after(yesterday)){
            return "昨天 "+time.split(" ")[1];
        }else{
            if(haveYear) {
                int index = time.indexOf(" ");
                return time.substring(0,index);
            }else {
                int yearIndex = time.indexOf("-")+1;
                int index = time.indexOf(" ");
                return time.substring(yearIndex).substring(0,index);
            }
        }
    }


    public static String getTime(String commitDate) {
        // 在主页面中设置当天时间
        Date nowTime = new Date();
        String currDate = sdf1.format(nowTime);
        Date date = null ;
        try{
            // 将给定的字符串中的日期提取出来
            date = sdf1.parse(commitDate) ;
        }catch(Exception e){
            e.printStackTrace() ;
        }
        int nowDate = Integer.valueOf(currDate.substring(8, 10));
        int commit = Integer.valueOf(commitDate.substring(8, 10));
        String monthDay = sdf2.format(date).substring(5, 12);
        String yearMonthDay = sdf2.format(date).substring(0, 12);
        int month = Integer.valueOf(monthDay.substring(0, 2));
        int day = Integer.valueOf(monthDay.substring(3, 5));
        if (month < 10 && day <10) {
            monthDay = monthDay.substring(1, 3) + monthDay.substring(4);
        }else if(month < 10){
            monthDay = monthDay.substring(1);
        }else if (day < 10) {
            monthDay = monthDay.substring(0, 3) + monthDay.substring(4);
        }
        int yearMonth = Integer.valueOf(yearMonthDay.substring(5, 7));
        int yearDay = Integer.valueOf(yearMonthDay.substring(8, 10));
        if (yearMonth < 10 && yearDay <10) {
            yearMonthDay = yearMonthDay.substring(0, 5) + yearMonthDay.substring(6,8) + yearMonthDay.substring(9);
        }else if(yearMonth < 10){
            yearMonthDay = yearMonthDay.substring(0, 5) + yearMonthDay.substring(6);
        }else if (yearDay < 10) {
            yearMonthDay = yearMonthDay.substring(0, 8) + yearMonthDay.substring(9);
        }
        String str = " 00:00:00";
        float currDay = farmatTime(currDate.substring(0, 10) + str);
        float commitDay = farmatTime(commitDate.substring(0, 10) +str);
        int currYear = Integer.valueOf(currDate.substring(0, 4));
        int commitYear = Integer.valueOf(commitDate.substring(0, 4));
        int flag = (int)(farmatTime(currDate)/1000 - farmatTime(commitDate)/1000);
        String des = null;
        String hourMin = commitDate.substring(11, 16);
        int temp = flag;
        if (temp < 60) {
            System.out.println("A");
            if (commitDay < currDay) {
                des = "昨天  " + hourMin;
            }else {
                des = "刚刚";
            }
        }else if (temp < 60 * 60) {
            System.out.println("B");
            if (commitDay < currDay) {
                des = "昨天  " + hourMin;
            }else {
                des = temp/60 + "分钟以前";
            }
        }else if(temp< 60*60*24){
            System.out.println("C");
            int hour = temp / (60*60);
            if (commitDay < currDay) {
                des = "昨天  " + hourMin;
            }else {
                if (hour < 6) {
                    des = hour + "小时前";
                }else {
                    des = hourMin;
                }
            }
        }else if (temp < (60 * 60 * 24 *2 )) {
            System.out.println("D");
            if (nowDate - commit == 1) {
                des = "昨天  " + hourMin;
            }else {
                des = "前天  " + hourMin;
            }
        }else if (temp < 60 * 60 * 60 * 3) {
            System.out.println("E");
            if (nowDate - commit == 2) {
                des = "前天  " + hourMin;
            }else {
                if (commitYear < currYear) {
                    des = yearMonthDay + hourMin;
                }else {
                    des = monthDay + hourMin;
                }
            }
        }else {
            System.out.println("F");
            if (commitYear < currYear) {
                des = yearMonthDay + hourMin;
            }else {
                des = monthDay + hourMin;
            }
        }
        if (des == null) {
            des = commitDate;
        }
        return des;
    }



}
