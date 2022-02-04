package net.pixlies.core.events;

import net.pixlies.core.Main;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class PixliesEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    protected Main instance = Main.getInstance();

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
