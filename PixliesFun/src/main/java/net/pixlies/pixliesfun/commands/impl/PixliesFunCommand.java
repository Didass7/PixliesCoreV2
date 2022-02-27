package net.pixlies.pixliesfun.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// TODO: All things, everything, they all must be completed.
@CommandAlias("pixliesfun|slimefun|mickfun|coolfun|fun|pixcool|machine|pixfun|pf|sf|mf|cf|pc|m")
public class PixliesFunCommand extends BaseCommand {

    @Default
    @Subcommand("gui")
    public void onGuide(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.getServer().dispatchCommand(sender, getExecCommandLabel() + " help");
            return;
        }
        // TODO: Open the GUI
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
