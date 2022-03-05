package net.pixlies.core.events.impl.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.events.PixliesCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class StaffModeStatusChangeEvent extends PixliesCancellableEvent {

    @Getter private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final User user;
    private final StaffModeStatus status;

    public enum StaffModeStatus {
        ENABLE, DISABLE
    }

}
