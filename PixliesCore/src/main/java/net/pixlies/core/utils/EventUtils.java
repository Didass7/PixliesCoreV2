package net.pixlies.core.utils;

import net.pixlies.core.Main;
import net.pixlies.core.events.PixliesCancellableEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public final class EventUtils {

    private static final Main instance = Main.getInstance();

    private EventUtils() {}

    public static Event callEvent(Event event) {
        if (event.isAsynchronous()) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, () ->
                    instance.getServer().getPluginManager().callEvent(event));
        } else {
            instance.getServer().getPluginManager().callEvent(event);
        }
        return event;
    }

    public static PixliesCancellableEvent callCancelable(PixliesCancellableEvent event) {
        if (event.isAsynchronous()) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, () ->
                    instance.getServer().getPluginManager().callEvent(event));
        } else {
            instance.getServer().getPluginManager().callEvent(event);
        }
        return event;
    }

}
