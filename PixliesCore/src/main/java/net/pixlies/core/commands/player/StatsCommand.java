package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("stats")
public class StatsCommand extends BaseCommand {

    @Default
    @Syntax("[player]")
    public void onStats(CommandSender sender, OfflinePlayer player) {
        User requested = User.get(player.getUniqueId());

        ChatColor scoreColor = ChatColor.YELLOW;
        if (requested.getStats().getCivilPoints() > 50)
            scoreColor = ChatColor.GREEN;
        if (requested.getStats().getCivilPoints() < -50)
            scoreColor = ChatColor.RED;

        Lang.PLAYER_STATS.send(sender, "%PLAYER%;" + player.getName(),
                "%HOUSE%;" + requested.getStats().getHouse().getIcon(),
                "%BALANCE%;" + requested.getServerCurrency().getFormattedBalance(),
                "%SCORE%;" + scoreColor + requested.getStats().getCivilPoints(),
                "%JOINED%;" + requested.getStats().getDateJoined());
    }

}
