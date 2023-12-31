package org.example.data;

import com.google.gson.*;
import org.example.media.extensions.SoundFactory;
import org.example.media.interfaces.ISound;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SoundSerializer implements JsonSerializer<ISound>, JsonDeserializer<ISound> {
    @Override
    public JsonElement serialize(ISound src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("soundID", src.getSoundID());
        jsonObject.addProperty("title", src.getTitle());

        String input = (src.getMedia().getSource());
        String decodedString = URLDecoder.decode(input, StandardCharsets.UTF_8);

        jsonObject.addProperty("path","src\\main\\resources\\data\\sounds\\" + decodedString.substring(decodedString.lastIndexOf('/')+1));
        jsonObject.addProperty("uploadedBy", src.getUploadedBy().getUserID());
        return jsonObject;
    }

    @Override
    public ISound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String soundID = jsonObject.get("soundID").getAsString();
        String title = jsonObject.get("title").getAsString();
        File path = new File(jsonObject.get("path").getAsString());
        User uploadedBy = UserManager.getInstance().getUserById(jsonObject.get("uploadedBy").getAsString());
        return SoundFactory.getInstance().createSound(soundID, title, path, uploadedBy);
    }
}

