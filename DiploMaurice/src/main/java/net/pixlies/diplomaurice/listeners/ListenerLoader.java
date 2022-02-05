package net.pixlies.diplomaurice.listeners;

import net.pixlies.diplomaurice.listeners.impl.CommandListenerImpl;
import org.javacord.api.DiscordApi;

public class ListenerLoader {
    private DiscordApi api;

    public ListenerLoader(DiscordApi api) {
        this.api = api;
    }

    public void loadAll() {
        api.addListener(new CommandListenerImpl());
    }
}
