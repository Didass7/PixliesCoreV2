package net.pixlies.DiploMaurice.listeners;

import net.pixlies.DiploMaurice.listeners.impl.CommandListenerImpl;
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
