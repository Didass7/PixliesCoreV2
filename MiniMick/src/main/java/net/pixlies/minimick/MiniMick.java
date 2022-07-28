package net.pixlies.minimick;

import lombok.Getter;
import net.pixlies.core.modules.Module;
import net.pixlies.minimick.listeners.ListenerManager;
import net.pixlies.minimick.managers.DiscordManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.jetbrains.annotations.NotNull;

public class MiniMick extends JavaPlugin implements Module {

    @Getter private static MiniMick instance;
    @Getter private String token = ""; // set token from config

    @Getter private DiscordManager discordManager;

    @Override
    public void onEnable() {

        instance = this;

        // DISCORD MANAGER
        discordManager = new DiscordManager();
        discordManager.init();

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
