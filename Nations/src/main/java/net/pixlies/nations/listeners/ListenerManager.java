package net.pixlies.nations.listeners;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.nations.listeners.impl.LockedChestListener;
import net.pixlies.nations.listeners.impl.NationDisbandListener;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {

    private static final Main instance = Main.getInstance();

    private final ImmutableList<Listener> listeners = ImmutableList.of(
            new LockedChestListener(),
            new NationDisbandListener()
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
