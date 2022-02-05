package net.pixlies.lobby.listeners.impl;

import net.pixlies.lobby.Lobby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private static final Lobby instance = Lobby.getInstance();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        instance.getLobbyManager().removeBuildModePlayer(event.getPlayer().getUniqueId());
    }

}
