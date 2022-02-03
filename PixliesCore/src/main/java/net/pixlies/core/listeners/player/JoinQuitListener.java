package net.pixlies.core.listeners.player;

import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final Main instance = Main.getInstance();
    private final Config config = Main.getInstance().getConfig();
    private final VanishHandler handler = instance.getHandlerManager().getHandler(VanishHandler.class);

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isVanished()) {
            event.joinMessage(null);
            return;
        }
        event.joinMessage(Component.text(config.getStringFormatted("joinQuit.joinMessage")
                .replace("%PLAYER%", player.getName())));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isVanished()) {
            event.quitMessage(null);
            return;
        }
        event.quitMessage(Component.text(config.getStringFormatted("joinQuit.quitMessage")
                .replace("%PLAYER%", player.getName())));
    }

}
