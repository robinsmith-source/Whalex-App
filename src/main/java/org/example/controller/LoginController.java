package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ReadDataException;
import org.example.profile.UserManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final Logger log = LogManager.getLogger(LoginController.class);
    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField confirmPasswordField;

    @FXML
    Button loginButton;

    @FXML
    Button registerButton;

    @FXML
    Button fileChooserButton;

    @FXML
    Label errorMessageLabel;

    private final FileChooser fileChooser = new FileChooser();

    private File choosenImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            UserManager.getInstance().usersFromJSON();
        } catch (ReadDataException e) {
            log.fatal("Failed to read user data from JSON file.");
            System.exit(1);
        }
    }

    @FXML
    public void checkUsername() {
        log.debug("Checking username {}.", usernameField.getText());
        try {
            if (UserManager.getInstance().getUserByName(usernameField.getText()) != null) {
                confirmPasswordField.setDisable(true);
                fileChooserButton.setDisable(true);
                registerButton.setDisable(true);
            }
        } catch (Exception e) {
            confirmPasswordField.setDisable(false);
            fileChooserButton.setDisable(false);
            registerButton.setDisable(false);
        }
    }

    @FXML
    public void login() {
        log.debug("Login button pressed with username {}.", usernameField.getText());
        try {
            UserManager.getInstance().login(usernameField.getText(), passwordField.getText());
            SceneManager.LOADING.changeScene();
        } catch (IllegalArgumentException e) {
            errorMessageLabel.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void register() {
        log.info("Register button pressed with username {}. - ProfilePicture: {}", usernameField.getText(), choosenImage);
        Path targetFolder = Path.of("src/main/resources/data/profilePictures");
        String extension = choosenImage.getName().substring(choosenImage.getName().lastIndexOf("."));
        Path targetFile = targetFolder.resolve(usernameField.getText() + extension);
        try {
            Files.copy(choosenImage.toPath(), targetFile);
            File profilePicture = targetFile.toFile();
            UserManager.getInstance().createUser(profilePicture, usernameField.getText(), passwordField.getText(), confirmPasswordField.getText());
            SceneManager.LOADING.changeScene();
            UserManager.getInstance().usersToJSON();
        } catch (IllegalArgumentException e) {
            errorMessageLabel.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void chooseFile() {
        Stage fileChooserWindow = new Stage();
        fileChooserWindow.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        choosenImage = fileChooser.showOpenDialog(fileChooserWindow);
    }
}
