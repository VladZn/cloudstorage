package ru.cloud.storage.client;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.cloud.storage.common.FileMsg;
import sun.nio.ch.Net;

import java.io.IOException;
import java.nio.file.Paths;

public class ScreenManager {
    private static Stage stage;

    public static void setStage(Stage newStage){
        stage = newStage;
    }

    public static void showLoginScreen(){
        Platform.runLater(()->{
            stage.close();
            try {
                Parent root = FXMLLoader.load(ScreenManager.class.getResource("/Login.fxml"));
                stage.setTitle("Sign in");
                stage.setScene(new Scene(root, 300, 275));
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public static void showMainScreen(){
        Platform.runLater(()->{
            stage.close();
            try {
                Parent root = FXMLLoader.load(ScreenManager.class.getResource("/Main.fxml"));
                stage.setTitle("Cloud storage client");
                stage.setScene(new Scene(root, 800, 600));
                stage.setResizable(false);
                stage.show();
                //для теста отправки файла
                FileMsg fileMsg = new FileMsg("user", Paths.get("C:\\Temp\\123.txt"));
                Network.getInstance().sendMsg(fileMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
