module com.example.emailclientserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.emailclientmain to javafx.fxml;
    exports com.example.emailclientmain;
    exports com.example.emailclientmain.controller;
    opens com.example.emailclientmain.controller to javafx.fxml;
    exports com.example.emailclientmain.model;
    opens com.example.emailclientmain.model to javafx.fxml;
}