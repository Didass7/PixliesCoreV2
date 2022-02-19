package net.pixlies.nations.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.events.PixliesCancellableEvent;
import net.pixlies.nations.nations.Nation;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class NationDisbandEvent extends PixliesCancellableEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Nation nation;

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
