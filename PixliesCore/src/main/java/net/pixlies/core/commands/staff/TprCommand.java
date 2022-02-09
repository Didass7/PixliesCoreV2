package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Private;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Teleport between players randomly.
 * @author dynmie
 */
@CommandAlias("tprandom|tpr")
@CommandPermission("pixlies.staff.tprandom")
public class TprCommand extends BaseCommand {

    @Default
    @Private
    public void onRandomTeleport(Player player) {
        Player target = PlayerUtils.getRandomPlayer(player);
        if (target == null) {
            Lang.PLAYER_TELEPORT_NO_PLAYERS.send(player);
            return;
        }
        player.teleport(target);
        Lang.STAFF_RANDOM_TELEPORT.send(player, "%TARGET%;" + target.getName());
    }

    @CommandPermission("pixlies.staff.tprandom.others")
    public void onRandomTeleport(CommandSender sender, Player player) {
        Player target = PlayerUtils.getRandomPlayer(player);
        if (target == null) {
            Lang.PLAYER_TELEPORT_NO_PLAYERS.send(sender);
            return;
        }
        player.teleport(target);
        Lang.STAFF_RANDOM_TELEPORT_OTHER.send(player, "%PLAYER%;" + player.getName() ,"%TARGET%;" + target.getName());
    }

}
