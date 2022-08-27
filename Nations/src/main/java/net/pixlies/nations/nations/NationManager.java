package net.pixlies.nations.nations;

import lombok.Getter;
import net.pixlies.nations.Nations;
import net.pixlies.nations.utils.NationUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;

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

    public World getActiveWorld() {
        return Bukkit.getWorld(instance.getConfig().getString("nations.world", "world"));
    }

    /**
     * Refreshes all nations saved in memory with new data.
     * Not async
     */
    public void refreshNations() {

        // Clear old nations in case of a (unrecommended) reload
        if (!nations.isEmpty()) {
            nations.clear();
        }

        // Load nations from database
        for (Document document : instance.getMongoManager().getNationsCollection().find()) {
            try {
                Nation nation = Nation.getNewNationFromDocument(document);
                nations.put(nation.getNationId(), nation);
            } catch (Exception e) {
                e.printStackTrace();
                instance.getLogger().warning("Failed to load a nation.");
            }
        }

        Nation warzone = Nation.getFromId("warzone");
        if (warzone == null) {
            warzone = Nation.createSystemNation("warzone"); // TODO: COLOR, DESC
            warzone.cache();
            warzone.backup();
        }

        Nation spawn = Nation.getFromId("spawn");
        if (spawn == null) {
            spawn = Nation.createSystemNation("spawn"); // TODO: COLOR, DESC
            warzone.cache();
            spawn.backup();
        }

        Nation warp = Nation.getFromId("warp");
        if (warp == null) {
            warp = Nation.createSystemNation("warp"); // TODO: COLOR, DESC
            warzone.cache();
            warp.backup();
        }

    }

}