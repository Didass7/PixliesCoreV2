package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

/**
 * Broadcast command to broadcast
 * @author dynmie
 */
@CommandPermission("pixlies.staff.broadcast")
public class BroadcastCommand extends BaseCommand {

    @CommandAlias("broadcast|bc")
    @CommandCompletion("@empty")
    @Syntax("<message>")
    @Description("Broadcast a message")
    public void onBroadcast(CommandSender sender, String message) {
        // if player has perm below then allow color, else normal message
        String msg = sender.hasPermission("pixlies.staff.broadcast.color") ? CC.format(message) : message;
        Lang.STAFF_BROADCAST_MESSAGE.broadcast("%MESSAGE%;" + msg.trim());
    }

    @Default
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
