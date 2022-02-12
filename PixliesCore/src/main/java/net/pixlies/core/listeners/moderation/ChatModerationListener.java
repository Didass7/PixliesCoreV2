package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatModerationListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();
    private final ChatHandler chatHandler = instance.getHandlerManager().getHandler(ChatHandler.class);

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = String.valueOf(event.message());

        // If chat is muted
        if (chatHandler.isMuted() && !player.hasPermission("pixlies.moderation.chat.exempt")) {
            Lang.CHAT_MUTED_FORMAT.send(player);
            event.setCancelled(true);
            return;
        }

        // Checks for blocked words
        if (chatHandler.isBlocked(message)) {
            Lang.PLAYER_BLOCKED_WORD_DISABLED.send(player);
            event.setCancelled(true);
        }

    }

}
