package net.pixlies.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public final class EventUtils {

    private EventUtils() {}

    public static void call(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

}
