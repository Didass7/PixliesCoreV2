package net.pixlies.core.events;

import net.pixlies.core.Main;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class PixliesEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    protected Main pixlies = Main.getInstance();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
