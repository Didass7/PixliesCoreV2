package net.pixlies.core.events.impl.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.events.PixliesCancellableEvent;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class VanishStatusChangeEvent extends PixliesCancellableEvent {

    private final Player player;
    private final VanishState state;

    private final VanishHandler vanishHandler = instance.getHandlerManager().getHandler(VanishHandler.class);

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum VanishState {
        VANISH, UNVANISH
    }

}
