package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;

@CommandAlias("company|business|com")
@CommandPermission("pixlies.business.company")
public class CompanyCommand extends BaseCommand {

    @Default
    @Description("Company command")
    public void onCompany(CommandSender sender) {
        // TODO
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
