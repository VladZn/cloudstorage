package ru.cloud.storage.client;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.cloud.storage.common.Command;
import ru.cloud.storage.common.FileListMsg;
import ru.cloud.storage.common.FileMsg;

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
                stage.setScene(new Scene(root));
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
                //TODO заглушка для теста отправки файла
                FileMsg fileMsg = new FileMsg("user", Paths.get("C:\\Temp\\123.txt"), Command.PUT_FILE);
                Network.getInstance().sendMsg(fileMsg);
                FileListMsg fileListMsg = new FileListMsg("user1", Command.GET_FILELIST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
