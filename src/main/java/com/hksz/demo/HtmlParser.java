package com.hksz.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class HtmlParser {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);
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
}
