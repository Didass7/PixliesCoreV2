package net.pixlies.pixliesfun.listeners;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {

    private static final Main instance = Main.getInstance();

    private final ImmutableList<Listener> listeners = ImmutableList.of(

    );

    public void registerAll() {
        listeners.forEach(listener -> instance.getServer().getPluginManager().registerEvents(listener, instance));
    }

    public void unregisterAll() {
        listeners.forEach(HandlerList::unregisterAll);
    }


}
