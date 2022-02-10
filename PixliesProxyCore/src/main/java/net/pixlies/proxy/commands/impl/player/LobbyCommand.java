package net.pixlies.proxy.commands.impl.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.localization.Lang;

/**
 * Teleport the player to the lobby.
 * @author Dynmie
 */
@CommandAlias("lobby|hub")
public class LobbyCommand extends BaseCommand {

    private static final Proxy instance = Proxy.getInstance();
    private final Config config = instance.getConfig();

    @Default
    public void onLobby(ProxiedPlayer player) {
        ServerInfo serverInfo = instance.getProxy().getServerInfo(config.getConfig().getString("commands.lobby.server", "Lobby"));
        if (serverInfo == null) {
            Lang.PLAYER_SERVER_NOT_EXIST.send(player);
            return;
        }
        player.connect(serverInfo);
        Lang.PLAYER_SERVER_CONNECTED.send(player, "%SERVER%;" + serverInfo.getName());
    }

    @Private
    @CommandCompletion("@players")
    @CommandPermission("pixlies.lobby.others")
    public void onLobby(CommandSender sender, ProxiedPlayer player) {
        ServerInfo serverInfo = instance.getProxy().getServerInfo(config.getConfig().getString("commands.lobby.server", "Lobby"));
        if (serverInfo == null) {
            Lang.PLAYER_SERVER_NOT_EXIST.send(player);
            return;
        }
        player.connect(serverInfo);
        Lang.PLAYER_SERVER_CONNECTED.send(player, "%SERVER%;" + serverInfo.getName());
    }

}
