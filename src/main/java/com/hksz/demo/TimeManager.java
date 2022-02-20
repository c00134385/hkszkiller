package com.hksz.demo;

import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    private static Calendar calendar = Calendar.getInstance();

    public static Date beginTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Configure.timeOffset);
        return calendar.getTime();
    }

    public static Date endTime() {
        return new Date(beginTime().getTime() + 10 * 1000);
    }

    public static Date endTimeOfQueryRooms() {
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 30);
        calendar.setTimeInMillis(calendar.getTimeInMillis());
        return calendar.getTime();
    }
}
