package com.hksz.demo.task;

import com.hksz.demo.models.LoginInfo;

public class Task extends Thread{

    private LoginInfo loginInfo;

    @Override
    public void run() {
        super.run();
    }

    public Task(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }
}
