package net.pixlies.core.utils;

import net.pixlies.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public final class EventUtils {

    private static final Main instance = Main.getInstance();

    private EventUtils() {}

    public static void call(Event event) {
        instance.getServer().getPluginManager().callEvent(event);
    }

}
