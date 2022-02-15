package com.droid.hkszkiller;

import org.junit.Test;

import static org.junit.Assert.*;

import com.droid.hkszkiller.http.Client;
import com.droid.hkszkiller.http.Utils;
import com.droid.hkszkiller.models.BasicResponse;
import com.droid.hkszkiller.models.Certificate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

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

    @Test
    public void retrofit_test1() {
        Call<BasicResponse<List<Certificate>>> call = Client.getInstance().getApi().getCertificateList1();
        try {
            Response<BasicResponse<List<Certificate>>> response = call.execute();
            System.out.println("exit:" + Utils.currentTimestamp());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("exit:" + Utils.currentTimestamp());

        assertEquals(4, 2 + 2);
    }


    @Test
    public void saveToFile_test() {
        double random = Math.random();
        Utils.saveToFile(random + ".bin", null);
    }



    @Test
    public void input_test() throws IOException {
        InputStreamReader is = new InputStreamReader(System.in); //new构造InputStreamReader对象
        BufferedReader br = new BufferedReader(is); //拿构造的方法传到BufferedReader中，此时获取到的就是整个缓存流
        String name = null;
        do {
            System.out.println("ReadTest, Please Enter Data:");
            name = br.readLine();
            System.out.println("ReadTest Output:" + name);
        }while (name == null);
    }

    @Test
    public void verify_test1() {
        double random = Math.random();
        Call<ResponseBody> call = Client.getInstance().getApi().getVerify(random);
        try {
            Response<ResponseBody> response = call.execute();
            System.out.println("exit:" + Utils.currentTimestamp());
//            Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Call<BasicResponse<List<Certificate>>> certificateListCall = ClientProxy.getInstance().getApi().getCertificateList1();
//            Response<BasicResponse<List<Certificate>>> response = certificateListCall.execute();
//            System.out.println("onResponse:" + response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            int certType = 4;
            String certNo = "G49480256";
            String pwd = "c00134385";
            String verifyCode;
            System.out.println("ReadTest, Please Enter Data:");
            InputStreamReader is = new InputStreamReader(System.in); //new构造InputStreamReader对象
            BufferedReader br = new BufferedReader(is); //拿构造的方法传到BufferedReader中，此时获取到的就是整个缓存流
            while (true) {
                verifyCode = br.readLine();
                if(null != verifyCode) {
                    System.out.println("ReadTest Output:" + verifyCode);
                    break;
                }
            }
//
//            Call<ResponseBody> responseBodyCall = ClientProxy.getInstance().getApi().login(certType,
//                    Base64.getEncoder().encodeToString(certNo.getBytes(StandardCharsets.UTF_8)),
//                    Base64.getEncoder().encodeToString(Utils.md5(pwd).getBytes(StandardCharsets.UTF_8)),
//                    verifyCode);
//            Response<ResponseBody> response = responseBodyCall.execute();
//            System.out.println("onResponse:" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("exit:" + Utils.currentTimestamp());
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("last exit:" + Utils.currentTimestamp());
        assertEquals(4, 2 + 2);
    }

    @Test
    public void retrofit_test() {
        Observable result = Client.getInstance().getApi().getCertificateList()
                .doOnNext(new Action1<Object>() {
            @Override
            public void call(Object o) {
                System.out.println(o);
            }
        })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable);
                    }
                })
                .doOnCompleted(new Action0() {
            @Override
            public void call() {
                System.out.println("");
            }
        });

        System.out.println("exit:" + Utils.currentTimestamp());
        Object r = result.toBlocking().first();
        System.out.println("exit:" + Utils.currentTimestamp());
        System.out.println("r:"+r);
    }
}