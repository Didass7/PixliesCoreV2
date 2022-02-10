package net.pixlies.proxy;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.pixlies.proxy.commands.CommandManager;
import net.pixlies.proxy.listeners.ListenerManager;
import net.pixlies.proxy.localization.Lang;
import net.pixlies.proxy.utils.FileUtils;

public class Proxy extends Plugin {

    @Getter private static Proxy instance;

    @Getter private CommandManager commandManager;
    @Getter private ListenerManager listenerManager;

    @Override
    public void onEnable() {

        // INSTANCE
        instance = this;

        // LANGUAGE
        FileUtils.saveResource("languages/LANG_ENG.yml", false);
        Lang.init();

        // COMMANDS
        commandManager = new CommandManager();
        commandManager.registerConfigs();
        commandManager.registerHandlers();
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
