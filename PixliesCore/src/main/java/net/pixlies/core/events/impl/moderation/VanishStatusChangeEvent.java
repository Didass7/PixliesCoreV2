package net.pixlies.core.events.impl.moderation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class VanishStatusChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private @Setter boolean cancelled;

    private final VanishHandler vanishHandler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    private final Player player;
    private final VanishState state;


    public enum VanishState {
        VANISH, UNVANISH
    }

}
