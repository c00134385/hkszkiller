package com.hksz.demo.task;

import com.hksz.demo.TimeManager;
import com.hksz.demo.models.RoomInfo;
import com.hksz.demo.service.ClientApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HackTask2 {

    private ClientApi api;
    private Listener listener;

    private Timer timerForHack;

    public HackTask2(ClientApi api, long timeOffset, Listener listener) {
        this.api = api;
        this.listener = listener;

        timerForHack = new Timer();
        Date beginTime = new Date(TimeManager.beginTime().getTime() + timeOffset);
        System.out.println("hackTask2 beginTime: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(beginTime));
        timerForHack.schedule(new TimerTask() {
            @Override
            public void run() {
                RoomInfo roomInfo = listener.getRoomInfo();
                String checkInDate = new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate());
                String checkCode = listener.getCheckCode();
                long timespan = roomInfo.getTimespan();
                String sign = roomInfo.getSign();
                int houseType = 1;

                try {
                    Call<ResponseBody> call = api.submitReservation(checkInDate, checkCode, houseType, timespan, sign);
                    Response<ResponseBody> response = call.execute();
                    if (response.isSuccessful()) {
                        listener.onResult("successfully:" + response.body().string());
                    } else {
                        listener.onResult("error:" + response.raw().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, beginTime);
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
