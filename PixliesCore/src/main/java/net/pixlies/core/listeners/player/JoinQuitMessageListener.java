package net.pixlies.core.listeners.player;

import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessageListener implements Listener {

    private final Config config = Main.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(Component.text(config.getStringFormatted("joinQuit.joinMessage")
                .replace("%PLAYER%", player.getName())));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.quitMessage(Component.text(config.getStringFormatted("joinQuit.quitMessage")
                .replace("%PLAYER%", player.getName())));
    }

}
