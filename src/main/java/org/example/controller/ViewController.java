package org.example.controller;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.player.Player;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewController extends ExceptionVisualizer implements Initializable  {
    private static final Logger log = LogManager.getLogger(ViewController.class);

    @FXML
    private Label soundTableLabel;
    @FXML
    private TableView<ISound> soundTable;
    @FXML
    private TableColumn<ISound, StackPane> soundCover;
    @FXML
    private TableColumn<ISound, String> soundTitle;
    @FXML
    private TableColumn<ISound, HBox> soundUploadedBy;

    @FXML
    private Label playlistTableLabel;
    @FXML
    private TableView<Playlist> playlistTable;
    @FXML
    private TableColumn<Playlist, StackPane> playlistCover;
    @FXML
    private TableColumn<Playlist, String> playlistName;
    @FXML
    private TableColumn<Playlist, Integer> numberOfSounds;
    @FXML
    private TableColumn<Playlist, HBox> playlistCreatedBy;

    @FXML
    private TableView<ISound> queueTable;
    @FXML
    private TableColumn<ISound, ImageView> queueSoundCover;
    @FXML
    private TableColumn<ISound, String> queueSoundTitle;

    @FXML
    public TableView<ISound> historyTable;
    @FXML
    public TableColumn<ISound, ImageView> historySoundCover;
    @FXML
    public TableColumn<ISound, String> historySoundTitle;


    private final ObservableList<ISound> queueObjectList = FXCollections.observableList(Player.getInstance().getSoundQueue());
    private final ObservableList<ISound> historyObjectList = FXCollections.observableList(Player.getInstance().getSoundHistory());
    private ViewType view;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.info("Initializing view controller");
        //Sound table
        soundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getInstance().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            StackPane stackPane = new StackPane();
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setId("soundCover");

            Button button = new Button();
            MaterialIconView iconView = new MaterialIconView();
            iconView.setGlyphName("PLAY_CIRCLE_FILLED");
            iconView.setSize("35");
            iconView.setFill(Color.WHITE);
            iconView.setOpacity(0.5);
            button.setGraphic(iconView);
            stackPane.getChildren().addAll(imageView, button);
            button.setId("playlistCoverButton");
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleOnSoundCoverButtonClicked(cellData.getValue()));
            button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> iconView.setOpacity(1));
            button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> iconView.setOpacity(0.5));
            return new ReadOnlyObjectWrapper<>(stackPane);
        });
        soundTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        soundUploadedBy.setCellValueFactory(cellData -> {
            User user = UserManager.getInstance().getUserByName(cellData.getValue().getUploadedBy().getUsername());
            Label label = new Label(user.getUsername());
            ImageView imageView = new ImageView(new Image(cellData.getValue().getUploadedBy().getProfilePicture().toURI().toString()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(label, imageView);
            return new ReadOnlyObjectWrapper<>(hBox);
        });
        soundTable.setPlaceholder(new Label("No sounds found"));

        soundCover.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.2));
        soundTitle.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.5));
        soundUploadedBy.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.3));

        soundTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        //Playlist table
        playlistCover.setCellValueFactory(cellData -> {
            Image image = new Image(cellData.getValue().getPlaylistCover().toURI().toString());
            ImageView imageView = new ImageView(image);
            StackPane stackPane = new StackPane();
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setId("playlistCover");
            Button button = new Button();
            MaterialIconView iconView = new MaterialIconView();
            iconView.setGlyphName("PLAY_CIRCLE_FILLED");
            iconView.setSize("35");
            iconView.setFill(Color.WHITE);
            iconView.setOpacity(0.5);
            button.setGraphic(iconView);
            stackPane.getChildren().addAll(imageView, button);
            button.setId("playlistCoverButton");
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleOnPlaylistCoverButtonClicked(cellData.getValue()));
            button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> iconView.setOpacity(1));
            button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> iconView.setOpacity(0.5));
            return new ReadOnlyObjectWrapper<>(stackPane);
        });
        playlistName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        numberOfSounds.setCellValueFactory(new PropertyValueFactory<>("numberOfSounds"));
        playlistCreatedBy.setCellValueFactory(cellData -> {
            User user = UserManager.getInstance().getUserByName(cellData.getValue().getCreatedBy().getUsername());
            Label label = new Label(user.getUsername());
            ImageView imageView = new ImageView(new Image(cellData.getValue().getCreatedBy().getProfilePicture().toURI().toString()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(label, imageView);
            return new ReadOnlyObjectWrapper<>(hBox);
        });
        playlistTable.setPlaceholder(new Label("No playlists found"));

        playlistCover.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        numberOfSounds.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        playlistName.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));
        playlistCreatedBy.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));


        //Queue table
        queueSoundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getInstance().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setId("soundCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        queueSoundTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        queueTable.setPlaceholder(new Label("Queue is empty"));

        queueSoundCover.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.4));
        queueSoundTitle.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.6));

        queueTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        queueTable.setItems(queueObjectList);

        //History table
        historySoundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getInstance().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setId("soundCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        historySoundTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        historyTable.setPlaceholder(new Label("History is empty"));

        historySoundCover.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.4));
        historySoundTitle.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.6));
        historyTable.setItems(historyObjectList);
        setView(ViewType.ALL);
        updateView();

        //TODO: FIX THIS!! YEYYYYYYY IT WORKS 😊😊

        Player.getInstance().registerOnNextSongEvent(() -> Platform.runLater(() -> {
            queueTable.refresh();
            historyTable.refresh();
        }));
       /* Timer updateTimer = new Timer();
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    queueTable.refresh();
                    historyTable.refresh();
                });
            }
        };
        updateTimer.schedule(updateTask, 0, 1000);
        */
    }

    public void setView(ViewType viewType) {
        this.view = viewType;
        log.info("View set to " + viewType.toString());
    }

    public void updateView() {
        switch (view) {
            case ALL:
                soundTableLabel.setText("All Sounds");
                initializeSoundContent(SoundManager.getInstance().getAllSounds());
                playlistTableLabel.setText("All Playlists");
                initializePlaylistContent(PlaylistManager.getInstance().getAllPlaylists());
                break;
            case USER:
                soundTableLabel.setText("My Sounds");
                initializeSoundContent(SoundManager.getInstance().getAllSoundsByUser(UserManager.getInstance().getActiveUser()));
                playlistTableLabel.setText("My Playlists");
                initializePlaylistContent(PlaylistManager.getInstance().getPlaylistsByUser(UserManager.getInstance().getActiveUser()));
                break;
        }
        soundTable.selectionModelProperty().get().clearSelection();
        playlistTable.selectionModelProperty().get().clearSelection();
        queueTable.selectionModelProperty().get().clearSelection();
        log.debug("View updated");
    }

    private void initializeSoundContent(ArrayList<ISound> sounds) {
        ObservableList<ISound> soundObjectList = FXCollections.observableArrayList();
        soundObjectList.setAll(sounds);
        FXCollections.sort(soundObjectList, Comparator.comparing(ISound::getTitle));
        soundTable.setItems(soundObjectList);
        soundTable.refresh();
    }

    private void initializePlaylistContent(ArrayList<Playlist> playlists) {
        ObservableList<Playlist> playlistObjectList = FXCollections.observableArrayList();
        playlistObjectList.setAll(playlists);
        FXCollections.sort(playlistObjectList, Comparator.comparing(Playlist::getName));
        playlistTable.setItems(playlistObjectList);
        playlistTable.refresh();
    }

    /**
     * Probably the method to set the sound / playlist in queues first place
     *
     * @see org.example.player.Player
     */
    @FXML
    private void deleteSound() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        try {
            SoundManager.getInstance().deleteSoundByID(UserManager.getInstance().getActiveUser(), sound.getSoundID());
            for (Playlist playlist : PlaylistManager.getInstance().getAllPlaylists()) {
                playlist.removeSound(sound);
            }
            Player.getInstance().removeSoundFromQueue(sound);
        } catch (IllegalArgumentException e) {
            showPopup(e);
        }
        new DataThread(DataType.SOUND_PLAYLIST, DataOperation.WRITE).start();
        updateView();
    }

    @FXML
    private void deletePlaylist() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        try {
            PlaylistManager.getInstance().deletePlaylistByID(UserManager.getInstance().getActiveUser(), playlist.getPlaylistID());
        } catch (IllegalArgumentException e) {
            showPopup(e);
        }
        new DataThread(DataType.PLAYLIST, DataOperation.WRITE).start();
        updateView();
    }

    @FXML
    private void addToPlaylist() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        try {
            playlist.addSound(UserManager.getInstance().getActiveUser(), sound);
        } catch (IllegalArgumentException e) {
            showPopup(e);
        }
        new DataThread(DataType.PLAYLIST, DataOperation.WRITE).start();
        updateView();
    }

    @FXML
    private void addSoundToQueue() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        Player.getInstance().addSoundToQueue(sound);
        queueTable.refresh();
    }

    @FXML
    private void removeSoundFromQueue() {
        log.info("Removing sound from queue");
        ISound sound = queueTable.getSelectionModel().getSelectedItem();
        Player.getInstance().removeSoundFromQueue(sound);
        queueTable.refresh();
    }

    @FXML
    private void addPlaylistToQueue() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        Player.getInstance().addPlaylistToQueue(playlist);
        queueTable.refresh();
    }

    @FXML
    private void handleOnPlaylistClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            try {
                Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
                soundTableLabel.setText("Sounds in Playlist: " + playlist.getName());
                initializeSoundContent(playlist.getSounds());
            } catch (NullPointerException e) {
                log.error("No playlist selected");
                showPopup(e);
            }
        }
    }

    private void handleOnSoundCoverButtonClicked(ISound sound) {
        try {
            Player.getInstance().clearQueue();
            Player.getInstance().addSoundToQueue(sound);
            Player.getInstance().playPause();
            queueTable.refresh();
        } catch (NullPointerException e) {
            log.error("No sound selected");
            showPopup(e);
        }
    }

    private void handleOnPlaylistCoverButtonClicked(Playlist value) {
        try {
            Player.getInstance().clearQueue();
            Player.getInstance().addPlaylistToQueue(value);
            Player.getInstance().playPause();
            queueTable.refresh();
        } catch (NullPointerException e) {
            log.error("No playlist selected");
            showPopup(e);
        }
    }

    void handleSearchBar(String search) {
        if (search.isEmpty()) {
            updateView();
        } else if (search.startsWith("user:")) {
            ArrayList<ISound> soundsInSearch = SoundManager.getInstance().getAllSoundsByUser(UserManager.getInstance().getUserByName(search.substring(5)));
            ArrayList<Playlist> playlistsInSearch = PlaylistManager.getInstance().getPlaylistsByUser(UserManager.getInstance().getUserByName(search.substring(5)));
            initializeSoundContent(soundsInSearch);
            initializePlaylistContent(playlistsInSearch);
        } else if (search.startsWith("sound:")) {
            ArrayList<ISound> soundsInSearch = SoundManager.getInstance().getAllSounds().stream().filter(sound -> sound.getTitle().toLowerCase().contains(search.substring(6).toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            initializeSoundContent(soundsInSearch);
        } else if (search.startsWith("playlist:")) {
            ArrayList<Playlist> playlistsInSearch = PlaylistManager.getInstance().getAllPlaylists().stream().filter(playlist -> playlist.getName().toLowerCase().contains(search.substring(9).toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            initializePlaylistContent(playlistsInSearch);
        } else {
            ArrayList<ISound> soundsInSearch = SoundManager.getInstance().getAllSounds().stream().filter(sound -> sound.getTitle().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Playlist> playlistsInSearch = PlaylistManager.getInstance().getAllPlaylists().stream().filter(playlist -> playlist.getName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            initializeSoundContent(soundsInSearch);
            initializePlaylistContent(playlistsInSearch);
        }
    }

    public void handleAddSoundButton() {
        SceneManager.ADD_SOUND.showSecondaryStage();
    }

    public void handleCreatePlaylistButton() {
        SceneManager.CREATE_PLAYLIST.showSecondaryStage();
    }
}

