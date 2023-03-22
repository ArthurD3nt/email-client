package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.*;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;


public class ClientController {
    private Socket socket;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;

    private ClientModel clientModel;

    public ClientController(ClientModel clientModel) {
        this.clientModel = clientModel;
    }

    private void connectToSocket() throws  IOException {
        String nomeHost = InetAddress.getLocalHost().getHostName();

        socket = new Socket(nomeHost, 8189);
        socket.setSoTimeout(3000);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    private void closeSocketConnection() throws IOException {
        if(socket != null){
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }

    private Communication sendCommunication (Communication c) throws IOException, ClassNotFoundException {
        outputStream.writeObject(c);
        return  (Communication) inputStream.readObject();

    }

    public void firstConnection(String email) throws IOException, ClassNotFoundException {
        this.connectToSocket();
        /* Connection: prende tutte le email ricevute, e setta il content delle email ricevute */
        Communication connection = new Communication("connection",new BaseBody(email));
        Communication response = sendCommunication(connection);
        ArrayList<ArrayList<EmailBody>> emails = ((ConnectionBody)(response.getBody())).getEmails();
        clientModel.setInboxContent(emails.get(0));
        clientModel.setSentContent(emails.get(1));
        clientModel.setBinContent(emails.get(2));
        this.closeSocketConnection();
    }

    public void sendEmail(EmailBody email) throws IOException, ClassNotFoundException {
       this.connectToSocket();
       Communication communication = new Communication("send_email", email);
       Communication response = sendCommunication(communication);
       if(response.getAction().equals("emails_saved")){
           ArrayList emails = new ArrayList<>();
           emails.add(email);
           clientModel.setSentContent(emails);
       }
        this.closeSocketConnection();
    }

    public void moveToBin(String id, String email) throws IOException, ClassNotFoundException {
        this.connectToSocket();
        BinBody bin = new BinBody(id, email);
        Communication communication =  new Communication("bin", bin);
        Communication response =  sendCommunication(communication);
        EmailBody emailBody = null;

        if(response.getAction().equals("bin_ok")){
            ObservableList<EmailBody> inboxContent =  clientModel.getInboxContent();
            for(int i = 0; i < inboxContent.size(); i++ ) {
                if (inboxContent.get(i).getId().equals(id)) {
                    emailBody = inboxContent.get(i);
                    inboxContent.remove(i);
                    break;
                }
            }

            if(emailBody != null){
                ArrayList emails = new ArrayList<>();
                emails.add(emailBody);
                clientModel.setBinContent(emails);
                clientModel.setTextView("received");
                this.clientModel.setCurrentEmails();
            }
            else {
                System.out.println("rotto");
            }
        }
        this.closeSocketConnection();
    }


    public void deleteAllEmails() throws IOException, ClassNotFoundException {
        this.connectToSocket();
        Communication request = new Communication("delete",new BaseBody(this.clientModel.emailAddressProperty().getValue()));
        Communication response = sendCommunication(request);
        BooleanBody responseBody = (BooleanBody)response.getBody();

        System.out.println(responseBody.isResult());
        System.out.println(response.getAction());

        if(response.getAction().equals("delete_permanently_ok") && responseBody.isResult()){
            this.clientModel.removeBinContent();
        }
        this.closeSocketConnection();
    }
}
