package net.pixlies.lobby.listeners.impl;

import net.pixlies.core.entity.user.User;
import net.pixlies.lobby.utils.LobbyUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (!user.getSettings().isInStaffMode()) {
            LobbyUtils.resetPlayer(player);
        }

    }

}
