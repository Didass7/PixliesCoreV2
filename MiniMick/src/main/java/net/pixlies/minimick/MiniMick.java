package net.pixlies.minimick;

import lombok.Getter;
import net.pixlies.core.modules.Module;
import net.pixlies.minimick.listeners.ListenerManager;
import net.pixlies.minimick.managers.DiscordManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class MiniMick extends Module {

    @Getter private static MiniMick instance;
    @Getter private String token = ""; // set token from config

    @Getter private DiscordManager discordManager;

    @Override
    public void onLoad() {

        instance = this;

        // DISCORD MANAGER
        discordManager = new DiscordManager();
        discordManager.init();

    }

    @Override
    public void onDrop() {
        instance = null;
    }

}
