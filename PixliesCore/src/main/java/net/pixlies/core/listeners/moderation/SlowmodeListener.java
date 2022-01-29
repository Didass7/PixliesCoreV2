package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.SlowmodeHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class SlowmodeListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final SlowmodeHandler slowmodeHandler = instance.getHandlerManager().getHandler(SlowmodeHandler.class);

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        long timePassed = System.currentTimeMillis() / 1000 - slowmodeHandler.getCooldown(uuid);
        if (timePassed > slowmodeHandler.getSlowmode() || player.hasPermission("pixlies.moderation.slowmode.bypass")) {
            slowmodeHandler.setCooldown(uuid, System.currentTimeMillis() / 1000);
        } else {
            event.setCancelled(true);
            Lang.PLAYER_MESSAGED_ON_SLOWMODE.send(player, "%SECONDS%;" +
                    (slowmodeHandler.getSlowmode() - timePassed));
        }
    }
}
