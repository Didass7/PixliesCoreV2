package net.pixlies.proxy.commands.impl.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Description;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.localization.Lang;

@CommandAlias("server")
public class ServerCommand extends BaseCommand {

    private static final Proxy instance = Proxy.getInstance();

    @CommandCompletion("@players")
    @Description("Send a player to a server.")
    public void onServer(CommandSender sender, ProxiedPlayer player, ServerInfo serverInfo) {
        if (!serverInfo.canAccess(player)) {
            Lang.PLAYER_SERVER_CANNOT_CONNECT_OTHER.send(sender, "%PLAYER%;" + player.getName());
            return;
        }
        player.connect(serverInfo);
        Lang.PLAYER_SERVER_CONNECTED_OTHER.send(sender, "%PLAYER%;" + player.getName(), "%SERVER%;" + serverInfo.getName());
        Lang.PLAYER_SERVER_CONNECTED.send(player, "%SERVER%;" + serverInfo.getName());
    }

    @CommandCompletion("@players")
    @Description("Send a player to a server.")
    public void onServer(ProxiedPlayer player, ServerInfo serverInfo) {
        if (!serverInfo.canAccess(player)) {
            Lang.PLAYER_SERVER_CANNOT_CONNECT.send(player);
            return;
        }
        player.connect(serverInfo);
        Lang.PLAYER_SERVER_CONNECTED.send(player, "%SERVER%;" + serverInfo.getName());
    }

}
