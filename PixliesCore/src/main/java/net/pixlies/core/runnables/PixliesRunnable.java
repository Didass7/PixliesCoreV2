package net.pixlies.core.runnables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.core.Main;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@AllArgsConstructor
public abstract class PixliesRunnable extends BukkitRunnable {

    protected final Main instance = Main.getInstance();

    private final boolean async;
    private final long delay;
    private final long period;

}
