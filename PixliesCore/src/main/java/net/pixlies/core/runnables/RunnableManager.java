package net.pixlies.core.runnables;

import net.pixlies.core.Main;

import java.util.ArrayList;
import java.util.List;

public class RunnableManager {

    private final List<PixliesRunnable> runnables = new ArrayList<>();

    public void register(PixliesRunnable runnable) {
        if (runnable.isAsync()) {
            runnable.runTaskTimerAsynchronously(Main.getInstance(), runnable.getDelay(), runnable.getPeriod());
        } else {
            runnable.runTaskTimer(Main.getInstance(), runnable.getDelay(), runnable.getPeriod());
        }
        runnables.add(runnable);
    }

    public void unregister(PixliesRunnable runnable) {
        if (runnables.isEmpty()) return;
        runnables.remove(runnable);
        if (runnable.isCancelled()) return;
        runnable.cancel();
    }

}
