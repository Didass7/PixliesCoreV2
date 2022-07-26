package net.pixlies.lobby;

import lombok.Getter;
import net.pixlies.core.modules.Module;
import net.pixlies.lobby.commands.CommandManager;
import net.pixlies.lobby.config.Config;
import net.pixlies.lobby.listeners.ListenerManager;
import net.pixlies.lobby.managers.GrapplingHookManager;
import net.pixlies.lobby.managers.JumpPadManager;
import net.pixlies.lobby.managers.LobbyManager;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.messaging.PluginMessagingManager;

import java.io.File;

@Getter
public class Lobby extends Module {

    @Getter private static Lobby instance;

    Config config;
    LobbyManager lobbyManager;
    ListenerManager listenerManager;
    PluginMessagingManager pluginMessagingManager;
    JumpPadManager jumpPadManager;
    GrapplingHookManager grapplingHookManager;
    QueueManager queueManager;

    @Override
    public void onLoad() {

        // INSTANCE
        instance = this;

        // CONFIG
        config = new Config(new File(this.getModuleFolder().getAbsolutePath(), "config.yml"), "config.yml");

        // MANAGERS
        lobbyManager = new LobbyManager();
        jumpPadManager = new JumpPadManager();
        grapplingHookManager = new GrapplingHookManager();
        queueManager = new QueueManager();

        // PLUGIN MESSAGES
        pluginMessagingManager = new PluginMessagingManager();
        pluginMessagingManager.registerAll();

        // COMMANDS & LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerAll();
        new CommandManager().registerAll();


    }

    @Override
    public void onDrop() {
        listenerManager.unregisterAll();
        pluginMessagingManager.unregisterAll();
        instance = null;
    }

}
