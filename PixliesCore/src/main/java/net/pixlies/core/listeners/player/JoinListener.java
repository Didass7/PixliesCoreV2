package net.pixlies.core.listeners.player;

import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Config config = Main.getInstance().getConfig();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.joinMessage(Component.text(config.getStringFormatted("joinQuit.joinMessage")
                .replace("%PLAYER%", player.getName())));
    }

}
