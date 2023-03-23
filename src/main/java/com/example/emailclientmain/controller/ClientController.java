package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientController {
    private Socket socket;

    private final ExecutorService threadPool;

    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    private ClientModel clientModel;

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private BoxButtonsController buttonsController;

    private boolean connected;

    private boolean serverStatus;
    /* mettere variabile a true/false per lo status del server:
        se true: il server è online
        se false: il serve è morto e deve far vedere una sola volta il popup di errore
    * */

    private boolean showOneTimeAlert;

    public ClientController(ClientModel clientModel, BoxButtonsController buttonsController) {
        this.clientModel = clientModel;
        this.buttonsController = buttonsController;
        this.connected = false;
        this.serverStatus = false;
        this.showOneTimeAlert = false;
        threadPool = Executors.newFixedThreadPool(10);
    }

    private void showAlert(){
        Alert.AlertType alertType = Alert.AlertType.ERROR;
        Alert alert = new Alert(alertType, "Errore nella connessione al server, riprova più tardi");
        alert.showAndWait();
    }

    private void connectToSocket() {
        try {
            String nomeHost = InetAddress.getLocalHost().getHostName();
            socket = new Socket(nomeHost, 8189);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.serverStatus = true;
            this.showOneTimeAlert = false;
        } catch (IOException e) {
            this.serverStatus = false;
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

    public void tryConnection(String email){
        scheduler.scheduleAtFixedRate(() -> {
            this.connectToSocket();
            if(this.serverStatus == true)
                firstConnection(email);
            else {
                if(showOneTimeAlert == false) {
                    Platform.runLater(() -> this.showAlert());
                    showOneTimeAlert = true;
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

    }

    public void firstConnection(String email) {
        threadPool.execute(()-> {
            try {
                 this.connectToSocket();
                if (connected == true) {
                    ObservableList<EmailBody> emailReceived = this.clientModel.getInboxContent();
                    Communication connection = new Communication("get_new_emails", new GetEmailsBody(email, emailReceived.get(0).getTimestamp()));
                    Communication response = sendCommunication(connection);

                    if (response == null) {
                        return;
                    }

                    ArrayList<EmailBody> emails = ((EmailListBody) response.getBody()).getEmails();
                    Collections.reverse(emails);

                    Platform.runLater(()-> this.clientModel.setNewEmailInboxContent(emails));

                    return;
                }

                Communication connection = new Communication("connection", new BaseBody(email));
                Communication response = sendCommunication(connection);

                if (response == null) {
                    return;
                }

                ArrayList<ArrayList<EmailBody>> emails = ((ConnectionBody) (response.getBody())).getEmails();

                /* FARE SU SERVER: Faccio il reverse per avere le email in ordine di orario */
                Collections.reverse(emails.get(0));
                Collections.reverse(emails.get(1));
                Collections.reverse(emails.get(2));

                Platform.runLater(()-> clientModel.setInboxContent(emails.get(0)));
                Platform.runLater(()-> clientModel.setSentContent(emails.get(1)));
                Platform.runLater(()-> clientModel.setBinContent(emails.get(2)));
                this.connected = true;
                this.closeSocketConnection();
            } catch (IOException e) {
            }
        });
    }

    public void sendEmail(EmailBody email) {
        threadPool.execute(()-> {
            try {
                this.connectToSocket();
                Communication communication = new Communication("send_email", email);
                Communication response = sendCommunication(communication);
                if (response.getAction().equals("emails_saved")) {
                    Platform.runLater(()-> clientModel.setNewEmailSentContent(email));
                    Platform.runLater(()-> this.buttonsController.loadPage("INVIATE"));
                }
                this.closeSocketConnection();
            } catch (IOException e) {
            }
        });
    }

    public void moveToBin(String id, String email)  {
        threadPool.execute(()-> {
            try {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteAllEmails()  {
        threadPool.execute(()-> {
            try {
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void closeThreadPool(){
        scheduler.shutdownNow();
        threadPool.shutdownNow();
    }
}
