package ru.cloud.storage.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launch extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Network.getInstance().connect();
        ScreenManager.setStage(primaryStage);
        ScreenManager.showLoginScreen();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
