module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.apache.logging.log4j;
    requires com.google.gson;
    requires java.desktop;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.materialicons;

    opens org.example to javafx.fxml;
    opens org.example.controller to javafx.fxml;
    opens org.example.media.extensions to javafx.base;
    exports org.example;
    exports org.example.controller;
    exports org.example.exceptions;
    exports org.example.media.interfaces;
    exports org.example.playlist;
    exports org.example.profile;
}