import com.google.gson.Gson;
import com.hksz.demo.models.UserAccount;
import com.hksz.demo.ui.UserPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainUI extends Application {

    private Button openFileBtn;
    private ListView userList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        Parent root = null;
//        try {
//            root = FXMLLoader.load(getClass().getResource("test.fxml"));
//            primaryStage.setScene(new Scene(root));
//            primaryStage.show();
//
//            openFileBtn = (Button) primaryStage.getScene().lookup("#button");
//            openFileBtn.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                    FileChooser fileChooser = new FileChooser();
//                    fileChooser.setInitialDirectory(new File("."));
//                    File file = fileChooser.showOpenDialog(primaryStage);
//                    System.out.println(file);
//                    processFile(file);
//                }
//            });
//
//            userList = new ListView();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        VBox hBox = new VBox();

        Button openFileBtn = new Button("open file");
//        openFileBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                hBox.getChildren().add(new Button("button3"));
//            }
//        });
        openFileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("."));
                File file = fileChooser.showOpenDialog(primaryStage);
                System.out.println(file);
                List<UserAccount> userAccounts = getUserAccountsFromFile(file);
                userAccounts.forEach(new Consumer<UserAccount>() {
                    @Override
                    public void accept(UserAccount userAccount) {
                        UserPane userPane = new UserPane(userAccount);
                        hBox.getChildren().add(userPane);
                    }
                });
            }
        });
        hBox.getChildren().add(openFileBtn);
//        hBox.getChildren().add(new Button("button1"));
//        hBox.getChildren().add(new Button("button2"));


        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        primaryStage.setWidth(700);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    List<UserAccount> getUserAccountsFromFile(File file) {

        List<UserAccount> userAccounts = new ArrayList<>();

        if (null == file || !file.exists()) {
            return userAccounts;
        }
        FileReader fr = null;
        BufferedReader bf = null;
        try {
            fr = new FileReader(file.getName());
            bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                UserAccount userAccount = UserAccount.parseFromString(str);
                if (null != userAccount) {
                    userAccounts.add(userAccount);
                }
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(new Gson().toJson(userAccounts));
        return userAccounts;
    }
}
