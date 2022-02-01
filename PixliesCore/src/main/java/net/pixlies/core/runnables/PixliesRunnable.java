package net.pixlies.core.runnables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@AllArgsConstructor
public abstract class PixliesRunnable extends BukkitRunnable {

    private final boolean async;
    private final long delay;
    private final long period;

}
