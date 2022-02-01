package net.pixlies.proxy.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;

@CommandAlias("lobby|hub")
public class LobbyCommand extends BaseCommand {

    private static final Proxy instance = Proxy.getInstance();
    @Dependency Config config;

    @Default
    public void onLobby(ProxiedPlayer player) {
        ServerInfo serverInfo = instance.getProxy().getServerInfo(config.getConfig().getString("commands.lobby.server", "Lobby"));
        player.connect(serverInfo);
    }

    @Private
    @CommandCompletion("@players")
    @CommandPermission("pixlies.lobby.others")
    public void onLobby(CommandSender sender, ProxiedPlayer player) {
        ServerInfo serverInfo = instance.getProxy().getServerInfo(config.getConfig().getString("commands.lobby.server", "Lobby"));
        player.connect(serverInfo);
        // TODO: Send message to sender because of lang
    }

}
