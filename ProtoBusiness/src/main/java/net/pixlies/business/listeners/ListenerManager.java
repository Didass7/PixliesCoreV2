package net.pixlies.business.listeners;

import com.google.common.collect.ImmutableList;
import net.pixlies.business.listeners.impl.OrderSignsListener;
import net.pixlies.core.Main;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {

    private static final Main instance = Main.getInstance();

    private final ImmutableList<Listener> listeners = ImmutableList.of(
            new OrderSignsListener()
    );

    public void registerAllListeners() {
        listeners.forEach(listener -> instance.getServer().getPluginManager().registerEvents(listener, instance));
    }

    public void unregisterAllListeners() {
        listeners.forEach(HandlerList::unregisterAll);
    }

    public ImmutableList<Listener> getListeners() {
        return listeners;
    }

}
