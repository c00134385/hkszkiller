package com.hksz.demo.task;

import com.google.gson.Gson;
import com.hksz.demo.TimeManager;
import com.hksz.demo.models.*;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.service.RetrofitUtils;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

/**
 * 1. 登录
 * 2. 查询个人信息
 * 3. 查询资源信息
 * 4. 查询是否可操作
 */

public class Task {

    private UserAccount userAccount;
    private ClientApi api;

    private List<Certificate> certificates;
    private String verifyCodePath;
    private UserInfo userInfo;
    private List<RoomInfo> roomInfos;

    private Thread threadForCheckingReserve;
    private Thread threadForgetDistrictHouseList;
    Queue<ConfirmOrderTask> confirmOrderTaskList = new LinkedList<>();

    public Task(UserAccount userAccount) {
        this.userAccount = userAccount;
        api = new RetrofitUtils().getRetrofit().create(ClientApi.class);
    }

    public void start() {
        while (true) {
            try {
                System.out.println("getCertificateList");
                Call<BasicResponse<List<Certificate>>> call = api.getCertificateList();
                Response<BasicResponse<List<Certificate>>> response = call.execute();
                certificates = response.body().getData();
                System.out.println("response: " + response);
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        while (true) {
            //        getVerify
            try {
                System.out.println("getVerify");
                double random = Math.random();
                Call<ResponseBody> call = api.getVerify(random);
                Response<ResponseBody> response = call.execute();
                System.out.println("response: " + response);
                if(200 == response.code()) {
                    Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        login

        //        login
        System.out.println("login");
        while (true) {
            try {
                int certType = userAccount.getCertType();
                String certNo = Base64.getEncoder().encodeToString(userAccount.getCertNo().getBytes());
                String pwd = Base64.getEncoder().encodeToString(Utils.md5(userAccount.getPwd()).getBytes());
                System.out.println("please input verify code: ");
                Scanner sc = new Scanner(System.in);
                String verifyCode = sc.nextLine();
                Call<BasicResponse> call = api.login(certType, certNo, pwd, verifyCode);
                Response<BasicResponse> response = call.execute();
                System.out.println("response: " + response);
                if(200 == response.body().getStatus()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {
            System.out.println("getUserInfo");
            Call<BasicResponse<UserInfo>> call = api.getUserInfo();
            Response<BasicResponse<UserInfo>> response = call.execute();
            System.out.println("response: " + new Gson().toJson(response.body()));
            if(200 == response.body().getStatus()) {
                userInfo = response.body().getData();
                startThreadForgetDistrictHouseList();
//                startOrderTimer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startThreadForgetDistrictHouseList() {
        System.out.println("startCheckAvailableThread...");
        threadForgetDistrictHouseList = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("getDistrictHouseList");
                while (true) {
                    try {
                        if(new Date().after(TimeManager.endTime())) {
                            break;
                        }
                        Call<BasicResponse<List<RoomInfo>>> call = api.getDistrictHouseList(null);
                        Response<BasicResponse<List<RoomInfo>>> response = call.execute();
                        if(response.code() != 200) {
                            System.out.println("response: " + new Gson().toJson(response.body()));
                            continue;
                        }
                        if (200 == response.body().getStatus()) {
                            roomInfos = response.body().getData();
                            roomInfos.forEach(new Consumer<RoomInfo>() {
                                @Override
                                public void accept(RoomInfo roomInfo) {
                                    confirmOrderTaskList.offer(new ConfirmOrderTask(api, roomInfo));
                                    System.out.println(new Gson().toJson(roomInfo));
                                    while (confirmOrderTaskList.size() > 100) {
                                        ConfirmOrderTask task = confirmOrderTaskList.poll();
                                        task.cancel();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadForgetDistrictHouseList.start();
    }

    private void startThreadForCheckingReserve() {
        threadForCheckingReserve = new Thread(new Runnable() {
            @Override
            public void run() {
                ////        isCanReserve
                System.out.println("isCanReserve");
                try {
                    Call<BasicResponse> call = api.isCanReserve();
                    Response<BasicResponse> response = call.execute();
                    System.out.println("response: " + new Gson().toJson(response.body()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        threadForCheckingReserve.start();
    }
}
