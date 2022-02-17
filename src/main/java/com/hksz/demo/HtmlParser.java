package com.hksz.demo;

import com.hksz.demo.models.UserAccount;
import com.hksz.demo.task.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class HtmlParser {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);


        System.out.println("---- 1");
        CountDownLatch countDownLatch = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("---- 2");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
                System.out.println("---- 3");
            }
        }).start();

        System.out.println("---- 4");
        try {
            countDownLatch.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("---- 5");
        System.exit(0);



        File file = new File("test.html");
        System.out.println("file: " + file.exists());

        if(file.exists()) {
            try {
                Document document = Jsoup.parse(file, null);
                String checkInDate = document.getElementById("hidCheckinDate").attr("value");
                String timespan = document.getElementById("hidTimespan").attr("value");
                String sign = document.getElementById("hidSign").attr("value");
                String houseType = document.getElementById("hidHouseType").attr("value");
                System.out.println(checkInDate);
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
