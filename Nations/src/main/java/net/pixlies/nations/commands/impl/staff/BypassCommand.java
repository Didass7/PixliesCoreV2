package net.pixlies.nations.commands.impl.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Nation bypass command
 * @author vPrototype__
 * @author Dynmie
 */
@CommandAlias("bypass|b|nationbypass")
@CommandPermission("pixlies.moderation.bypass")
public class BypassCommand extends BaseCommand {

    @Default
    @Description("Toggle your nation bypass status")
    public void onStaffMode(Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getSettings().hasNationBypass()) {
            Lang.STAFF_BYPASS_OFF.send(player);
        } else {
            Lang.STAFF_BYPASS_ON.send(player);
        }
    }

    @Private
    @Description("Toggle another player's nation bypass status")
    public void onStaffMode(CommandSender sender, Player player) {
        User user = User.get(player.getUniqueId());
        if (user.getSettings().hasNationBypass()) {
            Lang.STAFF_BYPASS_OFF_OTHER.send(sender, "%PLAYER%;" + player.getName());
        } else {
            Lang.STAFF_BYPASS_ON_OTHER.send(sender, "%PLAYER%;" + player.getName());
        }
    }

}
