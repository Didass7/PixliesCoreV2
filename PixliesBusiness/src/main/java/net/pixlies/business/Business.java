package net.pixlies.business;

import lombok.Getter;
import net.pixlies.core.modules.Module;

import net.pixlies.business.commands.CommandManager;

public class Business extends Module {

    @Getter private static Business instance;
    @Getter private CommandManager commandManager;

    @Override
    public void onLoad() {
        instance = this;
        commandManager = new CommandManager();
        commandManager.registerAllCommands();
    }

    @Override
    public void onDrop() {
        commandManager.unregisterAllCommands();
        instance = null;
    }

}
