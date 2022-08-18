package net.pixlies.core.listeners.player;

import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.player.PixliesSenderMessagePlayerEvent;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SocialSpyListener implements Listener {

    @EventHandler
    public void onMessage(PixliesSenderMessagePlayerEvent event) {
        CommandSender sender = event.getSender();
        Player target = event.getTarget();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            User spyingUser = User.get(p.getUniqueId());
            if (!p.hasPermission("pixlies.moderation.socialspy")) {
                continue;
            }
            if (spyingUser.isSocialSpyEnabled()) {
                Lang.PLAYER_MESSAGE_SOCIALSPY.send(p, "%PLAYER%;" + sender.getName(), "%TARGET%;" + target.getName(), "%MESSAGE%;" + event.getMessage());
            }
        }
    }

}
