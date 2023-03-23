package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class ClientController {
    private Socket socket;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    private ClientModel clientModel;

    private BoxButtonsController buttonsController;

    public ClientController(ClientModel clientModel, BoxButtonsController buttonsController) {
        this.clientModel = clientModel;
        this.buttonsController = buttonsController;
    }

    private void connectToSocket() {

        try {
            String nomeHost = InetAddress.getLocalHost().getHostName();

            socket = new Socket(nomeHost, 8189);
            socket.setSoTimeout(3000);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Alert.AlertType alertType = Alert.AlertType.ERROR;
            Alert alert = new Alert(alertType, "Errore nella connessione al server, riprova più tardi");
            alert.showAndWait();
        }
    }

    private void closeSocketConnection() throws IOException {
        if (socket != null) {
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }

    private Communication sendCommunication(Communication c) {
        try {
            if (outputStream == null || inputStream == null) {
                return null;
            }
            outputStream.writeObject(c);
            return (Communication) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

    }

    public void firstConnection(String email) {
        try {
            this.connectToSocket();
            Communication connection = new Communication("connection", new BaseBody(email));
            Communication response = sendCommunication(connection);
            if(response == null){
                return;
            }
            ArrayList<ArrayList<EmailBody>> emails = ((ConnectionBody) (response.getBody())).getEmails();
            /* Faccio il reverse per avere le email in ordine di orario */
            Collections.reverse(emails.get(0));
            Collections.reverse(emails.get(1));
            Collections.reverse(emails.get(2));

            clientModel.setInboxContent(emails.get(0));
            clientModel.setSentContent(emails.get(1));
            clientModel.setBinContent(emails.get(2));
            this.closeSocketConnection();
        } catch (IOException e) {
        }
    }

    public void sendEmail(EmailBody email) {
        try {
            this.connectToSocket();
            Communication communication = new Communication("send_email", email);
            Communication response = sendCommunication(communication);
            if (response.getAction().equals("emails_saved")) {
                clientModel.setNewEmailSentContent(email);
                this.buttonsController.loadPage("INVIATE");
            }
            this.closeSocketConnection();
        } catch (IOException e) {
        }
    }

    public void moveToBin(String id, String email) throws IOException, ClassNotFoundException {
        this.connectToSocket();
        EmailBody emailBody = null;
        BinBody bin = new BinBody(id, email);
        Communication communication = new Communication("bin", bin);
        Communication response = sendCommunication(communication);
        BooleanBody responseBody = (BooleanBody) response.getBody();

        if (response.getAction().equals("bin_ok") && responseBody.isResult()) {
            ObservableList<EmailBody> inboxContent = clientModel.getInboxContent();
            for (int i = 0; i < inboxContent.size(); i++) {
                if (inboxContent.get(i).getId().equals(id)) {
                    emailBody = inboxContent.get(i);
                    inboxContent.remove(i);
                    break;
                }
            }

            if (emailBody != null) {
                ArrayList emails = new ArrayList<>();
                emails.add(emailBody);
                clientModel.setBinContent(emails);
                clientModel.setTextView("received");
                this.clientModel.setCurrentEmails();
            } else {
                System.out.println("rotto: moveToBin"); // TODO
            }
        }
        this.closeSocketConnection();
    }

    public void deleteAllEmails() throws IOException, ClassNotFoundException {
        this.connectToSocket();
        Communication request = new Communication("delete",
                new BaseBody(this.clientModel.emailAddressProperty().getValue()));
        Communication response = sendCommunication(request);
        if (response == null) {
            return;
        }
        BooleanBody responseBody = (BooleanBody) response.getBody();

        if (response.getAction().equals("delete_permanently_ok") && responseBody.isResult()) {
            this.clientModel.removeBinContent();
        } else {
            System.out.println("rotto: deleteAllEmails"); // TODO
        }
        this.closeSocketConnection();
    }
}
