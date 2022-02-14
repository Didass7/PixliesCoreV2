package net.pixlies.proxy.runnables;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

public class RunnableManager {

    private final Collection<PixliesRunnable> runnables = ImmutableList.of(
            // RUNNABLE
    );

    public void runAll() {
        runnables.forEach(PixliesRunnable::execute);
    }

}
