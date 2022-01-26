package net.pixlies.nations.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.pixlies.nations.Nations;
import net.pixlies.nations.handlers.Handler;

import java.io.*;

public class JsonData implements Handler {

    private static final Nations instance = Nations.getInstance();
    private final Gson gson = instance.getGson();

    private JsonObject jsonObject = new JsonObject();

    private final File file;

    public JsonData(File file) {
        this.file = file;
        createIfNotExists();
    }

    private void createIfNotExists() {
        if (file.exists()) return;
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            instance.getLogger().warning("Failed to save JSON file " + file.getName() + ".");
        }
    }

    public void save() {
        createIfNotExists();
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!file.exists()) {
            jsonObject = new JsonObject();
        }
        try {
            FileReader reader = new FileReader(file);
            jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public <T> T get(String set, Class<T> clazz) {
        return gson.fromJson(jsonObject.get(set), clazz);
    }

    public void set(String set, Object object) {
        jsonObject.add(set, gson.toJsonTree(object));
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

}
