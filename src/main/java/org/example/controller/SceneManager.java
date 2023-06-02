package org.example.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.WhalexApp;

import java.io.IOException;

public enum SceneManager {

    LOGIN("/fxml/login.fxml"),
    LOADING("/fxml/loading.fxml"),
    MAIN("/fxml/mainScene.fxml");
    private static final Logger log = LogManager.getLogger(SceneManager.class);

    private final String fxmlPath;

    private final Stage applicationStage = WhalexApp.getApplicationStage();

    SceneManager(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public Parent getScene() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource(this.fxmlPath));
        return loader.load();
    }

    public void changeScene() throws Exception {
        Scene scene;
        switch (this) {
            case LOGIN:
                applicationStage.setTitle("Whalex - Login");
                scene = new Scene(SceneManager.LOGIN.getScene(), 600, 400);
                applicationStage.setScene(scene);
                applicationStage.setResizable(false);
                break;
            case LOADING:
                applicationStage.setTitle("Whalex - Loading");
                scene = new Scene(SceneManager.LOADING.getScene(), 600, 400);
                applicationStage.setScene(scene);
                applicationStage.setResizable(false);
                break;
            case MAIN:
                applicationStage.setTitle("Whalex - Overview");
                scene = new Scene(SceneManager.MAIN.getScene());
                applicationStage.setScene(scene);
                applicationStage.setResizable(true);
                applicationStage.setMaximized(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
        applicationStage.getIcons().add(new Image(getClass().getResourceAsStream("/fxml/assets/WhalexLOGO.png")));
        applicationStage.show();
        log.debug("Showing Scene: {}, from: {}",WhalexApp.getApplicationStage().getTitle(), this.fxmlPath);
    }
}


