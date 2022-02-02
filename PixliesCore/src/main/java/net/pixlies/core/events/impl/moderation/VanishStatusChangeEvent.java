package net.pixlies.core.events.impl.moderation;

import net.pixlies.core.events.PixliesEventCancellable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class VanishStatusChangeEvent extends PixliesEventCancellable {

    private final Player player;
    private final VanishState state;

    private final VanishHandler vanishHandler = instance.getHandlerManager().getHandler(VanishHandler.class);

    public enum VanishState {
        VANISH, UNVANISH
    }

}
