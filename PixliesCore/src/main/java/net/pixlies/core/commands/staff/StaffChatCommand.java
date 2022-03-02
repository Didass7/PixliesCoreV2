package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.ChatColor;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.StaffChatHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

/**
 * Staffchat command
 * @author vPrototype_
 */
@CommandAlias("staffchat|sc")
@CommandPermission("pixlies.staff.staffchat")
public class StaffChatCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final StaffChatHandler scHandler = instance.getHandlerManager().getHandler(StaffChatHandler.class);

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
            scHandler.sendStaffChat(Lang.STAFF + ChatColor.GOLD + player.getName() +
                    ChatColor.DARK_GRAY + " >> " + ChatColor.GRAY + message);
        }
    }

}
