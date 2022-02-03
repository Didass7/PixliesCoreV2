package net.pixlies.core.events.impl.moderation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.PixliesCancellableEvent;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class StaffModeStatusChangeEvent extends PixliesCancellableEvent {

    private final Player player;
    private final User user;
    private final StaffModeStatus status;

    public enum StaffModeStatus {
        ENABLE, DISABLE
    }

}
