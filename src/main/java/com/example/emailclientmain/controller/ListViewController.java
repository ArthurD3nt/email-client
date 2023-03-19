package com.example.emailclientmain.Controller;

import com.example.emailclientmain.Email;
import com.example.emailclientmain.EmailClientMain;
import com.example.emailclientmain.Model.ClientModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class ListViewController {
    @FXML
    public ListView listviewEmail;

    public void loadController(ClientModel clientModel){

            listviewEmail.itemsProperty().bind(clientModel.inboxProperty());

            for(int i  = 0; i< 2; i++){
                System.out.println(listviewEmail);
            }

    }
}
