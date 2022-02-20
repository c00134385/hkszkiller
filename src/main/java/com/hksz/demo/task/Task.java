package com.hksz.demo.task;

import com.google.gson.Gson;
import com.hksz.demo.TimeManager;
import com.hksz.demo.models.*;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.service.RetrofitUtils;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    Scanner sc = new Scanner(System.in);

    public Task(UserAccount userAccount) {
        this.userAccount = userAccount;
        api = new RetrofitUtils().getRetrofit().create(ClientApi.class);
    }

    public void start() {
        System.out.println("index");
        while (true) {
            try {
                Call<ResponseBody> call = api.index();
                Response<ResponseBody> response = call.execute();
                if(response.isSuccessful()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("getCertificateList");
        while (true) {
            try {
                Call<BasicResponse<List<Certificate>>> call = api.getCertificateList();
                Response<BasicResponse<List<Certificate>>> response = call.execute();
                if(response.isSuccessful()) {
                    certificates = response.body().getData();
                    System.out.println("response: " + response);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("getVerify");
        while (true) {
            try {
                double random = Math.random();
                Call<ResponseBody> call = api.getVerify(random);
                Response<ResponseBody> response = call.execute();
                System.out.println("response: " + response.code());
                if(response.isSuccessful()) {
                    Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("login");
        while (true) {
            try{
                int certType = userAccount.getCertType();
                String certNo = Base64.getEncoder().encodeToString(userAccount.getCertNo().getBytes());
                String pwd = Base64.getEncoder().encodeToString(Utils.md5(userAccount.getPwd()).getBytes());
                System.out.println("please input verify code: ");
                String verifyCode = sc.nextLine();
                Call<BasicResponse> call = api.login(certType, certNo, pwd, verifyCode);
                Response<BasicResponse> response = call.execute();
                System.out.println("response: " + response);
                if(response.isSuccessful()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("getUserInfo");
        while (true) {
            try {
                Call<BasicResponse<UserInfo>> call = api.getUserInfo();
                Response<BasicResponse<UserInfo>> response = call.execute();
                if(response.isSuccessful()) {
                    System.out.println("response: " + new Gson().toJson(response.body()));
                    userInfo = response.body().getData();
                    startThreadForgetDistrictHouseList();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        Call<BasicResponse<List<RoomInfo>>> call = api.getDistrictHouseList(null);
                        Response<BasicResponse<List<RoomInfo>>> response = call.execute();
                        if(response.code() != 200) {
                            System.out.println("response: " + new Gson().toJson(response.body()));
                            continue;
                        }
                        if (response.isSuccessful()) {
                            roomInfos = response.body().getData();
                            roomInfos.forEach(new Consumer<RoomInfo>() {
                                @Override
                                public void accept(RoomInfo roomInfo) {
                                    confirmOrderTaskList.offer(new ConfirmOrderTask(api, roomInfo, new ConfirmOrderTask.Listener() {
                                        @Override
                                        public void onConfirmed(String html) {
                                            processOrder(html);
                                        }
                                    }));
                                    System.out.println(new Gson().toJson(roomInfo));
                                    while (confirmOrderTaskList.size() > 100) {
                                        ConfirmOrderTask task = confirmOrderTaskList.poll();
                                        task.cancel();
                                    }
                                }
                            });
                        }
                        if(new Date().after(TimeManager.endTime())) {
                            break;
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

    private static Object lock = new Object();
    private boolean bReserved = false;
    void processOrder(String html) {
        String checkInDate;
        String checkCode;
        long timespan;
        String sign;
        int houseType;

        synchronized (lock) {
            if(bReserved) {
                System.out.println("Room reservation successfully.");
//                return;
            }

            Document document = Jsoup.parse(html);
            if(null == document.getElementById("hidCheckinDate")
                    || null == document.getElementById("hidTimespan")
                    || null == document.getElementById("hidSign")
                    || null == document.getElementById("hidHouseType")) {
                return;
            }
            checkInDate = document.getElementById("hidCheckinDate").attr("value");
            timespan = Long.parseLong(document.getElementById("hidTimespan").attr("value"));
            sign = document.getElementById("hidSign").attr("value");
            houseType = Integer.parseInt(document.getElementById("hidHouseType").attr("value"));

            System.out.println("******** checkInDate: " + checkInDate);
            System.out.println("******** timespan: " + timespan);
            System.out.println("******** sign: " + sign);
            System.out.println("******** houseType: " + houseType);
            while (true) {
                try {
                    double random = Math.random();
                    Call<ResponseBody> call = api.getVerify(random);
                    Response<ResponseBody> response = call.execute();
                    System.out.println("response: " + response.code());
                    if(response.isSuccessful()) {
                        Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                    } else {
                        continue;
                    }

                    System.out.println("Please input checkCode: ");
                    checkCode = sc.nextLine();

                    call = api.submitReservation(checkInDate, checkCode, houseType, timespan, sign);
                    response = call.execute();
                    if(response.isSuccessful()) {
                        bReserved = true;
                        System.out.println("" + response.body().string());

                        System.out.println("Will you exit? ");
                        String bExit = sc.nextLine();
                        if("y".equalsIgnoreCase(bExit) || "yes".equalsIgnoreCase(bExit)) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }


    boolean parseHtml(String html) {
        String checkInDate;
        String timespan;
        String sign;
        String houseType;
        Document document = Jsoup.parse(html);
        if(null == document.getElementById("hidCheckinDate")
                || null == document.getElementById("hidTimespan")
                || null == document.getElementById("hidSign")
                || null == document.getElementById("hidHouseType")) {
            return false;
        }
        checkInDate = document.getElementById("hidCheckinDate").attr("value");
        timespan = document.getElementById("hidTimespan").attr("value");
        sign = document.getElementById("hidSign").attr("value");
        houseType = document.getElementById("hidHouseType").attr("value");
        return true;
    }

    /**
     * 1. getVerifyCode
     * 2. submitReservation
     */
    private void submitReservation() {
        System.out.println("ready for submitReservation.");

        while (true) {
            while (true) {
                try {
                    double random = Math.random();
                    Call<ResponseBody> call = api.getVerify(random);
                    Response<ResponseBody> response = call.execute();
                    System.out.println("response: " + response.code());
                    if(response.isSuccessful()) {
                        Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("please input verifyCode");
        }
    }
}
