package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.playlist.Playlist;

public class PlaylistController {

    private static final Logger log = LogManager.getLogger(PlaylistController.class);
    @FXML
    private Label name;

    @FXML
    private Label createdBy;

    @FXML
    private ImageView playlistCover;
    public void setData(Playlist playlist) {
        name.setText(playlist.getName());
        createdBy.setText(playlist.getCreatedBy().getUsername());
        playlistCover.setImage(new Image(playlist.getPlaylistCover().toURI().toString()));
    }
}
