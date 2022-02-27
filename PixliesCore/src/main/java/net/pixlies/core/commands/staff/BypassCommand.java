package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Nation bypass command
 * @author vPrototype_
 * @author Dynmie
 */
@CommandAlias("bypass|b")
@CommandPermission("pixlies.moderation.bypass")
public class BypassCommand extends BaseCommand {

    @Default
    @Description("Toggle your staff bypass status")
    @Subcommand("toggle")
    public void onBypass(Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isBypassing()) {
            user.getSettings().setBypassing(false);
            user.save();
            Lang.STAFF_BYPASS_OFF.send(player);
        } else {
            user.getSettings().setBypassing(true);
            user.save();
            Lang.STAFF_BYPASS_ON.send(player);
        }
    }

    @CommandPermission("pixlies.moderation.bypass.other")
    @Description("Toggle another player's staff bypass bypass status")
    @Subcommand("other")
    public void onBypass(CommandSender sender, Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isBypassing()) {
            user.getSettings().setBypassing(false);
            user.save();
            Lang.STAFF_BYPASS_OFF.send(player); // To alert the player that their bypass is now off
            Lang.STAFF_BYPASS_OFF_OTHER.send(sender, "%PLAYER%;" + player.getName());
        } else {
            user.getSettings().setBypassing(true);
            user.save();
            Lang.STAFF_BYPASS_ON.send(player); // To alert the player that their bypass is now on
            Lang.STAFF_BYPASS_ON_OTHER.send(sender, "%PLAYER%;" + player.getName());
        }
    }

}
