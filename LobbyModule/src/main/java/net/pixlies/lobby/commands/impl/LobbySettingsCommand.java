package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.config.Config;
import net.pixlies.lobby.managers.LobbyManager;
import org.bukkit.entity.Player;

@CommandAlias("lobbysettings|lobset")
@CommandPermission("pixlies.lobby.settings")
public class LobbySettingsCommand extends BaseCommand {

    private static final Lobby lobby = Lobby.getInstance();

    @Subcommand("setspawn")
    @CommandPermission("pixlies.lobby.setspawn")
    public void onSetSpawn(Player player) {
        Config config = lobby.getConfig();
        config.set("locations.spawn", player.getLocation());
        config.save();
        player.sendMessage(Lang.PIXLIES + "§7You just set the spawn!");
    }

    @Subcommand("build")
    @CommandPermission("pixlies.lobby.build")
    public void onBuild(Player player) {
        LobbyManager manager = lobby.getLobbyManager();
        if (manager.getBuildModePlayers().contains(player.getUniqueId())) {
            Lang.LOBBY_BUILDMODE_TOGGLE.send(player, "%STATE%;§cdisabled");
            manager.getBuildModePlayers().remove(player.getUniqueId());
        } else {
            Lang.LOBBY_BUILDMODE_TOGGLE.send(player, "%STATE%;§aenabled");
            manager.getBuildModePlayers().add(player.getUniqueId());
        }
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
