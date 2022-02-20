package com.hksz.demo.task;

import com.google.gson.Gson;
import com.hksz.demo.TimeManager;
import com.hksz.demo.models.RoomInfo;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HackTask1 {

    private ClientApi api;
    private Listener listener;
    private Timer timerForHack;

    public HackTask1(ClientApi api, Listener listener) {
        this.api = api;
        this.listener = listener;

        timerForHack = new Timer();
        Date beginTime = TimeManager.beginTime();
        timerForHack.schedule(new TimerTask() {
            @Override
            public void run() {
//                System.out.println("HackTask1 timestamp:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis()));
                RoomInfo roomInfo = listener.getRoomInfo();
                try {
                    Call<ResponseBody> call = api.confirmOrder(
                            new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate()),
                            roomInfo.getTimespan(),
                            roomInfo.getSign()
                    );
                    Response<ResponseBody> response = call.execute();
                    if (!response.isSuccessful()) {
                        return;
                    }
                    String html = response.body().string();
                    System.out.println("response: " + html);
                    processHtml(html);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, beginTime);
    }

    void processHtml(String html) {
        Document document = Jsoup.parse(html);
        if (null == document.getElementById("hidCheckinDate")
                || null == document.getElementById("hidTimespan")
                || null == document.getElementById("hidSign")
                || null == document.getElementById("hidHouseType")) {
            System.out.println("no rooms");
            return;
        }
        String checkInDate = document.getElementById("hidCheckinDate").attr("value");
        long timespan = Long.parseLong(document.getElementById("hidTimespan").attr("value"));
        String sign = document.getElementById("hidSign").attr("value");
        int houseType = Integer.parseInt(document.getElementById("hidHouseType").attr("value"));
        String checkCode = listener.getCheckCode();

        try {
            Call<ResponseBody> call = api.submitReservation(checkInDate, checkCode, houseType, timespan, sign);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                listener.onResult("successfully:" + response.body().string());
            } else {
                listener.onResult("error:" + response.raw().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        timerForHack.cancel();
    }

    public interface Listener {
        RoomInfo getRoomInfo();

        String getCheckCode();

        void onResult(String result);
    }
}
