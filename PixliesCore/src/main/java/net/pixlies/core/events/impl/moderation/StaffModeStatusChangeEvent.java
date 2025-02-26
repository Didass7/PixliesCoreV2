package net.pixlies.core.events.impl.moderation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class StaffModeStatusChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    private @Setter boolean cancelled = false;

    private final Player player;
    private final User user;
    private final StaffModeStatus status;

    public enum StaffModeStatus {
        ENABLE, DISABLE
    }

}
