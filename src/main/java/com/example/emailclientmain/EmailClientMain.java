package com.example.emailclientmain;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class EmailClientMain extends Application {

    @FXML
    private BorderPane root;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader login = new FXMLLoader(EmailClientMain.class.getResource("login.fxml"));
        Scene scene = new Scene(login.load(), 600, 300);
        scene.getStylesheets().add(getClass().getResource( "style/style.css" ).toExternalForm());
        stage.setTitle("Login");
        stage.setScene(scene);
        scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (WindowEvent e)-> System.exit(1));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}