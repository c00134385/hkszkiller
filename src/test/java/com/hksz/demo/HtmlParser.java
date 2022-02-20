package com.hksz.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class HtmlParser {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);


//        System.out.println("---- 1");
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("---- 2");
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                countDownLatch.countDown();
//                System.out.println("---- 3");
//            }
//        }).start();
//
//        System.out.println("---- 4");
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("---- 5");
//        System.exit(0);



        File file = new File("test.html");
        System.out.println("file: " + file.exists());

        if(file.exists()) {
            try {
                Document document = Jsoup.parse(file, null);
//                String checkInDate = document.getElementById("hidCheckinDate").attr("value");
//                String timespan = document.getElementById("hidTimespan").attr("value");
//                String sign = document.getElementById("hidSign").attr("value");
//                String houseType = document.getElementById("hidHouseType").attr("value");
//                System.out.println(checkInDate);

//                Document document = Jsoup.parse(html);
                if(null == document.getElementById("hidCheckinDate")
                        || null == document.getElementById("hidTimespan")
                        || null == document.getElementById("hidSign")
                        || null == document.getElementById("hidHouseType")) {
                    return;
                }

                String checkInDate;
                String checkCode;
                long timespan;
                String sign;
                int houseType;

                checkInDate = document.getElementById("hidCheckinDate").attr("value");
                timespan = Long.parseLong(document.getElementById("hidTimespan").attr("value"));
                sign = document.getElementById("hidSign").attr("value");
                houseType = Integer.parseInt(document.getElementById("hidHouseType").attr("value"));

                System.out.println("******** checkInDate: " + checkInDate);
                System.out.println("******** timespan: " + timespan);
                System.out.println("******** sign: " + sign);
                System.out.println("******** houseType: " + houseType);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static Object lock = new Object();
    void test() {
        synchronized (lock) {

        }
    }
}
