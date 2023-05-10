package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class viewController implements Initializable {

    private static final Logger log = LogManager.getLogger(viewController.class);

    @FXML
    private ScrollPane pane1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Node node;
        try {
            node = FXMLLoader.load(getClass().getResource("/fxml/sounds.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pane1.setContent(node);
    }
}
