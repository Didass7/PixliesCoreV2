package net.pixlies.core.listeners.player;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TeleportHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TeleportListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] args = message.split(" ");

        if (args[0].equalsIgnoreCase("/tp") || args[0].equalsIgnoreCase("/teleport")) {
            tpHandler.setBackLocation(player.getUniqueId(), player.getLocation());
        }
    }

}
