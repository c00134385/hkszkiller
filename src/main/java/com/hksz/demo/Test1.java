package com.hksz.demo;

import com.hksz.demo.utils.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class Test1 {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);
        System.out.println("getCertificateList");

        System.out.println("startTime"+ TimeManager.beginTime());
        System.out.println("endTime"+ TimeManager.endTime());

        System.out.println("isAfter " + new Date().after(TimeManager.endTime()));
        System.out.println("isBefore " + new Date().before(TimeManager.endTime()));

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Configure.startTime);

        System.out.println("getCertificateList: " + calendar.getTime());

        Timer timer = new Timer();
        System.out.println("start: " + Utils.currentTimestamp());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("getCertificateList: " + Utils.currentTimestamp());
                countDownLatch.countDown();
            }
        }, calendar.getTime());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");
    }
}
