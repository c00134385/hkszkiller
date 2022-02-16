package com.hksz.demo;

import com.hksz.demo.utils.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class Test1 {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);
        System.out.println("getCertificateList");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Timer timer = new Timer();
        System.out.println("start: " + Utils.currentTimestamp());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("getCertificateList: " + Utils.currentTimestamp());
                countDownLatch.countDown();
            }
        }, 1000*5);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");
    }
}
