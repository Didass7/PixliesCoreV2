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

    @Getter private static final HandlerList handlerList = new HandlerList();
    private final VanishHandler vanishHandler = pixlies.getHandlerManager().getHandler(VanishHandler.class);

    private final Player player;
    private final VanishState state;


    public enum VanishState {
        VANISH, UNVANISH
    }

}
