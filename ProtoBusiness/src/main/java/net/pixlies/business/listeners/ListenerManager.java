package net.pixlies.business.listeners;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.pixlies.business.listeners.impl.InventoryClickListener;
import net.pixlies.business.listeners.impl.InventoryCloseListener;
import net.pixlies.business.listeners.impl.JoinNotifyListener;
import net.pixlies.core.Main;
import org.bukkit.event.Listener;

public class ListenerManager {
    private static final Main instance = Main.getInstance();
    
    @Getter
    private final ImmutableList<Listener> listeners = ImmutableList.of(
            new JoinNotifyListener(),
            new InventoryClickListener(),
            new InventoryCloseListener()
    );
    
    public void registerAllListeners() {
        listeners.forEach(listener -> instance.getServer().getPluginManager().registerEvents(listener, instance));
    }
}
