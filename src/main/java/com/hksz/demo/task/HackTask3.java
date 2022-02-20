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

public class HackTask3 {

    private ClientApi api;
    private RoomInfo roomInfo;
    private Listener listener;

    private Timer timerForHack;

    public HackTask3(ClientApi api, RoomInfo roomInfo, Listener listener) {
        this.api = api;
        this.roomInfo = roomInfo;
        this.listener = listener;

        timerForHack = new Timer();
        Date beginTime = TimeManager.beginTime();
        timerForHack.schedule(new TimerTask() {
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
                    if(!response.isSuccessful()) {
                        return;
                    }

                    String html = response.body().string();
//                    processHtml(html);

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
        void onResult(String result);
    }
}
