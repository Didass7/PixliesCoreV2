package net.pixlies.core.listeners.cosmetics;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.pixlies.core.utils.Emojis;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatEmojiListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        event.message(Component.text(Emojis.replaceEmojis(event.message().toString())));
    }

}
