package net.pixlies.lobby;

import lombok.Getter;
import net.pixlies.core.modules.Module;
import net.pixlies.lobby.commands.CommandManager;
import net.pixlies.lobby.config.Config;
import net.pixlies.lobby.listeners.ListenerManager;
import net.pixlies.lobby.managers.LobbyManager;

import java.io.File;

public class Lobby extends Module {

    @Getter static Lobby instance;
    @Getter Config config;
    @Getter LobbyManager lobbyManager;
    @Getter ListenerManager listenerManager;

    @Override
    public void onLoad() {

        // INSTANCE
        instance = this;

        // CONFIG
        config = new Config(new File(this.getModuleFolder().getAbsolutePath(), "config.yml"), "config.yml");

        // MANAGERS
        lobbyManager = new LobbyManager();

        // COMMANDS & LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerAll();
        new CommandManager().registerAll();

    }

    @Override
    public void onDrop() {
        listenerManager.unregisterAll();
        instance = null;
    }

}
