package com.example.projectpetcentar;

import Threads.ThreadLastChange;
import Threads.ThreadTodaysDateNTime;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Starting.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Volimo zivotinje!");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage(){ return mainStage; }

    public static void setMainStageTitle(String noviNaslov){

        mainStage.setTitle(noviNaslov);
        mainStage.show();

    }
    public static void main(String[] args) {

        Timeline threadovi = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new ThreadTodaysDateNTime());
            }
        }));

        threadovi.getKeyFrames().add(new KeyFrame(Duration.seconds(20), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.runLater(new ThreadLastChange());
            }
        }));

        threadovi.setCycleCount(Timeline.INDEFINITE);
        threadovi.play();
        launch();
    }
}