package net.pixlies.core.runnables;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.runnables.impl.VanishRunnable;

public class RunnableRegisterManager {

    private final ImmutableList<PixliesRunnable> runnables = ImmutableList.of(
            new VanishRunnable()
    );

    public void runAll() {
        runnables.forEach(runnable -> Main.getInstance().getRunnableManager().register(runnable));
    }

    public void stopAll() {
        runnables.forEach(runnable -> Main.getInstance().getRunnableManager().unregister(runnable));
    }

}
