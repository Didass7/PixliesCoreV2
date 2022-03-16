package net.pixlies.core.handlers.impl;

import lombok.Getter;
import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.Handler;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;

import java.util.*;

public class PassiveHandler implements Handler {

    private static final Main instance = Main.getInstance();

    private final @Getter Map<UUID, Container> silentChestPlayers = new HashMap<>();

    public void passive(Player player) {

        val user = User.get(player.getUniqueId());

        // SET
        player.setInvulnerable(true);
        player.setSleepingIgnored(true);
        player.setAllowFlight(true);
        player.setCollidable(false);

        // SET PASSIVE
        user.getSettings().setPassive(true);
        user.save();

    }

    public void unpassive(Player player) {

        val user = User.get(player.getUniqueId());

        // SET
        player.setInvulnerable(false);
        player.setSleepingIgnored(false);
        player.setAllowFlight(player.hasPermission("pixlies.fly"));
        player.setCollidable(true);

        // SET PASSIVE
        user.getSettings().setPassive(false);
        user.save();

    }

    public void setPassive(Player player, boolean state) {
        if (state) {
            passive(player);
        } else {
            unpassive(player);
        }
    }

}
