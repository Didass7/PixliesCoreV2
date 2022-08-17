package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Cool sudo command
 * @author Dynmie
 */

public class SudoCommand extends BaseCommand {

    @CommandAlias("sudo")
    @CommandPermission("pixlies.staff.sudo")
    @Syntax("<player> <command>")
    @Description("Sudo a player")
    public void onSudo(CommandSender sender, OnlinePlayer onlinePlayer, String text) {

        Player target = onlinePlayer.getPlayer();
        if (target.hasPermission("pixlies.staff.sudo.exempt")) {
            Lang.STAFF_SUDO_CANNOT.send(sender);
            return;
        }

        String toRun = text;

        if (toRun.toLowerCase().startsWith("c:")) {
            toRun = toRun.replaceFirst("c:|C:", "");
            target.chat(toRun);
            Lang.STAFF_SUDO_CHAT.send(target, "%PLAYER%;" + target.getName(), "%MESSAGE%;" + toRun);
        } else {
            target.performCommand(toRun);
            Lang.STAFF_SUDO_COMMAND.send(target, "%PLAYER%;" + target.getName(), "%MESSAGE%;" + toRun);
        }

    }

}
