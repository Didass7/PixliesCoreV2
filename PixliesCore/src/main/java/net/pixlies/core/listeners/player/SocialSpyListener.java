package net.pixlies.core.listeners.player;

import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.impl.player.PixliesSenderMessagePlayerEvent;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.ranks.Rank;
import net.pixlies.core.utils.RankUtils;
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

                Rank senderRank = RankUtils.getRankFromSender(sender);
                Rank targetRank = Rank.getRank(target.getUniqueId());

                Lang.PLAYER_MESSAGE_SOCIALSPY.send(p, "%PLAYER%;" + senderRank.getColor() + sender.getName(), "%TARGET%;" + targetRank.getColor() + target.getName(), "%MESSAGE%;" + event.getMessage());
            }
        }
    }

}
