package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
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

    @Description("Set speed to a number")
    @Subcommand("set")
    @CommandCompletion("@range:0-10")
    @Syntax("[speed] <player>")
    public void onSender(CommandSender sender, @Conditions("limits:min=0,max=10") Integer s, Player target) {

        float speed = s.floatValue();

        if (sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId())) {
            if (player.isFlying()) {
                player.setFlySpeed(speed / 10);
                Lang.STAFF_SPEED_FLY_SET.send(player, "%SPEED%;" + speed);
            } else {
                player.setWalkSpeed(speed / 10);
                Lang.STAFF_SPEED_WALK_SET.send(player, "%SPEED%;" + speed);
            }
            return;
        }

        if (sender.hasPermission("pixlies.staff.speed.others")) {
            if (target.isFlying()) {
                target.setFlySpeed(speed / 10);
                Lang.STAFF_SPEED_FLY_SET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + (int) speed);
            } else {
                target.setWalkSpeed(speed / 10);
                Lang.STAFF_SPEED_WALK_SET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + (int) speed);
            }
            return;
        }

        throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED);

    }

    @Description("Get speed")
    @Subcommand("get")
    @CommandCompletion("@players")
    @Syntax("<player> [speed]")
    public void onSender(CommandSender sender, Player target) {

        if (sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId())) {
            if (player.isFlying()) {
                Lang.STAFF_SPEED_FLY_GET.send(player, "%SPEED%;" + player.getFlySpeed() * 10);
            } else {
                Lang.STAFF_SPEED_WALK_GET.send(player, "%SPEED%;" + player.getWalkSpeed() * 10);
            }
            return;
        }

        if (sender.hasPermission("pixlies.staff.speed.others")) {
            if (target.isFlying()) {
                Lang.STAFF_SPEED_FLY_GET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + (int) target.getFlySpeed() * 10);
            } else {
                Lang.STAFF_SPEED_WALK_GET_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SPEED%;" + (int) target.getWalkSpeed() * 10);
            }
            return;
        }

        throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED);

    }
    @Description("Reset your own speed")
    @Subcommand("reset")
    @CommandCompletion("@empty")
    public void onReset(CommandSender sender, Player target) {

        if (sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId())) {
            player.setFlySpeed(0.1F);
            player.setWalkSpeed(0.2F);
            Lang.STAFF_SPEED_RESET.send(player);
            return;
        }

        if (sender.hasPermission("pixlies.staff.speed.others")) {
            target.setFlySpeed(0.1F);
            target.setWalkSpeed(0.2F);
            Lang.STAFF_SPEED_RESET_OTHER.send(sender, "%PLAYER%;" + target.getName());
            return;
        }

        throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED);
    }

    @HelpCommand
    public void onHelp(CommandHelp help){
        help.showHelp();
    }

}
