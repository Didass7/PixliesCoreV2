package net.pixlies.lobby.listeners.impl;

import net.pixlies.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private static final Lobby instance = Lobby.getInstance();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        instance.getLobbyManager().removeBuildModePlayer(player);
        instance.getJumpPadManager().removeDelay(player.getUniqueId());
    }

}
