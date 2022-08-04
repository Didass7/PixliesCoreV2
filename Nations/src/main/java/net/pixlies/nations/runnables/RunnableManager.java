package net.pixlies.nations.runnables;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.runnables.PixliesRunnable;

public class RunnableManager {

    private static final Main pixlies = Main.getInstance();

    private final ImmutableList<PixliesRunnable> runnables = ImmutableList.of(
    );

    public void registerAll() {
        runnables.forEach(runnable -> pixlies.getRunnableManager().register(runnable));
    }

    public void unregisterAll() {
        runnables.forEach(runnable -> pixlies.getRunnableManager().unregister(runnable));
    }

}
