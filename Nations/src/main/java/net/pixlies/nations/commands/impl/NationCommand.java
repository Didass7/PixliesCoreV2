package net.pixlies.nations.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;

@CommandAlias("%nationscommand")
public class NationCommand extends BaseCommand {

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
