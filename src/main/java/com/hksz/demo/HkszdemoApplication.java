package com.hksz.demo;

import com.google.gson.Gson;
import com.hksz.demo.models.BasicResponse;
import com.hksz.demo.models.RoomInfo;
import com.hksz.demo.models.UserAccount;
import com.hksz.demo.models.UserInfo;
import com.hksz.demo.service.Client;
import com.hksz.demo.service.Configure;
import com.hksz.demo.task.Task;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@SpringBootApplication
public class HkszdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);
        System.out.println("getCertificateList");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        List<UserAccount> accounts = Configure.getUserAccounts();
        Task task = new Task(accounts.get(0));
        task.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");
    }


//    public static void _main(String[] args) {
//        SpringApplication.run(HkszdemoApplication.class, args);
//
////        getCertificateList
//        System.out.println("getCertificateList");
//        try {
//            Call call = Client.getInstance().getApi().getCertificateList();
//            Response response = call.execute();
//            System.out.println("response: " + response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        getVerify
//        System.out.println("getVerify");
//        try {
//            double random = Math.random();
//            Call<ResponseBody> call = Client.getInstance().getApi().getVerify(random);
//            Response<ResponseBody> response = call.execute();
//            System.out.println("response: " + response);
////            System.out.println("exit:" + Utils.currentTimestamp());
//            Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        login
//        System.out.println("login");
//        int certType = Configure.certType;
//        String certNo = Base64.getEncoder().encodeToString(Configure.certNo.getBytes());
//        String pwd = Base64.getEncoder().encodeToString(Utils.md5(Configure.pwd).getBytes());
//        while (true) {
//            try {
//                System.out.println("please input verify code: ");
//                Scanner sc = new Scanner(System.in);
//                String verifyCode = sc.nextLine();
//
////            Map<String, Object> param = new HashMap<>();
////            param.put("certType", certType);
////            param.put("certNo", certNo);
////            param.put("pwd", pwd);
////            param.put("verifyCode", verifyCode);
////            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
////                    new Gson().toJson(param));
////            Call<ResponseBody> call = Client.getInstance().getApi().login(requestBody);
//                Call<BasicResponse> call = Client.getInstance().getApi().login(certType, certNo, pwd, verifyCode);
//                Response<BasicResponse> response = call.execute();
//                System.out.println("response: " + response);
//                if(200 == response.body().getStatus()) {
//                    break;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //        getUserInfo
////        System.out.println("getUserInfo");
////        try {
////            Call<ResponseBody> call = Client.getInstance().getApi().getUserInfo1();
////            Response<ResponseBody> response = call.execute();
////            System.out.println("response: " + new Gson().toJson(response.body()));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//        int count = 0;
//        while (true) {
//            //        getUserInfo
//            System.out.println("getUserInfo");
//            try {
//                Call<BasicResponse<UserInfo>> call = Client.getInstance().getApi().getUserInfo();
//                Response<BasicResponse<UserInfo>> response = call.execute();
//                if(null != response.body()) {
//                    System.out.println("response: " + new Gson().toJson(response.body()));
//                    if(200 != response.body().getStatus()) {
//                        break;
//                    }
//                }
//                Thread.sleep(1000 * 5);
//            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//            count ++;
//        }
//
//
//        if(count > 0){
//            return;
//        }
////        isCanReserve
//        System.out.println("isCanReserve");
//        try {
//            Call<BasicResponse> call = Client.getInstance().getApi().isCanReserve();
//            Response<BasicResponse> response = call.execute();
//            System.out.println("response: " + new Gson().toJson(response.body()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        getCheckInDate
//        System.out.println("getCheckInDate");
//        try {
//            Call<BasicResponse> call = Client.getInstance().getApi().getCheckInDate();
//            Response<BasicResponse> response = call.execute();
//            System.out.println("response: " + new Gson().toJson(response.body()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        getReserveOrderInfo
//        System.out.println("getReserveOrderInfo");
//        try {
//            Call<BasicResponse> call = Client.getInstance().getApi().getReserveOrderInfo();
//            Response<BasicResponse> response = call.execute();
//            System.out.println("response: " + new Gson().toJson(response.body()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        getDistrictHouseList
//        System.out.println("getDistrictHouseList");
//        try {
//            Call<BasicResponse<List<RoomInfo>>> call = Client.getInstance().getApi().getDistrictHouseList(null);
//            Response<BasicResponse<List<RoomInfo>>> response = call.execute();
//            System.out.println("response: " + new Gson().toJson(response.body()));
//            if(null != response.body().getData()) {
//                response.body().getData().forEach(new Consumer<RoomInfo>() {
//                    @Override
//                    public void accept(RoomInfo roomInfo) {
//                        System.out.println("roomInfo: " + new Gson().toJson(roomInfo));
//                    }
//                });
//
//                response.body().getData().forEach(new Consumer<RoomInfo>() {
//                    @Override
//                    public void accept(RoomInfo roomInfo) {
//                        confirmOrder(roomInfo);
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        confirmOrder
////        System.out.println("confirmOrder");
////        try {
////            Call<BasicResponse> call = Client.getInstance().getApi().confirmOrder();
////            Response<BasicResponse> response = call.execute();
////            System.out.println("response: " + new Gson().toJson(response.body()));
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//        System.out.println("---------------- end -----------------");
//    }

    static void confirmOrder(RoomInfo roomInfo) {
        String checkInDate = new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate());
        System.out.println("confirmOrder");
        try {
            Call<ResponseBody> call = Client.getInstance().getApi().confirmOrder(
                    new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate()),
                    roomInfo.getTimespan(),
                    roomInfo.getSign()
            );
            Response<ResponseBody> response = call.execute();
            System.out.println("response: " + new Gson().toJson(response.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
