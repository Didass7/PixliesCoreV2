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

import java.util.List;

public class ChatModerationListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final Config config = instance.getConfig();
    private final ChatHandler chatHandler = instance.getHandlerManager().getHandler(ChatHandler.class);

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = String.valueOf(event.message());

        // If chat is muted
        if (chatHandler.isMuted() && !player.hasPermission("pixlies.moderation.bypass.mutechat")) {
            Lang.CHAT_MUTED.send(player);
            event.setCancelled(true);
            return;
        }

        // If swear filter is enabled
        if (!chatHandler.isSwearFilterEnabled() && !player.hasPermission("pixlies.moderation.bypass.swearfilter")) {
            List<String> swearWords = config.getStringList("swearWords");
            for (String s : swearWords) {
                if (message.toLowerCase().contains(s)) {
                    Lang.PLAYER_TRIED_TO_SWEAR.send(player);
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // Checks for blocked words
        List<String> blockedWords = instance.getConfig().getStringList("blockedWords");
        for (String s : blockedWords) {
            if (message.contains(s) && !player.hasPermission("pixlies.moderation.bypass.blockedwords")) {
                Lang.PLAYER_TRIED_BLOCKED_WORD.send(player);
                event.setCancelled(true);
                return;
            }
        }
    }

}
