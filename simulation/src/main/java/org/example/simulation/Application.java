package org.example.simulation;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;

public class Application extends javafx.application.Application {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        scene.getStylesheets().add(Application.class.getResource("styles.css").toExternalForm());
        stage.setTitle("Orbital Mechanics Simulator");
        stage.setScene(scene);
        stage.setResizable(true); // Ensure the stage is resizabl
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}