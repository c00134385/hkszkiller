package com.hksz.demo.ui;

import com.google.gson.Gson;
import com.hksz.demo.Configure;
import com.hksz.demo.TimeManager;
import com.hksz.demo.models.*;
import com.hksz.demo.service.ClientApi;
import com.hksz.demo.service.RetrofitUtils;
import com.hksz.demo.task.ConfirmOrderTask;
import com.hksz.demo.task.HackTask1;
import com.hksz.demo.task.HackTask2;
import com.hksz.demo.task.Task;
import com.hksz.demo.utils.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class UserPane extends AnchorPane {

    private UserAccount userAccount;
    private ClientApi api;
    private List<Certificate> certificates;
    UserState userState = UserState.IDLE;
    private String verifyCodeFile;
    private String result;
    private Label resultLabel;
    private ImageView verifyCodeImageView;
    TextField verifyCodeTextField;
    private boolean isLogin = false;
    private List<RoomInfo> roomInfos;

    Queue<HackTask1> hackTask1s = new LinkedList<>();
    Queue<HackTask2> hackTask2s = new LinkedList<>();

    public UserPane(UserAccount userAccount) {
        this.userAccount = userAccount;
        api = new RetrofitUtils().getRetrofit().create(ClientApi.class);
        setPadding(new Insets(30));

        resultLabel = new Label();
//        resultLabel.setMaxWidth(900D);
        resultLabel.setWrapText(true);
//        resultLabel.setMaxHeight(100D);
        verifyCodeImageView = new ImageView();
        verifyCodeTextField = new TextField();
        verifyCodeTextField.setMaxWidth(100);
        initLayout();
    }



    private void initLayout() {
        VBox vBox = new VBox();
        /**
         * Row 1
         */
        {
            Label userAccountLabel = new Label(userAccount.getCertType() + " / " + userAccount.getCertNo() + " / " + userAccount.getPwd());
            vBox.getChildren().add(userAccountLabel);
        }

        /**
         * Row 2
         */


//        TextField checkCodeTextField = new TextField();
//        checkCodeTextField.setMaxWidth(100);

        {
            HBox hBox = new HBox();
//            hBox.setMinHeight(50D);
            Button testBtn = new Button("Init");
            testBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    init();
                }
            });
            hBox.getChildren().add(testBtn);

            Button initBtn = new Button("Get Verify Code");
            initBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    getVerifyCode();
                }
            });
            hBox.getChildren().add(initBtn);

            hBox.getChildren().add(verifyCodeImageView);
            verifyCodeImageView.setFitWidth(200D);
            verifyCodeImageView.setFitHeight(50D);
            verifyCodeImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    getVerifyCode();
                }
            });

            hBox.getChildren().add(verifyCodeTextField);

//            Button checkCodeBtn = new Button("Get Check Code");
//            checkCodeBtn.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    getCheckCode();
//                }
//            });
//            hBox.getChildren().add(checkCodeBtn);

//            if(null != checkCodeFile && new File(checkCodeFile).exists()) {
//                Image image = null;
//                try {
//                    image = new Image(getLocalFile(new File(checkCodeFile)));
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                ImageView imageView = new ImageView(image);
//                imageView.setFitWidth(200D);
//                imageView.setFitHeight(50D);
//                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        getCheckCode();
//                    }
//                });
//                hBox.getChildren().add(imageView);
//                hBox.getChildren().add(checkCodeTextField);
//            }

            vBox.getChildren().add(hBox);
        }

        /**
         * Row 3
         */
        {
            HBox hBox = new HBox();
            vBox.getChildren().add(hBox);

            Button loginBtn = new Button("Login");
            loginBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    login(verifyCodeTextField.getText().trim());
                }
            });
            hBox.getChildren().add(loginBtn);

            Button logoutBtn = new Button("Logout");
            logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    logout();
                }
            });
            hBox.getChildren().add(logoutBtn);

            Button getUserInfoBtn = new Button("Get UserInfo");
            getUserInfoBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    getUserInfo();
                }
            });
            hBox.getChildren().add(getUserInfoBtn);

            Button queryRoomsBtn = new Button("Query Rooms");
            queryRoomsBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    queryRooms();
                }
            });
            hBox.getChildren().add(queryRoomsBtn);

            Button canBeReservedBtn = new Button("Can be Reserved");
            canBeReservedBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    canBeReserved();
                }
            });
            hBox.getChildren().add(canBeReservedBtn);

            Button hackBtn1 = new Button("Hack 1");
            hackBtn1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    hack1();
                }
            });
            hBox.getChildren().add(hackBtn1);

            Button hackBtn2 = new Button("Hack2");
            hackBtn2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    hack2();
                }
            });
            hBox.getChildren().add(hackBtn2);

            Button hackBtn3 = new Button("Hack3");
            hackBtn3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    hack3();
                }
            });
            hBox.getChildren().add(hackBtn3);
        }

        /**
         * Row 4
         */
        {
            if(null != roomInfos) {
                ListView roomListView = new ListView<>(FXCollections.observableArrayList(roomInfos));
                roomListView.setItems(FXCollections.observableArrayList(roomInfos));
                roomListView.setCellFactory(new Callback<ListView, ListCell>() {
                    @Override
                    public ListCell call(ListView param) {
                        ListCell cell = new ListCell<RoomInfo>() {
                            @Override
                            protected void updateItem(RoomInfo item, boolean empty) {
                                super.updateItem(item, empty);
                                if(!empty) {
                                    setGraphic(new Label(new Gson().toJson(item)));
                                }
                            }
                        };

                        cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                RoomInfo roomInfo = (RoomInfo) cell.getItem();
//                                chooseRoom(roomInfo);
                                submitReservation(roomInfo, verifyCodeTextField.getText(), 1);
                            }
                        });

                        return cell;
                    }
                });
                roomListView.setMaxHeight(210D);
                vBox.getChildren().add(roomListView);
            }
        }

        /**
         * Row 5
         */
        {
//            Label resultLabel = new Label(result);
//            resultLabel.setMaxWidth(300);
//            resultLabel.setStyle("-fx-color:Red;");
//            resultLabel.setWrapText(true);
            vBox.getChildren().add(resultLabel);
        }

        /**
         * final layout
         */
        getChildren().removeAll(getChildren());
        getChildren().add(vBox);
    }

    private void init() {
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
                        userState = UserState.IDLE;
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getVerifyCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    Image image = new Image(getLocalFile(new File(verifyCodeFile)));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            verifyCodeImageView.setImage(image);
                            verifyCodeTextField.clear();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void login(String verifyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    void logout() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ResponseBody> call = api.logout();
                    Response<ResponseBody> response = call.execute();
                    System.out.println("response: " + response.body().string());
                    if(response.isSuccessful()) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void getUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    boolean isPeriod = false;
    void queryRooms() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Call<BasicResponse<List<RoomInfo>>> call = api.getDistrictHouseList(null);
                        Response<BasicResponse<List<RoomInfo>>> response = call.execute();
                        if (response.isSuccessful()) {
                            if(200 != response.body().getStatus()) {
                                continue;
                            }
                            roomInfos = response.body().getData();
                            updateResult(new Gson().toJson(response.body()));
                            invalidate();
                            if(new Date().after(TimeManager.beginTime())) {
                                break;
                            }
                            Thread.sleep(1000 * 3);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
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
                    checkCode = "";

                    call = api.submitReservation(checkInDate, checkCode, houseType, timespan, sign);
                    response = call.execute();
                    if(response.isSuccessful()) {
                        bReserved = true;
                        System.out.println("" + response.body().string());

//                        System.out.println("Will you exit? ");
//                        String bExit = sc.nextLine();
//                        if("y".equalsIgnoreCase(bExit) || "yes".equalsIgnoreCase(bExit)) {
//                            break;
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void chooseRoom(RoomInfo roomInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ResponseBody> call = api.confirmOrder(
                            new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate()),
                            roomInfo.getTimespan(),
                            roomInfo.getSign()
                    );
                    Response<ResponseBody> response = call.execute();
                    if(response.isSuccessful()) {
                        System.out.println("response: " + response.body().string());
                        String html = response.body().string();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void canBeReserved() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<BasicResponse> call = api.isCanReserve();
                    Response<BasicResponse> response = call.execute();
                    System.out.println("response: " + response.raw().toString());
                    if(response.isSuccessful()) {
                        System.out.println("response: " + response.body().toString());
                        updateResult(response.body().toString());
                    } else {
                        System.out.println("response: " + response.raw().toString());
                        updateResult(response.raw().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void submitReservation(RoomInfo roomInfo, String checkCode, int houseType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Call<ResponseBody> call = api.submitReservation(
                            new SimpleDateFormat("yyyy-MM-dd").format(roomInfo.getDate()),
                            checkCode,
                            houseType,
                            roomInfo.getTimespan(),
                            roomInfo.getSign()
                    );
                    Response<ResponseBody> response = call.execute();
                    if(response.isSuccessful()) {
                        System.out.println("response: " + response.body().string());
                    } else {
                        System.out.println("response: " + response.raw().toString());
                        System.out.println(response.errorBody());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void hack1() {
        updateResult("hackTask1 prepare");
        while (true) {
            hackTask1s.offer(new HackTask1(api, new HackTask1.Listener() {
                @Override
                public RoomInfo getRoomInfo() {
                    RoomInfo roomInfo = roomInfos.get(0);
                    for(RoomInfo item: roomInfos) {
                        if(item.getCount() > roomInfo.getCount()) {
                            roomInfo = item;
                        }
                    }
                    System.out.println(new Gson().toJson(roomInfo));
                    return roomInfo;
                }

                @Override
                public String getCheckCode() {
                    return verifyCodeTextField.getText();
                }

                @Override
                public void onResult(String result) {
                    updateResult(result);
                }
            }));

            if(hackTask1s.size() >= Configure.taskCount) {
                break;
            }
        }
        updateResult("hackTask1 prepared");
    }

    void hack2() {
        updateResult("hackTask2 prepare");
        while (true) {
            hackTask2s.offer(new HackTask2(api, new HackTask2.Listener() {
                @Override
                public RoomInfo getRoomInfo() {
                    RoomInfo roomInfo = roomInfos.get(0);
                    for(RoomInfo item: roomInfos) {
                        if(item.getCount() > roomInfo.getCount()) {
                            roomInfo = item;
                        }
                    }
                    System.out.println(new Gson().toJson(roomInfo));
                    return roomInfo;
                }

                @Override
                public String getCheckCode() {
                    return verifyCodeTextField.getText();
                }

                @Override
                public void onResult(String result) {
                    updateResult(result);
                }
            }));
            if(hackTask2s.size() >= Configure.taskCount) {
                break;
            }
        }
        updateResult("hackTask1 prepared");
    }

    void hack3() {
        System.out.println("hack3");
    }

    void invalidate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initLayout();
            }
        });
    }

    void updateResult(String message) {
        System.out.println(message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
        IDLE(0),
        LOGGED(3);
        UserState(int value) {
            this.value = value;
        }

        int value;
    }
}
