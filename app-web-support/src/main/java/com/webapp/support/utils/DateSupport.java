package com.webapp.support.utils;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateSupport {

    public static Date getBeijingTime(){
        TimeZone cnTimeZone = TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT"));
        Calendar calendar = Calendar.getInstance(cnTimeZone);
        Date cnCurrTime = calendar.getTime();
        return cnCurrTime;
    }

    public static Calendar getBeijingCalendar(){
        TimeZone cnTimeZone = TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT"));
        Calendar calendar = Calendar.getInstance(cnTimeZone);
        return calendar;
    }

    public static TimeZone Beijing_ShangHai_TimeZone(){
        TimeZone cnTimeZone = TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT"));
        return cnTimeZone;
    }

    /**
     * 获取当前剩余时间(单位毫秒)
     * @return
     */
    public static long getRemainingTime(TimeZone timeZone){
        Calendar calendar = null;
        if(timeZone!=null){
            calendar = Calendar.getInstance(timeZone);
        }else
            calendar = Calendar.getInstance();

        Date nowDateTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date tomorrowTIme = calendar.getTime();

        return tomorrowTIme.getTime() - nowDateTime.getTime();
    }
}
