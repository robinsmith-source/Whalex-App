package org.example.data;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;

public class PlaylistSerializer implements JsonSerializer<Playlist>, JsonDeserializer<Playlist> {
    private static final Logger log = LogManager.getLogger(PlaylistSerializer.class);

    @Override
    public JsonElement serialize(Playlist src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("playlistID", src.getPlaylistID());
        jsonObject.addProperty("playlistCover", src.getPlaylistCover().getPath());
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("createdBy", src.getCreatedBy().getUserID());
        jsonObject.addProperty("createdAt", src.getCreatedAt().getTime());
        JsonArray soundsArray = new JsonArray();
        for (ISound sound : src.getSounds().values()) {
            JsonObject soundJson = new JsonObject();
            soundJson.addProperty("soundID", sound.getSoundID());
            soundsArray.add(soundJson);
        }
        jsonObject.add("sounds", soundsArray);
        return jsonObject;
    }

    @Override
    public Playlist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String playlistID = jsonObject.get("playlistID").getAsString();
        File playlistCover = new File(jsonObject.get("playlistCover").getAsString());
        String name = jsonObject.get("name").getAsString();
        User createdBy = UserManager.getInstance().getUserById(jsonObject.get("createdBy").getAsString());
        Date createdAt = new Date(jsonObject.get("createdAt").getAsLong());

        Playlist playlist = new Playlist(playlistID, playlistCover, name, createdBy, createdAt);

        JsonArray soundsArray = jsonObject.getAsJsonArray("sounds");

        for (JsonElement soundElement : soundsArray) {
            JsonObject soundJsonObject = soundElement.getAsJsonObject();
            String soundID = soundJsonObject.get("soundID").getAsString();
            playlist.addSound(createdBy, SoundManager.getINSTANCE().getSoundByID(soundID));
        }
        return playlist;
    }
}
