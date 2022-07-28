package net.pixlies.lobby;

import lombok.Getter;
import net.pixlies.core.modules.Module;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.lobby.commands.CommandManager;
import net.pixlies.lobby.listeners.ListenerManager;
import net.pixlies.lobby.managers.GrapplingHookManager;
import net.pixlies.lobby.managers.JumpPadManager;
import net.pixlies.lobby.managers.LobbyManager;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.messaging.PluginMessagingManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class Lobby extends JavaPlugin implements Module {

    @Getter private static Lobby instance;

    ModuleConfig config;
    LobbyManager lobbyManager;
    ListenerManager listenerManager;
    PluginMessagingManager pluginMessagingManager;
    JumpPadManager jumpPadManager;
    GrapplingHookManager grapplingHookManager;
    QueueManager queueManager;

    @Override
    public void onEnable() {

        // INSTANCE
        instance = this;

        // CONFIG
        config = new ModuleConfig(this, new File(getDataFolder().getAbsolutePath(), "config.yml"), "config.yml");

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
    public void onDisable() {
        listenerManager.unregisterAll();
        pluginMessagingManager.unregisterAll();
        instance = null;
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
