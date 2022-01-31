package net.pixlies.core.handlers.impl;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VanishHandler implements Handler {

    // TODO
    private static final Main instance = Main.getInstance();

    private final List<UUID> vanishedPlayers = new ArrayList<>();

    public boolean vanish(Player player) {
        if (vanishedPlayers.contains(player.getUniqueId())) return false;
        PlayerUtils.heal(player);
        for (Player target : instance.getServer().getOnlinePlayers()) {
            if (target.hasPermission("pixlies.moderation.vanish.exempt")) continue;
            target.hidePlayer(instance, player);
        }
        vanishedPlayers.add(player.getUniqueId());
        return true;
    }

    public boolean unvanish(Player player) {
        if (!vanishedPlayers.contains(player.getUniqueId())) return false;
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

}
