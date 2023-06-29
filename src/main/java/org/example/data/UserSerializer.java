package org.example.data;

import com.google.gson.*;
import org.example.profile.User;

import java.io.File;
import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User>, JsonDeserializer<User> {
    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userID", src.getUserID());
        jsonObject.addProperty("profilePicture", src.getProfilePicture().getPath());
        jsonObject.addProperty("username", src.getUsername());
        jsonObject.addProperty("password", src.getPassword());
        return jsonObject;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String id = jsonObject.get("userID").getAsString();
        File profilePicture = new File(jsonObject.get("profilePicture").getAsString());
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        return new User(id, profilePicture, username, password);
    }
}
