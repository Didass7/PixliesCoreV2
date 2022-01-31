package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.staff.randomteleport")
public class RandomTeleportCommand extends BaseCommand {

    @Default
    @Private
    @CommandAlias("randomteleport|srt")
    public void onRandomTeleport(Player player) {
        Player target = PlayerUtils.getRandomPlayer(player);
        if (target == null) {
            Lang.PLAYER_DOESNT_EXIST.send(player);
            return;
        }
        player.teleport(target);
    }

    @CommandAlias("randomteleport|srt")
    @CommandPermission("pixlies.staff.randomteleport.others")
    public void onRandomTeleport(CommandSender sender, Player player) {
        Player target = PlayerUtils.getRandomPlayer(player);
        if (target == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }
        player.teleport(target);
    }

}
