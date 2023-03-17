module com.example.emailclientserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.emailclientmain to javafx.fxml;
    exports com.example.emailclientmain;
    exports com.example.emailclientmain.Controller;
    opens com.example.emailclientmain.Controller to javafx.fxml;
    exports com.example.emailclientmain.Model;
    opens com.example.emailclientmain.Model to javafx.fxml;
}