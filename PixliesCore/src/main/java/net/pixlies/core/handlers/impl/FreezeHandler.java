package net.pixlies.core.handlers.impl;

import net.pixlies.core.handlers.Handler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * A handler to manage frozen players.
 * @author Dynmie
 */
public class FreezeHandler implements Handler {

    /**
     * List of all frozen players, stored as UUID.
     */
    private final List<UUID> frozenPlayers = new ArrayList<>();

    /**
     * Freeze a player
     * @param player an online, notnull player.
     * @return true if it was successful, false if the player is frozen already.
     */
    public boolean freeze(@NotNull Player player) {
        if (frozenPlayers.contains(player.getUniqueId())) return false;
        frozenPlayers.add(player.getUniqueId());
        return true;
    }

    /**
     * Unfreeze a player
     * @param player an online, notnull player.
     * @return true if it was successful, false if the player is not frozen.
     */
    public boolean unfreeze(@NotNull Player player) {
        if (!frozenPlayers.contains(player.getUniqueId())) return false;
        frozenPlayers.remove(player.getUniqueId());
        return true;
    }

    /**
     * Get a collection of all frozen players.
     * @return collection of all frozen players.
     */
    public @NotNull Collection<Player> getFrozenPlayers() {
        List<Player> players = new ArrayList<>();
        for (UUID uuid : frozenPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                frozenPlayers.remove(uuid);
                continue;
            }
            players.add(player);
        }
        return players;
    }

    /**
     * Check if a player is frozen.
     * @return true if the player is frozen
     */
    public boolean isFrozen(UUID uuid) {
        return frozenPlayers.contains(uuid);
    }

}
