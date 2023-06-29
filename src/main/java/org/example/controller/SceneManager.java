package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.WhalexApp;

import java.io.IOException;
import java.util.Objects;

public enum SceneManager {

    LOGIN("/fxml/login.fxml"),
    LOADING("/fxml/loading.fxml"),
    MAIN("/fxml/mainScene.fxml"),
    ADD_SOUND("/fxml/addSound.fxml"),
    CREATE_PLAYLIST("/fxml/createPlaylist.fxml"),
    EDIT_PROFILE("/fxml/editProfile.fxml");

    private static final Logger log = LogManager.getLogger(SceneManager.class);

    private final String fxmlPath;

    private final Stage applicationStage = WhalexApp.getApplicationStage();
    private final Stage secondaryStage = new Stage();
    private MainSceneController msc;

    SceneManager(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public Parent getScene() {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource(this.fxmlPath));
        try {
            loader.load();
            if (this == MAIN) msc = loader.getController();
        } catch (IOException e) {
            log.fatal("Error loading scene: " + this.fxmlPath);
            e.printStackTrace();
            System.exit(2);
        }
        return loader.getRoot();
    }

    public void showSecondaryStage() {
        switch (this) {
            case ADD_SOUND:
                secondaryStage.setTitle("Whalex - Add Sound");
                secondaryStage.setScene(new Scene(SceneManager.ADD_SOUND.getScene()));
                break;
            case CREATE_PLAYLIST:
                secondaryStage.setTitle("Whalex - Create Playlist");
                secondaryStage.setScene(new Scene(SceneManager.CREATE_PLAYLIST.getScene()));
                break;
            case EDIT_PROFILE:
                secondaryStage.setTitle("Whalex - Edit Profile");
                secondaryStage.setScene(new Scene(SceneManager.EDIT_PROFILE.getScene()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
        secondaryStage.show();
    }

    public void closeSecondaryStage() {
        secondaryStage.close();
    }

    public MainSceneController getController() {
        return msc;
    }

    public void changeScene() {
        Scene scene;
        switch (this) {
            case LOGIN:
                applicationStage.setTitle("Whalex - Login");
                scene = new Scene(SceneManager.LOGIN.getScene(), 600, 400);
                applicationStage.setScene(scene);
                applicationStage.setResizable(false);
                applicationStage.centerOnScreen();
                break;
            case LOADING:
                applicationStage.setTitle("Whalex - Loading");
                scene = new Scene(SceneManager.LOADING.getScene(), 600, 400);
                applicationStage.setScene(scene);
                applicationStage.setResizable(false);
                applicationStage.centerOnScreen();
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
        applicationStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/fxml/assets/WhalexLOGO.png"))));
        new Thread(() -> Platform.runLater(applicationStage::show)).start();
        log.info("Showing Scene: {}, from: {}", WhalexApp.getApplicationStage().getTitle(), this.fxmlPath);
    }
}


