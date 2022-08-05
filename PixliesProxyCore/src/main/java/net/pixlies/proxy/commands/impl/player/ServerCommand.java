package net.pixlies.proxy.commands.impl.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.localization.Lang;

@CommandAlias("server|pserver")
@CommandPermission("pixlies.server")
public class ServerCommand extends BaseCommand {

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    @CommandPermission("pixlies.server.others")
    @CommandCompletion("@players")
    @Syntax("<server> <player>")
    @Description("Connect a target to a server.")
    public void onServer(CommandSender sender, ServerInfo serverInfo, ProxiedPlayer target) {
        if (target.getServer().getInfo().equals(serverInfo)) {
            Lang.PLAYER_SERVER_ALREADY_CONNECTED.send(target, "%SERVER%;" + serverInfo.getName());
            return;
        }
        if (!serverInfo.canAccess(target)) {
            Lang.PLAYER_SERVER_CANNOT_CONNECT_OTHER.send(sender, "%PLAYER%;" + target.getName());
            return;
        }
        Lang.PLAYER_SERVER_CONNECTING_OTHER.send(sender, "%PLAYER%;" + target.getName(), "%SERVER%;" + serverInfo.getName());
        Lang.PLAYER_SERVER_CONNECTING.send(target, "%SERVER%;" + serverInfo.getName());
        target.connect(serverInfo);
    }

    @CommandCompletion("@players")
    @Syntax("<server>")
    @Description("Connect to a server.")
    public void onServer(ProxiedPlayer player, ServerInfo serverInfo) {
        if (player.getServer().getInfo().equals(serverInfo)) {
            Lang.PLAYER_SERVER_ALREADY_CONNECTED.send(player, "%SERVER%;" + serverInfo.getName());
            return;
        }
        if (!serverInfo.canAccess(player)) {
            Lang.PLAYER_SERVER_CANNOT_CONNECT.send(player);
            return;
        }
        Lang.PLAYER_SERVER_CONNECTING.send(player, "%SERVER%;" + serverInfo.getName());
        player.connect(serverInfo);
    }

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
