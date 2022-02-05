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

    @Subcommand("setspawn")
    @CommandPermission("pixlies.lobby.setspawn")
    public void onSetSpawn(Player player) {
        lobby.getLobbyManager().setSpawnLocation(player.getLocation());
        player.sendMessage(Lang.PIXLIES + "§7The lobby spawn has been set to your current location.");
    }

    @Subcommand("build")
    @CommandPermission("pixlies.lobby.build")
    public void onBuild(Player player) {
        LobbyManager manager = lobby.getLobbyManager();
        UUID uuid = player.getUniqueId();
        if (manager.isInBuildMode(uuid)) {
            Lang.LOBBY_BUILDMODE_TOGGLE.send(player, "%STATE%;disabled");
            manager.removeBuildModePlayer(uuid);
        } else {
            Lang.LOBBY_BUILDMODE_TOGGLE.send(player, "%STATE%;enabled");
            manager.addBuildModePlayer(uuid);
        }
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
