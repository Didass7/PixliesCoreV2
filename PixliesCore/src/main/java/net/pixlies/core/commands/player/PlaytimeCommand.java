package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * simple playtime command
 * @author Dynmie
 */
public class PlaytimeCommand extends BaseCommand {

    @Default
    @CommandAlias("playtime|pt")
    public void onPlaytime(Player player) {
        User user = User.get(player.getUniqueId());
        String playtime = new PrettyTime().format(new Date(user.getPlaytime()));
        Lang.PLAYER_PLAYTIME.send(player, "%TIME%;" + playtime);
    }

    @CommandPermission("pixlies.player.playtime.others")
    @CommandCompletion("@players")
    public void onPlaytimeTarget(CommandSender sender, Player target) {
        User user = User.get(target.getUniqueId());
        String playtime = new PrettyTime().format(new Date(user.getPlaytime()));
        Lang.PLAYER_PLAYTIME_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%TIME%;" + playtime);
    }

}
