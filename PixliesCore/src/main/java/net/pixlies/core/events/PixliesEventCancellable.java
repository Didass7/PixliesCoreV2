package net.pixlies.core.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public abstract class PixliesEventCancellable extends PixliesEvent implements Cancellable {

    private boolean isCancelled;

}
