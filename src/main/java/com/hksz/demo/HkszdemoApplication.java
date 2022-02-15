package com.hksz.demo;

import com.hksz.demo.service.Client;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@SpringBootApplication
public class HkszdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);

        double random = Math.random();
        Call<ResponseBody> call = Client.getInstance().getApi().getVerify(random);
        try {
            Response<ResponseBody> response = call.execute();
            System.out.println("exit:" + Utils.currentTimestamp());
//            Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
