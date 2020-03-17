package com.newland.spring.platcore.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: garfield
 * @Date: 2019/3/11 14:08
 * @Description:
 */
public class TimeUtils {

    public static SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss SSS");

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
//    public static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss SSS");


    public static Date getDateTime() throws ParseException {
        String dateTime = "2016-12-31 12:30:45 123";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(dateTime));
        System.out.println("日期[2016-12-31 12:30:45 123]对应毫秒：" + calendar.getTimeInMillis());
        return new Date();
    }

    private static Calendar calendar = Calendar.getInstance();


    //该方法多线程会有问题，所以，新建两个属性，用来算时间
    public static long stringToMil(String time) throws ParseException {
        calendar.setTime(format.parse(time));
        return calendar.getTimeInMillis();
    }

    public void milToDate(Long mil) {
        long millisecond = 1483159625851l;
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss SSS a");
        System.out.println("毫秒[1483159625851]对应日期时间字符串：" + format.format(date));
    }

    public static String getStringTime(Date date){
        return format.format(date);
    }

    public static String getDayStringTime(Date date){
        return format.format(date).substring(0,10);
    }


//    public static void main(String[] args) throws ParseException {
//        System.out.println(stringToMil("2019-03-13 15:04:54 973") - stringToMil("2019-03-13 15:04:54 973"));
//        System.out.println(format.format(new Date()));
//    }
}
