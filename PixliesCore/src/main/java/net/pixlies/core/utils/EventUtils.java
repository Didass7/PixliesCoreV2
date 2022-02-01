package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.event.Event;

public final class EventUtils {

    private final Main instance = Main.getInstance();

    private EventUtils() {}

    public static void callEvent(Event event) {
        instance.getServer().getPluginManager().callEvent(event);
    }

}
