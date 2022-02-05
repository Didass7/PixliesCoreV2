package net.pixlies.minimick.managers;

import net.pixlies.minimick.MiniMick;
import net.pixlies.minimick.listeners.ListenerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class DiscordManager {

    private final MiniMick instance = MiniMick.getInstance();

    private final String token = instance.getToken();

    public void init() {
        new Thread(this::execute);
    }

    private void execute() {

        // Create discord-bot with token
        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login()
                .join();

        // Loads the listeners
        ListenerManager listenerManager = new ListenerManager(api);
        listenerManager.registerAll();

    }

}
