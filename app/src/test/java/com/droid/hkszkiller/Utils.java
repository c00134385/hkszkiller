package com.droid.hkszkiller;

import java.text.SimpleDateFormat;

public class Utils {

    static public String currentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(System.currentTimeMillis());
    }

    static public void printf() {
        System.out.println();
    }
}
