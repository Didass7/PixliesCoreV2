package net.pixlies.core.handlers.impl;

import lombok.val;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.Handler;
import org.bukkit.entity.Player;

import java.util.*;

public class PassiveHandler implements Handler {

    private static final Main instance = Main.getInstance();

    public void passive(Player player) {
        val user = User.get(player.getUniqueId());
        player.setInvulnerable(true);
        player.setSleepingIgnored(true);
        player.setAllowFlight(true);
        user.getSettings().setPassive(true);
    }

    public void unpassive(Player player) {
        val user = User.get(player.getUniqueId());
        player.setInvulnerable(false);
        player.setSleepingIgnored(false);
        player.setAllowFlight(player.hasPermission("pixlies.fly"));
        user.getSettings().setVanished(false);
    }

    public void setPassive(Player player, boolean state) {
        if (state) {
            passive(player);
        } else {
            unpassive(player);
        }
    }

}
