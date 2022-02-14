package com.droid.hkszkiller;


import org.junit.Test;

import static org.junit.Assert.*;

import com.droid.hkszkiller.http.Method;
import com.droid.hkszkiller.http.Request;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

//    https://hk.sz.gov.cn:8118/userPage/login
    @Test
    public void socket_test() {
        System.out.println("start:" + Utils.currentTimestamp());
        Client client = new Client();
        client.release();
        System.out.println("exit" + Utils.currentTimestamp());
    }

    @Test
    public void request_test() {
        System.out.println("start:" + Utils.currentTimestamp());
        Request request = new Request(Method.GET, "/");
        System.out.println(request.toString());

        Client client = new Client();
        client.write(request.toString());

        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.release();
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("exit" + Utils.currentTimestamp());
    }

    @Test
    public void okhttp_test() {
        String url = "https://hk.sz.gov.cn:8118/userPage/login";
        // 3. 获取实例
//        Retrofit retrofit = new Retrofit.Builder()
//                // 设置OKHttpClient,如果不设置会提供一个默认的
//                .client(new OkHttpClient())
//                //设置baseUrl
//                .baseUrl(url)
//                //添加Gson转换器
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}