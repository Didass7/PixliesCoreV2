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
@CommandAlias("bypass|b|nationbypass")
@CommandPermission("pixlies.moderation.bypass")
public class BypassCommand extends BaseCommand {

    @Default
    @Description("Toggle your nation commands bypass status")
    public void onStaffMode(Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isBypassing()) {
            Lang.STAFF_BYPASS_OFF.send(player);
        } else {
            Lang.STAFF_BYPASS_ON.send(player);
        }
    }

    @Private
    @Description("Toggle another player's nation commands bypass status")
    public void onStaffMode(CommandSender sender, Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isBypassing()) {
            Lang.STAFF_BYPASS_OFF_OTHER.send(sender, "%PLAYER%;" + player.getName());
            Lang.STAFF_BYPASS_ON.send(player); // To alert the player that their bypass is now on
        } else {
            Lang.STAFF_BYPASS_ON_OTHER.send(sender, "%PLAYER%;" + player.getName());
            Lang.STAFF_BYPASS_OFF.send(player); // To alert the player that their bypass is now off
        }
    }

}
