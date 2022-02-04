package net.pixlies.nations.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.entity.User;
import net.pixlies.nations.nations.Nation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("nation|n")
public class NationCommand extends BaseCommand {

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @Description("Creates a nation")
    public void onCreate(User user, String name) {

    }

}
