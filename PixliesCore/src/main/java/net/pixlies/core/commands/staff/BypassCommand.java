package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

/**
 * Nation bypass command
 * @author vyketype
 * @author Dynmie
 */
@CommandAlias("bypass|b")
@CommandPermission("pixlies.moderation.bypass")
public class BypassCommand extends BaseCommand {

    @Default
    @Description("Toggle staff bypass")
    @CommandCompletion("@empty")
    public void onBypass(Player player, Player target) {

        if (player.getUniqueId().equals(target.getUniqueId())) {
            User user = User.get(player.getUniqueId());
            if (user.isBypassing()) {
                user.setBypassing(false);
                user.save();
                Lang.STAFF_BYPASS_OFF.send(player);
            } else {
                user.setBypassing(true);
                user.save();
                Lang.STAFF_BYPASS_ON.send(player);
            }
            return;
        }

        if (player.hasPermission("pixlies.moderation.bypass.other")) {
            User user = User.get(target.getUniqueId());
            if (user.isBypassing()) {
                user.setBypassing(false);
                user.save();
                Lang.STAFF_BYPASS_OFF.send(target); // To alert the player that their bypass is now off
                Lang.STAFF_BYPASS_OFF_OTHER.send(player, "%PLAYER%;" + target.getName());
            } else {
                user.setBypassing(true);
                user.save();
                Lang.STAFF_BYPASS_ON.send(target); // To alert the player that their bypass is now on
                Lang.STAFF_BYPASS_ON_OTHER.send(player, "%PLAYER%;" + target);
            }
            return;
        }

        throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED);
    }

    @HelpCommand
    public void onHelp(CommandHelp help){
        help.showHelp();
    }

}
