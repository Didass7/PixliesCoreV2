package net.pixlies.core.utils;

import net.pixlies.core.Main;
import net.pixlies.core.events.PixliesCancellableEvent;
import org.bukkit.event.Event;

public final class EventUtils {

    private static final Main instance = Main.getInstance();

    private EventUtils() {}

    public static Event callEvent(Event event) {
         instance.getServer().getPluginManager().callEvent(event);
         return event;
    }

    public static PixliesCancellableEvent callCancelable(PixliesCancellableEvent event) {
        instance.getServer().getPluginManager().callEvent(event);
        return event;
    }

}
