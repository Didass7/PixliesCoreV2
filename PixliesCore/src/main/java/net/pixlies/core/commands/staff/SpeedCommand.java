package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * cool speed command
 * @author Dynmie
 */
@CommandAlias("speed|flyspeed|walkspeed")
@CommandPermission("pixlies.staff.speed")
public class SpeedCommand extends BaseCommand {

    @Default
    @Description("Set your speed to a number")
    @Syntax("[speed]")
    public void onPlayer(Player player, @Optional @Conditions("intLimits:min=0,max=10") Integer speed) {

        if (speed == null) {
            if (player.isFlying()) {
                Lang.STAFF_SPEED_FLY_GET.send(player, "%SPEED%;" + player.getFlySpeed());
            } else {
                Lang.STAFF_SPEED_WALK_GET.send(player, "%SPEED%;" + player.getWalkSpeed());
            }
            return;
        }

        if (player.isFlying()) {
            player.setFlySpeed(speed);
            Lang.STAFF_SPEED_FLY_SET.send(player, "%SPEED%;" + speed);
        } else {
            player.setWalkSpeed(speed);
            Lang.STAFF_SPEED_WALK_SET.send(player, "%SPEED%;" + speed);
        }

    }

    @CommandPermission("pixlies.staff.speed.others")
    @Description("Set someone else's speed to a number")
    @CommandCompletion("@players")
    @Syntax("<player> [speed]")
    public void onSender(CommandSender sender, Player target, @Optional @Conditions("intLimits:min=0,max=10") Integer speed) {
        if (speed == null) {
            if (target.isFlying()) {
                Lang.STAFF_SPEED_FLY_GET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + target.getFlySpeed());
            } else {
                Lang.STAFF_SPEED_WALK_GET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + target.getWalkSpeed());
            }
            return;
        }

        if (target.isFlying()) {
            target.setFlySpeed(speed);
            Lang.STAFF_SPEED_FLY_SET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + speed);
        } else {
            target.setWalkSpeed(speed);
            Lang.STAFF_SPEED_WALK_SET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + speed);
        }

    }

    @HelpCommand
    public void onHelp (CommandHelp help){
        help.showHelp();
    }

}
