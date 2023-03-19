package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.emailclientmain.EmailClientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    public TextField loginTextField;
    @FXML
    public BorderPane root;
    @FXML
    public ListView listviewEmail;

    private Stage stage;

    private Parent listview;

    private ClientModel clientModel;

    private BoxButtonsController buttonsController;

    private ListViewController listViewController;
    @FXML
    public void loadController(ActionEvent actionEvent) {

        try{

            /*recupero finestra tramite id e setto un nuovo titolo*/
            stage = (Stage) root.getScene().getWindow();
            stage.setTitle("client mail");

            clientModel = new ClientModel(loginTextField.getText());

            /* Carico l'xml dei bottoni a sinistra*/
            FXMLLoader boxButtons = new FXMLLoader(EmailClientMain.class.getResource("box-buttons.fxml"));
            root.setLeft(boxButtons.load());
            buttonsController = boxButtons.getController();

            /* Carico l'xml delle email che starà al centro*/
            FXMLLoader listview = new FXMLLoader(EmailClientMain.class.getResource("listview.fxml"));
            this.listview = listview.load();
            listViewController = listview.getController();
            root.setCenter(this.listview);
            listViewController.loadController(clientModel);

            buttonsController.loadController(clientModel, root, this.listview);

            /* Chiamo il server tramite client controller per fare la connessione */
            ClientController clientController = new ClientController(clientModel);
            clientController.firstConnection(loginTextField.getText());


        }
        catch (IOException e){
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
