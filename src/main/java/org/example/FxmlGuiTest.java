package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FxmlGuiTest extends Application {

	private static final Logger log = LogManager.getLogger(FxmlGuiTest.class);

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        log.info("Starting Whalex Application");
        final String fxmlFile =  "/fxml/MainScene.fxml";

        log.debug("Loading FXML for main view from: {}", fxmlFile);
        BorderPane root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));
        AnchorPane view = FXMLLoader.load(getClass().getResource("/fxml/view.fxml"));

        root.setCenter(view);


        Scene scene = new Scene(root);
        log.debug("Showing JFX scene");
        stage.setTitle("Whalex - Overview");
        stage.setScene(scene);
        stage.show();
    }
}
