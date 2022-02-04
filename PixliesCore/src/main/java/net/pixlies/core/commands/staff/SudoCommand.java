package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("sudo")
@CommandPermission("pixlies.staff.sudo")
public class SudoCommand extends BaseCommand {

    @Default
    @Description("Repairs items")
    public void onRepair(CommandSender sender, Player target) {
        if (target.hasPermission("pixlies.staff.sudo.exempt")) {
            // TODO
        }
    }

}
