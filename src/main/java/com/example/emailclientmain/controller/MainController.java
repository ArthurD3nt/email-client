package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.emailclientmain.EmailClientMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainController {

    @FXML
    public TextField loginTextField;
    @FXML
    public BorderPane root;

    private Stage stage;

    private Parent listview;

    private Parent writeView;

    private Parent showEmailView; 

    private ClientModel clientModel;

    private BoxButtonsController buttonsController;

    private ListViewController listViewController;

    private WriteController writeController;

    private ShowEmailController showEmailController;

    @FXML
    public void loadController(ActionEvent actionEvent) {

        try{

            stage = (Stage) root.getScene().getWindow();

            if(!loginTextField.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")){
                Alert.AlertType alertType = Alert.AlertType.ERROR;
                Alert alert = new Alert(alertType, "Email not valid");
                Platform.runLater(()-> alert.showAndWait());
                return;
            }

            stage.setTitle("client mail");
            stage.setHeight(600);
            stage.setWidth(900);

            clientModel = new ClientModel(loginTextField.getText());
            /* Carico l'xml dei bottoni a sinistra*/
            FXMLLoader boxButtons = new FXMLLoader(EmailClientMain.class.getResource("boxButtons.fxml"));
            root.setLeft(boxButtons.load());
            buttonsController = boxButtons.getController();

            /* Istanzio un nuovo ClientController*/
            ClientController clientController = new ClientController(clientModel, buttonsController);

            stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
                if (clientController != null){
                    clientController.closeThreadPool();
                }
            });

            /* Carico l'xml della write delle email*/
            FXMLLoader writeView = new FXMLLoader(EmailClientMain.class.getResource("write.fxml"));
            this.writeView = writeView.load();
            writeController = writeView.getController();
            writeController.loadWriteController(clientModel, clientController);

            /* Carico l'xml delle email che starà al centro*/
            FXMLLoader listview = new FXMLLoader(EmailClientMain.class.getResource("listview.fxml"));
            this.listview = listview.load();
            listViewController = listview.getController();

            /* Carico l'xml per mostrare l'email*/
            FXMLLoader showEmail = new FXMLLoader(EmailClientMain.class.getResource("showEmail.fxml"));
            this.showEmailView = showEmail.load();
            this.showEmailController = showEmail.getController();
            showEmailController.initializeController(clientModel, clientController, this.writeView, writeController, root, this.listview);

            /* Setto al centro la view che contiene le email*/
            root.setCenter(this.listview);
            listViewController.loadController(clientModel,root,this.showEmailView, this.showEmailController, clientController);

            buttonsController.loadController(clientModel, root, this.listview, this.writeView);
           
            /* Chiamo il server tramite client controller per fare la connessione */
            clientController.tryConnection(loginTextField.getText());

        }
        catch (IOException e){
            System.out.println("Errore");
        }


    }
}
