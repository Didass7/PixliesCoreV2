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

    @Private
    @Description("Set your speed to a number")
    @Subcommand("set")
    @Syntax("[speed]")
    public void onPlayer(Player player, @Conditions("intLimits:min=0,max=10") Float speed) {

        if (player.isFlying()) {
            player.setFlySpeed(speed / 10);
            Lang.STAFF_SPEED_FLY_SET.send(player, "%SPEED%;" + speed);
        } else {
            player.setWalkSpeed(speed / 10);
            Lang.STAFF_SPEED_WALK_SET.send(player, "%SPEED%;" + speed);
        }

    }

    @CommandPermission("pixlies.staff.speed.others")
    @Description("Set someone else's speed to a number")
    @Subcommand("set")
    @CommandCompletion("@players")
    @Syntax("<player> [speed]")
    public void onSender(CommandSender sender, Player target, @Conditions("intLimits:min=0,max=10") Float speed) {

        if (target.isFlying()) {
            target.setFlySpeed(speed / 10);
            Lang.STAFF_SPEED_FLY_SET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + speed.intValue());
        } else {
            target.setWalkSpeed(speed / 10);
            Lang.STAFF_SPEED_WALK_SET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + speed.intValue());
        }

    }

    @Private
    @Description("Get your own speed")
    @Subcommand("get")
    @Syntax("[speed]")
    public void onPlayer(Player player) {

            if (player.isFlying()) {
                Lang.STAFF_SPEED_FLY_GET.send(player, "%SPEED%;" + player.getFlySpeed() * 10);
            } else {
                Lang.STAFF_SPEED_WALK_GET.send(player, "%SPEED%;" + player.getWalkSpeed() * 10);
            }

    }

    @CommandPermission("pixlies.staff.speed.others")
    @Description("Get someone's speed")
    @Subcommand("get")
    @CommandCompletion("@players")
    @Syntax("<player> [speed]")
    public void onSender(CommandSender sender, Player target) {

        if (target.isFlying()) {
            Lang.STAFF_SPEED_FLY_GET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + (int) target.getFlySpeed() * 10);
        } else {
            Lang.STAFF_SPEED_WALK_GET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + (int) target.getWalkSpeed() * 10);
        }

    }

    @CommandPermission("pixlies.staff.speed.others")
    @Description("Reset someone's speed")
    @Subcommand("reset")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onReset(CommandSender sender, Player target) {
        target.setFlySpeed(0.1F);
        target.setWalkSpeed(0.2F);
        Lang.STAFF_SPEED_RESET_OTHER.send(sender);
    }

    @Private
    @Description("Reset your own speed")
    @Subcommand("reset")
    @CommandCompletion("@empty")
    public void onReset(Player player) {
        player.setFlySpeed(0.1F);
        player.setWalkSpeed(0.2F);
        Lang.STAFF_SPEED_RESET.send(player);
    }

    @HelpCommand
    public void onHelp(CommandHelp help){
        help.showHelp();
    }

}
