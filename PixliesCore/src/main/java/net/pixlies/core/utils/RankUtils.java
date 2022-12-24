package net.pixlies.core.utils;

import net.pixlies.core.ranks.Rank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankUtils {

    public static Rank getRankFromSender(CommandSender sender) {
        return sender instanceof Player player ? Rank.getRank(player.getUniqueId()) : Rank.DEFAULT;
    }

}
