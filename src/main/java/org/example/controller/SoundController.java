package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;

public class SoundController {

    private static final Logger log = LogManager.getLogger(SoundController.class);
    @FXML
    private Label title;

    @FXML
    private Label uploadedBy;

    @FXML
    private ImageView cover;
    public void setData(ISound sound) {
        title.setText(sound.getTitle());
        uploadedBy.setText(sound.getUploadedBy().getUsername());
        if (sound.getMedia().getMetadata().get("image") != null) {
            cover.setImage((Image) sound.getMedia().getMetadata().get("image"));
        }
        cover.setImage(new Image(SoundManager.getInstance().getDEFAULT_COVER().toURI().toString()));
    }
}
