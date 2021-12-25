package net.pixlies.events.listeners.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.entity.User;
import net.pixlies.localization.Lang;
import net.pixlies.moderation.Punishment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private static final String MUTE_BROADCAST_PERMISSION = "earth.mute.broadcast";

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (player.hasPermission("pixlies.moderation.bypass.mute")) return;
        if (user.getMute() == null) return;
        Punishment mute = user.getMute();
        if (mute.isExpired()) {
            user.getCurrentPunishments().remove("mute");
            user.save();
            return;
        }
        Lang.MUTED_PLAYER_TRIED_TO_TALK.broadcastPermission(MUTE_BROADCAST_PERMISSION, "%PLAYER%;" + player.getName());
        Lang.MUTE_MESSAGE.send(player);
        event.setCancelled(true);
    }

}
