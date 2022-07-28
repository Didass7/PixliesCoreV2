package net.pixlies.lobby.managers;

import net.pixlies.core.entity.Warp;
import net.pixlies.core.modules.ModuleConfig;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.utils.LobbyUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyManager {

    private static final Lobby instance = Lobby.getInstance();
    private final ModuleConfig config = instance.getConfig();

    private Warp pvpWarp = Warp.get(config.getString("warps.pvp.name", "pvp"));
    private final List<UUID> rightClickTimedPlayers = new ArrayList<>();

    private final List<UUID> buildModePlayers = new ArrayList<>();

    public boolean isInBuildMode(UUID uuid) {
        if (buildModePlayers.isEmpty()) return false;
        return buildModePlayers.contains(uuid);
    }

    public void addBuildModePlayer(Player player) {
        UUID uuid = player.getUniqueId();

        player.getInventory().clear();

        if (!buildModePlayers.isEmpty())
            buildModePlayers.remove(uuid);
        buildModePlayers.add(uuid);
    }

    public void removeBuildModePlayer(Player player) {
        UUID uuid = player.getUniqueId();

        LobbyUtils.resetPlayer(player);

        if (buildModePlayers.isEmpty()) return;
        buildModePlayers.remove(uuid);
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
