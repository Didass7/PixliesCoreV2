package net.pixlies.proxy.utils;

import net.md_5.bungee.api.plugin.Event;
import net.pixlies.proxy.PixliesProxy;

public final class EventUtils {

    private EventUtils() {
        throw new IllegalStateException("EventUtils cannot be instantiated");
    }

    public static void callEvent(Event event) {
        PixliesProxy.getInstance().getProxy().getPluginManager().callEvent(event);
    }

}
