package net.pixlies.proxy.runnables;

import com.google.common.collect.ImmutableList;
import net.pixlies.proxy.runnables.impl.AutoAnnounceRunnable;

import java.util.Collection;

public class RunnableManager {

    private final Collection<PixliesRunnable> runnables = ImmutableList.of(
            new AutoAnnounceRunnable()
    );

    public void runAll() {
        runnables.forEach(PixliesRunnable::execute);
    }

}
