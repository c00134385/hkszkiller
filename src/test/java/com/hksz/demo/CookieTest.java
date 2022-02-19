package com.hksz.demo;

import com.google.gson.Gson;
import com.hksz.demo.models.BasicResponse;
import com.hksz.demo.models.Certificate;
import com.hksz.demo.models.UserAccount;
import com.hksz.demo.models.UserInfo;
import com.hksz.demo.service.Client;
import com.hksz.demo.utils.Utils;
import okhttp3.ResponseBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
public class CookieTest {

    public static void main(String[] args) {
        SpringApplication.run(HkszdemoApplication.class, args);

        Scanner sc = new Scanner(System.in);
        Client client = Client.getInstance();

        while (true) {
            try {
                Call<ResponseBody> call = client.getApi().index();
                Response<ResponseBody> response = call.execute();
//                System.out.println("response: " + response.body().string());
                if(response.isSuccessful()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        while (true) {
//            try {
//                sc.nextLine();
//                Call<BasicResponse<List<Certificate>>> call = client.getApi().getCertificateList();
//                Response<BasicResponse<List<Certificate>>> response = call.execute();
//                if(response.isSuccessful()) {
//                    System.out.println("response: " + response.body());
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        while (true) {
            try {
                sc.nextLine();
                double random = Math.random();
                Call<ResponseBody> call = Client.getInstance().getApi().getVerify(random);
                Response<ResponseBody> response = call.execute();
                System.out.println("response: " + response);
                if(response.isSuccessful()) {
                    Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println("login");
        List<UserAccount> userAccounts = Configure.getUserAccounts();
        UserAccount userAccount = userAccounts.get(0);
        int certType = userAccount.getCertType();
        String certNo = Base64.getEncoder().encodeToString(userAccount.getCertNo().getBytes());
        String pwd = Base64.getEncoder().encodeToString(Utils.md5(userAccount.getPwd()).getBytes());
        while (true) {
            try {
                System.out.println("please input verify code: ");
                String verifyCode = sc.nextLine();
                Call<BasicResponse> call = Client.getInstance().getApi().login(certType, certNo, pwd, verifyCode);
                Response<BasicResponse> response = call.execute();
                System.out.println("response: " + response.body());
                if(response.isSuccessful()) {
                    System.out.println("response: " + response.body());
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            try {
                System.out.println("getUserInfo");
                System.out.println("please input anything to go next: ");
                sc.nextLine();
                Call<BasicResponse<UserInfo>> call = client.getApi().getUserInfo();
                Response<BasicResponse<UserInfo>> response = call.execute();
                System.out.println("response: " + response);
                if(response.isSuccessful()) {
                    System.out.println("response: " + response.body());
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("getVerify again");
        while (true) {
            try {
                String cmd = sc.nextLine();
                if("exit".equalsIgnoreCase(cmd)) {
                    break;
                }
                double random = Math.random();
                Call<ResponseBody> call = client.getApi().getVerify(random);
                Response<ResponseBody> response = call.execute();
                System.out.println("response: " + response);
                if(response.isSuccessful()) {
                    Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println("end");
    }
}
