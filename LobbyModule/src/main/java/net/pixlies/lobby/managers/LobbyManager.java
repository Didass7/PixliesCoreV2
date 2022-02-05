package net.pixlies.lobby.managers;

import lombok.Getter;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyManager {

    private static final Lobby instance = Lobby.getInstance();
    private final Config config = instance.getConfig();

    private final Location spawnLocation = instance.getConfig().getLocation("locations.spawn");
    private final Location pvpLocation = instance.getConfig().getLocation("locations.pvp");

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

    /* TODO: Warps on the core to replace this
    public Location getPvpLocation() {
        if (pvpLocation == null)
            return new Location(Bukkit.getWorld("world"), 0, 0, 0, 0, 0);
        return pvpLocation;
    }

    public void setPvpLocation(Location location) {
        config.set("locations.pvp", location);
        config.save();
    }
     */

}
