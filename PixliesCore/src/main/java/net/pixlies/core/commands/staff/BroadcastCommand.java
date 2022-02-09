package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.command.CommandSender;

/**
 * Broadcast command to broadcast
 * @author dynmie
 */
@CommandPermission("pixlies.staff.broadcast")
public class BroadcastCommand extends BaseCommand {

    @Default
    @CommandAlias("broadcast|bc")
    @Description("Broadcast a message")
    public void onBroadcast(CommandSender sender, String text) {
        // if player has perm below then allow color, else normal text
        String message = sender.hasPermission("pixlies.staff.broadcast.color") ? CC.format(text) : text;
        Lang.STAFF_BROADCAST_MESSAGE.broadcast("%MESSAGE%;" + message);
    }

}
