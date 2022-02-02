package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.event.Event;

public final class EventUtils {

    private static final Main instance = Main.getInstance();

    private EventUtils() {}

    public static Event callEvent(Event event) {
         instance.getServer().getPluginManager().callEvent(event);
         return event;
    }

}
