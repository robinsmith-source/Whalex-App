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
import org.example.profile.UserManager;

import java.io.File;
import java.net.URL;
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

    private File chosenImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            SceneManager.MAIN.changeScene();
        } catch (IllegalArgumentException e) {
            errorMessageLabel.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void register() {
        log.info("Register button pressed with username {}. - ProfilePicture: {}", usernameField.getText(), chosenImage);
        try {
            UserManager.getInstance().createUser(chosenImage, usernameField.getText(), passwordField.getText(), confirmPasswordField.getText());
            SceneManager.MAIN.changeScene();
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
        chosenImage = fileChooser.showOpenDialog(fileChooserWindow);
    }
}
