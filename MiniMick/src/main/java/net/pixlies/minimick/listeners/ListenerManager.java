package net.pixlies.minimick.listeners;

import com.google.common.collect.ImmutableList;
import net.pixlies.minimick.listeners.impl.MessageListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.listener.GloballyAttachableListener;

public class ListenerManager {

    private final DiscordApi api;

    public ListenerManager(DiscordApi api) {
        this.api = api;
    }

    private final ImmutableList<GloballyAttachableListener> listeners = ImmutableList.of(
            new MessageListener()
    );

    public void registerAll() {
        listeners.forEach(api::addListener);
    }

}
