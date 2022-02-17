package com.hksz.demo.task;

import com.google.gson.Gson;
import com.hksz.demo.TimeManager;
import com.hksz.demo.models.RoomInfo;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ConfirmOrderTask extends Thread {

    private ClientApi api;
    private RoomInfo roomInfo;
    private Timer timerForConfirmOrder;

    public ConfirmOrderTask(ClientApi api, RoomInfo roomInfo) {
        this.api = api;
        this.roomInfo = roomInfo;

        timerForConfirmOrder = new Timer();
        Date beginTime = TimeManager.beginTime();
//        System.out.println("beginTime: " + beginTime);
        timerForConfirmOrder.schedule(new TimerTask() {
            @Override
            public void run() {
                String log = String.format("tid(%d) %s %s",
                        Thread.currentThread().getId(),
                        Utils.currentTimestamp(),
                        new Gson().toJson(roomInfo));
                System.out.println(log);
                int i = 0;
                while (i < 2) {
                    i++;
                    try {
                        Call<ResponseBody> call = api.confirmOrder(
                                new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate()),
                                roomInfo.getTimespan(),
                                roomInfo.getSign()
                        );
                        Response<ResponseBody> response = call.execute();
                        System.out.println("response: " + response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, beginTime);
    }

    void cancel() {
        timerForConfirmOrder.cancel();
    }
}
