package com.hksz.demo.task;

import com.hksz.demo.service.ClientApi;

import java.util.Timer;

abstract public class AbstractTimerTask {

    protected ClientApi api;
    protected Timer timer;
    protected long timeOffset;
}
