package com.example.projekt_1.controlleri;

import com.example.projekt_1.main.Main;
import com.example.projekt_1.util.threads.ReadChangeThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static Stage mainstage;
    @Override
    public void start(Stage stage) throws IOException {
        mainstage=stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Aplikacija za upravljanje znanstvenim istrazivanjima");
        stage.setScene(scene);

        stage.show();

        ReadChangeThread readChangeThread = new ReadChangeThread();
        readChangeThread.setDaemon(true);
        readChangeThread.start();

    }


    public static Stage getStage(){
        return mainstage;
    }

    public static void main(String[] args) {
        launch();
    }
}