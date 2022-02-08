package net.pixlies.nations.nations;

import com.google.gson.Gson;
import lombok.Getter;
import net.pixlies.nations.Nations;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class NationManager {

    private static final Nations instance = Nations.getInstance();

    @Getter private final Map<String, Nation> nations = new HashMap<>(); // UUID/ID, Nation

    public NationManager() {

        Gson gson = new Gson();

        for (Document d : instance.getMongoManager().getNationCollection().find()) {
            Nation nation = gson.fromJson(d.toJson(), Nation.class);
            if (nation.getId() != null) {
                nations.put(nation.getId(), nation);
            }
        }

    }

    public void backupAll() {
        for (Nation nation : nations.values()) {
            nation.backup();
        }
    }

    public void loadAll() {
        // TODO: Load all nations to cache?
    }

}