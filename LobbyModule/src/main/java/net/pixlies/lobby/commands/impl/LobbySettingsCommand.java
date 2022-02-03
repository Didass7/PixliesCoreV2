package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

@CommandAlias("lobbysettings|lobset")
@CommandPermission("pixlies.lobby.settings")
public class LobbySettingsCommand extends BaseCommand {

    // TODO: everything
    @Subcommand("setspawn")
    @CommandPermission("pixlies.lobby.setspawn")
    public void onSetSpawn(Player player) {

    }

    @Subcommand("build")
    @CommandPermission("pixlies.lobby.build")
    public void onBuild(Player player) {

    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
