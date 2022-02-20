package com.hksz.demo.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UIController implements Initializable {
    @FXML
    private Button button;

    @FXML
    public TextField text;

    @FXML
    public Label resultLabel;

    @FXML
    public ListView userList;


    UIPresenter uiPresenter;

    private void initUI() {
        uiPresenter = new UIPresenter(new UIPresenter.EventListener() {
            @Override
            public void onIndexDone() {
                System.out.println("onIndexDone");
                resultLabel.setText("onIndexDone");
            }
        });
    }

    @FXML
    public void evenButton(ActionEvent actionEvent) {
        System.out.println("hello world");
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建一个消息对话框
        alert.setHeaderText("今日天气"); // 设置对话框的头部文本
        // 设置对话框的内容文本
        alert.setContentText("今天白天晴转多云，北转南风2、3间4级，最高气温28℃；夜间多云转阴，南风2级左右，最低气温16℃。");
        alert.show(); // 显示对话框
    }


    @FXML
    public void doChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.showOpenDialog(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("hashCode: " + hashCode());
    }
}
