package net.pixlies.nations.listeners;

import com.google.common.collect.ImmutableList;
import net.pixlies.nations.Nations;
import net.pixlies.nations.listeners.impl.NationProfileListener;
import net.pixlies.nations.listeners.impl.PlayerTerritoryListener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {

    private static final Nations instance = Nations.getInstance();

    private final ImmutableList<Listener> listeners = ImmutableList.of(
            new NationProfileListener(),
            new PlayerTerritoryListener()
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
