package net.pixlies.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;

import java.io.File;

public class CommandManager {

    private static final Proxy instance = Proxy.getInstance();
    private final BungeeCommandManager manager = new BungeeCommandManager(instance);

    private final ImmutableList<BaseCommand> commands = ImmutableList.of(
            // COMMANDS
    );

    public void registerCommands() {
        commands.forEach(manager::registerCommand);
    }

    public void registerDependencies() {
        manager.registerDependency(Config.class, "config", new Config(new File(instance.getDataFolder(), "config.yml"), "config.yml"));
        manager.registerDependency(Config.class, "serverListConfig", new Config(new File(instance.getDataFolder(), "serverlist.yml"), "serverlist.yml"));
        manager.registerDependency(Config.class, "settings", new Config(new File(instance.getDataFolder(), "settings.yml"), "settings.yml"));
    }

    public void registerCompletions() {
        // TODO
    }

}
