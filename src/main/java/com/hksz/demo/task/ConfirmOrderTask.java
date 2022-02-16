package com.hksz.demo.task;

import com.google.gson.Gson;
import com.hksz.demo.Configure;
import com.hksz.demo.models.RoomInfo;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Configure.startTime - Configure.timeDelay);
        System.out.println("DateTime: " + calendar.getTime());
        timerForConfirmOrder.schedule(new TimerTask() {
            @Override
            public void run() {
                String log = String.format("tid(%d) %s %s",
                        Thread.currentThread().getId(),
                        Utils.currentTimestamp(),
                        new Gson().toJson(roomInfo));
                System.out.println(log);
                try {
                    Call<ResponseBody> call = api.confirmOrder(
                            new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate()),
                            roomInfo.getTimespan(),
                            roomInfo.getSign()
                    );
                    Response<ResponseBody> response = call.execute();
                    System.out.println("response: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.getTime());  //calendar.getTime()
    }

    void cancel() {
        timerForConfirmOrder.cancel();
    }
}
