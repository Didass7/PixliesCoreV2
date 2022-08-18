package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SlowModeListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final ChatHandler chatHandler = instance.getHandlerManager().getHandler(ChatHandler.class);

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {

        if (chatHandler.getSlowModeDelay() == 0) return;

        Player player = event.getPlayer();

        if (!chatHandler.isSlowed(player.getUniqueId())) {
            chatHandler.setSlowMode(player.getUniqueId(), true);
        } else {
            long lastTalkTime = chatHandler.getLastChat(player.getUniqueId());
            long delay = chatHandler.getSlowModeDelay();
            long currentTime = System.currentTimeMillis();

            int remaining = (int) ((lastTalkTime + delay - currentTime) / 1000L) + 1;

            Lang.SLOWMODE_MESSAGE.send(player, "%VALUE%;" + remaining); // to seconds
            event.setCancelled(true);
        }


    }
}
