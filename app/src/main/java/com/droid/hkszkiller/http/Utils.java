package com.droid.hkszkiller.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    final public static String currentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }

    final public static int saveToFile(String filename, byte[] data) {
        if(null == data) {
            return -1;
        }
        try {
            FileOutputStream os = new FileOutputStream(filename);
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
