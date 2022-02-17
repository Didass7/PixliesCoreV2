package net.pixlies.nations.nations;

import lombok.Getter;
import net.pixlies.nations.Nations;

import java.util.HashMap;
import java.util.Map;

public class NationManager {

    private static final Nations instance = Nations.getInstance();

    @Getter private final Map<String, Nation> nations = new HashMap<>(); // ID, Nation
    @Getter private final Map<String, String> nameNations = new HashMap<>(); // Name, ID

    public NationManager() {
        loadAll();
    }

    public void backupAll() {
        for (Nation nation : nations.values()) {
            nation.backup();
        }
    }

    public void loadAll() {
        for (Nation nation : instance.getMongoManager().getDatastore().find(Nation.class).iterator().toList()) {
            if (nation.getNationsId() != null) {
                nations.put(nation.getNationsId(), nation);
                nameNations.put(nation.getName(), nation.getNationsId());
            }
        }
    }

}