package net.pixlies.lobby;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ScoreboardHandler;
import net.pixlies.core.modules.Module;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.lobby.commands.CommandManager;
import net.pixlies.lobby.listeners.ListenerManager;
import net.pixlies.lobby.managers.GrapplingHookManager;
import net.pixlies.lobby.managers.JumpPadManager;
import net.pixlies.lobby.managers.LobbyManager;
import net.pixlies.lobby.managers.QueueManager;
import net.pixlies.lobby.scoreboard.ScoreboardAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class Lobby extends JavaPlugin implements Module {

    @Getter private static Lobby instance;

    ModuleConfig config;
    LobbyManager lobbyManager;
    ListenerManager listenerManager;
    JumpPadManager jumpPadManager;
    GrapplingHookManager grapplingHookManager;
    QueueManager queueManager;

    @Override
    public void onEnable() {

        // INSTANCE
        instance = this;

        // DEPENDENCY CHECK
        if (this.getServer().getPluginManager().getPlugin("PixliesCore") == null) {
            getLogger().severe("PixliesCore is not loaded. Please make sure PixliesCore is installed on the target server.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI is not loaded. Please make sure PlaceholderAPI is installed on the target server.");
            this.getServer().getPluginManager().disablePlugin(this);
        }


        // CONFIG
        config = new ModuleConfig(this, new File(getDataFolder().getAbsolutePath(), "config.yml"), "config.yml");

        // MANAGERS
        lobbyManager = new LobbyManager();
        jumpPadManager = new JumpPadManager();
        grapplingHookManager = new GrapplingHookManager();
        queueManager = new QueueManager();

        // COMMANDS & LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerAll();
        new CommandManager().registerAll();

        // SCOREBOARD
        Main.getInstance().getHandlerManager().getHandler(ScoreboardHandler.class).load(new ScoreboardAdapter());

    }

    @Override
    public void onDisable() {
        listenerManager.unregisterAll();
        instance = null;
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
