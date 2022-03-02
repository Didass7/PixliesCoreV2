package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

/**
 * Staffchat command
 * @author vPrototype_
 */
@CommandAlias("staffchat|sc")
@CommandPermission("pixlies.staff.staffchat")
public class StaffChatCommand extends BaseCommand {

    @Default
    @Description("Toggle staffchat or send a message in staffchat")
    public void onStaffChat(Player player, @Optional String message) {
        if (message == null) {
            User user = User.get(player.getUniqueId());
            if (user.getSettings().isInStaffChat()) {
                user.getSettings().setInStaffChat(false);
                Lang.STAFFCHAT_DISABLED.send(player);
                return;
            } else {
                user.getSettings().setInStaffChat(true);
                Lang.STAFFCHAT_ENABLED.send(player);
            }
        } else {
            Lang.STAFFCHAT_FORMAT.broadcastPermission("pixlies.staff.staffchat", "%PLAYER%;" + player.getName(),
                    "%MESSAGE%;" + message);
        }
    }

}
