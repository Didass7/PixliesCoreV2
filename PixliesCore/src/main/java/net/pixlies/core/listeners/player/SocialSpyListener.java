package net.pixlies.core.listeners.player;

import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.player.SenderMessagePlayerEvent;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SocialSpyListener implements Listener {

    @EventHandler
    public void onMessage(SenderMessagePlayerEvent event) {
        CommandSender sender = event.getSender();
        Player target = event.getTarget();

        // Staff setting: socialspy
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            User msgUser = User.get(p.getUniqueId());
            if (msgUser.isSocialSpyEnabled()) {
                Lang.PLAYER_MESSAGE_SOCIALSPY.send(p, "%PLAYER%;" + sender.getName(), "%TARGET%;" + target.getName());
            }
        }
    }

}
