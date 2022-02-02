package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("randomteleport|rtp")
@CommandPermission("pixlies.staff.randomteleport")
public class RandomTeleportCommand extends BaseCommand {

    @Default
    @Private
    public void onRandomTeleport(Player player) {
        Player target = PlayerUtils.getRandomPlayer(player);
        if (target == null) {
            Lang.PLAYER_DOESNT_EXIST.send(player);
            return;
        }
        player.teleport(target);
        Lang.STAFF_RANDOM_TELEPORT.send(player, "%TARGET%;" + target.getName());
    }

    @CommandPermission("pixlies.staff.randomteleport.others")
    public void onRandomTeleport(CommandSender sender, Player player) {
        Player target = PlayerUtils.getRandomPlayer(player);
        if (target == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }
        player.teleport(target);
        Lang.STAFF_RANDOM_TELEPORT_OTHER.send(player, "%PLAYER%;" + player.getName() ,"%TARGET%;" + target.getName());
    }

}
