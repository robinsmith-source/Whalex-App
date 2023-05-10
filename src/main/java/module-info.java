module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.apache.logging.log4j;
    requires com.google.gson;

    opens org.example to javafx.fxml;
    opens org.example.controller to javafx.fxml;
    exports org.example;
    exports org.example.controller;
    exports org.example.exceptions;
}