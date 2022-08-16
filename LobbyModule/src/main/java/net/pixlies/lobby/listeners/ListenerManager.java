package net.pixlies.lobby.listeners;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.lobby.listeners.impl.JoinListener;
import net.pixlies.lobby.listeners.impl.LobbyInteractListener;
import net.pixlies.lobby.listeners.impl.QueueListener;
import net.pixlies.lobby.listeners.impl.QuitListener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {

    public final ImmutableList<Listener> listeners = ImmutableList.of(
            new JoinListener(),
            new QuitListener(),
            new LobbyInteractListener(),
            new QueueListener()
    );

    public void registerAll() {
        listeners.forEach(listener -> Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance()));
    }

    public void unregisterAll() {
        listeners.forEach(HandlerList::unregisterAll);
    }

}
