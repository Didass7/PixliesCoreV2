package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.moderation.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MuteListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (user.getMute() != null && !player.hasPermission("pixlies.moderation.mute.exempt")) {
            Punishment mute = user.getMute();
            if (mute.isExpired()) {
                user.getCurrentPunishments().remove("mute");
                user.save();
                return;
            }

            // Staff setting: view muted chat
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                User msgUser = User.get(p.getUniqueId());
                if (msgUser.isViewMutedChat()) {
                    Lang.MUTED_PLAYER_TRIED_TO_TALK.send(p, "%PLAYER%;", player.getName());
                }
            }

            Lang.MUTE_MESSAGE.send(player);
            event.setCancelled(true);
        }
    }

}
