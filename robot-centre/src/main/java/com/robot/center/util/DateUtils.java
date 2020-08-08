package com.robot.center.util;

import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class DateUtils {

    public static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String getEndPayDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX));
    }

    public static String getStartPayDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
    }

    public static LocalDateTime getStartDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        } else {
            return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
        }
    }

    public static LocalDateTime getStartDateAmerican(LocalDateTime localDateTime) {
        return LocalDateTime.of(getAmericaLocalDateTime(localDateTime).toLocalDate(), LocalTime.MIN);
    }

    public static LocalDateTime getStartDateAmerican() {
        return getStartDateAmerican(null);
    }

    public static LocalDateTime getEndDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }

    public static LocalDateTime getEndDateAmerican() {
        return getEndDateAmerican(null);
    }

    public static LocalDateTime getEndDateAmerican(LocalDateTime localDateTime) {
        return LocalDateTime.of(getAmericaLocalDateTime(localDateTime).toLocalDate(), LocalTime.MAX);
    }

    public static LocalDateTime getPreviousDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return localDateTime.plusDays(-1);
    }

    public static LocalDateTime getPreviousDateTime() {
        return getPreviousDateTime(null);
    }
    public static LocalDateTime getPreviousAmericaDateTime() {
        return getAmericaLocalDateTime(getPreviousDateTime(null));
    }

    public static String getPreviousAmericaDateTimeString() {
        return format(getAmericaLocalDateTime(getPreviousDateTime(null)));
    }
    public static String getPreviousAmericaPayDate() {
        return format(getStartDate(getPreviousDateTime(null)));
    }

//    public static LocalDateTime getPreviousStartDateTime(LocalDate localDate) {
//        if (localDate == null) {
//            return getStartDate(LocalDate.now().plusDays(-1));
//        } else {
//            return getStartDate(localDate.plusDays(-1));
//        }
//    }

    public static String getAmericanPayDate() {
        return getAmericanPayDate(null);
    }
//
    public static String getAmericanPayDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return format(LocalDateTime.of(getAmericaLocalDateTime(localDateTime).toLocalDate(), LocalTime.MIN));
    }

    public static String getPayDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
    }

    //
    public static LocalDateTime getPreviousDays(LocalDateTime localDateTime,int num) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return localDateTime.minusDays(num);
    }
//
//    public static LocalDateTime getPreviousStartDate() {
//        return getPreviousStartDateTime(null);
//    }

    public static LocalDateTime getAmericaLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
            // throw new IllegalArgumentException("localDateTime is null");
        }
        return localDateTime.plusHours(-12);
    }
    public static LocalDateTime format(String localDateTime) {
        if (StringUtils.isEmpty(localDateTime)) {
//            localDateTime = LocalDateTime.now();
             throw new IllegalArgumentException("localDateTime is null");
        }else {
           return LocalDateTime.parse(localDateTime,df);
        }
    }
    public static LocalDateTime formatAmerica(String localDateTime) {
        if (StringUtils.isEmpty(localDateTime)) {
//            localDateTime = LocalDateTime.now();
            throw new IllegalArgumentException("localDateTime is null");
        }else {
            return getAmericaLocalDateTime(LocalDateTime.parse(localDateTime,df)) ;
        }
    }

    public static LocalDateTime getAmericaLocalDateTime() {
        return getAmericaLocalDateTime(LocalDateTime.now());
    }

    public static LocalDate getAmericaLocalDate() {
        return getAmericaLocalDateTime(LocalDateTime.now()).toLocalDate();
    }

    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            throw new IllegalArgumentException("localDateTime is null");
        }
        return localDateTime.format(df);
    }



    public  static LocalDateTime getLocalDate(Long datetimeLong){
        if(datetimeLong==null){
            throw new IllegalArgumentException("datetimeLong is null");
        }
       return LocalDateTime.ofEpochSecond(datetimeLong, 0, ZoneOffset.ofHours(8));
    }
//
//    public static void main(String[] args) {
//
//
//        ZoneId america = ZoneId.of("America/New_York");
////
//        LocalDateTime localtDateAndTime = LocalDateTime.now();
//
//        ZonedDateTime dateAndTimeInNewYork = ZonedDateTime.of(localtDateAndTime, america );
//
//        System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork);
//
//        System.out.println(LocalDate.now().plusDays(-1));
//
//        System.out.println(localtDateAndTime.plusHours(-12));
//
//    }

    public static boolean isRange(LocalDateTime startDate,LocalDateTime endDate){
        isIllegalTime(startDate,endDate);
        LocalDateTime now=LocalDateTime.now();
        if(startDate.compareTo(now)<0&&endDate.compareTo(now)>0){
            return true;
        }
        return false;
    }
    public static void isIllegalTime(LocalDateTime startDate,LocalDateTime endDate){
        if(startDate==null){
            throw new  IllegalArgumentException("startDate is null");
        }
        if(endDate==null){
            throw new IllegalArgumentException("endDate is null");
        }
    }
    public static boolean isLocalTimeRange(LocalDateTime startDate, LocalDateTime endDate){
        isIllegalTime(startDate,endDate);
        LocalTime now=LocalTime.now();
        LocalTime startLocalTime=LocalTime.of(startDate.getHour(),startDate.getMinute(),startDate.getSecond());
        LocalTime endLocalTime=LocalTime.of(endDate.getHour(),endDate.getMinute(),endDate.getSecond());
        if(startLocalTime.compareTo(now)<=0&&endLocalTime.compareTo(now)>=0){
            return true;
        }
//        if(startDate.getHour()<now.getHour()&&endDate.getHour()>=now.getHour()){
//            return true;
//        }
        return false;
    }

    public static boolean isMaintainTime(LocalTime localTime1, LocalTime localTime2) {
        checkNull(localTime1, localTime2);
        localTime1 = getChinaTime(localTime1);
        localTime2 = getChinaTime(localTime2);
        return compare(localTime1, localTime2);
    }

    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static LocalTime getChinaTime(LocalTime localTime) {
        if (localTime == null) {
            throw new IllegalArgumentException("localDateTime is null");
        }
        return localTime.plusHours(12);
    }
    public static boolean isChinaMaintainTime(LocalTime localTime1, LocalTime localTime2) {
        checkNull(localTime1, localTime2);
        return compare(localTime1, localTime2);
    }

    private static void checkNull(LocalTime localTime1, LocalTime localTime2) {
        if (localTime1 == null) {
            throw new IllegalArgumentException("localTime1 is null");
        }
        if (localTime2 == null) {
            throw new IllegalArgumentException("localTime2 is null");
        }
    }

    private static boolean compare(LocalTime localTime1, LocalTime localTime2) {
        LocalTime localTime = LocalTime.now();
        if (localTime1.compareTo(localTime2) > 0) {
            if (localTime2.compareTo(localTime) < 0 && localTime1.compareTo(localTime) > 0) {
                return true;
            }
        } else {
            if (localTime1.compareTo(localTime) < 0 && localTime2.compareTo(localTime) > 0) {
                return true;
            }
        }
        return false;
    }


    public static boolean isRange(String startDate,String endDate){
        if(StringUtils.isEmpty(startDate)){
            throw new  IllegalArgumentException("startDate is null");
        }
        if(StringUtils.isEmpty(endDate)){
            throw new IllegalArgumentException("endDate is null");
        }
        LocalDateTime start=format(startDate);
        LocalDateTime end=format(endDate);
      return isRange(start,end);
    }


    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss 'GMT-04:00' yyyy").withLocale(Locale.US);
    //打款平台GMT字符串时间转LocalDatetime
    public static LocalDateTime gmtStrToTime(String dateStr) throws Exception{

        return LocalDateTime.parse(dateStr, formatter);
    }

    public static void main(String[] args) {
        String dateStr = "Mon Jun 17 03:55:36 GMT-04:00 2019";
        try {
            LocalDateTime localDateTime = gmtStrToTime(dateStr);
            System.out.println(formatter.format(localDateTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
