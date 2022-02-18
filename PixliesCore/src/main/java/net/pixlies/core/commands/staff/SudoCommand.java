package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Cool sudo command
 * @author Dynmie
 */
@CommandAlias("sudo")
@CommandPermission("pixlies.staff.sudo")
public class SudoCommand extends BaseCommand {

    @Default
    @Description("Sudo a player")
    public void onSudo(CommandSender sender, Player target, String text) {

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
