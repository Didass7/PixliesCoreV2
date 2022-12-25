package net.pixlies.lobby.managers;

import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.utils.LobbyUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyManager {

    private static final Lobby instance = Lobby.getInstance();
    private final ModuleConfig config = instance.getConfig();

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

}
