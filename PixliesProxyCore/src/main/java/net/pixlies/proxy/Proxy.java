package net.pixlies.proxy;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.pixlies.proxy.commands.CommandManager;
import net.pixlies.proxy.listeners.ListenerManager;

public class Proxy extends Plugin {

    @Getter private static Proxy instance;

    @Getter private CommandManager commandManager;
    @Getter private ListenerManager listenerManager;

    @Override
    public void onEnable() {

        // INSTANCE
        instance = this;

        // COMMANDS
        commandManager = new CommandManager();
        commandManager.registerDependencies();
        commandManager.registerCommands();

        // LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerListeners();

    }

    @Override
    public void onDisable() {
        instance = null;
    }

}
