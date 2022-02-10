package net.pixlies.proxy.commands.impl.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.localization.Lang;

@CommandAlias("server")
@CommandPermission("pixlies.server")
public class ServerCommand extends BaseCommand {

    private static final Proxy instance = Proxy.getInstance();

    @CommandPermission("pixlies.server.others")
    @CommandCompletion("@players")
    @Syntax("<player> <server>")
    @Description("Connect a player to a server.")
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
    @Syntax("<server>")
    @Description("Connect to a server.")
    public void onServer(ProxiedPlayer player, ServerInfo serverInfo) {
        if (!serverInfo.canAccess(player)) {
            Lang.PLAYER_SERVER_CANNOT_CONNECT.send(player);
            return;
        }
        player.connect(serverInfo);
        Lang.PLAYER_SERVER_CONNECTED.send(player, "%SERVER%;" + serverInfo.getName());
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
