package com.hksz.demo.task;

import com.hksz.demo.TimeManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerProcess {

    private Timer timerForHack;

    public TimerProcess(long timeOffset, Action taskAction) {
        timerForHack = new Timer();
        Date beginTime = new Date(TimeManager.beginTime().getTime() + timeOffset);
        System.out.println("hackTask2 beginTime: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(beginTime));
        timerForHack.schedule(new TimerTask() {
            @Override
            public void run() {
                taskAction.run();
            }
        }, beginTime);
    }

    public interface Action {
        void run();
    }
}
