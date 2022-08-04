package net.pixlies.nations.nations;

import lombok.Getter;
import net.pixlies.nations.Nations;

import java.util.HashMap;
import java.util.Map;

public class NationManager {

    private static final Nations instance = Nations.getInstance();

    private final @Getter Map<String, Nation> nations = new HashMap<>(); // ID, Nation

    public void backupAll() {
        for (Nation nation : nations.values()) {
            nation.backup();
        }
    }

    public void loadAll() {
        for (Nation nation : instance.getMongoManager().getDatastore().find(Nation.class).iterator().toList()) {
            if (nation.getNationId() != null) {
                nations.put(nation.getNationId(), nation);
            }
        }
    }

}