package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SlowModeListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final ChatHandler chatHandler = instance.getHandlerManager().getHandler(ChatHandler.class);

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        if (chatHandler.getSlowMode() == 0) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!chatHandler.isPlayerOnCooldown(uuid)) return;
        Lang.SLOWMODE_MESSAGE.send(player);
        event.setCancelled(true);

    }
}
