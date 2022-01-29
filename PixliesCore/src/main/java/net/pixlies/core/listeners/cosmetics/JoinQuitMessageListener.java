package net.pixlies.core.listeners.cosmetics;

import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessageListener implements Listener {

    private final Config config = Main.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text(config.getStringFormatted("joinQuit.joinMessage")));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text(config.getStringFormatted("joinQuit.quitMessage")));
    }

}
