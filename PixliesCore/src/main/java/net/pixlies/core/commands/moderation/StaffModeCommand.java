package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import org.bukkit.command.CommandSender;

public class StaffModeCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @CommandAlias("staff")
    @CommandPermission("pixlies.moderation.staffmode")
    @Description("Staff gamemode")
    public void onStaffMode(CommandSender sender) {
        // TODO
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
