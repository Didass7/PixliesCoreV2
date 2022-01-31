package net.pixlies.minimick;

import net.pixlies.core.modules.Module;
import net.pixlies.minimick.listeners.ListenerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class MiniMick extends Module {

    private static MiniMick instance;
    private static String token;

    @Override
    public void onLoad() {
        instance = this;
        new Thread(MiniMick::start); // Runs MiniMick in a different thread
    }

    @Override
    public void onDrop() {
        instance = null;
    }

    public static void start() {
        // Create discord-bot with token
        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        // Loads the listeners
        ListenerManager listenerManager = new ListenerManager(api);
        listenerManager.loadListeners();

    }
}
