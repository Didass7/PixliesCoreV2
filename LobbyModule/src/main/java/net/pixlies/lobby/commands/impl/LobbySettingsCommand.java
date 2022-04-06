package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.LobbyManager;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("lobbysettings|lobset")
@CommandPermission("pixlies.lobby.settings")
public class LobbySettingsCommand extends BaseCommand {

    private static final Lobby lobby = Lobby.getInstance();

    @Subcommand("build")
    @CommandPermission("pixlies.lobby.build")
    public void onBuild(Player player) {
        LobbyManager manager = lobby.getLobbyManager();
        UUID uuid = player.getUniqueId();
        if (manager.isInBuildMode(uuid)) {
            Lang.LOBBY_BUILDMODE_TOGGLE.send(player, "%STATE%;disabled");
            manager.removeBuildModePlayer(player);
        } else {
            Lang.LOBBY_BUILDMODE_TOGGLE.send(player, "%STATE%;enabled");
            manager.addBuildModePlayer(player);
        }
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
