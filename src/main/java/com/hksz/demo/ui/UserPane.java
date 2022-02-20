package com.hksz.demo.ui;

import com.google.gson.Gson;
import com.hksz.demo.models.BasicResponse;
import com.hksz.demo.models.Certificate;
import com.hksz.demo.models.UserAccount;
import com.hksz.demo.models.UserInfo;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.service.RetrofitUtils;
import com.hksz.demo.task.Task;
import com.hksz.demo.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserPane extends AnchorPane {

    private UserAccount userAccount;
    private ClientApi api;
    private List<Certificate> certificates;
    UserState userState = UserState.IDLE;
    private String verifyCodeFile;
    private Label resultLabel;
    private boolean isLogin = false;

    public UserPane(UserAccount userAccount) {
        this.userAccount = userAccount;
        api = new RetrofitUtils().getRetrofit().create(ClientApi.class);
        setPadding(new Insets(30));
        initLayout();
    }


    private void initLayout() {
        FlowPane pane = new FlowPane();
        getChildren().removeAll(getChildren());
        getChildren().add(pane);

        List<Node> nodes = new ArrayList<>();

        nodes.add(new Label(userAccount.getCertType() + " / " + userAccount.getCertNo() + " / " + userAccount.getPwd()));

        Button initBtn = new Button("Get Verify Code");
        initBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getVerifyCode();
            }
        });
        nodes.add(initBtn);

        if(null != verifyCodeFile) {
            Image image = null;
            try {
                image = new Image(getLocalFile(new File(verifyCodeFile)));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200D);
            imageView.setFitHeight(50D);
            imageView.setPreserveRatio(true);
            nodes.add(imageView);

            TextField verifyCode = new TextField();
            verifyCode.setMaxWidth(100);
            verifyCode.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println(event);
                }
            });
            nodes.add(verifyCode);

            Button loginBtn = new Button("Login");
            loginBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    login(verifyCode.getText().trim());
                }
            });
            nodes.add(loginBtn);

            Button getUserInfoBtn = new Button("Get UserInfo");
            getUserInfoBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    getUserInfo();
                }
            });
            nodes.add(getUserInfoBtn);
        }

        resultLabel = new Label();
        nodes.add(resultLabel);
        pane.getChildren().addAll(nodes);
    }

    private void getVerifyCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ResponseBody> call = api.index();
                    Response<ResponseBody> response = call.execute();
                    if (!response.isSuccessful()) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    Call<BasicResponse<List<Certificate>>> call = api.getCertificateList();
                    Response<BasicResponse<List<Certificate>>> response = call.execute();
                    if (!response.isSuccessful()) {
                        certificates = response.body().getData();
                        System.out.println("response: " + response);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    double random = Math.random();
                    Call<ResponseBody> call = api.getVerify(random);
                    Response<ResponseBody> response = call.execute();
                    System.out.println("response: " + response.code());
                    if (!response.isSuccessful()) {
                        return;
                    }
                    Utils.saveToFile(String.valueOf(random) + ".jfif", response.body().bytes());
                    verifyCodeFile = random + ".jfif";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        initLayout();
                    }
                });
            }
        }).start();
    }

    void login(String verifyCode) {
        try{
            int certType = userAccount.getCertType();
            String certNo = Base64.getEncoder().encodeToString(userAccount.getCertNo().getBytes());
            String pwd = Base64.getEncoder().encodeToString(Utils.md5(userAccount.getPwd()).getBytes());
            Call<BasicResponse> call = api.login(certType, certNo, pwd, verifyCode);
            Response<BasicResponse> response = call.execute();
            System.out.println("response: " + response);
            if(response.isSuccessful()) {
                updateResult(new Gson().toJson(response.body()));
                isLogin = true;
            } else {
                updateResult(response.code() + " " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getUserInfo() {
        try {
            Call<BasicResponse<UserInfo>> call = api.getUserInfo();
            Response<BasicResponse<UserInfo>> response = call.execute();
            if(response.isSuccessful()) {
                updateResult(new Gson().toJson(response.body()));
                System.out.println("response: " + new Gson().toJson(response.body()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateResult(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                result = message;
//                initLayout();
                resultLabel.setText(message);
            }
        });
    }

    String getLocalFile(File file) throws MalformedURLException {
        System.out.println(file.getAbsolutePath());
        System.out.println(file.toURI().toString());
        System.out.println(file.toURI().toURL().toString());
        return file.toURI().toURL().toString();
    }

    enum UserState {
        IDLE,
        LOGIN,
    }
}
