package net.pixlies.proxy.commands;

import co.aikar.commands.*;
import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.config.ServerInfo;
import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.commands.impl.player.LobbyCommand;
import net.pixlies.proxy.commands.impl.player.ServerCommand;
import net.pixlies.proxy.commands.impl.queue.QueueSettingsCommand;
import net.pixlies.proxy.commands.impl.staff.MaintenanceCommand;
import net.pixlies.proxy.localization.Lang;
import net.pixlies.proxy.utils.CC;

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

    public void registerConditions() {
        manager.getCommandConditions().addCondition(Integer.class, "limits", (context, execution, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7You can only enter a minimum value of &6" + context.getConfigValue("min", 0) + "&7."));
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException(Lang.PIXLIES + CC.format("&7You can only enter a maximum value of &6" + context.getConfigValue("max", 3) + "&7."));
            }
        });
    }

    public void registerAll() {
        manager.enableUnstableAPI("help");
        manager.enableUnstableAPI("brigadier");

        registerContexts();
        registerCompletions();
        registerConditions();
        registerCommands();
    }

}
