package net.pixlies.core.handlers.impl;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.entity.Player;

import java.util.*;

public class VanishHandler implements Handler {

    private static final Main instance = Main.getInstance();

    private final List<UUID> vanishedPlayers = new ArrayList<>();

    public boolean vanish(Player player) {
        if (vanishedPlayers.contains(player.getUniqueId())) return false;
        PlayerUtils.heal(player);
        player.setInvulnerable(true);
        player.setAllowFlight(true);
        for (Player target : instance.getServer().getOnlinePlayers()) {
            if (target.hasPermission("pixlies.moderation.vanish.exempt")) continue;
            target.hidePlayer(instance, player);
        }
        vanishedPlayers.add(player.getUniqueId());
        return true;
    }

    public boolean unvanish(Player player) {
        if (!vanishedPlayers.contains(player.getUniqueId())) return false;
        player.setInvulnerable(false);
        player.setAllowFlight(player.hasPermission("pixlies.fly"));
        for (Player target : instance.getServer().getOnlinePlayers()) {
            target.showPlayer(instance, player);
        }
        vanishedPlayers.remove(player.getUniqueId());
        return true;
    }

    public boolean isVanished(UUID uuid) {
        if (vanishedPlayers.isEmpty()) return false;
        return vanishedPlayers.contains(uuid);
    }

    public void setVanished(Player player, boolean state) {
        if (state) {
            vanish(player);
        } else {
            unvanish(player);
        }
    }

    public Collection<Player> getVanishedPlayers() {
        if (vanishedPlayers.isEmpty()) return Collections.emptyList();
        List<Player> players = new ArrayList<>();
        for (UUID uuid : vanishedPlayers) {
            Player player = instance.getServer().getPlayer(uuid);
            if (player == null) {
                vanishedPlayers.remove(uuid);
                continue;
            }
            players.add(player);
        }
        return players;
    }

    public Collection<UUID> getVanishedUniqueIds() {
        return vanishedPlayers;
    }

}
