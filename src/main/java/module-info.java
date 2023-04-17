module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.apache.logging.log4j;

    opens org.example to javafx.fxml;
    exports org.example;
}