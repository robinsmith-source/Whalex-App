package org.example.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
                WhalexApp.getApplicationStage().setTitle("Whalex - Login");
                scene = new Scene(SceneManager.LOGIN.getScene(), 600, 400);
                WhalexApp.getApplicationStage().setScene(scene);
                WhalexApp.getApplicationStage().setResizable(false);
                break;
            case LOADING:
                WhalexApp.getApplicationStage().setTitle("Whalex - Loading");
                scene = new Scene(SceneManager.LOADING.getScene(), 600, 400);
                WhalexApp.getApplicationStage().setScene(scene);
                WhalexApp.getApplicationStage().setResizable(false);
                break;
            case MAIN:
                WhalexApp.getApplicationStage().setTitle("Whalex - Overview");
                scene = new Scene(SceneManager.MAIN.getScene());
                WhalexApp.getApplicationStage().setScene(scene);
                WhalexApp.getApplicationStage().setResizable(true);
                WhalexApp.getApplicationStage().setMaximized(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
        WhalexApp.getApplicationStage().show();
        log.debug("Loading FXML for {} from: {}",WhalexApp.getApplicationStage().getTitle(), this.fxmlPath);
    }
}


