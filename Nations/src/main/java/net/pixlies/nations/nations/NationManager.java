package net.pixlies.nations.nations;

import lombok.Getter;
import net.pixlies.nations.Nations;

import java.util.ArrayList;
import java.util.List;

public class NationManager {

    private static final Nations instance = Nations.getInstance();

    @Getter private final List<Nation> nations = new ArrayList<>();

    public NationManager() {
        loadAll();
    }

    /**
     * Get a nation
     * @param name the name of a nation
     * @return the nation if found
     */
    public Nation getNation(String name) {
        for (Nation nation : nations) {
            if (nation.getNationsId().equals(name)) {
                return nation;
            }
            if (nation.getName().equalsIgnoreCase(name)) {
                return nation;
            }
        }
        return null;
    }

    public void backupAll() {
        for (Nation nation : nations) {
            nation.backup();
        }
    }

    public void loadAll() {
        nations.addAll(instance.getMongoManager().getDatastore().find(Nation.class).iterator().toList());
    }

}