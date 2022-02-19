package net.pixlies.core.events.impl.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.PixliesCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class StaffModeStatusChangeEvent extends PixliesCancellableEvent {

    private final Player player;
    private final User user;
    private final StaffModeStatus status;

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum StaffModeStatus {
        ENABLE, DISABLE
    }

}
