package com.example.emailclientmain.Controller;

import com.example.emailclientmain.Client;
import com.example.emailclientmain.EmailClientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


import java.io.*;
import java.net.*;


public class MainController {

    @FXML
    public TextField loginTextField;
    @FXML
    public BorderPane root;
    
    @FXML
    public ListView listviewEmail;

    private Stage stage;

    private Parent listview;

    private Client client;

    private BoxButtonsController buttonsController;
    @FXML
    public void loadController() {

        try{
            /*recupero finestra tramite id e setto un nuovo titolo*/
            stage = (Stage) root.getScene().getWindow();
            stage.setTitle("client mail");

            client = new Client(loginTextField.getText());

            /* Carico l'xml dei bottoni a sinistra*/
            FXMLLoader buttons = new FXMLLoader(EmailClientMain.class.getResource("box-buttons.fxml"));
            buttonsController = buttons.getController();
            root.setLeft(buttons.load());
            buttonsController.loadController(client);

            /* Carico l'xml delle email che starà al centro*/
            FXMLLoader listview = new FXMLLoader(EmailClientMain.class.getResource("listview.fxml"));
            this.listview = listview.load();
            root.setCenter(this.listview);


        }
        catch (IOException e){
            System.out.println(e);
        }



    }

    //TODO: Implementare tutti gli error handler 
    public void connectionController(ActionEvent actionEvent) {

        String email = loginTextField.getText();

        


        // check email with regex
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorAllert("Email format not valid");
            return;
        }
        
        // get the value in the textfield and open a connection with the server
        // if the connection is successful, load the next scene
        try {
            String server = InetAddress.getLocalHost().getHostName();

            Socket socket = new Socket(server, 8189);

        try{
            // send the email to the server
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            
            dataOutputStream.writeUTF(email);

            InputStream inputStream = socket.getInputStream();
            int response = inputStream.read();
            socket.close();
            switch (response) {
                case 1:
                    loadController();
                    break;
                case 0:
                    // show an error message 
                    errorAllert("Email not found");
                    break;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void errorAllert(String message) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
