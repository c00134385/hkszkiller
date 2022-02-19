import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainUI extends Application {

    public TextField text;
    public Button button;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;

        URL url = getClass().getResource("");
        System.out.println(url);
//        System.exit(0);

        try {
            root = FXMLLoader.load(getClass().getResource("test.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void evenButton(ActionEvent actionEvent) {
        System.out.println(text.getText());
    }
}
