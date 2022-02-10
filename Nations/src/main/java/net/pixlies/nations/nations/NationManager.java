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
        for (Nation nation : instance.getMongoManager().getDatastore().createQuery(Nation.class).find().toList()) {
            if (nation.getNationsId() != null) {
                nations.put(nation.getNationsId(), nation);
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