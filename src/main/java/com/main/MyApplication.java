package com.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class MyApplication extends Application {
    public static final String ICON_PATH = "geometric.png";
    public static final String WINDOW_TITLE = "Figures";
    public static final String PATH_TO_OUTPUT_FILE = "src\\main\\resources\\outputData.txt";
    public static final String PATH_TO_INPUT_FILE = "src\\main\\resources\\inputData.txt";
    public static final Color SCENE_BACKGROUND = Color.WHITE;
    public static final Color APP_BACKGROUND = Color.web("#2B2B2B");

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(APP_BACKGROUND);
        Image image = new Image(ICON_PATH);
        stage.getIcons().add(image);
        stage.setTitle(WINDOW_TITLE);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

