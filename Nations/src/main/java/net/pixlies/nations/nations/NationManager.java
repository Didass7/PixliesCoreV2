package net.pixlies.nations.nations;

import com.google.common.collect.Table;
import lombok.Getter;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.chunk.NationChunk;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class NationManager {

    private static final Nations instance = Nations.getInstance();

    private final @Getter Map<String, Nation> nations = new HashMap<>(); // ID, Nation
    private final @Getter Map<String, String> nationNames = new HashMap<>();

    private final @Getter Map<String, Table<Integer, Integer, NationChunk>> nationClaims = new HashMap<>();

    public void backupAll() {
        for (Nation nation : nations.values()) {
            nation.backup();
        }
    }

    public World getActiveWorld() {
        return Bukkit.getWorld(instance.getConfig().getString("nations.world", "world"));
    }

    /**
     * Refreshes all nations saved in memory with new data.
     * Not async
     */
    public void refreshNations() {

        // Clear old nations in case of a (unrecommended) reload
        nationClaims.clear();
        nations.clear();

        // Load nations from database
        for (Document document : instance.getMongoManager().getNationsCollection().find()) {
            try {
                Nation nation = Nation.getNewNationFromDocument(document);
                nation.cache();
                nation.loadClaims();
            } catch (Exception e) {
                e.printStackTrace();
                instance.getLogger().warning("Failed to load a nation.");
            }
        }

        Nation warzone = Nation.getFromId("warzone");
        if (warzone == null) {
            warzone = Nation.createSystemNation("warzone", "Warzone");
            warzone.cache();
            warzone.backup();
        }

        Nation spawn = Nation.getFromId("spawn");
        if (spawn == null) {
            spawn = Nation.createSystemNation("spawn", "Spawn");
            spawn.cache();
            spawn.backup();
        }

        Nation warp = Nation.getFromId("warp");
        if (warp == null) {
            warp = Nation.createSystemNation("warp", "Warp");
            warp.cache();
            warp.backup();
        }

    }

}