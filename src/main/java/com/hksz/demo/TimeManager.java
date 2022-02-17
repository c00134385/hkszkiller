package com.hksz.demo;

import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    private static Calendar calendar = Calendar.getInstance();

    public static Date beginTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Configure.startTime - Configure.timeDelay);
        return calendar.getTime();
    }

    public static Date endTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 30);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Configure.startTime);
        return calendar.getTime();
    }
}
