package net.pixlies.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.commands.impl.player.LobbyCommand;
import net.pixlies.proxy.commands.impl.staff.MaintenanceCommand;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;

import java.io.File;

public class CommandManager {

    private static final Proxy instance = Proxy.getInstance();
    private final BungeeCommandManager manager = new BungeeCommandManager(instance);

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(

            // PLAYER
            new LobbyCommand(),

            // STAFF
            new MaintenanceCommand()

    );

    public void registerCommands() {
        commands.forEach(manager::registerCommand);
    }

    public void registerConfigs() {

        // CONFIGS
        manager.registerDependency(Config.class, "config", new Config(new File(instance.getDataFolder(), "config.yml"), "config.yml"));
        manager.registerDependency(Config.class, "serverListConfig", new Config(new File(instance.getDataFolder(), "features/serverlist.yml"), "features/serverlist.yml"));
        manager.registerDependency(Config.class, "settings", new Config(new File(instance.getDataFolder(), "settings.yml"), "settings.yml"));
        manager.registerDependency(Config.class, "maintenanceConfig", new Config(new File(instance.getDataFolder(), "maintenance.yml"), "maintenance.yml"));

    }

    public void registerHandlers() {
        // MAINTENANCE
        manager.registerDependency(MaintenanceHandler.class, "maintenanceHandler", new MaintenanceHandler());
    }

    public void registerCompletions() {
        // TODO
    }

}
