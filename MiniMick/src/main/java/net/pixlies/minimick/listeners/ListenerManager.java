package net.pixlies.minimick.listeners;

import org.javacord.api.DiscordApi;

public class ListenerManager {
    private DiscordApi api;

    public ListenerManager(DiscordApi api) {
        this.api = api;
    }

    public void loadListeners() {
        api.addListener(new onMessageListener());
    }
}
