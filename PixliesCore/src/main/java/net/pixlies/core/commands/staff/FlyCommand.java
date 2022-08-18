package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermission("pixlies.staff.fly")
public class FlyCommand extends BaseCommand {

    @CommandAlias("fly")
    public void onFly(CommandSender sender, @Optional OnlinePlayer targetPlayer) {

        if (targetPlayer == null) {
            if (!(sender instanceof Player player)) {
                throw new ConditionFailedException(MessageKeys.NOT_ALLOWED_ON_CONSOLE);
            }

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                Lang.STAFF_FLY_DISABLED.send(sender, "%PLAYER%;" + player.getName());
                return;
            }

            player.setAllowFlight(true);
            Lang.STAFF_FLY_ENABLED.send(sender, "%PLAYER%;" + player.getName());

            return;
        }

        if (!sender.hasPermission("pixlies.staff.fly.other")) {
            throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER);
        }

        Player target = targetPlayer.getPlayer();

        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            Lang.STAFF_FLY_DISABLED.send(sender, "%PLAYER%;" + target.getName());
            return;
        }

        target.setAllowFlight(true);
        Lang.STAFF_FLY_ENABLED.send(sender, "%PLAYER%;" + target.getName());

    }

}
