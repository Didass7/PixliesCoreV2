package net.pixlies.core.listeners.player;

import net.pixlies.core.entity.User;
import net.pixlies.core.events.impl.player.SenderMessagePlayerEvent;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MessageListener implements Listener {

    @EventHandler
    public void onMessage(SenderMessagePlayerEvent event) {

        CommandSender sender = event.getSender();
        Player target = event.getTarget();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            User user = User.get(player.getUniqueId());
            // TODO: check if USER is social spy
            //if (!user.isSocialSpy()) continue;
            Lang.PLAYER_MESSAGE_SOCIALSPY.send(player, "%PLAYER%;" + sender.getName(), "%TARGET%;" + target.getName());
        }

    }

}
