package net.pixlies.proxy.commands;

import co.aikar.commands.*;
import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.commands.impl.player.LobbyCommand;
import net.pixlies.proxy.commands.impl.player.ServerCommand;
import net.pixlies.proxy.commands.impl.queue.QueueSettingsCommand;
import net.pixlies.proxy.commands.impl.staff.MaintenanceCommand;
import net.pixlies.proxy.localization.Lang;

/**
 * @author Dynmie
 */
public class CommandManager {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private final BungeeCommandManager manager = new BungeeCommandManager(instance);

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(

            // PLAYER
            new LobbyCommand(),
            new ServerCommand(),

            // QUEUE
            new QueueSettingsCommand(),

            // STAFF
            new MaintenanceCommand()

    );

    public void registerCommands() {
        commands.forEach(manager::registerCommand);
    }

    public void registerContexts() {

        CommandContexts<BungeeCommandExecutionContext> contexts = manager.getCommandContexts();

        contexts.registerContext(ServerInfo.class, context -> {
            ServerInfo info = instance.getProxy().getServerInfo(context.getFirstArg());
            if (info == null) {
                throw new ConditionFailedException(Lang.PLAYER_SERVER_NOT_EXIST.get());
            }
            return info;
        });

    }

    public void registerCompletions() {
        // TODO
    }

    public void registerAll() {
        manager.enableUnstableAPI("help");
        manager.enableUnstableAPI("brigadier");

        registerContexts();
        registerCompletions();
        registerCommands();
    }

}
