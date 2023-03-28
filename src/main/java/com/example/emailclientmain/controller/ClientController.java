package com.example.emailclientmain.controller;

import com.example.emailclientmain.model.ClientModel;
import com.example.transmission.EmailBody;
import com.example.transmission.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream.GetField;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

	private Timestamp timestamp;
	private boolean showOneTimeAlert;

	private List<String> emailList;


	public ClientController(ClientModel clientModel, BoxButtonsController buttonsController) {
		this.clientModel = clientModel;
		this.buttonsController = buttonsController;
		this.connected = false;
		this.serverStatus = true;
		this.showOneTimeAlert = false;
		this.timestamp = null;
		this.emailList = new ArrayList<>();
		threadPool = Executors.newFixedThreadPool(10);
	}

	private void showAlert(String msg) {
		if (showOneTimeAlert == false) {
			Alert.AlertType alertType = Alert.AlertType.ERROR;
			Alert alert = new Alert(alertType, msg);
			alert.showAndWait();
			showOneTimeAlert = true;
		}
	}

	private void showSendAlert(String msg, String type) {

		Alert.AlertType alertType = type.equals("info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
		Alert alert = new Alert(alertType, msg);
		alert.show();
	}

	private void showNewEmailsAlert(String msg) {
		Alert.AlertType alertType = Alert.AlertType.INFORMATION;
		Alert alert = new Alert(alertType, msg);
		alert.show();
	}

	private void connectToSocket() {
		try {
			String nomeHost = InetAddress.getLocalHost().getHostName();
			socket = new Socket(nomeHost, 8189);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());
			this.serverStatus = true;
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

	public void tryConnection(String email) {
		scheduler.scheduleAtFixedRate(() -> {
			firstConnection(email);
			if (!this.serverStatus) {
				Platform.runLater(() -> this.showAlert("Errore nella connessione al server, riprova più tardi"));
			}
		}, 0, 5, TimeUnit.SECONDS);

	}

	public void firstConnection(String email) {
		threadPool.execute(() -> {
			try {
				this.connectToSocket();

				if (this.serverStatus == false) {
					Platform.runLater(() -> this.showAlert("Errore nella connessione al server, riprova più tardi"));
					return;
				}

				if (connected == true) {
					ObservableList<EmailBody> emailReceived = this.clientModel.getInboxContent();

					if (emailReceived.size() > 0) {
						if (timestamp == null || timestamp.compareTo(emailReceived.get(0).getTimestamp()) < 0) {
							this.timestamp = emailReceived.get(0).getTimestamp();
						}
					}

					Communication connection = new Communication("get_new_emails", new GetEmailsBody(email, timestamp));
					Communication response = sendCommunication(connection);

					if (response == null) {
						Platform.runLater(() -> this.showAlert("Errore nella connessione al server, riprova più tardi"));
						return;
					}

					if (response.getAction().equals("no_emails")) {
						Platform.runLater(() -> this.showAlert("Non sono presenti email!"));
						return;
					}

					ArrayList<EmailBody> emails = ((EmailListBody) response.getBody()).getEmails();
					Collections.reverse(emails);

					Platform.runLater(() -> this.clientModel.setNewEmailInboxContent(emails));
					if(emails.size() > 0)
						Platform.runLater(() -> this.showNewEmailsAlert("Hai "+ emails.size() + " nuove email"));
					this.showOneTimeAlert = false;
					return;
				}

				Communication connection = new Communication("connection", new BaseBody(email));
				Communication response = sendCommunication(connection);

				if (response == null) {
					Platform.runLater(() -> this.showAlert("Errore nella connessione al server, riprova più tardi"));
					return;
				}

				if (response.getAction().equals("user_created")) {
					Platform.runLater(() -> this.showAlert("La tua utenza è stata creata!"));
					return;
				}

				ArrayList<ArrayList<EmailBody>> emails = ((ConnectionBody) (response.getBody())).getEmails();

				Collections.reverse(emails.get(0));
				Collections.reverse(emails.get(1));
				Collections.reverse(emails.get(2));

				Platform.runLater(() -> clientModel.setInboxContent(emails.get(0)));
				Platform.runLater(() -> clientModel.setSentContent(emails.get(1)));
				Platform.runLater(() -> clientModel.setBinContent(emails.get(2)));

				this.connected = true;
				this.showOneTimeAlert = false;
				this.closeSocketConnection();
			} catch (IOException e) {
			}
		});
	}


	public void sendEmail(EmailBody email) {
		threadPool.execute(() -> {
			try {

				this.emailList.clear();
				this.connectToSocket();

				if (this.serverStatus == false) {
					Platform.runLater(() -> this.showSendAlert("Errore nella connessione al server, riprova più tardi", "error"));
					return;
				}

				Communication communication = new Communication("send_email", email);
				Communication response = sendCommunication(communication);

				if (response == null) {
					Platform.runLater(() -> this.showSendAlert("Errore nella connessione al server, riprova più tardi","error"));
					return;
				}

				if (response.getAction().equals("emails_not_saved")) {
					Platform.runLater(() -> this.showSendAlert("L'email non è stata inviata. Utenti non esistente.", "error"));
					return;
				}

				if(response.getAction().equals("emails_saved")) {
					Platform.runLater(() ->  clientModel.setNewEmailSentContent(email));
					Platform.runLater(() -> this.buttonsController.stage.setTitle("INVIATE"));
					Platform.runLater(() -> this.buttonsController.loadPage("INVIATE"));
					Platform.runLater(() -> this.buttonsController.setButtonCss("INVIATE"));
				}
				else if (response.getAction().equals("emails_saved_with_error")) {
					this.emailList = ((EmailBody) response.getBody()).getReceivers();
					Platform.runLater(() -> clientModel.setNewEmailSentContent(email));
					Platform.runLater(() -> this.buttonsController.stage.setTitle("INVIATE"));
					Platform.runLater(() -> this.buttonsController.loadPage("INVIATE"));
					Platform.runLater(() -> this.buttonsController.setButtonCss("INVIATE"));
					Platform.runLater(() -> this.showSendAlert("Errore, email non inviata ai seguenti utenti inesistenti: " + this.emailList.toString(), "info"));
				}

				this.closeSocketConnection();
			} catch (IOException e) {
				Platform.runLater(() -> this.showSendAlert("Errore nella connessione al server, riprova più tardi", "error"));
			}
		});
	}

	public void moveToBin(String id, String email, String from) {
		threadPool.execute(() -> {
			try {
				this.connectToSocket();
				BinBody bin = new BinBody(id, email);
				Communication communication = new Communication("bin", bin);
				Communication response = sendCommunication(communication);
				BooleanBody responseBody = (BooleanBody) response.getBody();

				if (response.getAction().equals("bin_ok") && responseBody.isResult()) {
					EmailBody emailBody = null;
					ObservableList<EmailBody> contentToUse = null;

					if(from.equals("sent")){
						contentToUse = clientModel.getSentContent();
					}
					else if(from.equals("received")){
						contentToUse = clientModel.getInboxContent();
					}

					if(contentToUse == null) {
						Platform.runLater(() ->this.showSendAlert("Errore, l'email non può essere spostata nel cestino.","error" ));
						return;
					}

					for (int i = 0; i < contentToUse.size(); i++) {
						if (contentToUse.get(i).getId().equals(id)) {
							emailBody = contentToUse.get(i);
							contentToUse.remove(i);
							break;
						}
					}

					if (emailBody != null) {
						ArrayList emails = new ArrayList<>();
						emails.add(emailBody);
						Platform.runLater(() -> clientModel.setBinContent(emails));
						Platform.runLater(() -> clientModel.setTextView("received"));
						Platform.runLater(() -> this.clientModel.setCurrentEmails());
					} else {
						Platform.runLater(() ->this.showSendAlert("Errore, email non trovata!", "error"));
					}

					/* change title to  cestino*/
					Platform.runLater(() -> this.buttonsController.stage.setTitle("CESTINO"));
					Platform.runLater(() -> this.buttonsController.loadPage("CESTINO"));
				}
				else {
					Platform.runLater(() -> this.showSendAlert("Errore, l'email non può essere spostata nel cestino.", "error" ));
				}

				this.closeSocketConnection();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public void deleteAllEmails() {
		threadPool.execute(() -> {
			try {
				this.connectToSocket();
				Communication request = new Communication("delete",
						new BaseBody(this.clientModel.emailAddressProperty().getValue()));
				Communication response = sendCommunication(request);

				if (response == null) {
					Platform.runLater(() -> this.showAlert("Errore nella connessione al server, riprova più tardi"));
				}

				BooleanBody responseBody = (BooleanBody) response.getBody();

				if (response.getAction().equals("delete_permanently_ok") && responseBody.isResult()) {
					Platform.runLater(() -> this.clientModel.removeBinContent());
				} else {
					Platform.runLater(() -> this.showSendAlert("Le email non sono state eliminate.", "error" ));

				}

				this.closeSocketConnection();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public void closeThreadPool() {
		scheduler.shutdownNow();
		threadPool.shutdownNow();
	}
}
