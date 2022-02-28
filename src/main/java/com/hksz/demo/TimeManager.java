package com.hksz.demo;

import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    private static Calendar calendar = Calendar.getInstance();

    public static Date beginTime() {
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeInMillis(calendar.getTimeInMillis());
        return calendar.getTime();
    }

    public static Date endTime() {
        return new Date(beginTime().getTime() + 10 * 1000);
    }

}
