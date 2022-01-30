package net.pixlies.proxy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.Proxy;

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
        manager.registerDependency(Proxy.class, instance);
    }

    public void registerCompletions() {
        // TODO
    }

}
