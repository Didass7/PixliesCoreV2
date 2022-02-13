package net.pixlies.lobby.managers;

import net.pixlies.core.entity.Warp;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyManager {

    private static final Lobby instance = Lobby.getInstance();
    private final Config config = instance.getConfig();

    private final Location spawnLocation = instance.getConfig().getLocation("locations.spawn");
    private Warp pvpWarp = Warp.get(config.getString("warps.pvp.name", "pvp"));

    private final List<UUID> buildModePlayers = new ArrayList<>();

    public boolean isInBuildMode(UUID uuid) {
        if (buildModePlayers.isEmpty()) return false;
        return buildModePlayers.contains(uuid);
    }

    public void addBuildModePlayer(UUID uuid) {
        if (!buildModePlayers.isEmpty())
            buildModePlayers.remove(uuid);
        buildModePlayers.add(uuid);
    }

    public void removeBuildModePlayer(UUID uuid) {
        if (buildModePlayers.isEmpty()) return;
        buildModePlayers.remove(uuid);
    }

    public Location getSpawnLocation() {
        if (spawnLocation == null)
            return new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        config.set("locations.spawn", location);
        config.save();
    }

    public Warp getPvpWarp() {
        return pvpWarp;
    }

    public void setPvpWarp(Location location) {
        pvpWarp = new Warp(
                config.getString("warps.pvp.name", "pvp"),
                config.getString("warps.pvp.description", "No description."),
                Material.valueOf(config.getString("warps.pvp.material", "DIAMOND_SWORD")),
                location
        );
        pvpWarp.save();
    }

}
